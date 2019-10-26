/* *****************************************************************************
 *  Name: RandomizedQueue
 *  Date: 26/10/19
 *  Description: Similar to a stack or queue, except that the item removed is chosen uniformly at random among items
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] items;

    private int size;
    private int holes;

    // construct an empty randomized queue
    public RandomizedQueue() {
        items = (Item[]) new Object[2];
        size = 0;
        holes = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return realSize() == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return realSize();
    }

    private int realSize() {
        return size - holes;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Null enqueue");
        }

        if (size == items.length) {
            resize(2 * items.length);
        }

        items[size] = item;
        size++;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("No element to dequeue");
        }

        int idxToDequeue = StdRandom.uniform(size);
        while (items[idxToDequeue] == null) {
            idxToDequeue = StdRandom.uniform(size);
        }
        Item toDequeue = items[idxToDequeue];
        items[idxToDequeue] = null;
        holes++;
        if (holes > size / 2) {
            compact();
        }
        if (size < items.length / 4) {
            resize(items.length / 4);
        }
        return toDequeue;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException("No element to sample");
        }

        int idxToSample = StdRandom.uniform(size);
        while (items[idxToSample] == null) {
            idxToSample = StdRandom.uniform(size);
        }
        return items[idxToSample];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private int i;
        private int fixedSize;

        private int[] order;

        public RandomizedQueueIterator() {
            if (holes > 0) {
                compact();
            }

            i = 0;
            fixedSize = size;
            order = new int[fixedSize];

            for (int j = 0; j < fixedSize; j++) {
                order[j] = j;
            }
            StdRandom.shuffle(order);
        }

        public boolean hasNext() {
            return i < fixedSize;
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No more elements to iterate");
            }
            Item item = items[order[i]];
            i++;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private void resize(int capacity) {
        Item[] newItems = (Item[]) new Object[capacity];
        fixHoles(items, newItems);
        items = newItems;
        size = realSize();
        holes = 0;
    }

    private void fixHoles(Item[] source, Item[] dest) {
        int fast = 0;
        int slow = 0;
        for (; fast < size; fast++) {
            if (source[fast] != null) {
                dest[slow] = source[fast];
                slow++;
            }
        }
        if (slow < fast) {
            while (slow != fast) {
                source[slow] = null;
                slow++;
            }
        }
    }

    private void compact() {
        fixHoles(items, items);
        size = realSize();
        holes = 0;
    }

    private static void assertTrue(boolean cond, String msg) {
        if (!cond) {
            throw new RuntimeException(msg);
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> rq = new RandomizedQueue<>();
        assertTrue(rq.isEmpty(), "Empty after construction");

        rq.enqueue(3);
        assertTrue(!rq.isEmpty(), "Not empty after enqueuing");
        rq.dequeue();
        assertTrue(rq.isEmpty(), "Empty after one element dequeuing");

        rq.enqueue(1);
        assertTrue(1 == rq.dequeue(), "Dequeued element equals to enqueued");

        assertTrue(rq.isEmpty(), "Empty sanity check");

        int n = 10;
        for (int i = 0; i < n; i++) {
            rq.enqueue(i);
        }

        for (int i = 0; i < n; i++) {
            rq.dequeue();
        }
        assertTrue(rq.isEmpty(), "Empty sanity check");

        rq.enqueue(1);
        rq.enqueue(2);

        boolean isDifferent = false;
        for (int i = 0; i < 1000; i++) {
            if (rq.sample() == 1) {
                isDifferent = true;
                break;
            }
        }
        assertTrue(isDifferent, "Different values on sampling");

        n = 100;
        for (int i = 0; i < n; i++) {
            rq.enqueue(i);
        }

        while (!rq.isEmpty()) {
            assertTrue(rq.dequeue() != null, "Dequeing all elements");
        }

        n = 10;
        for (int i = 0; i < n; i++) {
            rq.enqueue(i);
        }

        for (Integer i : rq) {
            StdOut.print(" " + i);
        }
        StdOut.println();

        StdOut.println("---");

        for (Integer i : rq) {
            StdOut.print(" " + i);
        }
        StdOut.println();
    }
}
