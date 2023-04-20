package app;

import controller.Controller;
import listener.MyFieldUpdateListener;
import listener.MyMinesCountListener;
import listener.MyNewGameListener;
import model.MinesweeperModel;
import view.*;

public class Application {
    public static void main(String[] args) {
        MainWindow mainWindow = new MainWindow();
        SettingsWindow settingsWindow = new SettingsWindow(mainWindow);
        HighScoresWindow highScoresWindow = new HighScoresWindow(mainWindow);
        LoseWindow loseWindow = new LoseWindow(mainWindow);
        WinWindow winWindow = new WinWindow(mainWindow);

        MinesweeperModel model = new MinesweeperModel();
        model.setGameLostListener(() -> loseWindow.setVisible(true));
        model.setCellOpenListener(new MyFieldUpdateListener(mainWindow));
        model.setGameWonListener(() -> winWindow.setVisible(true));
        model.setNewGameListener(new MyNewGameListener(mainWindow));
        model.setMinesCountListener(new MyMinesCountListener(mainWindow));
        Controller controller = new Controller(model);

        mainWindow.setNewGameMenuAction(actionEvent -> controller.handleNewGame());
        mainWindow.setSettingsMenuAction(e -> settingsWindow.setVisible(true));
        mainWindow.setHighScoresMenuAction(e -> highScoresWindow.setVisible(true));
        mainWindow.setExitMenuAction(e -> mainWindow.dispose());

        mainWindow.setCellListener(controller::handleCellClick);
        mainWindow.setVisible(true);

        settingsWindow.setGameTypeListener(controller::handleGameTypeChange);

        loseWindow.setNewGameListener(e -> controller.handleNewGame());
        loseWindow.setExitListener(e -> mainWindow.dispose());

        winWindow.setNewGameListener(e -> controller.handleNewGame());
        winWindow.setExitListener(e -> mainWindow.dispose());
    }
}
