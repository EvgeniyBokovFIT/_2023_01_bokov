package model;

import model.listener.*;

public interface MinesweeperModel {
    void addNewGameListener(NewGameListener newGameListener);

    void addMinesCountListener(MinesCountListener minesCountListener);

    void addTimerListener(TimerListener timerListener);

    void addGameLostListener(GameLostListener gameLostListener);

    void addFieldUpdateListener(FieldUpdateListener fieldUpdateListener);

    void addGameWonListener(GameWonListener gameWonListener);

    void addRecordListener(RecordListener recordListener);

    void addHighScoresListener(HighScoresListener highScoresListener);

    void initGame(GameInfo gameInfo);

    void initGame();

    void openCell(int x, int y);

    void markCellWithFlag(int x, int y);

    void openCellsNearbyIfMinesMarked(int x, int y);

    void writeRecord(String username);
}
