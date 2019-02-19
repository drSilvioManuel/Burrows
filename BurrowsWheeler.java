import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class BurrowsWheeler {

    // apply Burrows-Wheeler transform, reading from standard input and writing to standard output
    public static void transform() {
        StringBuilder input = new StringBuilder();
        while (!BinaryStdIn.isEmpty()) {
            Character ch = BinaryStdIn.readChar();
            input.append(ch);
        }
        CircularSuffixArray csa = new CircularSuffixArray(input.toString());
        int cntChars = input.length();
        char[] t = new char[cntChars];
        int first = 0;
        int lastIndex = cntChars - 1;
        for (int i = 0; i < cntChars; i++) {
            if (csa.index(i) == 0) first = i;
            int pointer = lastIndex + i;
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
        HashMap<Character, List<Integer>> map = new HashMap<>();
        int row = 0;
        while (!BinaryStdIn.isEmpty()) {
            if (-1 == first) {
                first = BinaryStdIn.readInt();
                continue;
            }
            Character ch = BinaryStdIn.readChar();
            input.append(ch);
            if (!map.containsKey(ch)) {
                map.put(ch, new LinkedList<>());
            }
            map.get(ch).add(row);
            row++;
        }
        int cntChars = input.length();
        int[] next = new int[cntChars];
        char[] sorted = sortLsd(input);
        next[0] = first;
        for (int i = 1; i < cntChars; i++) {
            char charAtSorted = sorted[i];
            next[i] = map.get(charAtSorted).remove(0);
        }
        StringBuilder reassembled = new StringBuilder();
        row = 0;
        while (row < cntChars) {
            char ch = sorted[next[first]];
            reassembled.append(ch);
            first = next[first];
            row++;
        }
    }

    private static char[] sortLsd(StringBuilder origin) {
        final int R = 256;
        int cntChars = origin.length();
        char[] sorted = new char[cntChars];
        for (int ch = cntChars - 1; ch >= 0; ch--) {
            int[] cnt = new int[R + 1];

            cnt[origin.charAt(ch) + 1]++;

            for (int r = 0; r < R; r++) cnt[r + 1] += cnt[r];

            sorted[cnt[origin.charAt(ch)]++] = (char) ch;
        }
        return sorted;
    }

    // if args[0] is '-', apply Burrows-Wheeler transform
    // if args[0] is '+', apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        throw new RuntimeException("Not implemented");
    }
}
