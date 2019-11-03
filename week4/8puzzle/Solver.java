/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class Solver {

    private class SearchNode {
        private final SearchNode parent;
        private final Board board;
        private final int moves;

        public SearchNode(SearchNode parent, Board board, int moves) {
            this.parent = parent;
            this.board = board;
            this.moves = moves;
        }

        public int priority() {
            return board.manhattan() + moves;
        }
    }

    private SearchNode goalNode = null;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("Null initial board");
        }

        Comparator<Integer> natural = Comparator.naturalOrder();
        MinPQ<SearchNode> queue = new MinPQ<>((o1, o2) -> natural.compare(o1.priority(), o2.priority()));

        SearchNode root = new SearchNode(null, initial, 0);
        queue.insert(root);

        while (!queue.isEmpty()) {

            SearchNode minNode = queue.delMin();
            Board board = minNode.board;

            if (board.isGoal()) {
                goalNode = minNode;
                break;
            } else {
                Board parent = (minNode.parent == null) ? null : minNode.parent.board;
                for (Board next : board.neighbors()) {
                    // critical optimisation
                    if (!next.equals(parent)) {
                        SearchNode nextNode = new SearchNode(minNode, next, minNode.moves + 1);
                        queue.insert(nextNode);
                    }
                }
            }
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return goalNode != null;
    }

    // min number of moves to solve initial board
    public int moves() {
        return goalNode.moves;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        Stack<Board> boards = new Stack<>();
        SearchNode current = goalNode;
        while (current != null) {
            boards.push(current.board);
            current = current.parent;
        }
        return boards;
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}
