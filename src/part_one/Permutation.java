package part_one;

import edu.princeton.cs.algs4.StdIn;

import java.util.NoSuchElementException;

/**
 * Created by zhenyali on 26/9/18.
 */
public class Permutation {
    public static void main(String[] args) {
        if (args.length < 1) {
            throw new IllegalArgumentException("Not enough arguments");
        }
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> randomizedQueue = new RandomizedQueue<>();
        while (true) {
            try {
                String item = StdIn.readString();
                randomizedQueue.enqueue(item);
            } catch (NoSuchElementException e) {
                break;
            }
        }
        int counter = 0;
        for (String item : randomizedQueue) {
            System.out.println(item);
            counter++;
            if (counter >= k) {
                break;
            }
        }
    }
}
