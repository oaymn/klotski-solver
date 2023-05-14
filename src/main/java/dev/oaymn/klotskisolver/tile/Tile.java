package dev.oaymn.klotskisolver.tile;

import static dev.oaymn.klotskisolver.tile.TileType.*;
import static dev.oaymn.klotskisolver.tile.TileType.VERTICAL;

public class Tile {
    protected final int width;
    protected final int height;
    protected final Position position;

    public Tile(int width, int height, Position position) {
        this.width = width;
        this.height = height;
        this.position = position;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Position getPosition() {
        return position;
    }

    public TileType type() {
        if (width == height) {
            return (width == 1) ? SMALL_SQUARE : BIG_SQUARE;
        }
        return (width > height) ? HORIZONTAL : VERTICAL;
    }

    public Cell[] generateOccupiedCells() {
        Cell[] occupiedCells = new Cell[width * height];

        int index = 0;
        for (int y = position.y(); y < position.y() + height; y++) {
            for (int x = position.x(); x < position.x() + width; x++) {
                occupiedCells[index++] = new Cell(new Position(x, y));
            }
        }
        return occupiedCells;
    }
}
