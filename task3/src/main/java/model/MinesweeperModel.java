package model;

import model.listener.*;

import java.util.*;

public class MinesweeperModel{
    private GameInfo gameInfo;
    private CellState[][] userField;
    private Set<Location> minesLocations;
    private int openCellsCount;
    private int flagsRemaining;
    private GameLostListener gameLostListener;
    private FieldUpdateListener fieldUpdateListener;
    private GameWonListener gameWonListener;
    private NewGameListener newGameListener;
    private MinesCountListener minesCountListener;
    private TimerListener timerListener;
    private Timer timer;

    public void setGameInfo(GameInfo gameInfo) {
        this.gameInfo = gameInfo;
    }

    public void createNewGame() {
        if(timer != null) {
            timer.cancel();
        }
        this.openCellsCount = 0;
        this.flagsRemaining = this.gameInfo.minesCount();
        int fieldHeight = this.gameInfo.fieldHeight();
        int fieldWidth = this.gameInfo.fieldWidth();
        this.userField = new CellState[fieldHeight][fieldWidth];
        for (int i = 0; i < fieldHeight; i++) {
            Arrays.fill(this.userField[i], CellState.UNKNOWN);
        }

        notifyNewGameListener();
        notifyMinesCountListener(this.gameInfo.minesCount());
    }

    private void notifyMinesCountListener(int minesCount) {
        if(this.minesCountListener != null) {
            this.minesCountListener.onMinesCountChanges(minesCount);
        }
    }

    private void notifyNewGameListener() {
        if (this.newGameListener != null) {
            this.newGameListener.onGameCreating(this.gameInfo.fieldHeight(), this.gameInfo.fieldWidth());
        }
    }

    private void generateMinesLocations(Location firstTurnLocation) {
        this.minesLocations = new HashSet<>();
        Random random = new Random();
        int fieldHeight = this.gameInfo.fieldHeight();
        int fieldWidth = this.gameInfo.fieldWidth();
        while (this.minesLocations.size() < this.gameInfo.minesCount()) {
            int x = random.nextInt(fieldWidth);
            int y = random.nextInt(fieldHeight);
            Location mineLocation = new Location(x, y);
            if(!mineLocation.equals(firstTurnLocation)) {
                this.minesLocations.add(mineLocation);
            }
        }
    }

    public void openCell(Location cellLocation) {
        int CellX = cellLocation.x();
        int CellY = cellLocation.y();
        CellState curCellState = this.userField[CellY][CellX];
        if (!curCellState.equals(CellState.UNKNOWN)) {
            return;
        }

        this.openCellsCount++;
        if (this.openCellsCount == 1) {
            generateMinesLocations(cellLocation);
            this.timer = new Timer();
            this.timer.scheduleAtFixedRate(new TimerTask() {
                private int seconds = 0;
                @Override
                public void run() {
                    timerListener.onTimerUpdate(this.seconds++);
                }
            }, 0, 1000);
        }

        if (this.minesLocations.contains(new Location(CellX, CellY))) {
            this.minesLocations.forEach(location -> this.userField[location.y()][location.x()] = CellState.MINE);
            this.timer.cancel();
            notifyFieldUpdateListener();
            notifyGameLostListener();
            return;
        }

        int minesNearby = countMinesNearby(cellLocation);
        if (minesNearby == 0) {
            this.userField[CellY][CellX] = CellState.EMPTY;
            openCellsNearby(cellLocation);
        }

        this.userField[CellY][CellX] = CellState.getByMinesNearbyCount(minesNearby);
        int fieldSize = this.gameInfo.fieldHeight() * this.gameInfo.fieldWidth();
        if (this.openCellsCount == fieldSize - this.gameInfo.minesCount()) {
            notifyGameWonListener();
            return;
        }
        notifyFieldUpdateListener();
    }

    private void notifyFieldUpdateListener() {
        fieldUpdateListener.onFieldUpdate(this.userField, this.gameInfo);
    }

    private void notifyGameLostListener() {
        if (this.gameLostListener != null) {
            this.gameLostListener.onGameLost();
        }
    }

    private void notifyGameWonListener() {
        if (this.gameWonListener != null) {
            this.gameWonListener.onGameWon();
        }
    }

    private boolean isValidLocation(int x, int y) {
        return x >= 0 && x < gameInfo.fieldWidth() && y >= 0 && y < gameInfo.fieldHeight();
    }

    private int countMinesNearby(Location cellLocation) {
        int curCellX = cellLocation.x();
        int curCellY = cellLocation.y();
        int minesNearby = 0;
        for (int x = curCellX - 1; x <= curCellX + 1; x++) {
            for (int y = curCellY - 1; y <= curCellY + 1; y++) {
                if (isValidLocation(x, y) && (x != curCellX || y != curCellY) &&
                        this.minesLocations.contains(new Location(x, y))) {
                    minesNearby++;
                }
            }
        }
        return minesNearby;
    }

    private void openCellsNearby(Location cellLocation) {
        int curCellX = cellLocation.x();
        int curCellY = cellLocation.y();
        for (int x = curCellX - 1; x <= curCellX + 1; x++) {
            for (int y = curCellY - 1; y <= curCellY + 1; y++) {
                if (isValidLocation(x, y) && (x != curCellX || y != curCellY)) {
                    openCell(new Location(x, y));
                }
            }
        }
    }

    public void setGameLostListener(GameLostListener gameLostListener) {
        this.gameLostListener = gameLostListener;
    }

    public void setCellOpenListener(FieldUpdateListener fieldUpdateListener) {
        this.fieldUpdateListener = fieldUpdateListener;
    }

    public void setGameWonListener(GameWonListener gameWonListener) {
        this.gameWonListener = gameWonListener;
    }

    public void markCellWithFlag(Location location) {
        int x = location.x();
        int y = location.y();
        CellState cellState = this.userField[y][x];

        if (cellState.equals(CellState.UNKNOWN) && this.flagsRemaining > 0) {
            this.flagsRemaining--;
            this.userField[y][x] = CellState.FLAG;
            notifyFieldUpdateListener();
            notifyMinesCountListener(this.flagsRemaining);
            return;
        }

        if (cellState.equals(CellState.FLAG)) {
            this.flagsRemaining++;
            this.userField[y][x] = CellState.UNKNOWN;
            notifyFieldUpdateListener();
            notifyMinesCountListener(this.flagsRemaining);
        }
    }

    public void openCellsNearbyIfMinesMarked(Location location) {
        int cellX = location.x();
        int cellY = location.y();
        CellState cellState = this.userField[cellY][cellX];
        if (cellState.equals(CellState.UNKNOWN) || cellState.equals(CellState.FLAG) ||
                cellState.equals(CellState.EMPTY) || cellState.equals(CellState.MINE)) {
            return;
        }

        int flagsNearby = 0;
        for (int x = cellX - 1; x <= cellX + 1; x++) {
            for (int y = cellY - 1; y <= cellY + 1; y++) {
                if (isValidLocation(x, y) && (x != cellX || y != cellY) && this.userField[y][x].equals(CellState.FLAG)) {
                    flagsNearby++;
                }
            }
        }
        if (flagsNearby == CellState.getMinesNearbyCountByCellState(cellState)) {
            openCellsNearby(new Location(cellX, cellY));
        }
    }

    public void setNewGameListener(NewGameListener newGameListener) {
        this.newGameListener = newGameListener;
    }

    public void setMinesCountListener(MinesCountListener minesCountListener) {
        this.minesCountListener = minesCountListener;
    }

    public void setTimerListener(TimerListener timerListener) {
        this.timerListener = timerListener;
    }
}
