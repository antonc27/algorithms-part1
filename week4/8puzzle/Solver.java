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
        Comparator<SearchNode> priority = (o1, o2) -> natural.compare(o1.priority(), o2.priority());

        MinPQ<SearchNode> queue = new MinPQ<>(priority);
        MinPQ<SearchNode> twinQueue = new MinPQ<>(priority);

        SearchNode root = new SearchNode(null, initial, 0);
        queue.insert(root);

        SearchNode twinRoot = new SearchNode(null, initial.twin(), 0);
        twinQueue.insert(twinRoot);

        while (!queue.isEmpty()) {

            SearchNode minNode = queue.delMin();
            Board board = minNode.board;

            if (board.isGoal()) {
                goalNode = minNode;
                break;
            }

            Board parent = (minNode.parent == null) ? null : minNode.parent.board;
            for (Board next : board.neighbors()) {
                // critical optimisation
                if (!next.equals(parent)) {
                    SearchNode nextNode = new SearchNode(minNode, next, minNode.moves + 1);
                    queue.insert(nextNode);
                }
            }

            SearchNode twinMinNode = twinQueue.delMin();
            Board twinBoard = twinMinNode.board;

            if (twinBoard.isGoal()) {
                goalNode = null;
                break;
            }

            Board twinParent = (twinMinNode.parent == null) ? null : twinMinNode.parent.board;
            for (Board twinNext : twinBoard.neighbors()) {
                if (!twinNext.equals(twinParent)) {
                    SearchNode twinNextNode = new SearchNode(twinMinNode, twinNext, twinMinNode.moves + 1);
                    twinQueue.insert(twinNextNode);
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
        if (isSolvable()) {
            return goalNode.moves;
        } else {
            return -1;
        }
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        if (!isSolvable()) return null;

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
