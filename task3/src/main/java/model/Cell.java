package model;

public class Cell{
    private CellState cellState;
    private int minesNearbyCount;
    private boolean armed;

    public Cell(CellState cellState, int minesNearbyCount, boolean armed) {
        this.cellState = cellState;
        this.minesNearbyCount = minesNearbyCount;
        this.armed = armed;
    }

    public CellState getCellState() {
        return this.cellState;
    }

    public void setCellState(CellState cellState) {
        this.cellState = cellState;
    }

    public int getMinesNearbyCount() {
        return this.minesNearbyCount;
    }

    public void setMinesNearbyCount(int minesNearbyCount) {
        this.minesNearbyCount = minesNearbyCount;
    }

    public boolean isArmed() {
        return this.armed;
    }

    public void setArmed(boolean armed) {
        this.armed = armed;
    }
}
