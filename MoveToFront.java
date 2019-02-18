import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

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
            char code = sequence.add((int) ch);
            BinaryStdOut.write(code);
        }
        BinaryStdOut.flush();
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
        if (args.length != 1 || args[0].length() > 1) throw new IllegalArgumentException();

        if (args[0].charAt(0) == '-') MoveToFront.encode();
        else if (args[0].charAt(0) == '+') MoveToFront.decode();
        else throw new IllegalArgumentException();
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

        // helper linked list class
        private static class Node {
            private char letter;
            private Node next;
        }

        /**
         * Initializes an empty bag.
         */
        Bag() {
            first = null;
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
         * Adds the letter to this bag.
         *
         * @param letter the letter to add to this bag
         */
        int add(char letter) {
            int index = 0;
            if (inserted[letter]) {
                Iterator<Character> it = iterator();
                while (it.hasNext()) {
                    char ch = it.next();
                    if (ch == letter) {
                        it.remove();
                        break;
                    }
                    index++;
                }
            } else {
                index = letter;
            }
            inserted[letter] = true;
            first = new Node();
            Node oldFirst = first;
            first.letter = letter;
            first.next = oldFirst;
            return index;
        }

        /**
         * Adds the letter to this bag.
         *
         * @param index the letter to add to this bag
         */
        char add(int index) {
            char character = 0;
            if (inserted[index]) {
                Iterator<Character> it = iterator();
                int i = 0;
                while (it.hasNext()) {
                    char ch = it.next();
                    if (i == index) {
                        it.remove();
                        character = ch;
                        break;
                    }
                    i++;
                }
            } else {
                character = (char) index;
            }
            inserted[index] = true;
            first = new Node();
            Node oldFirst = first;
            first.letter = character;
            first.next = oldFirst;
            return character;
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
            }

            @Override
            public Character next() {
                if (!hasNext()) throw new NoSuchElementException();
                char item = current.letter;
                current = current.next;
                return item;
            }
        }
    }
}