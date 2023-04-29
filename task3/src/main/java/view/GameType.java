package view;

public enum GameType {
    NOVICE(9, 9, 10),
    MEDIUM(16, 16, 40),
    EXPERT(16, 30, 99);

    private final int fieldHeight;
    private final int fieldWidth;
    private final int bombsCount;

    GameType(int fieldHeight, int fieldWidth, int bombsCount) {
        this.fieldHeight = fieldHeight;
        this.fieldWidth = fieldWidth;
        this.bombsCount = bombsCount;
    }

    public int getFieldHeight() {
        return fieldHeight;
    }

    public int getFieldWidth() {
        return fieldWidth;
    }

    public int getBombsCount() {
        return bombsCount;
    }
}
