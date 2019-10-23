/* *****************************************************************************
 *  Name: PercolationStats
 *  Date: 23/10/19
 *  Description: Stats assocciated with performing independent trials on an n-by-n grid
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private double[] results;
    private int T;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("Incorrect N or trials value(s)");
        }

        T = trials;
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
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(results);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(results);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        double x_ = mean();
        double s = stddev();
        return x_ - 1.96 * s / Math.sqrt(T);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        double x_ = mean();
        double s = stddev();
        return x_ + 1.96 * s / Math.sqrt(T);
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
