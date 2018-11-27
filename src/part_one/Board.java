package part_one;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by zhenyali on 17/11/18.
 */
public class Board {
    private static int UNINITIALIZED = -1;
    private static int BLANK_BLOCK = 0;

    private static int getGoalPositionValue(int row, int col, int dimension) {
        if (row == dimension && col == dimension) {
            return BLANK_BLOCK;
        } else {
            return (row - 1) * dimension + col;
        }
    }

    private static int getGoalPositionRow(int value, int dimension) {
        return (value + dimension - 1) / dimension;
    }

    private static int getGoalPositionCol(int value, int dimension) {
        if (value % dimension == 0) {
            return dimension;
        } else {
            return value % dimension;
        }
    }

    private int dimension;
    private int[][] blocks;
    // cached values
    private int hamming = UNINITIALIZED;
    private int manhattan = UNINITIALIZED;

    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        if (blocks == null) {
            throw new IllegalArgumentException("Input blocks array should not be null");
        }
        this.dimension = blocks[0].length;
        this.blocks = blocks;
    }

    // board dimension n
    public int dimension() {
        return dimension;
    }

    // number of blocks out of place
    public int hamming() {
        if (this.hamming == UNINITIALIZED) {
            int hammingCount = 0;
            for (int i = 0; i < dimension; i++) {
                for (int j = 0; j < dimension; j++) {
                    if (this.blocks[i][j] != BLANK_BLOCK &&
                            this.blocks[i][j] != getGoalPositionValue(i + 1, j + 1, dimension)) {
                        hammingCount++;
                    }
                }
            }
            this.hamming = hammingCount;
        }
        return hamming;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        if (this.manhattan == UNINITIALIZED) {
            int manhattanCount = 0;
            for (int i = 0; i < dimension; i++) {
                //System.out.println("Count I: " + i);
                for (int j = 0; j < dimension; j++) {
                    //System.out.println("Count J: " + j);
                    int blockValue = this.blocks[i][j];
                    if (blockValue != BLANK_BLOCK
                            && blockValue != getGoalPositionValue(i + 1, j + 1, dimension)) {
                        int goalRow = getGoalPositionRow(blockValue, dimension);
                        int goalCol = getGoalPositionCol(blockValue, dimension);
                        int distance = Math.abs((i + 1) - goalRow) + Math.abs((j + 1) - goalCol);
//                        System.out.println("i: " + i + "; j: " + j + "; value: " + blockValue);
//                        System.out.println("goalRow: " + goalRow + "; goalCol: " + goalCol);
//                        System.out.println("distance: " + distance);
                        manhattanCount += distance;
                    }
                }
            }
            this.manhattan = manhattanCount;
        }
        return this.manhattan;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        if (blocks[0][0] == BLANK_BLOCK || blocks[0][1] == BLANK_BLOCK) {
            return createTwinBoard(1, 0, 1, 1);
        } else {
            return createTwinBoard(0, 0, 0, 1);
        }
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y instanceof Board) {
            if (((Board) y).dimension() != this.dimension()) {
                return false;
            } else {
                for (int i = 0; i < dimension; i++) {
                    for (int j = 0; j < dimension; j++) {
                        if (((Board) y).blocks[i][j] != this.blocks[i][j]) {
                            return false;
                        }
                    }
                }
                return true;
            }
        } else {
            return false;
        }
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        return new BoardIterable(this);
    }

    // string representation of this board (in the output format specified below)
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer("" + dimension + "\n");
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (blocks[i][j] < 10) {
                    stringBuffer.append(" ");
                }
                stringBuffer.append(blocks[i][j] + " ");
            }
            stringBuffer.append("\n");
        }
        return stringBuffer.toString();
    }

    // unit tests (not graded)
    public static void main(String[] args) {
        int[][] test1 = new int[3][3];
        test1[0][0] = 8;
        test1[0][1] = 1;
        test1[0][2] = 3;
        test1[1][0] = 4;
        test1[1][1] = 0;
        test1[1][2] = 2;
        test1[2][0] = 7;
        test1[2][1] = 6;
        test1[2][2] = 5;

        Board testBoard = new Board(test1);
        System.out.println("Hamming distance should be 5: " + testBoard.hamming());
        System.out.println("Manhattan distance should be 10: " + testBoard.manhattan());

        System.out.println("Check twin: " + testBoard.twin().toString());
        for (Board b : testBoard.neighbors()) {
            System.out.println("Check neighbours: " + b);
        }

        int[][] test2 = new int[3][3];
        test2[0][0] = 2;
        test2[0][1] = 1;
        test2[0][2] = 3;
        test2[1][0] = 4;
        test2[1][1] = 5;
        test2[1][2] = 6;
        test2[2][0] = 7;
        test2[2][1] = 8;
        test2[2][2] = 0;
        Board test2Board = new Board(test2);
        for (Board b : test2Board.neighbors()) {
            System.out.println("Check corner case neighbours : " + b);
        }

        int[][] test3 = new int[2][2];
        test3[0][0] = 1;
        test3[0][1] = 3;
        test3[1][0] = 2;
        test3[1][1] = 0;
        Board test3Board = new Board(test3);
        System.out.println("Manhattan distance should be 4: " + test3Board.manhattan());
    }

    private class BoardIterable implements Iterable<Board> {
        private ArrayList<Board> neighbours = new ArrayList<>();

        public BoardIterable(Board board) {
            int blankRow = 0, blankCol = 0;
            for (int i = 0; i < dimension; i++) {
                for (int j = 0; j < dimension; j++) {
                    if (board.blocks[i][j] == BLANK_BLOCK) {
                        blankRow = i;
                        blankCol = j;
                        break;
                    }
                }
            }
            if (blankCol != 0) {
                neighbours.add(createTwinBoard(blankRow, blankCol, blankRow, blankCol - 1));
            }
            if (blankRow != 0) {
                neighbours.add(createTwinBoard(blankRow, blankCol, blankRow - 1, blankCol));
            }
            if (blankCol != dimension - 1) {
                neighbours.add(createTwinBoard(blankRow, blankCol, blankRow, blankCol + 1));
            }
            if (blankRow != dimension - 1) {
                neighbours.add(createTwinBoard(blankRow, blankCol, blankRow + 1, blankCol));
            }
        }

        @Override
        public Iterator<Board> iterator() {
            return neighbours.iterator();
        }
    }

    private int[][] copyBlocks() {
        int[][] copy = new int[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            System.arraycopy(this.blocks[i], 0, copy[i], 0, dimension);
        }
        return copy;
    }

    // Create a twin by swap a pair of position
    private Board createTwinBoard(int aRow, int aCol, int bRow, int bCol) {
        int[][] copyBlocks = copyBlocks();
        int temp = copyBlocks[aRow][aCol];
        copyBlocks[aRow][aCol] = copyBlocks[bRow][bCol];
        copyBlocks[bRow][bCol] = temp;
        return new Board(copyBlocks);
    }
}
