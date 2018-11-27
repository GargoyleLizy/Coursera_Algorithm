package part_one;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by zhenyali on 24/9/18.
 */
public class Deque<Item> implements Iterable<Item> {
    private Node<Item> firstNode;
    private Node<Item> lastNode;
    private int itemNumber = 0;

    public Deque() {
        firstNode = null;
        lastNode = null;
        itemNumber = 0;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public int size() {
        return itemNumber;
    }

    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("add first parameter should not be null");
        }
        Node<Item> tempNode = new Node<>(item);
        if (isEmpty()) {
            firstNode = tempNode;
            lastNode = tempNode;
        } else {
            tempNode.setNext(firstNode);
            firstNode.setPrev(tempNode);
            firstNode = tempNode;
        }
        itemNumber++;
    }

    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("add last parameter should not be null");
        }
        Node<Item> tempNode = new Node<>(item);
        if (isEmpty()) {
            firstNode = tempNode;
            lastNode = tempNode;
        } else {
            lastNode.setNext(tempNode);
            tempNode.setPrev(lastNode);
            lastNode = tempNode;
        }
        itemNumber++;
    }

    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("Deque is empty");
        } else {
            Node<Item> tempNode = firstNode;
            if (size() == 1) {
                firstNode = null;
                lastNode = null;
            } else {
                firstNode = tempNode.next();
                firstNode.setPrev(null);
            }
            itemNumber--;
            return tempNode.currentNodeItem;
        }
    }

    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("Deque is empty");
        } else {
            Node<Item> tempNode = lastNode;
            if (size() == 1) {
                firstNode = null;
                lastNode = null;
            } else {
                lastNode = tempNode.prev();
                lastNode.setNext(null);
            }
            itemNumber--;
            return tempNode.currentNodeItem;
        }
    }

    @Override
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private Node<Item> currentNode = firstNode;

        @Override
        public boolean hasNext() {
            return currentNode != null;
        }

        @Override
        public Item next() {
            if (hasNext()) {
                Node<Item> tempNode = currentNode;
                currentNode = currentNode.next();
                return tempNode.currentNodeItem;
            } else {
                throw new NoSuchElementException("No more item available to return");
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Remove is not supported yet");
        }
    }

    private class Node<NodeItem> {
        private NodeItem currentNodeItem;
        private Node<NodeItem> next;
        private Node<NodeItem> prev;

        public Node(NodeItem value) {
            this.currentNodeItem = value;
            next = null;
            prev = null;
        }

        public Node<NodeItem> prev() {
            return this.prev;
        }

        public void setPrev(Node<NodeItem> newPrev) {
            this.prev = newPrev;
        }

        public Node<NodeItem> next() {
            return this.next;
        }

        public void setNext(Node<NodeItem> newNext) {
            this.next = newNext;
        }
    }

    public static void main(String[] args) {
        Deque<String> test = new Deque<>();
        test.addFirst("2");
        test.addLast("3");
        test.addFirst("1");
        test.addLast("4");
        test.addLast("5");
        for (String item : test) {
            System.out.println(item);
        }
    }
}
