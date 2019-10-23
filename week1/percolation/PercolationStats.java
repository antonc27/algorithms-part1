/* *****************************************************************************
 *  Name: PercolationStats
 *  Date: 23/10/19
 *  Description: Stats assocciated with performing independent trials on an n-by-n grid
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private final double[] results;
    private final int t;

    private final double mean;
    private final double stddev;
    private final double confidenceHi;
    private final double confidenceLo;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("Incorrect N or trials value(s)");
        }

        t = trials;
        results = new double[trials];

        for (int i = 0; i < trials; i++) {
            Percolation perc = new Percolation(n);

            while (!perc.percolates()) {
                int randRow = StdRandom.uniform(n) + 1;
                int randCol = StdRandom.uniform(n) + 1;

                if (!perc.isOpen(randRow, randCol)) {
                    perc.open(randRow, randCol);
                }
            }

            results[i] = (double) perc.numberOfOpenSites() / (n*n);
        }

        mean = StdStats.mean(results);
        stddev = StdStats.stddev(results);

        confidenceLo = mean - 1.96 * stddev / Math.sqrt(t);
        confidenceHi = mean + 1.96 * stddev / Math.sqrt(t);
    }

    // sample mean of percolation threshold
    public double mean() {
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return stddev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return confidenceLo;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return confidenceHi;
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);

        PercolationStats percStats = new PercolationStats(n, t);

        StdOut.printf("mean = %f\n", percStats.mean());
        StdOut.printf("stddev = %f\n", percStats.stddev());
        StdOut.printf("95%% confidence interval = [%f, %f]\n", percStats.confidenceLo(), percStats.confidenceHi());
    }

}
