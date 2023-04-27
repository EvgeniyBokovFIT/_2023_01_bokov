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

    void openCell(Location cellLocation);

    void markCellWithFlag(Location location);

    void openCellsNearbyIfMinesMarked(Location location);

    void writeRecord(String username);
}
