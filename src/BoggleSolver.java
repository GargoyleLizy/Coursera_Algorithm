import edu.princeton.cs.algs4.TrieST;

import java.util.*;

public class BoggleSolver {

    TrieST<Boolean> dictionaryTries = new TrieST<>();

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        for(String value: dictionary){
            dictionaryTries.put(value,true);
        }

    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        return null;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        return -1;
    }

    private TreeSet<String> iterateBoard(BoggleBoard board){
        for(int i=0;i<board.rows();i++){
            for(int j=0;j<board.cols();j++){

            }
        }
        return null;
    }

    // Do a Depth-First-Search from a position of Boggle Board,
    // Each time, check if there are still words in dictionary matched the prefix.
    private TreeSet<String> iterateBoardFromPosition(BoggleBoard board, int startRow, int startCol){
        // Use a 2D boolean array to mark remember the DFS.
        Boolean[][] marks = new Boolean[board.rows()][board.cols()];
        marks[startRow][startCol] = true;
        return null;
    }

    private boolean checkWordExist(String word) {
        return false;
    }

//    private class IntPair {
//        private int x, y;
//
//        public IntPair(int x, int y) {
//            this.x = x;
//            this.y = y;
//        }
//        // Equal might be helpful.
//    }
}
