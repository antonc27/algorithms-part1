/* *****************************************************************************
 *  Name: Board
 *  Date: 3/10/19
 *  Description: Models an n-by-n board with sliding tiles
 **************************************************************************** */

import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Arrays;

public class Board {

    private static final int BLANK = 0;

    private final int n;
    private final int[][] tiles;

    private final int hamming;
    private final int manhattan;

    private final int blankRow;
    private final int blankCol;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        n = tiles.length;
        this.tiles = deepCopy(tiles);

        int hammingCount = 0;
        int manhattanCount = 0;
        int blankRowTmp = -1;
        int blankColTmp = -1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int tile = tiles[i][j];
                if (tile == BLANK) {
                    blankRowTmp = i;
                    blankColTmp = j;
                    continue;
                }

                if (tile != targetTile(i, j)) {
                    hammingCount++;

                    manhattanCount += Math.abs(i - getTargetRow(tile));
                    manhattanCount += Math.abs(j - getTargetCol(tile));
                }
            }
        }

        hamming = hammingCount;
        manhattan = manhattanCount;

        blankCol = blankColTmp;
        blankRow = blankRowTmp;
    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        return hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return manhattan;
    }

    private int targetTile(int row, int col) {
        int index = row * n + col;
        return index + 1;
    }

    private int getTargetRow(int tile) {
        int index = (tile - 1);
        return  index / n;
    }

    private int getTargetCol(int tile) {
        int index = (tile - 1);
        return  index % n;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != getClass()) return false;
        Board anotherBoard = (Board) y;
        return n == anotherBoard.n && Arrays.deepEquals(tiles, anotherBoard.tiles);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Stack<Board> neighbors = new Stack<>();

        int[][] newTiles = deepCopy(tiles);
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == j || i == -j) continue;

                int newRow = blankRow + i;
                int newCol = blankCol + j;
                if (checkIndices(newRow, newCol)) {
                    swap(newTiles, blankRow, blankCol, newRow, newCol);

                    Board neighbor = new Board(newTiles);
                    neighbors.push(neighbor);

                    // swap bakc in order to not waste space with deep copying
                    swap(newTiles, newRow, newCol, blankRow, blankCol);
                }
            }
        }

        return neighbors;
    }

    private boolean checkIndices(int i, int j) {
        return 0 <= i && i < n && 0 <= j && j < n;
    }

    private static void swap(int[][] arr, int i, int j, int k, int l) {
        int tmp = arr[i][j];
        arr[i][j] = arr[k][l];
        arr[k][l] = tmp;
    }

    private static int[][] deepCopy(int[][] original) {
        final int[][] result = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            result[i] = Arrays.copyOf(original[i], original[i].length);
        }
        return result;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int blanck = targetTile(blankRow, blankCol);

        int p1 = -1;
        while (p1 == -1 || p1 == blanck) {
            p1 = StdRandom.uniform(n) + 1;
        }

        int p2 = -1;
        while (p2 == -1 || p2 == blanck || p2 == p1) {
            p2 = StdRandom.uniform(n) + 1;
        }

        int[][] newTiles = deepCopy(tiles);
        swap(newTiles, getTargetRow(p1), getTargetCol(p1), getTargetRow(p2), getTargetCol(p2));

        return new Board(newTiles);
    }

    private static void assertTrue(boolean cond, String msg) {
        if (!cond) {
            throw new RuntimeException(msg);
        }
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] tiles = {
            {8, 1, 3},
            {4, 0, 2},
            {7, 6, 5},
        };
        Board board = new Board(tiles);

        assertTrue(board.dimension() == 3, "Board dimension");

        String expectedBoardStr = "3\n"
                + " 8  1  3 \n"
                + " 4  0  2 \n"
                + " 7  6  5 \n";
        assertTrue(board.toString().equals(expectedBoardStr), "String representation");

        assertTrue(board.hamming() == 5, "Hamming distance");

        assertTrue(board.manhattan() == 10, "Manhattan distance");

        assertTrue(!board.isGoal(), "Check not goal board");

        int[][] goalTiles = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 0},
        };
        Board goalBoard = new Board(goalTiles);

        assertTrue(goalBoard.isGoal(), "Check goal board");

        assertTrue(goalBoard.hamming() == 0, "Hamming distance, goal board");
        assertTrue(goalBoard.manhattan() == 0, "Manhattan distance, goal board");

        assertTrue(!board.equals(new Object()), "Equality: some Object");
        assertTrue(board.equals(board), "Equality: self");
        assertTrue(board.equals(new Board(tiles)), "Equality: some tiles");
        assertTrue(!board.equals(goalBoard), "Equality: different boards");

        int[][] neighborsTiles = {
                {1, 0, 3},
                {4, 2, 5},
                {7, 8, 6},
        };
        Board neighborsBoard = new Board(neighborsTiles);

        int[][] expectedNeighborTiles = {
                {1, 3, 0},
                {4, 2, 5},
                {7, 8, 6},
        };
        Board expectedNeighborBoard = new Board(expectedNeighborTiles);

        Iterable<Board> neighbors = neighborsBoard.neighbors();
        int neighborsCount = 0;
        boolean hasExpectedNeighbor = false;
        for (Board b : neighbors) {
            neighborsCount++;
            if (b.equals(expectedNeighborBoard)) {
                hasExpectedNeighbor = true;
            }
        }
        assertTrue(neighborsCount == 3, "Neighbors count");
        assertTrue(hasExpectedNeighbor, "Neighbor inside neighbors");

        Board twin = board.twin();
        assertTrue(!twin.equals(board), "Twin non equality");
        assertTrue(twin.blankCol == board.blankCol, "Twin blank col");
        assertTrue(twin.blankRow == board.blankRow, "Twin blank row");
    }

}
