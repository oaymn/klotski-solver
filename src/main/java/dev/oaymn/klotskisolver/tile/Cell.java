package dev.oaymn.klotskisolver.tile;

public class Cell extends Tile {

    public Cell(Position position) {
        super(1, 1, position);
    }

    public Cell(int width, int height, Position position) {
        super(width, height, position);
    }

    public boolean isAdjacent(Cell otherCell) {
        int distance = Math.abs(position.x() - otherCell.getPosition().x()) +
                        Math.abs(position.y() - otherCell.getPosition().y());

        return distance == 1;
    }

    public boolean isOnSameRow(Cell otherCell) {
        return position.y() == otherCell.getPosition().y();
    }

    public boolean isOnSameColumn(Cell otherCell) {
        return position.x() == otherCell.getPosition().x();
    }

    public boolean isBefore(Cell otherCell) {
        return position.x() < otherCell.getPosition().x() ||
                position.y() < otherCell.getPosition().y();
    }

    public Cell combine(Cell otherCell) {
        int combinedCellsWidth = width + Math.abs(position.x() - otherCell.getPosition().x());
        int combinedCellsHeight = height + Math.abs(position.y() - otherCell.getPosition().y());
        Position combinedCellsPosition = isBefore(otherCell) ? position : otherCell.getPosition();
        return new Cell(combinedCellsWidth, combinedCellsHeight, combinedCellsPosition);
    }

    @Override
    public int hashCode() {
        return position.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Cell otherCell)) {
            return false;
        }
        return position.equals(otherCell.getPosition());
    }
}
