package model;

import model.listener.*;
import model.record.Record;
import model.record.RecordsKeeper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DefaultMinesweeperModel implements MinesweeperModel{
    private GameInfo gameInfo;
    private Cell[][] gameField;
    private int openCellsCount;
    private int flagsRemaining;
    private GameState gameState;
    private final List<GameLostListener> gameLostListeners;
    private final List<FieldUpdateListener> fieldUpdateListeners;
    private final List<GameWonListener> gameWonListeners;
    private final List<NewGameListener> newGameListeners;
    private final List<MinesCountListener> minesCountListeners;
    private final List<RecordListener> recordListeners;
    private final List<HighScoresListener> highScoresListeners;
    private final MyTimer timer;
    private final RecordsKeeper recordsKeeper;

    public DefaultMinesweeperModel() {
        this.gameLostListeners = new ArrayList<>();
        this.fieldUpdateListeners = new ArrayList<>();
        this.gameWonListeners = new ArrayList<>();
        this.newGameListeners = new ArrayList<>();
        this.minesCountListeners = new ArrayList<>();
        this.recordListeners = new ArrayList<>();
        this.highScoresListeners = new ArrayList<>();
        this.timer = new MyTimer();
        this.recordsKeeper = new RecordsKeeper();
        this.gameState = GameState.NONE;
    }

    @Override
    public void addNewGameListener(NewGameListener newGameListener) {
        this.newGameListeners.add(newGameListener);
    }

    @Override
    public void addMinesCountListener(MinesCountListener minesCountListener) {
        this.minesCountListeners.add(minesCountListener);
    }

    @Override
    public void addTimerListener(TimerListener timerListener) {
        this.timer.addTimerListener(timerListener);
    }

    @Override
    public void addGameLostListener(GameLostListener gameLostListener) {
        this.gameLostListeners.add(gameLostListener);
    }

    @Override
    public void addFieldUpdateListener(FieldUpdateListener fieldUpdateListener) {
        this.fieldUpdateListeners.add(fieldUpdateListener);
    }

    @Override
    public void addGameWonListener(GameWonListener gameWonListener) {
        this.gameWonListeners.add(gameWonListener);
    }

    @Override
    public void addRecordListener(RecordListener recordListener) {
        this.recordListeners.add(recordListener);
    }

    @Override
    public void addHighScoresListener(HighScoresListener highScoresListener) {
        this.highScoresListeners.add(highScoresListener);
        notifyHighScoresListeners(this.recordsKeeper.getRecords());
    }

    private void notifyFieldUpdateListeners() {
        this.fieldUpdateListeners.forEach(fieldUpdateListener ->
                fieldUpdateListener.onFieldUpdate(this.gameField, this.gameInfo));
    }

    private void notifyMinesCountListeners(int minesCount) {
        this.minesCountListeners
                .forEach(minesCountListener -> minesCountListener.onMinesCountUpdate(minesCount));
    }

    private void notifyNewGameListeners() {
        this.newGameListeners
                .forEach(newGameListener -> newGameListener.onGameCreating(this.gameInfo));
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

    @Override
    public void initGame(GameInfo gameInfo) {
        this.gameState = GameState.NONE;
        this.gameInfo = gameInfo;
        this.timer.stop();
        this.openCellsCount = 0;
        this.flagsRemaining = this.gameInfo.minesCount();
        //инициализирую поле до первого открытия ячейки, так как пользователь может поставить флаг до начала игры
        initGameField();
        notifyNewGameListeners();
    }

    @Override
    public void initGame() {
        initGame(this.gameInfo);
    }

    @Override
    public void openCell(Location cellLocation) {
        int cellX = cellLocation.x();
        int cellY = cellLocation.y();
        if (this.gameState.equals(GameState.NONE) &&
                this.gameField[cellY][cellX].getCellState().equals(CellState.CLOSED)) {
            startGame(cellLocation);
        }

        if (!this.gameState.equals(GameState.RUNNING)) {
            return;
        }

        if (!this.gameField[cellY][cellX].getCellState().equals(CellState.CLOSED)) {
            return;
        }

        if (this.gameField[cellY][cellX].isArmed()) {
            finishLostGame();
            return;
        }
        this.openCellsCount++;
        int minesNearby = countMinesNearby(cellLocation);
        this.gameField[cellY][cellX].setMinesNearbyCount(minesNearby);
        this.gameField[cellY][cellX].setCellState(CellState.OPEN);
        if (minesNearby == 0) {
            openCellsNearby(cellLocation);
        }
        notifyFieldUpdateListeners();
        if (this.gameState.equals(GameState.RUNNING) && allEmptyCellsOpen()) {
            finishWonGame();
        }
    }

    private void startGame(Location cellLocation) {
        generateMineLocations(cellLocation);
        this.timer.start();
        this.gameState = GameState.RUNNING;
    }

    private void initGameField() {
        int fieldHeight = this.gameInfo.fieldHeight();
        int fieldWidth = this.gameInfo.fieldWidth();
        this.gameField = new Cell[fieldHeight][fieldWidth];
        for (int x = 0; x < fieldWidth; x++) {
            for (int y = 0; y < fieldHeight; y++) {
                this.gameField[y][x] = new Cell(CellState.CLOSED, 0, false);
            }
        }
    }

    private void finishWonGame() {
        this.gameState = GameState.WON;
        this.timer.stop();
        if (this.recordsKeeper.isRecordBeaten(this.gameInfo, this.timer.getSeconds())) {
            notifyRecordListeners();
        }
        notifyGameWonListeners();
    }

    private void finishLostGame() {
        this.gameState = GameState.LOST;
        for (int x = 0; x < this.gameInfo.fieldWidth(); x++) {
            for (int y = 0; y < this.gameInfo.fieldHeight(); y++) {
                this.gameField[y][x].setCellState(CellState.OPEN);
            }
        }
        this.timer.stop();
        notifyFieldUpdateListeners();
        notifyGameLostListeners();
    }

    private void generateMineLocations(Location firstTurnLocation) {
        List<Location> possibleLocations = new ArrayList<>();
        for (int y = 0; y < this.gameInfo.fieldHeight(); y++) {
            for (int x = 0; x < this.gameInfo.fieldWidth(); x++) {
                if (x != firstTurnLocation.x() || y != firstTurnLocation.y()) {
                    possibleLocations.add(new Location(x, y));
                }
            }
        }
        Collections.shuffle(possibleLocations);
        for (Location location : possibleLocations.subList(0, this.gameInfo.minesCount())) {
            this.gameField[location.y()][location.x()].setArmed(true);
        }
    }

    private boolean allEmptyCellsOpen() {
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
                if (isValidLocation(x, y) && (x != curCellX || y != curCellY) && this.gameField[y][x].isArmed()) {
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
                if (isValidLocation(x, y) && (x != cellX || y != cellY) && this.gameState.equals(GameState.RUNNING)) {
                    openCell(new Location(x, y));
                }
            }
        }
    }

    @Override
    public void markCellWithFlag(Location location) {
        if (this.gameState.equals(GameState.LOST) || this.gameState.equals(GameState.WON)) {
            return;
        }

        int x = location.x();
        int y = location.y();
        CellState cellState = this.gameField[y][x].getCellState();

        if (cellState.equals(CellState.CLOSED) && this.flagsRemaining > 0) {
            this.flagsRemaining--;
            this.gameField[y][x].setCellState(CellState.MARKED);
            notifyFieldUpdateListeners();
            notifyMinesCountListeners(this.flagsRemaining);
            return;
        }

        if (cellState.equals(CellState.MARKED)) {
            this.flagsRemaining++;
            this.gameField[y][x].setCellState(CellState.CLOSED);
            notifyFieldUpdateListeners();
            notifyMinesCountListeners(this.flagsRemaining);
        }
    }

    @Override
    public void openCellsNearbyIfMinesMarked(Location location) {
        if (!this.gameState.equals(GameState.RUNNING)) {
            return;
        }

        int cellX = location.x();
        int cellY = location.y();
        CellState cellState = this.gameField[cellY][cellX].getCellState();
        if (!cellState.equals(CellState.OPEN)) {
            return;
        }
        //count flags nearby count
        int flagsNearby = 0;
        for (int x = cellX - 1; x <= cellX + 1; x++) {
            for (int y = cellY - 1; y <= cellY + 1; y++) {
                if (isValidLocation(x, y) && (x != cellX || y != cellY) &&
                        this.gameField[y][x].getCellState().equals(CellState.MARKED)) {
                    flagsNearby++;
                }
            }
        }
        if (flagsNearby == this.gameField[cellY][cellX].getMinesNearbyCount()) {
            openCellsNearby(location);
        }
    }

    @Override
    public void writeRecord(String username) {
        this.recordsKeeper.updateRecordIfBeaten(new Record(
                this.gameInfo, username, this.timer.getSeconds()));
        notifyHighScoresListeners(this.recordsKeeper.getRecords());
    }
}
