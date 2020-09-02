import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output 
    public static void transform() {
		String s = BinaryStdIn.readString();
   		ArrayAndIndex result = transformHelper(s);
   		int first = result.i;

   		BinaryStdOut.write(first);
   		for (int i = 0; i < result.b.length; i++) {
   			BinaryStdOut.write(result.b[i]);
   		}
		// BinaryStdOut.write(new String(result.b, "ISO-8859-1"));

   		BinaryStdOut.flush();
    }

    private static ArrayAndIndex transformHelper(String s) {
    	CircularSuffixArray csa = new CircularSuffixArray(s);
    	byte[] result = new byte[s.length()];
    	int first = 0;

    	for (int i = 0; i < csa.length(); i++) {
    		int index = csa.index(i);

    		// set last character to be starting int
    		if (index == 0) {
    			first = i;
    		}

    		result[i] = (byte) s.charAt((index + result.length - 1) % result.length); // -1 wrap around
    	}

    	return new ArrayAndIndex(result, first);
    }

    private static class ArrayAndIndex {
    	public byte[] b;
    	public int i;

    	public ArrayAndIndex(byte[] b, int i) {
    		this.b = b; 
    		this.i = i;
    	}
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
		byte[] result = inverseTransformHelper(BinaryStdIn.readInt(), BinaryStdIn.readString());
		for (int i = 0; i < result.length; i++) {
			BinaryStdOut.write(result[i]);
		}

   		BinaryStdOut.flush();
    }

    private static byte[] inverseTransformHelper(int first, String s) {
    	int slen = s.length();
    	// character freq table
    	int[] lookup = new int[258];
    	for (int i = 0; i < slen; i++) {
    		lookup[s.charAt(i) + 1]++;
    	}

    	// collapse
    	for (int i = 0; i < 257; i++) {
    		lookup[i + 1] += lookup[i];
    	}

    	// next array
    	int[] next = new int[slen];

    	for (int i = 0; i < slen; i++) {
    		next[lookup[s.charAt(i)]++] = i;
    	}


    	// build result given input string and next array
    	byte[] result = new byte[slen];

    	int n = next[first];
    	for (int i = 0; i < slen; i++) {
    		result[i] = (byte) s.charAt(n);
    		n = next[n];
    	}

    	return result;
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
    	if (args.length == 0) {
    		test1();
    		return;
    	}

    	if (args[0].equals("-")) {
    		transform();
    	} else {
    		inverseTransform();
    	}
    }

    private static void test1() {
    	String s = "ABRACADABRA!";
   		ArrayAndIndex result = transformHelper(s);
   		int first = result.i;
   		String enresult = new String(result.b);

   		assert first == 3;
   		assert enresult.equals("ARD!RCAAAABB");

   		String deresult = new String(inverseTransformHelper(3, "ARD!RCAAAABB"));

   		assert deresult.equals(s);
    }
}