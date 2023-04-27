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
        if (o instanceof GameInfo gameInfo) {
            return this.fieldHeight == gameInfo.fieldHeight &&
                    this.fieldWidth == gameInfo.fieldWidth &&
                    this.minesCount == gameInfo.minesCount;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.fieldHeight, this.fieldWidth, this.minesCount);
    }
}
