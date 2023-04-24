package model;

import java.util.Objects;

public record GameInfo(
        int fieldHeight,
        int fieldWidth,
        int minesCount
) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameInfo gameInfo = (GameInfo) o;
        return this.fieldHeight == gameInfo.fieldHeight &&
                this.fieldWidth == gameInfo.fieldWidth &&
                this.minesCount == gameInfo.minesCount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.fieldHeight, this.fieldWidth, this.minesCount);
    }
}
