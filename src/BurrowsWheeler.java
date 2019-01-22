import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.Arrays;

public class BurrowsWheeler {
    // apply Burrows-Wheeler transform, reading from standard input and writing to standard output
    public static void transform() {
        String message = BinaryStdIn.readString();
        CircularSuffixArray csa = new CircularSuffixArray(message);
        char[] original = message.toCharArray();
        char[] endLetter = new char[original.length];
        int first = 0;
        for (int i = 0; i < csa.length(); i++) {
            if (csa.index(i) == 0) {
                first = i;
            }
            if(csa.index(i)==0){
                endLetter[i] = original[original.length-1];
            }else{
                endLetter[i] = original[csa.index(i) -1];
            }
        }

        BinaryStdOut.write(first);
        for (int i = 0; i < csa.length(); i++) {
            BinaryStdOut.write(endLetter[i]);
        }
        BinaryStdOut.close();
    }

    private static String transform(String message){
        CircularSuffixArray csa = new CircularSuffixArray(message);
        char[] original = message.toCharArray();
        char[] endLetter = new char[original.length];
        int first = 0;
        for (int i = 0; i < csa.length(); i++) {
            if (csa.index(i) == 0) {
                first = i;
            }
            if(csa.index(i)==0){
                endLetter[i] = original[original.length-1];
            }else{
                endLetter[i] = original[csa.index(i) -1];
            }
        }
        return first + String.valueOf(endLetter);
    }

    // apply Burrows-Wheeler inverse transform, reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        String encoded = BinaryStdIn.readString();
        char[] t = encoded.toCharArray();
        char[] sorted = Arrays.copyOf(t, t.length);
        Arrays.sort(sorted);
        //Construct next[]
        // prep a start position for every
        int[] firstAppear = new int[256];
        for (int i = 0; i < 256; i++) {
            firstAppear[i] = -1;
        }
        // Record where the first certain Char value appears.
        for (int i = 0; i < sorted.length; i++) {
            if (firstAppear[sorted[i]] == -1) {
                firstAppear[sorted[i]] = i;
            }
        }
        // fill the next[]
        int[] next = new int[t.length];
        for (int i = 0; i < t.length; i++) {
            int sortedPos = firstAppear[t[i]];
            next[sortedPos] = i;
            firstAppear[t[i]]++;
        }
        // reconstruct original
        int nextPos = first;
        char[] original = new char[t.length];
        for(int i=0;i<original.length;i++){
            original[i] = sorted[nextPos];
            nextPos = next[nextPos];
            BinaryStdOut.write(original[i]);
        }
        BinaryStdOut.close();
    }

    private static String inverseTransform(String encoded){
        int first = Integer.parseInt(encoded.substring(0,1));
        encoded = encoded.substring(1);
        char[] t = encoded.toCharArray();
        char[] sorted = Arrays.copyOf(t, t.length);
        Arrays.sort(sorted);
        //Construct next[]
        // prep a start position for every
        int[] firstAppear = new int[256];
        for (int i = 0; i < 256; i++) {
            firstAppear[i] = -1;
        }
        // Record where the first certain Char value appears.
        for (int i = 0; i < sorted.length; i++) {
            if (firstAppear[sorted[i]] == -1) {
                firstAppear[sorted[i]] = i;
            }
        }
        // fill the next[]
        int[] next = new int[t.length];
        for (int i = 0; i < t.length; i++) {
            int sortedPos = firstAppear[t[i]];
            next[sortedPos] = i;
            firstAppear[t[i]]++;
        }
        for(int i=0;i<next.length;i++){
            System.out.println(next[i]);
        }

        // reconstruct original
        int nextPos = first;
        char[] original = new char[t.length];
        for(int i=0;i<original.length;i++){
            original[i] = sorted[nextPos];
            nextPos = next[nextPos];
        }
        return String.valueOf(original);
    }

    // if args[0] is '-', apply Burrows-Wheeler transform
    // if args[0] is '+', apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            transform();
        } else if (args[0].equals("+")) {
            inverseTransform();
        } else {
            throw new IllegalArgumentException("First argument illegal for main().");
        }

//        String testOrig = "ABRACADABRA!";
//        String encoded = transform(testOrig);
//        System.out.println(encoded);
//        String decoded = inverseTransform(encoded);
//        System.out.println(decoded);
    }
}
