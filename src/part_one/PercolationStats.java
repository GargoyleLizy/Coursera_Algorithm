package part_one;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private Percolation percolation;
    private double[] trialsResult;

    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("n or trails shall be positive integer.");
        }
        trialsResult = new double[trials];
        for (int i = 0; i < trials; i++) {
            double trialResult = percolatesThreshold(n);
            //System.out.println(i + ": " + trialResult);
            trialsResult[i] = trialResult;
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(trialsResult);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(trialsResult);
    }

    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - 1.96 * stddev() / Math.sqrt(trialsResult.length);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + 1.96 * stddev() / Math.sqrt(trialsResult.length);
    }

    // test client (described below)
    public static void main(String[] args) {
        PercolationStats stat = new PercolationStats(100, 1);
        System.out.println("mean: " + stat.mean());
        System.out.println("stddev: " + stat.stddev());
        System.out.println("95% confidence interval: [" + stat.confidenceLo() + ", " + stat.confidenceHi() + "]");
    }

    private double percolatesThreshold(int n) {
        percolation = new Percolation(n);
        int openCount = 0;
        int[] openSiteIndex = new int[n * n];
        for (int i = 0; i < n * n; i++) {
            openSiteIndex[i] = i;
        }
        StdRandom.shuffle(openSiteIndex);
        while (!percolation.percolates()) {
            int nextOpenSiteIndex = openSiteIndex[openCount];
            RowCol nextOpenSite = convertIndexToSite(nextOpenSiteIndex, n);
            percolation.open(nextOpenSite.row, nextOpenSite.col);
            openCount++;
        }
        return ((double) openCount) / ((double) n * n);
    }

    private RowCol convertIndexToSite(int index, int n) {
        int col = index % n;
        int row = (index - col) / n;
        return new RowCol(row + 1, col + 1);
    }

    private class RowCol{
        private int row,col;
        public RowCol(int row,int col){
            this.row = row;
            this.col = col;
        }
    }
}
