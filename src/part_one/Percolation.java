package part_one;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * Created by zhenyali on 11/9/18.
 */
public class Percolation {
    private int n;
    private WeightedQuickUnionUF quickUnionUF;

    private int top, bottom;
    private int[] openBlockState;
    private int numberOfOpenSites;

    // create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n should be positive integer.");
        }
        this.n = n;
        // remember open block state, all are zero at start, all blocked. Except two virtual sites.
        openBlockState = new int[n * n + 2];
        openBlockState[n * n] = 1;
        openBlockState[n * n + 1] = 1;
        // initialize
        quickUnionUF = new WeightedQuickUnionUF(n * n + 2);
        // All are blocked. except top and bottom extra sites
        top = n * n;
        bottom = n * n + 1;
        // initialize the top and bottom row
        for (int i = 0; i < n; i++) {
            quickUnionUF.union(i, top);
        }
        for (int i = n * n - n; i < n * n; i++) {
            quickUnionUF.union(i, bottom);
        }
        numberOfOpenSites = 0;
    }

    // open site (row, col) if it is not open already
    public void open(int row, int col) {
        checkSiteLegit(row, col);
        int index = indexOfSite(row, col);
        if (openBlockState[index] == 0) {
            numberOfOpenSites++;
            openBlockState[index] = 1;
            // connect
            // top
            if (isSiteLocationLegit(row - 1, col) && isOpen(row - 1, col)) {
                quickUnionUF.union(index, indexOfSite(row - 1, col));
            }
            // bottom
            if (isSiteLocationLegit(row + 1, col) && isOpen(row + 1, col)) {
                quickUnionUF.union(index, indexOfSite(row + 1, col));
            }
            // right
            if (isSiteLocationLegit(row, col + 1) && isOpen(row, col + 1)) {
                quickUnionUF.union(index, indexOfSite(row, col + 1));
            }
            // left
            if (isSiteLocationLegit(row, col - 1) && isOpen(row, col - 1)) {
                quickUnionUF.union(index, indexOfSite(row, col - 1));
            }
        }
    }

    // is site (row, col) open?
    public boolean isOpen(int row, int col) {
        checkSiteLegit(row, col);
        int index = indexOfSite(row, col);
        return openBlockState[index] == 1;
    }

    // is site (row, col) full?
    public boolean isFull(int row, int col) {
        checkSiteLegit(row, col);
        if (isOpen(row, col)) {
            int index = indexOfSite(row, col);
            return quickUnionUF.connected(index, top);
        } else {
            return false;
        }
    }

    // number of open sites
    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return quickUnionUF.connected(top, bottom);
    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation twoByTwo = new Percolation(2);
        System.out.println("percolates: " + twoByTwo.percolates());
        System.out.println("open 1,1");
        twoByTwo.open(1, 1);
        System.out.println("percolates: " + twoByTwo.percolates());
        System.out.println("open 2,2");
        twoByTwo.open(2, 2);
        System.out.println("percolates: " + twoByTwo.percolates());
        System.out.println("open 1,2");
        twoByTwo.open(1, 2);
        System.out.println("percolates: " + twoByTwo.percolates());
    }

    private void checkSiteLegit(int row, int col) {
        if (!isSiteLocationLegit(row, col)) {
            throw new IllegalArgumentException("row or col number should be between 1 and n");
        }
    }

    private boolean isSiteLocationLegit(int row, int col) {
        return !((row < 1 || row > n) || (col < 1 || col > n));
    }

    private int indexOfSite(int row, int col) {
        return (row - 1) * n + col - 1;
    }
}
