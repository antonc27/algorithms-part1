/* *****************************************************************************
 *  Name: Percolation data type
 *  Date: 23/10/19
 *  Description: Model a percolation system
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private int N;
    private boolean[][] field;
    private WeightedQuickUnionUF uf;

    private int inputSite;
    private int outputSite;

    private int openSites = 0;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Incorrect N value");
        }

        N = n;
        field = new boolean[N][N];
        // 2 for "in" and "out" fake open sites
        uf = new WeightedQuickUnionUF(N*N + 2);

        inputSite = N*N;
        outputSite = N*N + 1;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (!checkBounds(row, col)) {
            throw new IllegalArgumentException("Out of bounds");
        }

        if (isOpen(row, col)) {
            return;
        }

        field[row - 1][col - 1] = true;
        openSites++;

        int toOpen = position(row, col);

        if (row == 1) {
            uf.union(inputSite, toOpen);
        }

        if (row == N) {
            uf.union(outputSite, toOpen);
        }

        openInBounds(row, col, row, col-1);
        openInBounds(row, col, row, col+1);
        openInBounds(row, col, row-1, col);
        openInBounds(row, col, row+1, col);
    }

    private void openInBounds(int row1, int col1, int row2, int col2) {
        if (checkBounds(row2, col2) && isOpen(row2, col2)) {
            int pos1 = position(row1, col1);
            int pos2 = position(row2, col2);
            uf.union(pos1, pos2);
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (!checkBounds(row, col)) {
            throw new IllegalArgumentException("Out of bounds");
        }

        return field[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (!checkBounds(row, col)) {
            throw new IllegalArgumentException("Out of bounds");
        }

        int pos = position(row, col);
        return uf.connected(inputSite, pos);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        if (openSites > N*N) {
            throw new IllegalStateException("Bad open sites count: " + openSites + " for N=" + N);
        }
        return openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return uf.connected(inputSite, outputSite);
    }

    // test client (optional)
    public static void main(String[] args) {

    }

    private int position(int row, int col) {
        return (row - 1) * N + (col - 1);
    }

    private boolean checkBounds(int row, int col) {
        return 1 <= row && row <= N && 1 <= col && col <= N;
    }
}
