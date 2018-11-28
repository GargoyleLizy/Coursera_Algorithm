import edu.princeton.cs.algs4.Digraph;

import java.util.TreeSet;

public class SAP {

    Digraph mG;
    private SapVertice[] sapVertices;


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
        mG = G;

        sapVertices = new SapVertice[G.V()];
        for (int i = 0; i < G.V(); i++) {
            sapVertices[i] = new SapVertice(i);
        }
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (v < 0 || v >= mG.V() || w < 0 || w >= mG.V()) {
            throw new IllegalArgumentException("Input arguments contains illegal item");
        }
        if (processForTwoVertice(v, w)) {
            return length;
        } else {
            return -1;
        }
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (v < 0 || v >= mG.V() || w < 0 || w >= mG.V()) {
            throw new IllegalArgumentException("Input arguments contains illegal item");
        }
        if (processForTwoVertice(v, w)) {
            return ancestor;
        } else {
            return -1;
        }
    }

    private int ancestor = -1, length = -1;

    private boolean processForTwoVertice(int v, int w) {
        clearAdjListMarks();
        if (v == w) {
            ancestor = v;
            length = 0;
            return true;
        }
        TreeSet<Integer> neighbourV = new TreeSet<>();
        for (int neighbour : mG.adj(v)) {
            neighbourV.add(neighbour);
        }
        TreeSet<Integer> neighbourW = new TreeSet<>();
        for (int neighbour : mG.adj(w)) {
            neighbourW.add(neighbour);
        }
        sapVertices[v].mark(v, 0);
        sapVertices[w].mark(w, 0);
        int stepV = 1;
        int stepW = 1;

        while (!neighbourV.isEmpty() || !neighbourW.isEmpty()) {
            // check neighbours
            // do one round with v
            for (int neighbour : neighbourV) {
                if (sapVertices[neighbour].isMarked()) {
                    if (sapVertices[neighbour].reachedby == v) {
                        // ignore vertex marked by itself
                    } else {
                        // found ancestor, break;
                        //System.out.println("" + v + "found " + neighbour);
                        ancestor = neighbour;
                        length = stepV + sapVertices[neighbour].reachedDistance;
                        return true;
                    }
                } else {
                    //System.out.println("" + v + " mark " + neighbour);
                    sapVertices[neighbour].mark(v, stepV);
                }
            }
            // do one round with w
            for (int neighbour : neighbourW) {
                if (sapVertices[neighbour].isMarked()) {
                    if (sapVertices[neighbour].reachedby == w) {
                        // ignore vertex marked by self
                    } else {
                        // found ancestor, break;
                        //System.out.println("" + w + " found " + neighbour);
                        ancestor = neighbour;
                        length = stepW + sapVertices[neighbour].reachedDistance;
                        return true;
                    }
                } else {
                    //System.out.println("" + w + " mark " + neighbour);
                    sapVertices[neighbour].mark(w, stepW);
                }
            }
            // if not found, need to update the neighbour and step
            TreeSet<Integer> tempNeighbour = new TreeSet<>();
            for (int neighbour : neighbourV) {
                for (int neighboursNeighbour : mG.adj(neighbour)) {
                    //System.out.println("" + v + " after step " + stepV + " add : " + neighboursNeighbour);
                    tempNeighbour.add(neighboursNeighbour);
                }
            }
            neighbourV = tempNeighbour;
            stepV++;
            tempNeighbour = new TreeSet<>();
            for (int neighbour : neighbourW) {
                for (int neighboursNeighbour : mG.adj(neighbour)) {
                    //System.out.println("" + w + " after step " + stepW + " add : " + neighboursNeighbour);
                    tempNeighbour.add(neighboursNeighbour);
                }
            }
            neighbourW = tempNeighbour;
            stepW++;
        }
        return false;
    }

    private static int teamVMark = -2;
    private static int teamWMark = -3;

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        for (int vItem : v) {
            if (vItem < 0 || vItem >= mG.V()) {
                throw new IllegalArgumentException("Input arguments contains illegal item");
            }
        }
        for (int wItem : w) {
            if (wItem < 0 || wItem >= mG.V()) {
                throw new IllegalArgumentException("Input arguments contains illegal item");
            }
        }
        if (processForTwoSets(v, w)) {
            return length;
        } else {
            return -1;
        }
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        for (int vItem : v) {
            if (vItem < 0 || vItem >= mG.V()) {
                throw new IllegalArgumentException("Input arguments contains illegal item");
            }
        }
        for (int wItem : w) {
            if (wItem < 0 || wItem >= mG.V()) {
                throw new IllegalArgumentException("Input arguments contains illegal item");
            }
        }
        if (processForTwoSets(v, w)) {
            return ancestor;
        } else {
            return -1;
        }
    }

    private boolean processForTwoSets(Iterable<Integer> v, Iterable<Integer> w) {
        clearAdjListMarks();

        TreeSet<Integer> neighbourV = new TreeSet<>();
        for (int neighbour : v) {
            neighbourV.add(neighbour);
            sapVertices[neighbour].mark(teamVMark, 0);
        }
        TreeSet<Integer> neighbourW = new TreeSet<>();
        for (int neighbour : w) {
            neighbourW.add(neighbour);
            if (sapVertices[neighbour].isMarked()) {
                if (sapVertices[neighbour].reachedby == teamVMark) {
                    ancestor = neighbour;
                    length = 0;
                    return true;
                }
            } else {
                sapVertices[neighbour].mark(teamWMark, 0);
            }
        }
        int stepV = 1;
        int stepW = 1;

        while (!neighbourV.isEmpty() || !neighbourW.isEmpty()) {
            // check neighbours
            // do one round with v
            for (int neighbour : neighbourV) {
                if (sapVertices[neighbour].isMarked()) {
                    if (sapVertices[neighbour].reachedby == teamVMark) {
                        // ignore vertex marked by itself
                    } else {
                        // found ancestor, break;
                        ancestor = neighbour;
                        length = stepV + sapVertices[neighbour].reachedDistance;
                        return true;
                    }
                } else {
                    sapVertices[neighbour].mark(teamVMark, stepV);
                }
            }
            // do one round with w
            for (int neighbour : neighbourW) {
                if (sapVertices[neighbour].isMarked()) {
                    if (sapVertices[neighbour].reachedby == teamWMark) {
                        // ignore vertex marked by self
                    } else {
                        // found ancestor, break;
                        ancestor = neighbour;
                        length = stepW + sapVertices[neighbour].reachedDistance;
                        return true;
                    }
                } else {
                    sapVertices[neighbour].mark(teamWMark, stepW);
                }
            }
            // if not found, need to update the neighbour and step
            TreeSet<Integer> tempNeighbour = new TreeSet<>();
            for (int neighbour : neighbourV) {
                for (int neighboursNeighbour : mG.adj(neighbour)) {
                    tempNeighbour.add(neighboursNeighbour);
                }
            }
            neighbourV = tempNeighbour;
            stepV++;
            tempNeighbour = new TreeSet<>();
            for (int neighbour : neighbourW) {
                for (int neighboursNeighbour : mG.adj(neighbour)) {
                    tempNeighbour.add(neighboursNeighbour);
                }
            }
            neighbourW = tempNeighbour;
            stepW++;
        }
        return false;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        Digraph testGraph = new Digraph(13);
        testGraph.addEdge(7, 3);
        testGraph.addEdge(8, 3);
        testGraph.addEdge(3, 1);
        testGraph.addEdge(4, 1);
        testGraph.addEdge(5, 1);
        testGraph.addEdge(9, 5);
        testGraph.addEdge(10, 5);
        testGraph.addEdge(11, 10);
        testGraph.addEdge(12, 10);
        testGraph.addEdge(1, 0);
        testGraph.addEdge(2, 0);
        SAP testSap = new SAP(testGraph);
        int length_11_3 = testSap.length(11, 3);
        System.out.println("length of 11,3 is 4: " + length_11_3);
        int ancestor_7_3 = testSap.ancestor(11, 3);
        System.out.println("Ancestor of 11,3 is 1: " + ancestor_7_3);
    }

    private void clearAdjListMarks() {
        for (SapVertice sapVertice : sapVertices) {
            sapVertice.clearMark();
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
