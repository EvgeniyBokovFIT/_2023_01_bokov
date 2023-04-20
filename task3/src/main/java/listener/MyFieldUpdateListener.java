package listener;

import model.CellState;
import model.FieldUpdateListener;
import model.GameInfo;
import view.GameImage;
import view.MainWindow;

public class MyFieldUpdateListener implements FieldUpdateListener {
    private final MainWindow mainWindow;

    public MyFieldUpdateListener(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    @Override
    public void onFieldUpdate(CellState[][] field, GameInfo gameInfo) {
        for (int y = 0; y < gameInfo.fieldHeight(); y++) {
            for (int x = 0; x < gameInfo.fieldWidth(); x++) {
                mainWindow.setCellImage(x, y, getGameImageByCellState(field[y][x]));
            }
        }
    }

    private static GameImage getGameImageByCellState(CellState cellState) {
        return switch (cellState) {
            case MINES_NEARBY_1 -> GameImage.NUM_1;
            case MINES_NEARBY_2 -> GameImage.NUM_2;
            case MINES_NEARBY_3 -> GameImage.NUM_3;
            case MINES_NEARBY_4 -> GameImage.NUM_4;
            case MINES_NEARBY_5 -> GameImage.NUM_5;
            case MINES_NEARBY_6 -> GameImage.NUM_6;
            case MINES_NEARBY_7 -> GameImage.NUM_7;
            case MINES_NEARBY_8 -> GameImage.NUM_8;
            case FLAG -> GameImage.MARKED;
            case MINE -> GameImage.BOMB;
            case UNKNOWN -> GameImage.CLOSED;
            case EMPTY -> GameImage.EMPTY;
        };
    }
}
