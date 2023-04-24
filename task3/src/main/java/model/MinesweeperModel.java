package model;

import model.listener.*;
import model.record.Record;
import model.record.RecordsKeeper;

import java.util.*;

public class MinesweeperModel{
    private GameInfo gameInfo;
    private CellState[][] userField;
    private int openCellsCount;
    private int flagsRemaining;
    private boolean gameIsOngoing;
    private Set<Location> mineLocations;
    private final List<GameLostListener> gameLostListeners;
    private final List<FieldUpdateListener> fieldUpdateListeners;
    private final List<GameWonListener> gameWonListeners;
    private final List<NewGameListener> newGameListeners;
    private final List<MinesCountListener> minesCountListeners;
    private final List<RecordListener> recordListeners;
    private final List<HighScoresListener> highScoresListeners;
    private final MyTimer timer;
    private final RecordsKeeper recordsKeeper;

    public MinesweeperModel() {
        this.mineLocations = new HashSet<>();
        this.gameLostListeners = new ArrayList<>();
        this.fieldUpdateListeners = new ArrayList<>();
        this.gameWonListeners = new ArrayList<>();
        this.newGameListeners = new ArrayList<>();
        this.minesCountListeners = new ArrayList<>();
        this.recordListeners = new ArrayList<>();
        this.highScoresListeners = new ArrayList<>();
        this.timer = new MyTimer();
        this.recordsKeeper = new RecordsKeeper();
    }

    public void setGameInfo(GameInfo gameInfo) {
        this.gameInfo = gameInfo;
    }

    public void addNewGameListener(NewGameListener newGameListener) {
        this.newGameListeners.add(newGameListener);
    }

    public void addMinesCountListener(MinesCountListener minesCountListener) {
        this.minesCountListeners.add(minesCountListener);
    }

    public void addTimerListener(TimerListener timerListener) {
        this.timer.addTimerListener(timerListener);
    }

    public void addGameLostListener(GameLostListener gameLostListener) {
        this.gameLostListeners.add(gameLostListener);
    }

    public void addFieldUpdateListener(FieldUpdateListener fieldUpdateListener) {
        this.fieldUpdateListeners.add(fieldUpdateListener);
    }

    public void addGameWonListener(GameWonListener gameWonListener) {
        this.gameWonListeners.add(gameWonListener);
    }

    public void addRecordListener(RecordListener recordListener) {
        this.recordListeners.add(recordListener);
    }

    public void addHighScoresListener(HighScoresListener highScoresListener) {
        this.highScoresListeners.add(highScoresListener);
        notifyHighScoresListeners(this.recordsKeeper.getRecords());
    }

    private void notifyFieldUpdateListeners() {
        this.fieldUpdateListeners.forEach(fieldUpdateListener ->
                fieldUpdateListener.onFieldUpdate(this.userField, this.gameInfo));
    }

    private void notifyMinesCountListeners(int minesCount) {
        this.minesCountListeners
                .forEach(minesCountListener -> minesCountListener.onMinesCountUpdate(minesCount));
    }

    private void notifyNewGameListeners() {
        this.newGameListeners
                .forEach(newGameListener ->
                        newGameListener.onGameCreating(this.gameInfo));
    }

    private void notifyGameLostListeners() {
        this.gameLostListeners.forEach(GameLostListener::onGameLost);
    }

    private void notifyGameWonListeners() {
        this.gameWonListeners.forEach(GameWonListener::onGameWon);
    }

    private void notifyRecordListeners() {
        this.recordListeners.forEach(RecordListener::onNewRecord);
    }

    private void notifyHighScoresListeners(List<Record> records) {
        this.highScoresListeners.forEach(highScoresListener -> highScoresListener.onHighScoresUpdate(records));
    }

    public void createNewGame() {
        this.timer.stop();
        this.openCellsCount = 0;
        this.flagsRemaining = this.gameInfo.minesCount();
        int fieldHeight = this.gameInfo.fieldHeight();
        int fieldWidth = this.gameInfo.fieldWidth();
        this.userField = new CellState[fieldHeight][fieldWidth];
        for (int i = 0; i < fieldHeight; i++) {
            Arrays.fill(this.userField[i], CellState.UNKNOWN);
        }

        notifyNewGameListeners();
    }

    public void openCell(Location cellLocation) {
        int cellX = cellLocation.x();
        int cellY = cellLocation.y();
        if (!this.userField[cellY][cellX].equals(CellState.UNKNOWN)) {
            return;
        }

        this.openCellsCount++;
        if (this.openCellsCount == 1) {
            this.mineLocations = generateMineLocations(cellLocation);
            this.timer.start();
            this.gameIsOngoing = true;
        }
        if(!this.gameIsOngoing) {
            return;
        }

        boolean isGameLost = this.mineLocations.contains(cellLocation);
        if (isGameLost) {
            finishLostGame();
            return;
        }

        int minesNearby = countMinesNearby(cellLocation);
        this.userField[cellY][cellX] = CellState.getByMinesNearbyCount(minesNearby);
        if (minesNearby == 0) {
            openCellsNearby(cellLocation);
        }
        notifyFieldUpdateListeners();
        if (isGameWon()) {
            finishWonGame();
        }
    }

    private void finishWonGame() {
        this.gameIsOngoing = false;
        this.timer.stop();
        if(this.recordsKeeper.isRecordBeaten(this.gameInfo, this.timer.getSeconds())) {
            notifyRecordListeners();
        }
        notifyGameWonListeners();
    }

    private void finishLostGame() {
        this.gameIsOngoing = false;
        this.mineLocations.forEach(location -> this.userField[location.y()][location.x()] = CellState.MINE);
        this.timer.stop();
        notifyFieldUpdateListeners();
        notifyGameLostListeners();
    }

    private Set<Location> generateMineLocations(Location firstTurnLocation) {
        List<Location> possibleLocations = new ArrayList<>();
        for (int y = 0; y < this.gameInfo.fieldHeight(); y++) {
            for (int x = 0; x < this.gameInfo.fieldWidth(); x++) {
                Location nextLocation = new Location(x, y);
                if(!nextLocation.equals(firstTurnLocation)) {
                    possibleLocations.add(nextLocation);
                }
            }
        }
        Collections.shuffle(possibleLocations);

        return new HashSet<>(possibleLocations.subList(0, this.gameInfo.minesCount()));
    }

    private boolean isGameWon() {
        return this.openCellsCount + this.gameInfo.minesCount() ==
                this.gameInfo.fieldHeight() * this.gameInfo.fieldWidth();
    }

    private boolean isValidLocation(int x, int y) {
        return x >= 0 && x < gameInfo.fieldWidth() && y >= 0 && y < gameInfo.fieldHeight();
    }

    private int countMinesNearby(Location cellLocation) {
        int curCellX = cellLocation.x();
        int curCellY = cellLocation.y();
        int minesNearbyCount = 0;
        for (int x = curCellX - 1; x <= curCellX + 1; x++) {
            for (int y = curCellY - 1; y <= curCellY + 1; y++) {
                if (isValidLocation(x, y) && (x != curCellX || y != curCellY) &&
                        this.mineLocations.contains(new Location(x, y))) {
                    minesNearbyCount++;
                }
            }
        }
        return minesNearbyCount;
    }

    private void openCellsNearby(Location location) {
        int cellX = location.x();
        int cellY = location.y();
        for (int x = cellX - 1; x <= cellX + 1; x++) {
            for (int y = cellY - 1; y <= cellY + 1; y++) {
                if (isValidLocation(x, y) && (x != cellX || y != cellY) && this.gameIsOngoing) {
                    openCell(new Location(x, y));
                }
            }
        }
    }

    public void markCellWithFlag(Location location) {
        if(!this.gameIsOngoing && this.openCellsCount != 0) {
            return;
        }

        int x = location.x();
        int y = location.y();
        CellState cellState = this.userField[y][x];

        if (cellState.equals(CellState.UNKNOWN) && this.flagsRemaining > 0) {
            this.flagsRemaining--;
            this.userField[y][x] = CellState.FLAG;
            notifyFieldUpdateListeners();
            notifyMinesCountListeners(this.flagsRemaining);
            return;
        }

        if (cellState.equals(CellState.FLAG)) {
            this.flagsRemaining++;
            this.userField[y][x] = CellState.UNKNOWN;
            notifyFieldUpdateListeners();
            notifyMinesCountListeners(this.flagsRemaining);
        }
    }

    public void openCellsNearbyIfMinesMarked(Location location) {
        if(!this.gameIsOngoing) {
            return;
        }

        int cellX = location.x();
        int cellY = location.y();
        CellState cellState = this.userField[cellY][cellX];
        if (cellState.equals(CellState.UNKNOWN) || cellState.equals(CellState.FLAG) ||
                cellState.equals(CellState.EMPTY) || cellState.equals(CellState.MINE)) {
            return;
        }

        int flagsNearby = 0;
        for (int x = cellX - 1; x <= cellX + 1; x++) {
            for (int y = cellY - 1; y <= cellY + 1; y++) {
                if (isValidLocation(x, y) && (x != cellX || y != cellY) &&
                        this.userField[y][x].equals(CellState.FLAG)) {
                    flagsNearby++;
                }
            }
        }
        if (flagsNearby == CellState.getMinesNearbyCountByCellState(cellState)) {
            openCellsNearby(location);
        }
    }

    public void writeRecord(String username) {
        this.recordsKeeper.updateRecordIfBeaten(new Record(this.gameInfo, username, this.timer.getSeconds()));
        notifyHighScoresListeners(this.recordsKeeper.getRecords());
    }
}
