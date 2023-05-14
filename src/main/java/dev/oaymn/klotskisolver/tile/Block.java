package dev.oaymn.klotskisolver.tile;


import java.util.Objects;

public class Block extends Tile {
    private final Character id;
    private final Cell[] occupiedCells;

    public Block(Character id, int width, int height, Position position) {
        super(width, height, position);
        this.id = id;
        occupiedCells = generateOccupiedCells();
    }

    public Character getId() {
        return id;
    }

    public Cell[] getOccupiedCells() {
        return occupiedCells;
    }

    public Cell getReferenceCell() {
        return occupiedCells[0];
    }

    @Override
    public int hashCode() {
        return Objects.hash(type(), position);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Block otherBlock)) {
            return false;
        }
        return type() == otherBlock.type() && position.equals(otherBlock.getPosition());
    }
}