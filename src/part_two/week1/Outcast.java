public class Outcast {
    private WordNet mWordNet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.mWordNet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        int maxDistance = 0;
        int outcastId = 0;
        for (int i = 0; i < nouns.length; i++) {
            int overallDistance = computeOverallDistance(nouns[i], nouns);
            if (overallDistance > maxDistance) {
                maxDistance = overallDistance;
                outcastId = i;
            }
        }
        return nouns[outcastId];
    }

    private int computeOverallDistance(String source, String[] nouns) {
        int sum = 0;
        for (String item : nouns) {
            if (!source.endsWith(item)) {
                sum += mWordNet.distance(source, item);
            }
        }
        return sum;
    }

    // see test client below
    public static void main(String[] args) {
        WordNet wordNet = new WordNet("synsets.txt", "hypernyms.txt");
        Outcast outcast = new Outcast(wordNet);
        String[] test1 = new String[5];
        test1[0] = "horse";
        test1[1] = "zebra";
        test1[2] = "cat";
        test1[3] = "bear";
        test1[4] = "table";
        String outcastItem = outcast.outcast(test1);
        System.out.println("Outcast item is : " + outcastItem);
    }
}
