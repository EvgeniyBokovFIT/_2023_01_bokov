package model;

import java.util.Objects;

public record Location(int x, int y) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return this.x == location.x && this.y == location.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
