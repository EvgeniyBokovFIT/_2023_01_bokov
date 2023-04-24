package model.listener;

import model.GameInfo;

public interface NewGameListener {
    void onGameCreating(GameInfo gameInfo);
}
