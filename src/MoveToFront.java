import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    private static final int R = 256;
    private static int[] seq = new int[R];
    private static int[] seqPos = new int[R];


    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        resetSeqArr();
        String s = BinaryStdIn.readString();
        char[] input = s.toCharArray();
        for (int i = 0; i < input.length; i++) {
            input[i] = encodeProcess(input[i]);
            BinaryStdOut.write(input[i]);
        }
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        resetSeqArr();
        String s = BinaryStdIn.readString();
        char[] input = s.toCharArray();
        for (int i = 0; i < input.length; i++) {
            input[i] = decodeProcess(input[i]);
            BinaryStdOut.write(input[i]);
        }
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) {
            encode();
        } else if (args[0].equals("+")) {
            decode();
        } else {
            throw new IllegalArgumentException("First argument illegal for main().");
        }
//        char[] text = "ABRACADABRA!".toCharArray();
//        char[] encoded = new char[text.length];
//        for (int i = 0; i < text.length; i++) {
//            System.out.print(" " + (int) text[i] + " ");
//        }
//        resetSeqArr();
//        System.out.println();
//        for (int i = 0; i < text.length; i++) {
//            char code = encodeProcess(text[i]);
//            encoded[i] = code;
//            System.out.print(" " + (int) code + " ");
//        }
//        System.out.println();
//        resetSeqArr();
//        for (int i = 0; i < text.length; i++) {
//            char decode = decodeProcess(encoded[i]);
//            System.out.print(" " + decode + " ");
//        }
    }

    private static void resetSeqArr() {
        for (int i = 0; i < R; i++) {
            seq[i] = i;
            seqPos[i] = i;
        }
    }

    private static char encodeProcess(char c) {
        int code = seqPos[c];
        for (int i = code; i >= 1; i--) {
            seq[i] = seq[i - 1];
            seqPos[seq[i]] = i;
        }
        seq[0] = c;
        seqPos[c] = 0;
        return (char) code;
    }

    private static char decodeProcess(char c) {
        int code = seq[c];
        for (int i = (int) c; i >= 1; i--) {
            seq[i] = seq[i - 1];
        }
        seq[0] = code;
        return (char) code;
    }
}
