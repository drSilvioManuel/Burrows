import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.LinkedList;


public class MoveToFront {

    private final static int R = 256;

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        LinkedList<Character> sequence = new LinkedList<>();
        for (int i = 0; i < R; i++) {
            sequence.addLast((char) (i & 0xff));
        }

        while (!BinaryStdIn.isEmpty()) {
            char charCode = BinaryStdIn.readChar();

            int index = sequence.indexOf(charCode);

            sequence.removeFirstOccurrence(charCode);
            sequence.addFirst(charCode);

            BinaryStdOut.write((char) (index & 0xff));
        }
        BinaryStdOut.flush();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        LinkedList<Character> sequence = new LinkedList<>();
        for (int i = 0; i < R; i++) {
            sequence.addLast((char) (i & 0xff));
        }

        while (!BinaryStdIn.isEmpty()) {
            char charCode = BinaryStdIn.readChar();

            int index = sequence.get(charCode);

            sequence.remove(charCode);
            sequence.addFirst((char) (index & 0xff));

            BinaryStdOut.write((char) (index & 0xff));
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
}