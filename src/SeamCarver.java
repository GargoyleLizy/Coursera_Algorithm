import edu.princeton.cs.algs4.Picture;

public class SeamCarver {

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException("constructor does not take null argument");
        }
    }

    // current picture
    public Picture picture() {
        return null;
    }

    // width of current picture
    public int width() {
        return -1;
    }

    // height of current picture
    public int height() {
        return -1;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {

        return -1;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        return null;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        return null;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) {
            throw new IllegalArgumentException("removeHorizontalSeam does not take null argument");
        }
        if(width()<=1){
            throw new IllegalArgumentException("removeHorizontalSeam fail when width <=1");
        }
        checkHorizontalSeam(seam);

    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) {
            throw new IllegalArgumentException("removeVerticalSeam does not take null argument");
        }
        if(height()<=1){
            throw new IllegalArgumentException("removeVerticalSeam fail when height <=1 ");
        }
        checkVerticalSeam(seam);
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
}
