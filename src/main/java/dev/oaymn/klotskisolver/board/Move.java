package dev.oaymn.klotskisolver.board;

import dev.oaymn.klotskisolver.tile.Block;
import dev.oaymn.klotskisolver.tile.Cell;
import dev.oaymn.klotskisolver.tile.TileType;
import dev.oaymn.klotskisolver.enums.Direction;

import java.util.*;

import static dev.oaymn.klotskisolver.tile.TileType.*;
import static dev.oaymn.klotskisolver.enums.Direction.DOWN;
import static dev.oaymn.klotskisolver.enums.Direction.UP;

public class Move {
    private static final Map<TileType, Set<TileType>> EMPTY_BLOCK_INVALID_SWAP = new HashMap<>(3) {{
        put(SMALL_SQUARE, new HashSet<>(List.of(BIG_SQUARE)));
        put(HORIZONTAL, new HashSet<>(List.of(SMALL_SQUARE, VERTICAL)));
        put(VERTICAL, new HashSet<>(List.of(SMALL_SQUARE, HORIZONTAL)));
    }};

    private static boolean canSwap(Cell emptyCell, Block neighborBlock) {
        Set<TileType> invalidTypes = Optional.ofNullable(EMPTY_BLOCK_INVALID_SWAP.get(emptyCell.type()))
                                                .orElseThrow(() ->
                                                            new RuntimeException(
                                                                    "Could not find invalid types for empty cell: %s"
                                                                            .formatted(emptyCell)
                                                            )
                                                );

        return !invalidTypes.contains(neighborBlock.type());
    }

    public static boolean isValidMove(Cell emptyCell, Block neighborBlock, Direction direction) {
        if (!canSwap(emptyCell, neighborBlock)) {
            return false;
        }

        if (direction == UP || direction == DOWN) {
            if (!emptyCell.isOnSameColumn(neighborBlock.getReferenceCell())) {
                return false;
            }
            return switch (emptyCell.type()) {
                case SMALL_SQUARE -> neighborBlock.type() != HORIZONTAL;
                case VERTICAL -> neighborBlock.type() != BIG_SQUARE;
                default -> true;
            };
        } else {
            if (!emptyCell.isOnSameRow(neighborBlock.getReferenceCell())) {
                return false;
            }
            return switch (emptyCell.type()) {
                case SMALL_SQUARE -> neighborBlock.type() != VERTICAL;
                case HORIZONTAL -> neighborBlock.type() != BIG_SQUARE;
                default -> true;
            };
        }
    }
}
