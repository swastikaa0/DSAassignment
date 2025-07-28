package Question4;
import java.util.*;

public class AssignmentQuestion4b {

    static final int DRAW = 0;
    static final int PLAYER1_WIN = 1;
    static final int PLAYER2_WIN = 2;

    public int treasureGame(int[][] graph) {
        int n = graph.length;
        int[][][] memo = new int[n][n][2]; // [p1Pos][p2Pos][turn]
        return dfs(graph, 1, 2, 0, memo, new HashSet<>());
    }

    private int dfs(int[][] graph, int p1, int p2, int turn, int[][][] memo, Set<String> visited) {
        if (p1 == 0) return PLAYER1_WIN;
        if (p1 == p2) return PLAYER2_WIN;

        String stateKey = p1 + "," + p2 + "," + turn;
        if (visited.contains(stateKey)) return DRAW;

        if (memo[p1][p2][turn] != 0) return memo[p1][p2][turn];

        visited.add(stateKey);
        int result = turn == 0 ? PLAYER2_WIN : PLAYER1_WIN; // Default fallback

        if (turn == 0) {
            for (int next : graph[p1]) {
                int nextResult = dfs(graph, next, p2, 1, memo, visited);
                if (nextResult == PLAYER1_WIN) {
                    result = PLAYER1_WIN;
                    break;
                } else if (nextResult == DRAW) {
                    result = DRAW;
                }
            }
        } else {
            for (int next : graph[p2]) {
                if (next == 0) continue; // forbidden node
                int nextResult = dfs(graph, p1, next, 0, memo, visited);
                if (nextResult == PLAYER2_WIN) {
                    result = PLAYER2_WIN;
                    break;
                } else if (nextResult == DRAW) {
                    result = DRAW;
                }
            }
        }

        visited.remove(stateKey);
        memo[p1][p2][turn] = result;
        return result;
    }

    public static void main(String[] args) {
        AssignmentQuestion4b game = new AssignmentQuestion4b();
        int[][] graph = {
            {2, 5},
            {3},
            {0, 4, 5},
            {1, 4, 5},
            {2, 3},
            {0, 2, 3}
        };

        int result = game.treasureGame(graph);
        System.out.println("Game Result: " + result); // Expected output: 0 (Draw)
    }
}
