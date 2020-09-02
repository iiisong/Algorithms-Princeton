import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.StdOut;
import java.util.LinkedList;

public class MoveToFront {

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
    	LinkedList<Byte> charSeq = new LinkedList<>();
    	int[] result;

    	for (int i = 0; i < 256; i++) {
    		charSeq.add((byte) i);
    	}

    	while (!BinaryStdIn.isEmpty()) {
    		byte b = BinaryStdIn.readByte();
    		// StdOut.println(String.format("<%x>", b));
    		int key = charSeq.indexOf(b);
    		// StdOut.println(key);
    		BinaryStdOut.write((byte) key);
    		charSeq.addFirst(charSeq.remove(key));
    	}

    	BinaryStdOut.flush();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
    	LinkedList<Byte> charSeq = new LinkedList<>();
    	int[] result;

    	for (int i = 0; i < 256; i++) {
    		charSeq.add((byte) i);
    	}


    	while (!BinaryStdIn.isEmpty()) {
    		int index = BinaryStdIn.readChar();
    		BinaryStdOut.write(charSeq.get(index));
    		charSeq.addFirst(charSeq.remove(index));
    	}

    	BinaryStdOut.flush();
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
    	if (args[0].equals("-")) {
    		encode();
    	} else {
    		decode();
    	}
    }
}