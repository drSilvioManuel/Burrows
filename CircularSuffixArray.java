import edu.princeton.cs.algs4.StdOut;


public class CircularSuffixArray {

    private final int R = 256;
    private final String origin;
    private final int cntChars;
    private final Suffix[] suffixes;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (null == s) throw new IllegalArgumentException("Accepted string cannot be null");

        origin = s;
        cntChars = s.length();
        suffixes = new Suffix[cntChars];

        for (int i = 0; i < cntChars; i++) suffixes[i] = new Suffix(i);

        sortLsd();
    }

    // length of s
    public int length() {
        return cntChars;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= cntChars) throw new IllegalArgumentException("Wrong index");
        return suffixes[i].shift;
    }

    // unit testing (required)
    public static void main(String[] args) {
        String word = args[0];
        CircularSuffixArray circularSuffixArray = new CircularSuffixArray(word);
        for (Suffix suffix : circularSuffixArray.suffixes) {
            StdOut.println(suffix);
        }
        StdOut.println("length: " + circularSuffixArray.length());
        for (int i = 0; i < circularSuffixArray.length(); i++) {
            StdOut.println("index[" + i + "]: " + circularSuffixArray.index(i));
        }
    }

    private void sortLsd() {
        Suffix[] aux = new Suffix[suffixes.length];
        for (int ch = cntChars - 1; ch >= 0; ch--) {
            int[] cnt = new int[R + 1];

            for (Suffix suffix : suffixes) cnt[suffix.charAt(ch) + 1]++;

            for (int r = 0; r < R; r++) cnt[r + 1] += cnt[r];

            for (Suffix suffix : suffixes) aux[cnt[suffix.charAt(ch)]++] = suffix;

            System.arraycopy(aux, 0, suffixes, 0, suffixes.length);
        }
    }


    private class Suffix {

        final int shift;

        Suffix(int s) {
            shift = s;
        }

        char charAt(int i) {
            int pointer = shift + i;
            int j = pointer < cntChars
                    ? pointer
                    : pointer - cntChars;
            return origin.charAt(j);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < cntChars; i++) sb.append(charAt(i));
            return sb.toString();
        }
    }
}
