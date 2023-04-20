package model;

public enum CellState {
    UNKNOWN,
    MINES_NEARBY_1,
    MINES_NEARBY_2,
    MINES_NEARBY_3,
    MINES_NEARBY_4,
    MINES_NEARBY_5,
    MINES_NEARBY_6,
    MINES_NEARBY_7,
    MINES_NEARBY_8,
    FLAG,
    MINE,
    EMPTY;

    public static CellState getByMinesNearbyCount(int minesNearbyCount) {
        return switch (minesNearbyCount) {
            case 0 -> EMPTY;
            case 1 -> MINES_NEARBY_1;
            case 2 -> MINES_NEARBY_2;
            case 3 -> MINES_NEARBY_3;
            case 4 -> MINES_NEARBY_4;
            case 5 -> MINES_NEARBY_5;
            case 6 -> MINES_NEARBY_6;
            case 7 -> MINES_NEARBY_7;
            case 9 -> MINES_NEARBY_8;
            default -> UNKNOWN;
        };
    }

    public static int getMinesNearbyCountByCellState(CellState cellState) {
        return switch (cellState) {
            case MINES_NEARBY_1 -> 1;
            case MINES_NEARBY_2 -> 2;
            case MINES_NEARBY_3 -> 3;
            case MINES_NEARBY_4 -> 4;
            case MINES_NEARBY_5 -> 5;
            case MINES_NEARBY_6 -> 6;
            case MINES_NEARBY_7 -> 7;
            case MINES_NEARBY_8 -> 8;
            default -> 0;
        };
    }
}
