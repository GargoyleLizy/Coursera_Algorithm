public class BoggleDictTries {
    // Just store the English alphabet.
    // no space or other symbols.
    private static final int R = 26;

    private Node root; // root of trie
    private int n;// number of keys in trie

    private static class Node {
        private boolean isAnEnd;
        private boolean hasPostfix;
        private Node parent;
        private Node[] next = new Node[R];
    }

    public BoggleDictTries() {

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
     * @param starterC should be an upper case English alphabet character.
     */
    public void startABoggleTry(char starterC) {
        currentNode = root;
        currentNode = root.next[starterC - 'A'];

        currentWord = new StringBuilder();
        currentWord.append(starterC);
    }

    // Try expand the existing string
    public void appendCharacter(char appendC){
        currentNode = currentNode.next[appendC -'A'];
        currentWord.append(appendC);
    }

    // No more possible answers, step back to last state.
    public void stepBack(){
        currentNode = currentParentNode;
        currentWord.replace(currentWord.length()-1,currentWord.length(),"");
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
