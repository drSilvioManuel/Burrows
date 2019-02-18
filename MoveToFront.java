import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MoveToFront {

    private final static int R = 256;

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        Reader r = new Reader();

        Bag sequence = new Bag();
        for (Character ch : r) {
            int code = sequence.add(ch);
            BinaryStdOut.write(code);
        }
        BinaryStdOut.flush();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        Reader r = new Reader();

        Bag sequence = new Bag();
        for (Character ch : r) {
            char code = (char) sequence.add(ch);
            BinaryStdOut.write(code);
        }
        BinaryStdOut.flush();
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
        if (args.length != 1 || args[0].length() > 1) throw new IllegalArgumentException();

        boolean isEncoding;
        if (args[0].charAt(0) == '-') isEncoding = true;
        else if (args[0].charAt(0) == '+') isEncoding = false;
        else throw new IllegalArgumentException();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                if (isEncoding) MoveToFront.encode();
                else MoveToFront.decode();
            }
        } catch (IOException e) {
            System.out.println("Error while closing stream: " + e);
        }
    }

    private static class Reader implements Iterable<Character> {

        @Override
        public Iterator<Character> iterator() {
            return new ReaderIterator();
        }
    }

    private static class ReaderIterator implements Iterator<Character> {

        @Override
        public boolean hasNext() {
            return !BinaryStdIn.isEmpty();
        }

        @Override
        public Character next() {
            return BinaryStdIn.readChar();
        }
    }


    private static class Bag implements Iterable<Character> {


        boolean[] inserted = new boolean[R];
        private Node first;    // beginning of bag
        private int n;               // number of elements in bag

        // helper linked list class
        private static class Node {
            private char item;
            private Node next;
        }

        /**
         * Initializes an empty bag.
         */
        public Bag() {
            first = null;
            n = 0;
        }

        /**
         * Returns true if this bag is empty.
         *
         * @return {@code true} if this bag is empty;
         * {@code false} otherwise
         */
        public boolean isEmpty() {
            return first == null;
        }

        /**
         * Returns the number of items in this bag.
         *
         * @return the number of items in this bag
         */
        public int size() {
            return n;
        }

        /**
         * Adds the item to this bag.
         *
         * @param item the item to add to this bag
         */
        public int add(char item) {
            int index = 0;
            if (inserted[item]) {
                Iterator<Character> it = iterator();
                while (it.hasNext()) {
                    char ch = it.next();
                    if (ch == item) {
                        it.remove();
                        index++;
                        break;
                    }
                }
            } else {
                index = item;
            }
            inserted[item] = true;
            first = new Node();
            Node oldFirst = first;
            first.item = item;
            first.next = oldFirst;
            n++;
            return index;
        }


        /**
         * Returns an iterator that iterates over the items in this bag in arbitrary order.
         *
         * @return an iterator that iterates over the items in this bag in arbitrary order
         */
        public Iterator<Character> iterator() {
            return new ListIterator(first);
        }

        // an iterator, doesn't implement remove() since it's optional
        private class ListIterator implements Iterator<Character> {
            private Node current;

            ListIterator(Node first) {
                current = first;
            }

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public void remove() {
                Node oldCurrent = current;
                current = oldCurrent.next;
                n--;
            }

            @Override
            public Character next() {
                if (!hasNext()) throw new NoSuchElementException();
                char item = current.item;
                current = current.next;
                return item;
            }
        }
    }
}