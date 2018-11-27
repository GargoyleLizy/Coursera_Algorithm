package part_one;

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

import java.util.Comparator;
import java.util.Iterator;

/**
 * Created by zhenyali on 17/11/18.
 */
public class Solver {
    private MinPQ<PuzzleState> initialPQ;
    private MinPQ<PuzzleState> twinPQ;

    private boolean isSolvable;
    private PuzzleState solvedState;


    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("initial Board can not be null");
        }

        PuzzleStateComparator comparator = new PuzzleStateComparator();

        Board initialBoard = initial;
        initialPQ = new MinPQ<>(comparator);
        initialPQ.insert(new PuzzleState(initialBoard, 0, null));

        Board initialTwinBoard = initial.twin();
        twinPQ = new MinPQ<>(comparator);
        twinPQ.insert(new PuzzleState(initialTwinBoard, 0, null));

        // try solve
        solve();
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return this.isSolvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (this.isSolvable) {
            return solvedState.movesCount;
        } else {
            return -1;
        }
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (isSolvable) {
            return new Iterable<Board>() {
                @Override
                public Iterator<Board> iterator() {
                    Stack<Board> history = new Stack<>();
                    PuzzleState currentState = solvedState;
                    while (currentState != null) {
                        history.push(currentState.board);
                        currentState = currentState.parent;
                    }
                    return history.iterator();
                }
            };
        } else {
            return null;
        }

    }

    // solve a slider puzzle (given below)
    public static void main(String[] args) {
//        int[][] test = new int[3][3];
//        test[0][0] = 0;
//        test[0][1] = 1;
//        test[0][2] = 3;
//        test[1][0] = 4;
//        test[1][1] = 2;
//        test[1][2] = 5;
//        test[2][0] = 7;
//        test[2][1] = 8;
//        test[2][2] = 6;
//
//        Board testBoard = new Board(test);
//        Solver testSolver = new Solver(testBoard);
//
//        System.out.println("Should be solvable: " + testSolver.isSolvable());
//        System.out.println("Moves should be 4: " + testSolver.moves());
//        System.out.println("Solution: ");
//        for (Board b : testSolver.solution()) {
//            System.out.println(b.toString());
//        }
        int[][] test01 = new int[3][3];
        test01[0][0] = 1;
        test01[0][1] = 6;
        test01[0][2] = 2;
        test01[1][0] = 5;
        test01[1][1] = 3;
        test01[1][2] = 0;
        test01[2][0] = 4;
        test01[2][1] = 7;
        test01[2][2] = 8;

        Board testBoard01 = new Board(test01);
        Solver testSolver01 = new Solver(testBoard01);

        System.out.println("Should be solvable: " + testSolver01.isSolvable());
        System.out.println("Moves should be 10: " + testSolver01.moves());
        System.out.println("Solution: ");
        for (Board b : testSolver01.solution()) {
            System.out.println(b.toString());
        }

//        int[][] test01 = new int[3][3];
//        test01[0][0] = 1;
//        test01[0][1] = 2;
//        test01[0][2] = 3;
//        test01[1][0] = 4;
//        test01[1][1] = 6;
//        test01[1][2] = 5;
//        test01[2][0] = 7;
//        test01[2][1] = 8;
//        test01[2][2] = 0;
//
//        Board test01Board = new Board(test01);
//        Solver test01Solver = new Solver(test01Board);
//
//        System.out.println("Should be unsolvable: " + test01Solver.isSolvable());
//        System.out.println("Moves should be -1: " + test01Solver.moves());
//        System.out.println("Solution: ");
    }

    private void solve() {
        PuzzleState currentState = initialPQ.delMin();
        PuzzleState twinState = twinPQ.delMin();
        while (!(currentState.board.isGoal() || twinState.board.isGoal())) {
            //System.out.println("twin Steps: " + twinState.movesCount);
            // add legit neighbours;
            for (Board neighbour : currentState.board.neighbors()) {
                if (currentState.parent == null
                        || !currentState.parent.board.equals(neighbour)) {
                    // If the neighbour is legit, after critical optimization.
                    initialPQ.insert(
                            new PuzzleState(neighbour, currentState.movesCount + 1, currentState)
                    );
                }
            }
            for (Board neighbourT : twinState.board.neighbors()) {
                if (twinState.parent == null
                        || !neighbourT.equals(twinState.parent.board)) {
                    twinPQ.insert(
                            new PuzzleState(neighbourT, twinState.movesCount + 1, twinState)
                    );
                }
            }
            //Update the puzzle state.
            currentState = initialPQ.delMin();
            twinState = twinPQ.delMin();
        }
        if (twinState.board.isGoal()) {
            this.isSolvable = false;
        } else {
            this.isSolvable = true;
            this.solvedState = currentState;
        }
    }

    private class PuzzleState {
        private Board board;
        private int movesCount;
        private PuzzleState parent;

        public PuzzleState(Board board, int movesCount, PuzzleState parent) {
            this.board = board;
            this.movesCount = movesCount;
            this.parent = parent;
        }
    }

    private class PuzzleStateComparator implements Comparator<PuzzleState> {

        @Override
        public int compare(PuzzleState o1, PuzzleState o2) {
            if (o1.board.manhattan() + o1.movesCount < o2.board.manhattan() + o2.movesCount) {
                return -1;
            } else if (o1.board.manhattan() + o1.movesCount > o2.board.manhattan() + o2.movesCount) {
                return 1;
            } else {
                return 0;
            }
//            if (o1.board.manhattan() < o2.board.manhattan()) {
//                return -1;
//            } else if (o1.board.manhattan() > o2.board.manhattan()) {
//                return 1;
//            } else {
//                if (o1.movesCount < o2.movesCount) {
//                    return -1;
//                } else if (o1.movesCount > o2.movesCount) {
//                    return 1;
//                } else {
//                    return 0;
//                }
//            }
        }
    }
}
