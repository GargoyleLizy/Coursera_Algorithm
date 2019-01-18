import edu.princeton.cs.algs4.TrieST;

public class BoggleDictTries {
    // Just store the English alphabet.
    // no space or other symbols.
    private static final int R = 26;

    private Node root; // root of trie
    private int n;// number of keys in trie

    private static class Node {
        private boolean isAnEnd = false;
        private boolean hasPostfix = false;
        private Node parent;
        private Node[] next = new Node[R];
    }

    public BoggleDictTries() {
        root = new Node();
    }

    // Insert a word into the dictionary.
    public void put(String key) {
        if (key == null) throw new IllegalArgumentException("argument to put() is null");
        String upperKey = key.toUpperCase();
        root = put(root, upperKey, 0);
    }

    private Node put(Node x, String key, int d) {
        if (x == null) x = new Node();
        if (d == key.length()) {
            if (!x.isAnEnd) n++;
            x.isAnEnd = true;
            return x;
        }
        char c = key.charAt(d);
        x.next[c - 'A'] = put(x.next[c - 'A'], key, d + 1);
        x.next[c - 'A'].parent = x;
        x.hasPostfix = true;
        return x;
    }

    private Node get(String word) {
        if (word == null) throw new IllegalArgumentException("Argument to get() is null");
        return get(root, word, 0);
    }

    private Node get(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) return x;
        char c = key.charAt(d);
        return get(x.next[c], key, d + 1);
    }

    public boolean contains(String word) {
        if (word == null) throw new IllegalArgumentException("Argument to contains() is null");
        return get(word) != null;
    }

    public boolean hasMatchedPrefix(String prefix) {
        if (prefix == null) throw new IllegalArgumentException("Argument to hasPrefix() is null");
        Node prefixNode = get(prefix);
        if (prefixNode == null) {
            return false;
        } else {
            return prefixNode.hasPostfix || prefixNode.isAnEnd;
        }
    }

    /**
     * Returns the number of words in the dictionary.
     *
     * @return the number of words in the dictionary.
     */
    public int size() {
        return n;
    }

    private Node currentParentNode;
    private Node currentNode;
    private StringBuilder currentWord;

    /**
     * Start a Boggle run with a start character.
     *
     */
    public void startABoggleTry() {
        currentNode = root;
        currentWord = new StringBuilder();
    }

    // Try expand the existing string
    public void appendCharacter(char appendC) {
        //System.out.println("append Character: "  +appendC  +" node: " + currentNode);
        currentParentNode = currentNode;
        currentNode = currentNode.next[appendC - 'A'];
        currentWord.append(appendC);
    }

    // No more possible answers, step back to last state.
    public void stepBack() {
        currentNode = currentParentNode;
        if(currentNode!=null){
            currentParentNode = currentNode.parent;
        }
        currentWord.replace(currentWord.length() - 1, currentWord.length(), "");
    }

    public boolean isCurrentAWordMatch() {
        if (currentNode == null) {
            return false;
        }
        return currentNode.isAnEnd;
    }

    public String getMatchedWord() {
        return currentWord.toString();
    }

    public boolean stillHasPostfix() {
        if (currentNode == null) {
            return false;
        }
        return currentNode.hasPostfix;
    }

    // A valid word should not contain any character other than
    // the alphabet.
    //TODO: we assume all the words in dictionary is valid right now.
    private boolean isValidEnglishWord(String word) {
        return true;
    }


}
