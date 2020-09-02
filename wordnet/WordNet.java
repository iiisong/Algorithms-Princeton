import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.StdOut;
import java.util.ArrayList;
import java.util.HashMap; 


public class WordNet {
  private final HashMap<String, Integer> simap = new HashMap<>(); // string to int map
  private final ArrayList<String> ismap = new ArrayList<>(); // int to string
  private final SAP sap;

  // constructor takes the name of the two input files
  public WordNet(String synsets, String hypernyms) {
    if (synsets == null || hypernyms == null) {
      throw new IllegalArgumentException("no input file");
    }

    In synstring = new In(synsets);
    In hypstring = new In(hypernyms);

    while (synstring.hasNextLine()) {
      String[] splitLine = synstring.readLine().split(",");

      // for multiple words --> multiple entries
      for (String str : splitLine[1].split(" ")) {
        simap.put(str, Integer.parseInt(splitLine[0]));
      }

      // map number to multiple words
      ismap.add(splitLine[1]);
    }

    Digraph g = new Digraph(ismap.size());
    while (hypstring.hasNextLine()) {
      String[] splitLine = hypstring.readLine().split(",");

      for (int i = 1; i < splitLine.length; i++) {
        g.addEdge(Integer.parseInt(splitLine[0]), Integer.parseInt(splitLine[i]));
      }
    }

    if ((new DirectedCycle(g)).hasCycle()) {
      StdOut.println(g);
      throw new IllegalArgumentException("cycle detected");
    } 

    sap = new SAP(g);
  }

  // returns all WordNet nouns
  public Iterable<String> nouns() {
    ArrayList<String> result = new ArrayList<>(simap.size());
    simap.forEach((k, v) -> result.add(k));
    return result;
  }

  // is the word a WordNet noun?
  public boolean isNoun(String word) {
    if (word == null) {
      throw new IllegalArgumentException("no word");
    }

    return simap.get(word) != null;
  }

  // distance between nounA and nounB (defined below)
  public int distance(String nounA, String nounB) {
    if (nounA == null || nounB == null) {
      throw new IllegalArgumentException("input string empty");
    }

    if (simap.get(nounA) == null || simap.get(nounB) == null) {
      throw new IllegalArgumentException("cannot find distance between non-existant points");
    }
 
    int v = simap.get(nounA);
    int w = simap.get(nounB);

    return sap.length(v, w);
  }

  // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
  // in a shortest ancestral path (defined below)
  public String sap(String nounA, String nounB) {
    if (nounA == null || nounB == null) {
      throw new IllegalArgumentException("input string empty");
    }

    if (simap.get(nounA) == null || simap.get(nounB) == null) {
      throw new IllegalArgumentException("cannot find distance between non-existant points");
    }

    int v = simap.get(nounA);
    int w = simap.get(nounB);

    int s = sap.ancestor(v, w);

    return ismap.get(s);
  }

  // do unit testing of this class
  public static void main(String[] args) {
    if (args == null) {
      throw new IllegalArgumentException("input array of string empty");
    }

    WordNet wn = new WordNet("synset_test.txt", "hypernym_test.txt");

    assert wn.distance("3", "10") == 4;
    assert wn.sap("3", "10").equals("1") : "{" + wn.sap("3", "10") + "}";

    assert wn.distance("6", "1") == 2 : "wn.length(6, 1) = " + wn.distance("6", "1");
    assert wn.sap("6", "1").equals("1");

    assert !wn.isNoun("13");
    assert wn.isNoun("7");

    for (String str : wn.nouns()) {
      assert wn.isNoun(str);
      // StdOut.println(str);
    }
  }
}