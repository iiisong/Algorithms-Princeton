import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut; 
  


public class Outcast {
  private final WordNet wnet;
  // constructor takes a WordNet object
  public Outcast(WordNet wordnet) {
    this.wnet = wordnet;
  }

  // given an array of WordNet nouns, return an outcast
  public String outcast(String[] nouns) {
    if (nouns == null) {
      throw new IllegalArgumentException("input array of strings cannot be empty");
    }
    String out = null;
    int maxCounter = 0;

    for (String strfrom : nouns) {
      int cmax = 0;

      for (String strto : nouns) {
        cmax += wnet.distance(strfrom, strto);
      }

      if (cmax > maxCounter) {
        maxCounter = cmax;
        out = strfrom;
      }
    }

    return out;
  }

  // see test client below
  public static void main(String[] args) {
    WordNet wordnet = new WordNet(args[0], args[1]);
    Outcast outcast = new Outcast(wordnet);
    for (int t = 2; t < args.length; t++) {
        In in = new In(args[t]);
        String[] nouns = in.readAllStrings();
        StdOut.println(args[t] + ": " + outcast.outcast(nouns));
    }
  }
}