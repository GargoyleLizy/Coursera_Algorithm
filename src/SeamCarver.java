import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private Picture mPicture;
    private boolean isTransposed = false;

    // temp matrix required for computing the seam.
    private double[][] energyMatrix;
    private double[][] verticalDistTo;
    private int[][] verticalEdgeTo;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException("constructor does not take null argument");
        }
        mPicture = new Picture(picture);
    }

    // current picture
    public Picture picture() {
        if (isTransposed) {
            transposePicture();
        }
        return mPicture;
    }

    // width of current picture
    public int width() {
        if (isTransposed) {
            return mPicture.height();
        } else {
            return mPicture.width();
        }
    }

    // height of current picture
    public int height() {
        if (isTransposed) {
            return mPicture.width();
        } else {
            return mPicture.height();
        }
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (isTransposed) {
            return computeEnergy(y, x);
        } else {
            return computeEnergy(x, y);
        }
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        if (isTransposed) {
            return findVerticalSeam();
        } else {
            transposePicture();
            return findVerticalSeam();
        }
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        if (isTransposed) {
            transposePicture();
        }
        energyMatrix = computeEnergyMatrix();
        int startSeamIndex = findVerticalSeamStart();
        int[] seamArr = new int[mPicture.height()];
        seamArr[0] = startSeamIndex;
        for (int j = 0; j < mPicture.height() - 1; j++) {
            int prevEdgeTo = seamArr[j];
            seamArr[j + 1] = verticalEdgeTo[prevEdgeTo][j];
        }
        //***
//        System.out.println("the seam matrix: ");
//        for(int j=0;j<mPicture.height();j++){
//            for(int i=0;i<mPicture.width();i++){
//                StdOut.printf("%7.2f ", verticalDistTo[i][j]);
//            }
//            System.out.println();
//        }
//        for(int j=0;j<mPicture.height();j++){
//            for(int i=0;i<mPicture.width();i++){
//                StdOut.printf(verticalEdgeTo[i][j] + " ");
//            }
//            System.out.println();
//        }
        //***
        // clean extra data
        verticalEdgeTo = null;
        verticalDistTo = null;
        return seamArr;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) {
            throw new IllegalArgumentException("removeHorizontalSeam does not take null argument");
        }
        if (width() <= 1) {
            throw new IllegalArgumentException("removeHorizontalSeam fail when width <=1");
        }
        checkHorizontalSeam(seam);
        if(!isTransposed){
            transposePicture();
        }
        // mPicture is transposed,
        Picture temp = new Picture(mPicture.width()-1, mPicture.height());
        for (int j = 0; j < temp.height(); j++) {
            for (int i = 0; i < temp.width(); i++) {
                if (i < seam[j]) {
                    temp.setRGB(i, j, mPicture.getRGB(i, j));
                } else {
                    temp.setRGB(i, j, mPicture.getRGB(i+1, j ));
                }
            }
        }
        mPicture = temp;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) {
            throw new IllegalArgumentException("removeVerticalSeam does not take null argument");
        }
        if (height() <= 1) {
            throw new IllegalArgumentException("removeVerticalSeam fail when height <=1 ");
        }
        checkVerticalSeam(seam);
        // Temp
        if (isTransposed) {
            transposePicture();
        }
        Picture temp = new Picture(width() - 1, height());
        for (int j = 0; j < temp.height(); j++) {
            for (int i = 0; i < temp.width(); i++) {
                if (i < seam[j]) {
                    temp.setRGB(i, j, mPicture.getRGB(i, j));
                } else {
                    temp.setRGB(i, j, mPicture.getRGB(i+1, j));
                }
            }
        }
        mPicture = temp;
    }

    private void checkerPixelX(int x) {
        if (x < 0 || x >= width()) {
            throw new IllegalArgumentException("Pixel x is not in proper range: " + x);
        }
    }

    private void checkerPixelY(int y) {
        if (y < 0 || y >= height()) {
            throw new IllegalArgumentException("Pixel y is not in proper range: " + y);
        }
    }

    private void checkVerticalSeam(int[] seam) {
        if (seam.length <= 1) {
            throw new IllegalArgumentException("removeVerticalSeam fail when width <=1");
        }
        int prevX = seam[0];
        checkerPixelX(prevX);
        for (int i = 1; i < seam.length; i++) {
            checkerPixelX(seam[i]);
            int diff = seam[i] - prevX;
            if (diff < -1 || diff > 1) {
                throw new IllegalArgumentException("removeVerticalSeam argument failed at sequent index differ bigger than 1");
            }
            prevX = seam[i];
        }
    }

    private void checkHorizontalSeam(int[] seam) {
        if (seam.length <= 1) {
            throw new IllegalArgumentException("removeHorizontalSeam fail when height <=1");
        }
        int prevY = seam[0];
        checkerPixelY(prevY);
        for (int i = 1; i < seam.length; i++) {
            checkerPixelY(seam[i]);
            int diff = seam[i] - prevY;
            if (diff < -1 || diff > 1) {
                throw new IllegalArgumentException("removeHorizontalSeam argument failed at sequent index differ bigger than 1");
            }
            prevY = seam[i];
        }
    }

    // Computing the energy of a pixel.
    private double computeEnergy(int x, int y) {
        //System.out.println("compute x: " + x + " ; y: " + y);
        if (isPixelBorder(x, y)) {
            return 1000;
        }
        // calculate radiant x.
        int leftX = mPicture.getRGB(x - 1, y);
        int rightX = mPicture.getRGB(x + 1, y);

        int upY = mPicture.getRGB(x, y - 1);
        int downY = mPicture.getRGB(x, y + 1);

        double radiantValue = computeRadiant(leftX, rightX) + computeRadiant(upY, downY);
        return Math.sqrt(radiantValue);
    }

    private boolean isPixelBorder(int x, int y) {
        return x == 0 || y == 0 || x == mPicture.width() - 1 || y == mPicture.height() - 1;
    }

    private int computeRadiant(int oneRGB, int anotherRGB) {
        int r1 = (oneRGB >> 16) & 0xFF;
        int g1 = (oneRGB >> 8) & 0xFF;
        int b1 = (oneRGB >> 0) & 0xFF;

        int r2 = (anotherRGB >> 16) & 0xFF;
        int g2 = (anotherRGB >> 8) & 0xFF;
        int b2 = (anotherRGB >> 0) & 0xFF;

        int rDiff = r1 - r2;
        int gDiff = g1 - g2;
        int bDiff = b1 - b2;

        return rDiff * rDiff + gDiff * gDiff + bDiff * bDiff;
    }

    // we compute the energy 2d array.
    // Then we compute the seam array and a tracking edgeTo array.

    // compute the energy
    private double[][] computeEnergyMatrix() {
        double[][] energyArray = new double[mPicture.width()][mPicture.height()];
        for (int i = 0; i < mPicture.width(); i++) {
            for (int j = 0; j < mPicture.height(); j++) {
                if (isTransposed) {
                    energyArray[i][j] = energy(j, i);
                } else {
                    energyArray[i][j] = energy(i, j);
                }
            }
        }
        return energyArray;
    }

    // compute the seam from bottom.
    private int findVerticalSeamStart() {
        verticalDistTo = new double[mPicture.width()][mPicture.height()];
        verticalEdgeTo = new int[mPicture.width()][mPicture.height()];

        // except the first line, we accumulate the matrix values.
        for (int j = mPicture.height() - 1; j > 0; j--) {
            // for bottom line, seam value is the energy, and no edge to.
            if (j == mPicture.height() - 1) {
                for (int i = 0; i < mPicture.width(); i++) {
                    verticalEdgeTo[i][j] = -1;
                    verticalDistTo[i][j] = energyMatrix[i][j];
                }
            }
            // for second bottom line, seam value is itself plus 1000, and edge to left index if possible
            else if (j == mPicture.height() - 2) {
                for (int i = 0; i < mPicture.width(); i++) {
                    if (i == 0) {
                        verticalEdgeTo[i][j] = 0;
                    } else {
                        verticalEdgeTo[i][j] = i - 1;
                    }
                    verticalDistTo[i][j] = energyMatrix[i][j] + 1000;
                }
            }
            // for the rest, check pick the smallest coming branch
            else {
                for (int i = 0; i < mPicture.width(); i++) {
                    int minIndex = findMinEdgeToValue(i, j);
                    // then assign value to two matrix
                    verticalEdgeTo[i][j] = minIndex;
                    verticalDistTo[i][j] = verticalDistTo[minIndex][j + 1] + energyMatrix[i][j];
                }
            }
        }

        // for the first line, we assign the values and remember the min value.
        int minSeamIndex = -1;
        double minSeamValue = Double.MAX_VALUE;
        for (int i = 0; i < mPicture.width(); i++) {
            int nextMinIndex = findMinEdgeToValue(i, 0);
            verticalEdgeTo[i][0] = nextMinIndex;
            verticalDistTo[i][0] = verticalDistTo[nextMinIndex][1] + energyMatrix[i][0];
            if (verticalDistTo[i][0] < minSeamValue) {
                minSeamIndex = i;
                minSeamValue = verticalDistTo[i][0];
            }
        }
        return minSeamIndex;
    }

    // Assume there is always a line below (i,j)
    private int findMinEdgeToValue(int i, int j) {
        if (i == 0) {
            if (verticalDistTo[i][j + 1] < verticalEdgeTo[i + 1][j + 1]) {
                return i;
            } else {
                return i + 1;
            }
        }
        if (i == mPicture.width() - 1) {
            if (verticalDistTo[i][j + 1] < verticalDistTo[i - 1][j + 1]) {
                return i;
            } else {
                return i - 1;
            }
        }
        // Beside corner case,

        double left = verticalDistTo[i - 1][j + 1];
        double center = verticalDistTo[i][j + 1];
        double right = verticalDistTo[i + 1][j + 1];
        int minIndex = i - 1;
        if (center < left) {
            minIndex = i;
        }
        if (right < verticalDistTo[minIndex][j + 1]) {
            minIndex = i + 1;
        }
        return minIndex;
    }

    private void transposePicture() {
        Picture transposed = new Picture(mPicture.height(), mPicture.width());
        for (int i = 0; i < mPicture.width(); i++) {
            for (int j = 0; j < mPicture.height(); j++) {
                int rgb = mPicture.getRGB(i, j);
                transposed.setRGB(j, i, rgb);
            }
        }
        mPicture = transposed;
        isTransposed = !isTransposed;
    }
}
