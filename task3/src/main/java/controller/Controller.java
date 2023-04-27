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
        this.model.initGame(new GameInfo(
                GameType.NOVICE.getFieldHeight(), GameType.NOVICE.getFieldWidth(), GameType.NOVICE.getBombsCount()));
    }

    public void handleCellClick(int x, int y, ButtonType buttonType) {
        if (buttonType.equals(ButtonType.LEFT_BUTTON)) {
            this.model.openCell(new Location(x, y));
            return;
        }

        if (buttonType.equals(ButtonType.RIGHT_BUTTON)) {
            this.model.markCellWithFlag(new Location(x, y));
            return;
        }

        if (buttonType.equals(ButtonType.MIDDLE_BUTTON)) {
            this.model.openCellsNearbyIfMinesMarked(new Location(x, y));
        }
    }

    public void handleGameTypeChange(GameType gameType) {
        this.model.initGame(new GameInfo(
                gameType.getFieldHeight(), gameType.getFieldWidth(), gameType.getBombsCount()));
    }

    public void handleNewGame() {
        this.model.initGame();
    }

    public void handleUsernameRecord(String username) {
        this.model.writeRecord(username);
    }
}
