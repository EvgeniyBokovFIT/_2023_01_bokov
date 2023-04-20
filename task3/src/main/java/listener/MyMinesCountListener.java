package listener;

import model.MinesCountListener;
import view.MainWindow;

public class MyMinesCountListener implements MinesCountListener {
    MainWindow mainWindow;

    public MyMinesCountListener(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    @Override
    public void onMinesCountChanges(int minesCount) {
        mainWindow.setBombsCount(minesCount);
    }
}
