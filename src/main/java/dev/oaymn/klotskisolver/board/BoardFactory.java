package dev.oaymn.klotskisolver.board;

import dev.oaymn.klotskisolver.tile.Block;
import dev.oaymn.klotskisolver.tile.Position;

import java.util.HashMap;
import java.util.Map;

public class BoardFactory {

    public static Board build(char[][] board) {
        Block[][] blocks = new Block[board.length][board[0].length];
        Map<Character, Block> visited = new HashMap<>(10);

        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                char cell = board[row][col];

                if (cell == ' ') {
                    blocks[row][col] = null;
                    continue;
                }

                if (visited.containsKey(cell)) {
                    blocks[row][col] = visited.get(cell);
                    continue;
                }

                Position blockPosition = new Position(col, row);

                int blockWidth = 1;
                int blockHeight = 1;
                while (col + blockWidth < board[row].length && board[row][col + blockWidth] == cell) {
                    blockWidth++;
                }
                while (row + blockHeight < board.length && board[row + blockHeight][col] == cell) {
                    blockHeight++;
                }

                Block block = new Block(cell, blockWidth, blockHeight, blockPosition);
                visited.put(cell, block);

                blocks[row][col] = block;
            }
        }

        return new Board(blocks);
    }
}
