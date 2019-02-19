import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.HexDump;

import java.io.*;
import java.util.*;


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
        }
        else if (args[0].charAt(0) == 'h') {
            try {
                FileInputStream is = new FileInputStream(new File("__burrows/encoded_abra.txt"));
                System.setIn(is);
                HexDump.main(new String[]{"16"});
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (args[0].charAt(0) == 'd') {
            try {
                FileInputStream is = new FileInputStream(new File("__burrows/encoded_abra.txt"));
                System.setIn(is);
                MoveToFront.decode();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else throw new IllegalArgumentException();
    }
}