package model;

import model.listener.*;

public interface MinesweeperModel {
    void setGameInfo(GameInfo gameInfo);

    void addNewGameListener(NewGameListener newGameListener);

    void addMinesCountListener(MinesCountListener minesCountListener);

    void addTimerListener(TimerListener timerListener);

    void addGameLostListener(GameLostListener gameLostListener);

    void addFieldUpdateListener(FieldUpdateListener fieldUpdateListener);

    void addGameWonListener(GameWonListener gameWonListener);

    void addRecordListener(RecordListener recordListener);

    void addHighScoresListener(HighScoresListener highScoresListener);

    void createNewGame();

    void openCell(Location cellLocation);

    void markCellWithFlag(Location location);

    void openCellsNearbyIfMinesMarked(Location location);

    void writeRecord(String username);
}
