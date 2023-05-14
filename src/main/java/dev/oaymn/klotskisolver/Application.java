package dev.oaymn.klotskisolver;

import dev.oaymn.klotskisolver.board.Board;
import dev.oaymn.klotskisolver.board.BoardFactory;
import dev.oaymn.klotskisolver.solver.AStar;
import dev.oaymn.klotskisolver.solver.Node;

import java.util.List;

import static dev.oaymn.klotskisolver.config.Setup.GOAL_STATE_1;
import static dev.oaymn.klotskisolver.config.Setup.INITIAL_STATE_1;

public class Application {
    public static void main(String[] args) {

        Board initialState = BoardFactory.build(INITIAL_STATE_1);
        Board goalState = BoardFactory.build(GOAL_STATE_1);

        System.out.println();
        System.out.println("Initial State: \n" + initialState);
        System.out.println("Goal State: \n" + goalState);

        AStar aStar = new AStar(goalState);
        List<Node> path = aStar.solve(initialState);
        System.out.println("Number of moves: " + path.get(path.size() - 1).getNumberOfMoves());
        System.out.println();
        System.out.println("Solution: ");
        path.forEach(node -> System.out.println(node.getState()));
    }
}
