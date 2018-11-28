

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.Topological;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;

import java.util.ArrayList;
import java.util.LinkedList;

public class WordNet {
    private static String LINE_SEPERATOR = ",";
    private static String SYNSET_SEPERATOR = " ";

    // Two data structure to store the data
    // One to use the int as key
    private ArrayList<String> synsetArr = new ArrayList<>();
    // One to use the string as key
    private RedBlackBST<String, LinkedList<Integer>> synsetBST = new RedBlackBST<>();

    private Digraph hypernymGraph;
    private SAP synsetSap;

    // constructor takes the name of the two input files
    // Required linearithmic or better
    public WordNet(String synsets, String hypernyms) {

        In in = new In(synsets);
        int count = 0;
        while (in.hasNextLine()) {
            String line = in.readLine();
            String[] result = line.split(LINE_SEPERATOR);
            int id = Integer.parseInt(result[0]);
            String synsetValue = result[1];
            String[] synsetSplit = synsetValue.split(SYNSET_SEPERATOR);
            // populate data structure
            synsetArr.add(synsetValue);
            for (String splitSynset : synsetSplit) {
                LinkedList<Integer> previousIds = synsetBST.get(splitSynset);
                if (previousIds == null) {
                    LinkedList<Integer> currentResult = new LinkedList<>();
                    currentResult.add(id);
                    synsetBST.put(splitSynset, currentResult);
                } else {
                    previousIds.add(id);
                }
            }
            count = id;
        }
        //System.out.println("Count: " + count);
        hypernymGraph = digraphConstructAndCheck(hypernyms, count + 1);

        synsetSap = new SAP(hypernymGraph);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return synsetBST.keys();
    }

    // is the word a WordNet noun?
    // Required logarithmic or better
    // Then this should be in a BST, lets use redblack tree here.
    public boolean isNoun(String word) {
        if (word == null) {
            throw new IllegalArgumentException("isNoun does not take null as argument");
        }
        return synsetBST.contains(word);
    }

    // distance between nounA and nounB (defined below)
    // Required in linear to the size of WordNet
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA)) {
            throw new IllegalArgumentException("distance argument should be a noun: " + nounA);
        }
        if (!isNoun(nounB)) {
            throw new IllegalArgumentException("distance arguments should be a noun: " + nounB);
        }
        LinkedList<Integer> nounAidLinkedList = synsetBST.get(nounA);
        LinkedList<Integer> nounBidLinkedList = synsetBST.get(nounB);
        return synsetSap.length(nounAidLinkedList, nounBidLinkedList);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    // Required in linear to the size of word net.
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException("distance arguments should be inside the method.");
        }
        LinkedList<Integer> nounAidLinkedList = synsetBST.get(nounA);
        LinkedList<Integer> nounBidLinkedList = synsetBST.get(nounB);
        int ancestorId = synsetSap.ancestor(nounAidLinkedList, nounBidLinkedList);
        if (ancestorId == -1) {
            return null;
        } else {
            return synsetArr.get(ancestorId);
        }
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wordNet = new WordNet("synsets.txt", "hypernyms.txt");
    }

    private Digraph digraphConstructAndCheck(String hypernyms, int verticeCount) {
        Digraph hypernymGraph = new Digraph(verticeCount);
        int[] outArrowMark = new int[verticeCount];
        In hypernymIn = new In(hypernyms);
        while (hypernymIn.hasNextLine()) {
            String line = hypernymIn.readLine();
            String[] results = line.split(LINE_SEPERATOR);
            int id = Integer.parseInt(results[0]);
            for (int i = 1; i < results.length; i++) {
                int hypernymId = Integer.parseInt(results[i]);
                hypernymGraph.addEdge(id, hypernymId);
                outArrowMark[id] = 1;
            }
        }
        // Then check outArrowMark, there should be exactly only 1 zero left for rooted DAG.
        int potentialRoot = -1;
        for (int i = 0; i < outArrowMark.length; i++) {
            if (outArrowMark[i] == 0) {
                if (potentialRoot == -1) {
                    potentialRoot = i;
                } else {
                    throw new IllegalArgumentException("Wordnet digraph have multiple roots");
                }
            }
        }
        if (potentialRoot == -1) {
            throw new IllegalArgumentException("wordnet digraph has no root");
        }
        if (!(new Topological(hypernymGraph)).hasOrder()) {
            throw new IllegalArgumentException("Wordnet digraph is not DAG");
        }
        BreadthFirstDirectedPaths bfdp = new BreadthFirstDirectedPaths(hypernymGraph.reverse(), potentialRoot);
        //System.out.println("potential root: " + potentialRoot);
        for (int i = 0; i < hypernymGraph.V(); i++) {
            if (!bfdp.hasPathTo(i)) {
                throw new IllegalArgumentException("Wordnet digraph root can not reach " + i);
            }
        }
        return hypernymGraph;
    }

    private class Synset {
        int id;
        String value;

        public Synset(int id, String value) {
            this.id = id;
            this.value = value;
        }
    }
}
