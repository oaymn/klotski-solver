package dev.oaymn.klotskisolver.tile;

import java.util.Objects;

public record Position(int x, int y) {

    public boolean isInbounds(int boardWidth, int boardHeight) {
        return x >= 0 && x < boardWidth && y >= 0 && y < boardHeight;
    }

    public Position addOffset(int dx, int dy) {
        return new Position(x + dx, y + dy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Position otherPosition)) {
            return false;
        }
        return x == otherPosition.x() && y == otherPosition.y();
    }

    @Override
    public String toString() {
        return "(%d, %d)".formatted(x, y);
    }
}
