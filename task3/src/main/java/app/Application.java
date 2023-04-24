package app;

import controller.Controller;
import model.MinesweeperModel;
import view.*;

public class Application {
    public static void main(String[] args) {
        MainWindow mainWindow = new MainWindow();
        SettingsWindow settingsWindow = new SettingsWindow(mainWindow);
        HighScoresWindow highScoresWindow = new HighScoresWindow(mainWindow);
        LoseWindow loseWindow = new LoseWindow(mainWindow);
        WinWindow winWindow = new WinWindow(mainWindow);
        RecordsWindow recordsWindow = new RecordsWindow(mainWindow);

        MinesweeperModel model = new MinesweeperModel();
        model.addGameLostListener(() -> loseWindow.setVisible(true));
        model.addFieldUpdateListener(mainWindow);
        model.addGameWonListener(() -> winWindow.setVisible(true));
        model.addMinesCountListener(mainWindow);
        model.addTimerListener(mainWindow);
        model.addRecordListener(recordsWindow);
        model.addHighScoresListener(highScoresWindow);
        model.addNewGameListener(mainWindow);
        Controller controller = new Controller(model);

        mainWindow.setVisible(true);
        mainWindow.setNewGameMenuAction(actionEvent -> controller.handleNewGame());
        mainWindow.setSettingsMenuAction(e -> settingsWindow.setVisible(true));
        mainWindow.setHighScoresMenuAction(e -> highScoresWindow.setVisible(true));
        mainWindow.setExitMenuAction(e -> mainWindow.dispose());
        mainWindow.setCellListener(controller::handleCellClick);

        settingsWindow.setGameTypeListener(controller::handleGameTypeChange);

        loseWindow.setNewGameListener(e -> controller.handleNewGame());
        loseWindow.setExitListener(e -> mainWindow.dispose());

        winWindow.setNewGameListener(e -> controller.handleNewGame());
        winWindow.setExitListener(e -> mainWindow.dispose());

        recordsWindow.setNameListener(controller::handleUsernameRecord);
    }
}
