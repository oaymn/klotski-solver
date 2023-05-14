package dev.oaymn.klotskisolver.ui;

import dev.oaymn.klotskisolver.board.Board;
import dev.oaymn.klotskisolver.board.BoardFactory;
import dev.oaymn.klotskisolver.solver.AStar;
import dev.oaymn.klotskisolver.solver.Node;
import dev.oaymn.klotskisolver.tile.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static dev.oaymn.klotskisolver.config.Config.*;
import static dev.oaymn.klotskisolver.config.Setup.*;
import static dev.oaymn.klotskisolver.tile.TileType.*;
import static javafx.scene.paint.Color.*;

public class Klotski extends Application {

    private final Map<TileType, Color> colorByType = new HashMap<>(4) {{
        put(BIG_SQUARE, RED);
        put(VERTICAL, ORANGE);
        put(HORIZONTAL, MAGENTA);
        put(SMALL_SQUARE, DARKGREEN);
    }};

    @Override
    public void start(Stage stage) {
        Board initialState = BoardFactory.build(INITIAL_STATE_1);
        Board goalState = BoardFactory.build(GOAL_STATE_1);

        GridPane board = new GridPane();
        board.setVgap(5);
        board.setHgap(5);

        initializeBoard(board, initialState);

        Scene scene = new Scene(board, board.prefWidth(-1), board.prefHeight(-1));
        stage.setScene(scene);
        stage.setTitle("Ane Rouge");

        Thread updateThread = new Thread(() ->{
            AStar aStar = new AStar(new Board(goalState));
            List<Node> path = aStar.solve(new Board(initialState));
            path.remove(0);

            path.forEach(node -> {
                Platform.runLater(() -> {
                    board.getChildren().clear();
                    update(board, node.getState());
                });

                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        });

        stage.show();
        updateThread.start();
    }

    public static void main(String[] args) {
        launch();
    }

    private void initializeBoard(GridPane board, Board state) {
        for (int row = 0; row < BOARD_HEIGHT; row++) {
            for (int col = 0; col < BOARD_WIDTH; col++) {
                Rectangle cell = new Rectangle(
                        CELL_WIDTH,
                        CELL_HEIGHT,
                        TRANSPARENT
                );
                board.add(cell, col, row);
            }
        }

        state._getBlocks().forEach(block -> {
            Rectangle tile = new Rectangle(
                    realValue(block.getWidth()),
                    realValue(block.getHeight()),
                    colorByType.get(block.type())
            );

            board.add(
                    tile,
                    block.getPosition().x(),
                    block.getPosition().y(),
                    block.getWidth(),
                    block.getHeight()
            );
        });

        for (Cell emptyCell : state.getEmptyCells()) {
            Rectangle emptyTile = new Rectangle(
                    realValue(emptyCell.getWidth()),
                    realValue(emptyCell.getHeight()),
                    WHITE
            );

            board.add(
                    emptyTile,
                    emptyCell.getPosition().x(),
                    emptyCell.getPosition().y(),
                    emptyCell.getWidth(),
                    emptyCell.getHeight()
            );
        }
    }

    private void update(GridPane board, Board state) {
        state._getBlocks().forEach(block -> {
            Rectangle tile = new Rectangle(
                    realValue(block.getWidth()),
                    realValue(block.getHeight()),
                    colorByType.get(block.type())
            );

            board.add(
                    tile,
                    block.getPosition().x(),
                    block.getPosition().y(),
                    block.getWidth(),
                    block.getHeight()
            );
        });

        for (Cell emptyCell : state.getEmptyCells()) {
            Rectangle emptyTile = new Rectangle(
                    realValue(emptyCell.getWidth()),
                    realValue(emptyCell.getHeight()),
                    WHITE
            );

            board.add(
                    emptyTile,
                    emptyCell.getPosition().x(),
                    emptyCell.getPosition().y(),
                    emptyCell.getWidth(),
                    emptyCell.getHeight()
            );
        }
    }

    private int realValue(int value) {
        return value * 100;
    }
}