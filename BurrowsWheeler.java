import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.Queue;

import java.util.HashMap;


public class BurrowsWheeler {

    // apply Burrows-Wheeler transform, reading from standard input and writing to standard output
    public static void transform() {
        StringBuilder input = new StringBuilder();
        while (!BinaryStdIn.isEmpty()) {
            char ch = BinaryStdIn.readChar();
            input.append(ch);
        }
        CircularSuffixArray csa = new CircularSuffixArray(input.toString());
        int cntChars = input.length();
        char[] t = new char[cntChars];
        int first = 0;
        int lastIndex = cntChars - 1;
        for (int i = 0; i < cntChars; i++) {
            if (csa.index(i) == 0) first = i;
            int pointer = lastIndex + csa.index(i);
            int suffixLastIndex = pointer < cntChars
                    ? pointer
                    : pointer - cntChars;
            t[i] = input.charAt(suffixLastIndex);
        }

        BinaryStdOut.write(first);
        for (int i = 0; i < cntChars; i++) BinaryStdOut.write((char) (t[i] & 0xff));

        BinaryStdOut.flush();
    }

    // apply Burrows-Wheeler inverse transform, reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = -1;
        StringBuilder input = new StringBuilder();
        HashMap<Character, Queue<Integer>> map = new HashMap<>();
        int row = 0;
        while (!BinaryStdIn.isEmpty()) {
            if (-1 == first) {
                first = BinaryStdIn.readInt();
                continue;
            }
            char ch = BinaryStdIn.readChar();
            input.append(ch);
            if (!map.containsKey(ch)) {
                map.put(ch, new Queue<>());
            }
            map.get(ch).enqueue(row);
            row++;
        }

        int cntChars = input.length();
        int[] next = new int[cntChars];
        char[] sorted = sortLsd(input);

        for (int i = 0; i < cntChars; i++) {
            char charAtSorted = sorted[i];
            next[i] = map.get(charAtSorted).dequeue();
        }
        row = 0;
        while (row < cntChars) {
            char ch = sorted[first];
            BinaryStdOut.write((char) (ch & 0xff));
            first = next[first];
            row++;
        }
        BinaryStdOut.flush();
    }

    private static char[] sortLsd(StringBuilder origin) {
        final int R = 256;
        int cntChars = origin.length();
        char[] sorted = new char[cntChars];
        int[] cnt = new int[R + 1];

        for (int ch = cntChars - 1; ch >= 0; ch--) cnt[origin.charAt(ch) + 1]++;

        for (int r = 0; r < R; r++) cnt[r + 1] += cnt[r];

        for (int ch = cntChars - 1; ch >= 0; ch--) sorted[cnt[origin.charAt(ch)]++] = origin.charAt(ch);

        return sorted;
    }

    // if args[0] is '-', apply Burrows-Wheeler transform
    // if args[0] is '+', apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args.length != 1 || args[0].length() > 1) throw new IllegalArgumentException();

        if (args[0].charAt(0) == '-') BurrowsWheeler.transform();
        else if (args[0].charAt(0) == '+') BurrowsWheeler.inverseTransform();
        else throw new IllegalArgumentException();
    }
}
