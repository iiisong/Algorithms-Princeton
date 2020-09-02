import java.util.Comparator;
import java.util.Arrays;
import edu.princeton.cs.algs4.StdOut;


public class CircularSuffixArray {
	private final String s;
	private final Integer[] index;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
    	if (s == null) {
    		throw new IllegalArgumentException("input string cannot be null");
    	}

    	this.s = s;

    	index = new Integer[s.length()];

    	for (int i = 0; i < s.length(); i++) {
    		index[i] = i;
    	}

    	Arrays.sort(index, new MyComparator());
    }

    private class MyComparator implements Comparator<Integer> {
    	public int compare(Integer i1, Integer i2) {
    		for (int i = 0; i < s.length(); i++) {
    			char c1 = s.charAt((i1 + i) % s.length());
    			char c2 = s.charAt((i2 + i) % s.length());

    			int result = Character.compare(c1, c2);

    			if (result != 0) {
    				return result;
    			}
    		}
    		
    		return 0;
    	}
    }

    // length of s
    public int length() {
    	return s.length();
    }

    // returns index of ith sorted suffix
    public int index(int i) {
    	if (i < 0 || i >= index.length) {
    		throw new IllegalArgumentException();
    	}
    	return index[i];
    }

    // unit testing (required)
    public static void main(String[] args) {
    	if (args.length == 0) {
    		test1();
    		return;
    	}

    	String inString = args[0];
    	CircularSuffixArray csa = new CircularSuffixArray(inString);
    	for (int i = 0; i < csa.length(); i++) {
    		StdOut.println(csa.index(i));
    	} 
    }

    private static void test1() {
    	CircularSuffixArray csa = new CircularSuffixArray("ABRACADABRA!");
    	assert csa.length() == 12;
    	assert csa.index(0) == 11;
    	assert csa.index(3) == 0;
    	assert csa.index(7) != 0;
    }
}