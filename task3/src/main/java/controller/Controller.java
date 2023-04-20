package controller;

import model.GameInfo;
import model.Location;
import model.MinesweeperModel;
import view.ButtonType;
import view.GameType;

public class Controller {
    private final MinesweeperModel model;

    public Controller(MinesweeperModel model) {
        this.model = model;
        model.setGameInfo(new GameInfo(
                GameType.NOVICE.getFieldHeight(), GameType.NOVICE.getFieldWidth(), GameType.NOVICE.getMinesCount()));
        model.createNewGame();
    }

    public void handleCellClick(int x, int y, ButtonType buttonType) {
        if (buttonType.equals(ButtonType.LEFT_BUTTON)) {
            model.openCell(new Location(x, y));
            return;
        }

        if (buttonType.equals(ButtonType.RIGHT_BUTTON)) {
            model.markCellWithFlag(new Location(x, y));
            return;
        }

        if (buttonType.equals(ButtonType.MIDDLE_BUTTON)) {
            model.openCellsNearbyIfMinesMarked(new Location(x, y));
        }
    }

    public void handleGameTypeChange(GameType gameType) {
        model.setGameInfo(new GameInfo(
                gameType.getFieldHeight(), gameType.getFieldWidth(), gameType.getMinesCount()));
        model.createNewGame();
    }

    public void handleNewGame() {
        model.createNewGame();
    }
}
