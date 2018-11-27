import edu.princeton.cs.algs4.In;

public class WordNet {

    // constructor takes the name of the two input files
    // Required linearithmic or better
    public WordNet(String synsets, String hypernyms) {
        In in = new In(synsets);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return null;
    }

    // is the word a WordNet noun?
    // Required logarithmic or better
    public boolean isNoun(String word) {
        return false;
    }

    // distance between nounA and nounB (defined below)
    // Required in linear to the size of WordNet
    public int distance(String nounA, String nounB) {
        return -1;
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    // Required in linear to the size of word net.
    public String sap(String nounA, String nounB) {
        return null;
    }

    // do unit testing of this class
    public static void main(String[] args) {

    }
}
