package model;

import model.listener.*;
import model.record.Record;
import model.record.RecordsKeeper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    public void openCell(int x, int y) {
        if (this.gameState.equals(GameState.NONE) &&
                this.gameField[y][x].getCellState().equals(CellState.CLOSED)) {
            startGame(x, y);
        }

        if (!this.gameState.equals(GameState.RUNNING)) {
            return;
        }

        if (!this.gameField[y][x].getCellState().equals(CellState.CLOSED)) {
            return;
        }

        if (this.gameField[y][x].isArmed()) {
            finishLostGame();
            return;
        }
        this.openCellsCount++;
        int minesNearby = countMinesNearby(x, y);
        this.gameField[y][x].setMinesNearbyCount(minesNearby);
        this.gameField[y][x].setCellState(CellState.OPEN);
        if (minesNearby == 0) {
            openCellsNearby(x, y);
        }
        notifyFieldUpdateListeners();
        if (this.gameState.equals(GameState.RUNNING) && allEmptyCellsOpen()) {
            finishWonGame();
        }
    }

    private void startGame(int x, int y) {
        generateMineLocations(x, y);
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

    private void generateMineLocations(int firstTurnX, int firstTurnY) {
        Random random = new Random();
        int fieldHeight = this.gameInfo.fieldHeight();
        int fieldWidth = this.gameInfo.fieldWidth();
        int firstTurnIndex = firstTurnY * fieldWidth + firstTurnX;
        int armedCellsCount = 0;
        while (armedCellsCount < this.gameInfo.minesCount()){
            int cellWithMineIndex = random.nextInt(fieldHeight * fieldWidth);
            int x = cellWithMineIndex % fieldWidth;
            int y = cellWithMineIndex / fieldWidth;
            if(cellWithMineIndex != firstTurnIndex && !this.gameField[y][x].isArmed()) {
                this.gameField[y][x].setArmed(true);
                armedCellsCount++;
            }
        }
    }

    private boolean allEmptyCellsOpen() {
        return this.openCellsCount + this.gameInfo.minesCount() ==
                this.gameInfo.fieldHeight() * this.gameInfo.fieldWidth();
    }

    private boolean isValidLocation(int x, int y) {
        return x >= 0 && x < gameInfo.fieldWidth() && y >= 0 && y < gameInfo.fieldHeight();
    }

    private int countMinesNearby(int cellX, int cellY) {
        int minesNearbyCount = 0;
        for (int x = cellX - 1; x <= cellX + 1; x++) {
            for (int y = cellY - 1; y <= cellY + 1; y++) {
                if (isValidLocation(x, y) && (x != cellX || y != cellY) && this.gameField[y][x].isArmed()) {
                    minesNearbyCount++;
                }
            }
        }
        return minesNearbyCount;
    }

    private void openCellsNearby(int cellX, int cellY) {
        for (int x = cellX - 1; x <= cellX + 1; x++) {
            for (int y = cellY - 1; y <= cellY + 1; y++) {
                if (isValidLocation(x, y) && (x != cellX || y != cellY) && this.gameState.equals(GameState.RUNNING)) {
                    openCell(x, y);
                }
            }
        }
    }

    @Override
    public void markCellWithFlag(int x, int y) {
        if (this.gameState.equals(GameState.LOST) || this.gameState.equals(GameState.WON)) {
            return;
        }

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
    public void openCellsNearbyIfMinesMarked(int cellX, int cellY) {
        if (!this.gameState.equals(GameState.RUNNING)) {
            return;
        }

        CellState cellState = this.gameField[cellY][cellX].getCellState();
        if (!cellState.equals(CellState.OPEN)) {
            return;
        }
        // count flags nearby count
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
            openCellsNearby(cellX, cellY);
        }
    }

    @Override
    public void writeRecord(String username) {
        this.recordsKeeper.updateRecordIfBeaten(new Record(
                this.gameInfo, username, this.timer.getSeconds()));
        notifyHighScoresListeners(this.recordsKeeper.getRecords());
    }
}
