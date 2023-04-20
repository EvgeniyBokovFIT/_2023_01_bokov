package view;

public enum GameType {
    NOVICE(9, 9, 10),
    MEDIUM(16, 16, 40),
    EXPERT(16, 30, 99);

    private final int fieldHeight;
    private final int fieldWidth;
    private final int minesCount;

    GameType(int fieldHeight, int fieldWidth, int minesCount) {
        this.fieldHeight = fieldHeight;
        this.fieldWidth = fieldWidth;
        this.minesCount = minesCount;
    }

    public int getFieldHeight() {
        return fieldHeight;
    }

    public int getFieldWidth() {
        return fieldWidth;
    }

    public int getMinesCount() {
        return minesCount;
    }
}
