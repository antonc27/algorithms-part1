/* *****************************************************************************
 *  Name: Deque
 *  Date: 26/10/19
 *  Description: Generalization of a stack and a queue that supports adding and removing items from either the front or the back
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private int size = 0;

    private Node first = null;
    private Node last = null;

    private class Node {
        Item value;
        Node next;
        Node prev;
    }

    // construct an empty deque
    public Deque() {

    }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Null add first");
        }

        Node oldFirst = first;

        first = new Node();
        first.value = item;
        first.next = oldFirst;
        first.prev = null;

        if (oldFirst != null) {
            oldFirst.prev = first;
        }

        if (size == 0) {
            last = first;
        }

        size++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Null add last");
        }

        Node oldLast = last;

        last = new Node();
        last.value = item;
        last.next = null;
        last.prev = oldLast;

        if (oldLast != null) {
            oldLast.next = last;
        }

        if (size == 0) {
            first = last;
        }

        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("No more elements to remove from head");
        }
        Node toRemove = first;
        if (first.next != null) {
            first.next.prev = null;
        }
        first = first.next;
        size--;
        return toRemove.value;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("No more elements to remove from tail");
        }
        Node toRemove = last;
        if (last.prev != null) {
            last.prev.next = null;
        }
        last = last.prev;
        size--;
        return toRemove.value;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if (current == null) {
                throw new NoSuchElementException("No more elements to iterate");
            }
            Item value = current.value;
            current = current.next;
            return value;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private static void assertTrue(boolean cond, String msg) {
        if (!cond) {
            throw new RuntimeException(msg);
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<Integer> d = new Deque<>();
        assertTrue(d.isEmpty(), "Empty after construction");

        d.addFirst(3);
        assertTrue(!d.isEmpty(), "Not empty after insertion from head");
        d.removeFirst();
        assertTrue(d.isEmpty(), "Empty after one element removal from head");

        d.addFirst(1);
        assertTrue(1 == d.removeFirst(), "Removed from head element equals to inserted from head");

        assertTrue(d.isEmpty(), "Empty sanity check 1");

        d.addLast(3);
        assertTrue(!d.isEmpty(), "Not empty after insertion from tail");
        d.removeLast();
        assertTrue(d.isEmpty(), "Empty after one element removal from tail");

        d.addLast(1);
        assertTrue(1 == d.removeLast(), "Removed from tail element equals to inserted from tail");

        assertTrue(d.isEmpty(), "Empty sanity check 2");

        d.addFirst(2);
        assertTrue(2 == d.removeLast(), "Removed from tail element equals to inserted from head");

        d.addLast(5);
        assertTrue(5 == d.removeFirst(), "Removed from head element equals to inserted from tail");

        assertTrue(d.isEmpty(), "Empty sanity check 2");

        int n = 10;
        for (int i = 0; i < n; i++) {
            if (i % 2 == 0) {
                d.addFirst(i);
            } else {
                d.addLast(i);
            }
        }

        int count = 0;
        for (Integer i : d) {
            StdOut.println(i);
            count++;
        }
        assertTrue(count == n, "Iterate over all elements");
    }
}
