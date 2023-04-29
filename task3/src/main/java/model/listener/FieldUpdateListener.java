package model.listener;

import model.Cell;
import model.GameInfo;

public interface FieldUpdateListener {
    void onFieldUpdate(Cell[][] field, GameInfo gameInfo);
}
