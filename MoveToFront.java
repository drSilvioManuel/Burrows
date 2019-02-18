import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.HexDump;

import java.io.*;
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
            BinaryStdOut.write((char) (code & 0xff));
        }
        BinaryStdOut.flush();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        Reader r = new Reader();

        Bag sequence = new Bag();
        StringBuilder sb = new StringBuilder();
        for (Character ch : r) {
            sb.append(ch);
            char code = sequence.add(ch & 0xff);
            BinaryStdOut.write((char) (code & 0xff));
        }
        BinaryStdOut.flush();
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
        if (args.length != 1 || args[0].length() > 1) throw new IllegalArgumentException();

        if (args[0].charAt(0) == '-') MoveToFront.encode();
        else if (args[0].charAt(0) == '+') MoveToFront.decode();
        else if (args[0].charAt(0) == 'e') {

            try {
                FileInputStream is = new FileInputStream(new File("__burrows/abra.txt"));
                File f = new File("__burrows/encoded_abra.txt");
                f.createNewFile();
                PrintStream os = new PrintStream(new FileOutputStream(f));
                System.setIn(is);
                System.setOut(os);
                MoveToFront.encode();
                is.close();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (args[0].charAt(0) == 'h') {
            try {
                FileInputStream is = new FileInputStream(new File("__burrows/encoded_abra.txt"));
                System.setIn(is);
                HexDump.main(new String[]{"16"});
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (args[0].charAt(0) == 'd') {
            try {
                FileInputStream is = new FileInputStream(new File("__burrows/encoded_abra.txt"));
                System.setIn(is);
                MoveToFront.decode();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else throw new IllegalArgumentException();
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
            private char letter;
            private Node next;
            private Node prev;
        }

        /**
         * Initializes an empty bag.
         */
        Bag() {
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
                inserted[letter] = true;
                index = letter;
            }
            Node oldFirst = first;
            first = new Node();
            first.letter = letter;
            if (null != oldFirst) {
                first.next = oldFirst;
                oldFirst.prev = first;
            }
            n++;
            return index;
        }

        /**
         * Adds the letter to this bag.
         *
         * @param index the letter to add to this bag
         */
        char add(int index) {
            char character = 0;
            boolean isIndexBased = index < size();
            if (inserted[index] || isIndexBased) {
                Iterator<Character> it = iterator();
                int i = 0;
                while (it.hasNext()) {
                    char ch = it.next();
                    if (i == index && isIndexBased || ch == (char) index) {
                        it.remove();
                        character = ch;
                        break;
                    }
                    i++;
                }
            } else {
                character = (char) index;
                inserted[character] = true;
            }
            Node oldFirst = first;

            first = new Node();
            first.letter = character;

            if (null != oldFirst) {
                first.next = oldFirst;
                oldFirst.prev = first;
            }
            n++;
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
            private Node forRemoving;

            ListIterator(Node first) {
                current = first;
            }

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public void remove() {
                Node prev = forRemoving.prev;
                Node next = forRemoving.next;

                if (null != prev) prev.next = next;
                if (null != next) next.prev = prev;

                n--;
            }

            @Override
            public Character next() {
                if (!hasNext()) throw new NoSuchElementException();
                char letter = current.letter;
                forRemoving = current;
                current = current.next;
                return letter;
            }
        }
    }
}