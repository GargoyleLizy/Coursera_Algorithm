package part_two.week4;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.LinkedList;
import java.util.TreeSet;


public class BoggleSolver {

    private BoggleDictTries dictTries = new BoggleDictTries();

    // Temp variables to hold for each boggle game.
    private LinkedList<IntPair> existingPosList = new LinkedList<>();
    private TreeSet<String> validWords = new TreeSet<>();

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        for (String value : dictionary) {
            dictTries.put(value);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        return iterateBoard(board);
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if(!dictTries.contains(word)){
            return 0;
        }
        switch (word.length()) {
            case 0:
            case 1:
            case 2:
                return 0;
            case 3:
            case 4:
                return 1;
            case 5:
                return 2;
            case 6:
                return 3;
            case 7:
                return 5;
            case 8:
                return 11;
        }
        return 11;
    }

    private TreeSet<String> iterateBoard(BoggleBoard board) {
        validWords = new TreeSet<>();
        for (int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++) {
                iterateBoardFromPosition(board, i, j);
            }
        }
        return validWords;
    }

    // Do a Depth-First-Search from a position of Boggle Board,
    // Each time, check if there are still words in dictionary matched the prefix.
    private void iterateBoardFromPosition(BoggleBoard board, int startRow, int startCol) {
        dictTries.startABoggleTry();
        checkNode(startRow, startCol, board, dictTries);
    }

    private void checkNode(int row, int col, BoggleBoard board, BoggleDictTries dictTries) {
        char nodeChar = board.getLetter(row,col);
        // When it is Q, it actually is QU
        if(nodeChar == 'Q'){
            dictTries.appendCharacter('Q');
            if(dictTries.stillHasPostfix()){
                dictTries.appendCharacter('U');
            }else{
                // If after Q there is no postfix already, then quit this path
                dictTries.stepBack();
                return;
            }
        }else{
            dictTries.appendCharacter(nodeChar);
        }
        existingPosList.add(new IntPair(row, col));
        if (dictTries.isCurrentAWordMatch()) {
            String matchedWord = dictTries.getMatchedWord();
            // Only put long enough word into valid set.
            if (isWordValid(matchedWord)) {
                validWords.add(dictTries.getMatchedWord());
            }
        }
        // Continue search if there still possible
        if (dictTries.stillHasPostfix()) {
            for (IntPair nearbyPos : getNearbyPos(row, col, board.rows(), board.cols())) {
                //System.out.println("Nearby Pos: " + nearbyPos.x + "; " + nearbyPos.y);
                if (!existingPosList.contains(nearbyPos)) {
                    checkNode(nearbyPos.x, nearbyPos.y, board, dictTries);
                }
            }
        }
        // After end search on this pos, step back and remove the pos list.
        if(nodeChar=='Q'){
            // if the node is Q, there is a need to step twice. for Q and U.
            dictTries.stepBack();
        }
        dictTries.stepBack();
        existingPosList.removeLast();
    }

    private Iterable<IntPair> getNearbyPos(int row, int col, int rowNumber, int colNumber) {
        LinkedList<IntPair> nearbyPos = new LinkedList<>();
        if (row > 0) {
            nearbyPos.add(new IntPair(row - 1, col));
        }
        if (col > 0) {
            nearbyPos.add(new IntPair(row, col - 1));
        }
        if (col < colNumber - 1) {
            nearbyPos.add(new IntPair(row, col + 1));
        }
        if (row < rowNumber - 1) {
            nearbyPos.add(new IntPair(row + 1, col));
        }
        if (row > 0 && col > 0) {
            nearbyPos.add(new IntPair(row - 1, col - 1));
        }
        if (row > 0 && col < colNumber - 1) {
            nearbyPos.add(new IntPair(row - 1, col + 1));
        }
        if (row < rowNumber - 1 && col > 0) {
            nearbyPos.add(new IntPair(row + 1, col - 1));
        }
        if (row < rowNumber - 1 && col < colNumber - 1) {
            nearbyPos.add(new IntPair(row + 1, col + 1));
        }
        return nearbyPos;
    }

    // Check if word long enough
    private boolean isWordValid(String word) {
        return word.length() > 2;
    }

    private class IntPair {
        private int x, y;

        IntPair(int x, int y) {
            this.x = x;
            this.y = y;
        }
        // Equal might be helpful.

        @Override
        public boolean equals(Object obj) {
            if (obj == null) return false;
            if (obj.getClass() != this.getClass()) {
                return false;
            }
            IntPair another = (IntPair) obj;
            return this.x == another.x && this.y == another.y;
        }
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
