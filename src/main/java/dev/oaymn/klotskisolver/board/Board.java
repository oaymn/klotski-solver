package dev.oaymn.klotskisolver.board;

import dev.oaymn.klotskisolver.enums.Direction;
import dev.oaymn.klotskisolver.tile.Block;
import dev.oaymn.klotskisolver.tile.Cell;
import dev.oaymn.klotskisolver.tile.Position;

import java.util.*;

import static dev.oaymn.klotskisolver.config.Config.BOARD_HEIGHT;
import static dev.oaymn.klotskisolver.config.Config.BOARD_WIDTH;
import static dev.oaymn.klotskisolver.board.Move.isValidMove;
import static dev.oaymn.klotskisolver.enums.Direction.DOWN;
import static dev.oaymn.klotskisolver.enums.Direction.RIGHT;
import static dev.oaymn.klotskisolver.tile.TileType.*;

public class Board {
    private static final int width = BOARD_WIDTH;
    private static final int height = BOARD_HEIGHT;
    private final Block[][] blocks;
    private final Cell[] emptyCells;
    private Character lastMovedBlock;

    public Board(Block[][] blocks) {
        this.blocks = blocks;
        emptyCells = new Cell[2];

        int index = 0;
        for (int row = 0; row < blocks.length; row++) {
            for (int col = 0; col < blocks[row].length; col++) {
                if (blocks[row][col] == null) {
                    emptyCells[index++] = new Cell(new Position(col, row));
                }
            }
        }

        lastMovedBlock = null;
    }
    public Board(Board boardToCopy) {
        Block[][] copyBlocks = new Block[boardToCopy.blocks.length][];
        int index = 0;
        for (Block[] row : boardToCopy.blocks) {
            copyBlocks[index++] = Arrays.copyOf(row, row.length);
        }
        blocks = copyBlocks;
        emptyCells = Arrays.copyOf(boardToCopy.getEmptyCells(), boardToCopy.getEmptyCells().length);
        lastMovedBlock = boardToCopy.getLastMovedBlock();
    }

    public Block[][] getBlocks() {
        return blocks;
    }

    public Cell[] getEmptyCells() {
        return emptyCells;
    }

    public Character getLastMovedBlock() {
        return lastMovedBlock;
    }

    public List<Block> _getBlocks() {
        List<Block> nonEmptyBlocks = new ArrayList<>(10);
        Set<Character> visited = new HashSet<>(6);

        for (Block[] row : blocks) {
            for (Block block : row) {
                if (Objects.nonNull(block) && !visited.contains(block.getId())) {
                    nonEmptyBlocks.add(block);
                    if (block.type() != SMALL_SQUARE) {
                        visited.add(block.getId());
                    }
                }
            }
        }
        return nonEmptyBlocks;
    }

    public List<Board> generateChildBoards() {
        List<Board> childBoards = new ArrayList<>(2);
        Map<Map.Entry<Cell, Direction>, Block> movableNeighbors = new HashMap<>(2);

        validateEmptyBlocks();

        for(Cell emptyCell : emptyCells) {
            movableNeighbors.putAll(findMovableNeighbors(emptyCell));
        }

        if (emptyCells[0].isAdjacent(emptyCells[1])) {
            Cell combinedEmptyCells = emptyCells[0].combine(emptyCells[1]);
            movableNeighbors.putAll(findMovableNeighbors(combinedEmptyCells));
        }

        movableNeighbors.forEach((emptyCellDirectionPair, neighborBlock) -> {
            Board newBoard = new Board(this);
            newBoard.swap(emptyCellDirectionPair.getKey(), neighborBlock, emptyCellDirectionPair.getValue());
            childBoards.add(newBoard);
        });

        return childBoards;
    }

    private void validateEmptyBlocks() {
        if (emptyCells.length != 2) {
            throw new RuntimeException(
                    "There should be 2 empty blocks. Currently, %d are found".formatted(emptyCells.length)
            );
        }
    }

    private Map<Map.Entry<Cell, Direction>, Block> findMovableNeighbors(Cell emptyCell) {
        Map<Map.Entry<Cell, Direction>, Block> movableNeighbors = new HashMap<>(2);

        for (Direction direction : Direction.values()) {
            Position positionToMoveTo = emptyCell.getPosition()
                                            .addOffset(direction.getDx(), direction.getDy());

            if (emptyCell.type() == VERTICAL && direction == DOWN ||
                    emptyCell.type() == HORIZONTAL && direction == RIGHT) {
                positionToMoveTo = emptyCell.getPosition()
                                    .addOffset(emptyCell.getWidth() * direction.getDx(),
                                                emptyCell.getHeight() * direction.getDy());
            }

            if (!positionToMoveTo.isInbounds(width, height)) {
                continue;
            }

            Block neighborBlock = blocks[positionToMoveTo.y()][positionToMoveTo.x()];

            if (neighborBlock == null) {
                continue;
            }

            if (isValidMove(emptyCell, neighborBlock, direction)) {
                movableNeighbors.put(Map.entry(emptyCell, direction), neighborBlock);
            }
        }
        return movableNeighbors;
    }

    private void swap(Cell emptyCell, Block neighborBlock, Direction direction) {

        Block movedNeighborBlock = new Block(
                neighborBlock.getId(),
                neighborBlock.getWidth(),
                neighborBlock.getHeight(),
                neighborBlock.getPosition()
                        .addOffset(-(emptyCell.getWidth() * direction.getDx()),
                                    -(emptyCell.getHeight() * direction.getDy()))
        );

        Cell movedEmptyCell = new Cell(
                emptyCell.getWidth(),
                emptyCell.getHeight(),
                emptyCell.getPosition()
                        .addOffset(neighborBlock.getWidth() * direction.getDx(),
                                    neighborBlock.getHeight() * direction.getDy())
        );

        clearBlock(neighborBlock);
        fillBlock(movedNeighborBlock);

        if (emptyCell.type() != SMALL_SQUARE) {
            for (Cell cell : emptyCell.generateOccupiedCells()) {
                clearEmptyCell(cell);
            }
            for (Cell cell : movedEmptyCell.generateOccupiedCells()) {
                fillEmptyCell(cell);
            }
        } else {
            clearEmptyCell(emptyCell);
            fillEmptyCell(movedEmptyCell);
        }

        lastMovedBlock = movedNeighborBlock.getId();
    }

    private void clearBlock(Block block) {
        for (Cell occupiedCell : block.getOccupiedCells()) {
            blocks[occupiedCell.getPosition().y()][occupiedCell.getPosition().x()] = null;
        }
    }

    private void fillBlock(Block block) {
        for (Cell occupiedCell : block.getOccupiedCells()) {
            blocks[occupiedCell.getPosition().y()][occupiedCell.getPosition().x()] = block;
        }
    }

    private void clearEmptyCell(Cell cell) {
        for (int i = 0; i < emptyCells.length; i++) {
            if (emptyCells[i] != null && emptyCells[i].equals(cell)) {
                emptyCells[i] = null;
                return;
            }
        }
    }

    private void fillEmptyCell(Cell cell) {
        for (int i = 0; i < emptyCells.length; i++) {
            if (emptyCells[i] == null) {
                emptyCells[i] = cell;
                return;
            }
        }
    }

    @Override
    public int hashCode() {
        int hash = 1;
        for (Block[] row : blocks) {
            for (Block block : row) {
                if (block == null) {
                    continue;
                }
                hash = hash * 31 + block.hashCode();
            }
        }
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Board otherBoard)) {
            return false;
        }

        for (int row = 0; row < blocks.length; row++) {
            for (int col = 0; col < blocks[row].length; col++) {
                if (blocks[row][col] == null) {
                    continue;
                }
                if (!blocks[row][col].equals(otherBoard.getBlocks()[row][col])) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("-----------------\n");
        for (Block[] row : blocks) {
            stringBuilder.append("| ");
            for (Block block : row) {
                stringBuilder.append(block == null ? " " : block.getId());
                stringBuilder.append(" | ");
            }
            stringBuilder.append("\n-----------------\n");
        }
        return stringBuilder.toString();
    }
}

