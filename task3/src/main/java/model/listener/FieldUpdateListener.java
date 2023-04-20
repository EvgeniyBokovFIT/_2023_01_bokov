package model.listener;

import model.CellState;
import model.GameInfo;

public interface FieldUpdateListener {
    void onFieldUpdate(CellState[][] field, GameInfo gameInfo);
}
