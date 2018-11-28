import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {

    private Digraph mG;
    private SapVertice[] sapVertices;

    private static int teamVMark = -2;
    private static int teamWMark = -3;

    private boolean foundCommonAncestor = false;
    private int ancestor = -1, length = -1;

    // constructor takes a digraph (not necessarily a DAG)
    // All methods time proportional to E+V,
    public SAP(Digraph G) {
        if (G == null) {
            throw new IllegalArgumentException("Constructor does not take null argument");
        }
        for (int i = 0; i < G.V(); i++) {
            for (int neighbour : G.adj(i)) {
                if (neighbour < 0 || neighbour >= G.V()) {
                    throw new IllegalArgumentException("Input Digraph contains illegal item");
                }
            }
        }
        mG = new Digraph(G);

        sapVertices = new SapVertice[G.V()];
        for (int i = 0; i < G.V(); i++) {
            sapVertices[i] = new SapVertice(i);
        }
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        argumentCheckerVertice(v);
        argumentCheckerVertice(w);
        if (processForTwoVertice(v, w)) {
            return length;
        } else {
            return -1;
        }
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        argumentCheckerVertice(v);
        argumentCheckerVertice(w);
        if (processForTwoVertice(v, w)) {
            return ancestor;
        } else {
            return -1;
        }
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        argumentCheckerVerticeSet(v);
        argumentCheckerVerticeSet(w);
        if (processForTwoIterables(v, w)) {
            return length;
        } else {
            return -1;
        }
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        argumentCheckerVerticeSet(v);
        argumentCheckerVerticeSet(w);
        if (processForTwoIterables(v, w)) {
            return ancestor;
        } else {
            return -1;
        }
    }

    private boolean processForTwoVertice(int v, int w) {
        if (v == w) {
            ancestor = v;
            length = 0;
            return true;
        }
        Stack<Integer> vStack = new Stack<>();
        Stack<Integer> wStack = new Stack<>();
        vStack.push(v);
        wStack.push(w);
        return processForTwoStacks(vStack, wStack);
    }

    private boolean processForTwoIterables(Iterable<Integer> v, Iterable<Integer> w) {
        Stack<Integer> vStack = new Stack<>();
        Stack<Integer> wStack = new Stack<>();

        for (int item : v) {
            vStack.push(item);
        }
        for (int item : w) {
            wStack.push(item);
        }
        return processForTwoStacks(vStack, wStack);
    }


    private boolean processForTwoStacks(Stack<Integer> vTeam, Stack<Integer> wTeam) {
        clearPreviousRun();
        int step = 0;
        while (!vTeam.isEmpty() || !wTeam.isEmpty()) {
            // mark two teams
            vTeam = updateTeamVertices(vTeam, teamVMark, step);
            wTeam = updateTeamVertices(wTeam, teamWMark, step);
            // After a round, if we found an ancestor and there is no hope to find a shorter one.
            if (foundCommonAncestor && step >= length - 1) {
                return true;
            } else {
                // do another round
                vTeam = getNeighbours(vTeam);
                wTeam = getNeighbours(wTeam);
                step++;
            }
        }
        // if we finished, use the best result
        return foundCommonAncestor;
    }

    // Mark team members, and if they already marked, there may be an ancestor.
    private Stack<Integer> updateTeamVertices(Stack<Integer> currentTeam, int teamMarkId, int step) {
        Stack<Integer> updatedTeam = new Stack<>();
        for (int member : currentTeam) {
            //System.out.println("team "+ teamMarkId + " check " + member);
            if (sapVertices[member].isMarked()) {
                if (sapVertices[member].reachedby != teamMarkId) {
                    updateResultOnFindNewAncestor(member,
                            step + sapVertices[member].reachedDistance);
                    updatedTeam.push(member);
                }
            } else {
                sapVertices[member].mark(teamMarkId, step);
                // and we try to explore its member
                updatedTeam.push(member);
            }
        }
        return updatedTeam;
    }

    // TODO this depend on Graph which make it not immutable.
    private Stack<Integer> getNeighbours(Stack<Integer> currentTeam) {
        Stack<Integer> nextTeam = new Stack<>();
        for (int member : currentTeam) {
            for (int memberNeighbour : mG.adj(member)) {
                nextTeam.push(memberNeighbour);
            }
        }
        return nextTeam;
    }

    private void updateResultOnFindNewAncestor(int newAncestor, int newLength) {
        if (!foundCommonAncestor) {
            this.ancestor = newAncestor;
            this.length = newLength;
            foundCommonAncestor = true;
        } else {
            if (newLength < length) {
                this.ancestor = newAncestor;
                this.length = newLength;
            }
        }
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }

    private void clearPreviousRun() {
        // TODO this could be made more efficient
        // if we use different mark at each run.
        for (SapVertice sapVertice : sapVertices) {
            sapVertice.clearMark();
        }
        foundCommonAncestor = false;
        ancestor = -1;
        length = -1;
    }

    private void argumentCheckerVerticeSet(Iterable<Integer> v) {
        if (v == null) {
            throw new IllegalArgumentException("Argument can not be null");
        }
        for (Integer item : v) {
            if (item == null) {
                throw new IllegalArgumentException("Argument can not be null");
            }
            argumentCheckerVertice(item);
        }
    }

    private void argumentCheckerVertice(int v) {
        if (v < 0 || v >= mG.V()) {
            throw new IllegalArgumentException("detect illegal item value");
        }
    }

    private class SapVertice {
        int id;
        boolean marked = false;
        int reachedby = -1;// as default value;
        int reachedDistance = -1;

        public SapVertice(int id) {
            this.id = id;
        }

        public boolean mark(int childId, int reachedDistance) {
            if (!marked) {
                reachedby = childId;
                this.reachedDistance = reachedDistance;
                marked = true;
                return true;
            } else {
                return false;
            }
        }

        public boolean isMarked() {
            return this.marked;
        }

        public void clearMark() {
            marked = false;
            reachedby = -1;
            reachedDistance = -1;
        }
    }
}
