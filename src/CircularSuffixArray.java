public class CircularSuffixArray {

    private String originalString;
    private int[] sortingIndex;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) throw new IllegalArgumentException("Argument to constructor is null");
        originalString = s;
        sortingIndex = new int[originalString.length()];
        for (int i = 0; i < originalString.length(); i++) {
            sortingIndex[i] = i;
        }
        sort(originalString, sortingIndex);
    }

    // length of s
    public int length() {
        return originalString.length();
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= originalString.length()) {
            throw new IllegalArgumentException("Argument of index() is outside of its prescribed range");
        }
        return sortingIndex[i];
    }

    // unit testing (required)
    public static void main(String[] args) {
//        String testS = "ABRACADABRA!";
//        CircularSuffixArray tCSA = new CircularSuffixArray(testS);
//        for(int i=0;i<testS.length();i++){
//            System.out.println(i+" : " + tCSA.index(i));
//        }
    }

    private static void sort(String originalString, int[] sortingIndex) {
        sort(originalString.toCharArray(), sortingIndex, 0, originalString.length() - 1, 0);
    }

    // Use the 3-way string quick sort.
    private static void sort(char[] originalString, int[] sortingIndex, int lo, int hi, int d) {
        if (hi <= lo) return;
        int lt = lo, gt = hi;
        int v = originalString[getValidPos(originalString.length, sortingIndex[lo], d)];
        int i = lo + 1;
        while (i <= gt) {
            int t = originalString[getValidPos(originalString.length, sortingIndex[i], d)];
            if (t < v) {
                int temp = sortingIndex[i];
                sortingIndex[i] = sortingIndex[lt];
                sortingIndex[lt] = temp;
                lt++;
                i++;
            } else if (t > v) {
                int temp = sortingIndex[i];
                sortingIndex[i] = sortingIndex[gt];
                sortingIndex[gt] = temp;
                gt--;
            } else {
                i++;
            }
        }
        sort(originalString, sortingIndex, lo, lt - 1, d);
        if (d < originalString.length - 1) sort(originalString, sortingIndex, lt, gt, d + 1);
        sort(originalString, sortingIndex, gt + 1, hi, d);
    }

    private static int getValidPos(int originalLength, int suffixIndex, int distance) {
        if (suffixIndex + distance < originalLength) {
            return suffixIndex + distance;
        } else {
            return suffixIndex + distance - originalLength;
        }
    }
}
