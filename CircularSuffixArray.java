import edu.princeton.cs.algs4.StdOut;


public class CircularSuffixArray {

    private final int CUTOFF =  15;
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

        sortQuick3();
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

    private void sortQuick3() {
        sort(suffixes, 0, suffixes.length - 1, 0);
    }

    // return the dth character of s, -1 if d = length of s
    private static int charAt(Suffix s, int d) {
        assert d >= 0 && d <= s.length();
        if (d == s.length()) return -1;
        return s.charAt(d);
    }

    // 3-way string quicksort a[lo..hi] starting at dth character
    private void sort(Suffix[] a, int lo, int hi, int d) {

        // cutoff to insertion sort for small subarrays
        if (hi <= lo + CUTOFF) {
            insertion(a, lo, hi, d);
            return;
        }

        int lt = lo, gt = hi;
        int v = charAt(a[lo], d);
        int i = lo + 1;
        while (i <= gt) {
            int t = charAt(a[i], d);
            if (t < v) exch(a, lt++, i++);
            else if (t > v) exch(a, i, gt--);
            else i++;
        }

        // a[lo..lt-1] < v = a[lt..gt] < a[gt+1..hi].
        sort(a, lo, lt - 1, d);
        if (v >= 0) sort(a, lt, gt, d + 1);
        sort(a, gt + 1, hi, d);
    }

    // sort from a[lo] to a[hi], starting at the dth character
    private static void insertion(Suffix[] a, int lo, int hi, int d) {
        for (int i = lo; i <= hi; i++)
            for (int j = i; j > lo && less(a[j], a[j-1], d); j--)
                exch(a, j, j-1);
    }

    // exchange a[i] and a[j]
    private static void exch(Suffix[] a, int i, int j) {
        Suffix temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    // is v less than w, starting at character d
    private static boolean less(Suffix v, Suffix w, int d) {
        for (int i = d; i < Math.min(v.length(), w.length()); i++) {
            if (v.charAt(i) < w.charAt(i)) return true;
            if (v.charAt(i) > w.charAt(i)) return false;
        }
        return v.length() < w.length();
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

        int length() {
            return origin.length();
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < cntChars; i++) sb.append(charAt(i));
            return sb.toString();
        }
    }
}
