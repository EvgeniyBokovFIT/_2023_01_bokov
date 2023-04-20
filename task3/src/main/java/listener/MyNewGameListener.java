package listener;

import model.GameInfo;
import model.NewGameListener;
import view.MainWindow;

public class MyNewGameListener implements NewGameListener {
    private final MainWindow mainWindow;

    public MyNewGameListener(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    @Override
    public void onGameCreating(GameInfo gameInfo) {
        mainWindow.createGameField(gameInfo.fieldHeight(), gameInfo.fieldWidth());
    }
}
