package dev.oaymn.klotskisolver.solver;

import dev.oaymn.klotskisolver.board.Board;

public class Node implements Comparable<Node> {
    private final Board state;
    private final int gScore;
    private final int hScore;
    private final int fScore;
    private final Node parentNode;
    private final int numberOfMoves;

    public Node(Board board, int gScore, int hScore, Node parentNode) {
        this.state = board;
        this.gScore = gScore;
        this.hScore = hScore;
        fScore = this.gScore + this.hScore;
        this.parentNode = parentNode;
        if (parentNode == null) {
            numberOfMoves = 0;
        } else {
            if (parentNode.getState().getLastMovedBlock() != board.getLastMovedBlock()) {
                numberOfMoves = parentNode.getNumberOfMoves() + 1;
            } else {
                numberOfMoves = parentNode.getNumberOfMoves();
            }
        }
    }

    public Board getState() {
        return state;
    }

    public int getGScore() {
        return gScore;
    }

    public int getHScore() {
        return hScore;
    }

    public int getFScore() {
        return fScore;
    }

    public Node getParentNode() {
        return parentNode;
    }

    public int getNumberOfMoves() {
        return numberOfMoves;
    }

    @Override
    public int compareTo(Node otherNode) {
        int result = Integer.compare(fScore, otherNode.getFScore());
        if (result == 0) {
            result = Integer.compare(gScore, otherNode.getGScore());
        }
        return result;
    }

    @Override
    public int hashCode() {
        return state.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Node otherNode)) {
            return false;
        }
        return state.equals(otherNode.state);
    }

    @Override
    public String toString() {
        return "G: %d, H: %d, F: %d\n%s".formatted(gScore, hScore, fScore, state);
    }
}
