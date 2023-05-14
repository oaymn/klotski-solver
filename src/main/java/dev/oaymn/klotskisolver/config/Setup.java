package dev.oaymn.klotskisolver.config;

public class Setup {

    public static final char[][] INITIAL_STATE_1 = {
            {'A', 'B', 'B', 'C'},
            {'A', 'B', 'B', 'C'},
            {'D', 'E', 'E', 'F'},
            {'D', 'G', 'H', 'F'},
            {'I', ' ', ' ', 'J'}
    };

    public static final char[][] INITIAL_STATE_2 = {
            {'I', 'J', 'E', 'E'},
            {'A', 'B', 'B', 'H'},
            {'A', 'B', 'B', 'G'},
            {' ', 'D', 'Z', 'Z'},
            {' ', 'D', 'Y', 'Y'}
    };

    public static final char[][] INITIAL_STATE_3 = {
            {'A', 'B', 'B', 'C'},
            {'D', 'B', 'B', 'E'},
            {'F', 'G', 'H', 'I'},
            {'J', 'K', 'L', 'M'},
            {'N', ' ', ' ', 'O'}
    };

    public static final char[][] GOAL_STATE_1 = {
            {'D', 'A', 'F', 'C'},
            {'D', 'A', 'F', 'C'},
            {'E', 'E', 'G', 'H'},
            {'I', 'B', 'B', ' '},
            {'J', 'B', 'B', ' '}
    };

    public static final char[][] GOAL_STATE_2 = {
            {'D', 'A', 'I', 'J'},
            {'D', 'A', 'Z', 'Z'},
            {'X', 'X', 'Y', 'Y'},
            {'E', 'B', 'B', ' '},
            {'R', 'B', 'B', ' '}
    };

    public static final char[][] GOAL_STATE_3 = {
            {'A', ' ', ' ', 'C'},
            {'D', 'Z', 'X', 'E'},
            {'F', 'G', 'H', 'I'},
            {'J', 'B', 'B', 'M'},
            {'N', 'B', 'B', 'O'}
    };
}
