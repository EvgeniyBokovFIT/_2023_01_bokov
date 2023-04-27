package app;

import controller.Controller;
import model.DefaultMinesweeperModel;
import model.MinesweeperModel;
import view.*;

public class Application {
    public static void main(String[] args) {
        // creating view windows
        MainWindow mainWindow = new MainWindow();
        SettingsWindow settingsWindow = new SettingsWindow(mainWindow);
        HighScoresWindow highScoresWindow = new HighScoresWindow(mainWindow);
        LoseWindow loseWindow = new LoseWindow(mainWindow);
        WinWindow winWindow = new WinWindow(mainWindow);
        RecordsWindow recordsWindow = new RecordsWindow(mainWindow);
        // preparing model
        MinesweeperModel model = new DefaultMinesweeperModel();
        model.addGameLostListener(loseWindow);
        model.addFieldUpdateListener(mainWindow);
        model.addGameWonListener(winWindow);
        model.addMinesCountListener(mainWindow);
        model.addTimerListener(mainWindow);
        model.addRecordListener(recordsWindow);
        model.addHighScoresListener(highScoresWindow);
        model.addNewGameListener(mainWindow);
        // creating controller
        Controller controller = new Controller(model);
        // preparing main game window
        mainWindow.setVisible(true);
        mainWindow.setNewGameMenuAction(actionEvent -> controller.handleNewGame());
        mainWindow.setSettingsMenuAction(e -> settingsWindow.setVisible(true));
        mainWindow.setHighScoresMenuAction(e -> highScoresWindow.setVisible(true));
        mainWindow.setExitMenuAction(e -> mainWindow.dispose());
        mainWindow.setCellListener(controller::handleCellClick);
        // preparing settings window
        settingsWindow.setGameTypeListener(controller::handleGameTypeChange);
        // preparing lose window
        loseWindow.setNewGameListener(e -> controller.handleNewGame());
        loseWindow.setExitListener(e -> mainWindow.dispose());
        // preparing win window
        winWindow.setNewGameListener(e -> controller.handleNewGame());
        winWindow.setExitListener(e -> mainWindow.dispose());
        // preparing records window
        recordsWindow.setNameListener(controller::handleUsernameRecord);
    }
}
