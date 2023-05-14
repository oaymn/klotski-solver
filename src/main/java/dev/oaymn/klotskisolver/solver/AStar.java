package dev.oaymn.klotskisolver.solver;

import dev.oaymn.klotskisolver.board.Board;
import dev.oaymn.klotskisolver.tile.Block;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class AStar {
    private final Board goalState;
    private final PriorityQueue<Node> openSet;
    private final Set<Node> closedSet;

    public AStar(Board goalState) {
        this.goalState = goalState;
        openSet = new PriorityQueue<>();
        closedSet = new HashSet<>();
    }

    public List<Node> solve(Board initialState) {
        Clock clock = Clock.systemDefaultZone();
        Instant startTime, finishTime;
        List<Node> path = new ArrayList<>();
        openSet.offer(new Node(initialState, 0, 0, null));
        int evaluatedStatesCounter = 0;

        startTime = Instant.now(clock);
        while (!openSet.isEmpty()) {
            Node currentNode = openSet.poll();
            closedSet.add(currentNode);
            evaluatedStatesCounter++;

            if (isGoal(currentNode)) {
                reconstructPath(path, currentNode);
                break;
            }

            List<Node> childNodes = generateChildNodes(currentNode);

            for (Node childNode: childNodes) {
                if(closedSet.contains(childNode)) {
                   continue;
                }

                if (openSet.contains(childNode)) {
                    Node openedChildNode = openSet.stream()
                        .filter(node -> node.equals(childNode))
                        .toList().get(0);

                    if (childNode.getGScore() < openedChildNode.getGScore()) {
                        openSet.remove(openedChildNode);
                    } else {
                        continue;
                    }
                }

                openSet.add(childNode);
            }
        }
        finishTime = Instant.now(clock);

        if (path.isEmpty()) {
            System.out.println("Algorithm could not find solution.");
            System.exit(1);
        }

        System.out.println("Solution found.");
        System.out.println("Generated states: " + (openSet.size() + closedSet.size()));
        System.out.println("Evaluated states: " + evaluatedStatesCounter);
        System.out.println("Number of moves: " + path.get(path.size() - 1).getNumberOfMoves());
        System.out.printf("Elapsed time: %d ms.%n", calculateElapsedTime(startTime, finishTime));

        return path;
    }

    private boolean isGoal(Node currentNode) {
        return currentNode.getState().equals(goalState);
    }

    private long calculateElapsedTime(Instant startTime, Instant finishTime) {
        return Duration.between(startTime, finishTime).toMillis();
    }

    private List<Node> generateChildNodes(Node currentNode) {
        List<Node> childNodes = new ArrayList<>();

        currentNode.getState().generateChildBoards().forEach(childBoard ->
                childNodes.add(
                        new Node(
                                childBoard,
                                currentNode.getGScore() + 1,
                                calculateHeuristic(childBoard, goalState),
                                currentNode
                        )
                )
        );

        return childNodes;
    }

    private int calculateHeuristic(Board currentState, Board goalState) {
        int totalDistance = 0;

        List<Block> currentStateBlocks = currentState._getBlocks();
        List<Block> goalStateBlocks = goalState._getBlocks();

        for (Block currentBlock : currentStateBlocks) {
            int minDistance = Integer.MAX_VALUE;
            Block closestToBlock = null;
            for (Block goalBlock : goalStateBlocks) {
                if (currentBlock.type() == goalBlock.type()) {
                    int distance = calculateManhattanDistance(currentBlock, goalBlock);
                    if (distance < minDistance) {
                        minDistance = distance;
                        closestToBlock = goalBlock;
                    }
                }
            }
            totalDistance += minDistance;
            goalStateBlocks.remove(closestToBlock);
        }

        return totalDistance * 2;
    }

    private int calculateManhattanDistance(Block currentBlock, Block goalBlock) {
        return  Math.abs(goalBlock.getPosition().x() - currentBlock.getPosition().x()) +
                Math.abs(goalBlock.getPosition().y() - currentBlock.getPosition().y());
    }

    private void reconstructPath(List<Node> path, Node node) {
        if (Objects.nonNull(node.getParentNode())) {
            reconstructPath(path, node.getParentNode());
        }
        path.add(node);
    }
}
