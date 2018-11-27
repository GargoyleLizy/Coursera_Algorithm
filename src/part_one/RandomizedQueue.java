package part_one;

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by zhenyali on 24/9/18.
 */
public class RandomizedQueue<Item> implements Iterable<Item> {
    //private
    private int size = 0;
    private Object[] currentArray = new Object[2];

    public RandomizedQueue() {

    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Can not enqueue null item");
        }
        if (isEmpty()) {
            currentArray[size] = item;
        } else {
            //randomly swap this item with existing item.
            int swapIndex = StdRandom.uniform(size);
            currentArray[size] = currentArray[swapIndex];
            currentArray[swapIndex] = item;
        }
        size++;
        if (currentArray.length == size) {
            changeArraySize(size * 2);
        }
    }

    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        Item outOne = (Item) currentArray[size - 1];
        size--;
        if (size <= currentArray.length / 4) {
            changeArraySize(currentArray.length / 2);
        }
        return outOne;
    }

    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        int randomIndex = StdRandom.uniform(size);
        return (Item) currentArray[randomIndex];
    }

    @Override
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        int[] permutedIndex = new int[size];
        int currentIndex = 0;

        public RandomizedQueueIterator() {
            for (int i = 0; i < size; i++) {
                permutedIndex[i] = i;
            }
            StdRandom.shuffle(permutedIndex);
        }

        @Override
        public boolean hasNext() {
            return currentIndex < size;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("There is no next element available");
            }
            currentIndex++;
            return (Item) currentArray[permutedIndex[currentIndex - 1]];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Remove is not supported");
        }
    }

    public static void main(String[] args) {
        RandomizedQueue<String> test = new RandomizedQueue<>();
        test.enqueue("1");
        test.enqueue("2");
        test.enqueue("3");
        test.enqueue("4");
        test.enqueue("5");
        System.out.println("dequeue: " + test.dequeue());
        for (String item : test) {
            System.out.println(item);
        }
    }

    private void changeArraySize(int nextSize) {
        Object[] nextArray = new Object[nextSize];
        System.arraycopy(currentArray, 0, nextArray, 0, size);
        currentArray = nextArray;
    }
}
