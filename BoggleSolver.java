import java.util.HashSet;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;

public class BoggleSolver {
  private final TST trie = new TST();

  // Initializes the data structure using the given array of strings as the dictionary.
  // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
  public BoggleSolver(String[] dictionary) {
    // put entry into trie
    for (String str : dictionary) {
      // check if word is valid
      if (str.length() < 3) {
        continue;
      }

      trie.put(str);
    }
  }


  private void collectWordsRecursion(BoggleBoard board, boolean[] marked,
                                int r, int c, String str, 
                                HashSet<String> result, TST.Walker walker) {
    
    // each bit in marked represent a cell
    int cell = r * board.cols() + c;
    if (marked[cell]) {
      return;
    }

    marked[cell] = true;

    /*
    // check if cell bit is set
    if (((marked >> cell) & 0x1) == 0x1) {
      // we have visited before, retract
      return;
    } 

    // mark cell bit true
    marked |= (1 << cell);
    */

    char cc = board.getLetter(r, c);
    String letter = cc != 'Q' ? Character.toString(cc) : "QU";
  
    String word = str + letter;

    // if no substring exist, early terminate. also serves purpose of putting letter on stack
    if (!walker.walkDown(letter)) {
      return; // early terminate
    }

    // add word to result if in dictionary
    if (walker.walkCurrent()) {
      result.add(word);
    }

    int[] rNbors = {r - 1, r - 1, r - 1, r,     r,     r + 1, r + 1, r + 1};
    int[] cNbors = {c - 1, c,     c + 1, c - 1, c + 1, c - 1, c,     c + 1};

    // iterate through all the neighbors
    for (int n = 0; n < 8; n++) {

      // make sure neighbor is valid by seeing if both r and c values are declared and have not been accessed before
      if (rNbors[n] < 0 || cNbors[n] < 0 || rNbors[n] >= board.rows() || cNbors[n] >= board.cols()) {
        continue;
      }

      // go through its children
      collectWordsRecursion(board, marked.clone(), rNbors[n], cNbors[n], word, result, walker);
    }

    walker.walkUp();
  }

  // Returns the set of all valid words in the given Boggle board, as an Iterable.
  public Iterable<String> getAllValidWords(BoggleBoard board) {
    HashSet<String> result = new HashSet<>();

    // iterate through every cell
    for (int r = 0; r < board.rows(); r++) {
      for (int c = 0; c < board.cols(); c++) {
        TST.Walker walker = trie.newWalker();

        // add every valid word to result
        collectWordsRecursion(board, new boolean[board.rows() * board.cols()], r, c, "", result, walker);
      }
    }

    return result;
  }

  // Returns the score of the given word if it is in the dictionary, zero otherwise.
  // (You can assume the word contains only the uppercase letters A through Z.)
  public int scoreOf(String word) {
    if (!trie.get(word)) {
      return 0;
    }

    int[] scores = {0, 0, 0, 1, 1, 2, 3, 5, 11};
    int n = word.length();
    return n < scores.length - 1 ? scores[n] : 11;
  }

  public static void main(String[] args) {
    if (args.length != 0) {
      In in = new In(args[0]);
      String[] dictionary = in.readAllStrings();
      BoggleSolver solver = new BoggleSolver(dictionary);
      BoggleBoard board = new BoggleBoard(args[1]);
      int score = 0;
      for (String word : solver.getAllValidWords(board)) {
        StdOut.println(word);
        score += solver.scoreOf(word);
      }
      StdOut.println("Score = " + score);

    } else {
      test1();
    }
  }

  private static void test1() {
    char[][] a = {
      {'W', 'I', 'G', 'H'},
      {'A', 'E', 'R', 'S'}, 
      {'G', 'T', 'E', 'O'},
      {'F', 'S', 'U', 'Q'}
    };

    String[] words = {"WATER", "WAG", "WIG", "GET", "REST", "AERO",
                      "TAG", "WIRE", "WIRES", "WATERS", "WAGS", "WIGS",
                      "GETS", "AEROS", "TAGS", "QUEST", "RIG", "RIGS",
                      "WET", "WETS", "SORE", "SORES", "TREE", "TREES",
                      "GATE", "GATES"};

    BoggleSolver solver = new BoggleSolver(words);
    BoggleBoard board = new BoggleBoard(a);

    int score = 0;
    for (String word : solver.getAllValidWords(board)) {
      StdOut.println(word);
      score += solver.scoreOf(word);
    }

    StdOut.println("Score = " + score);
  }
}