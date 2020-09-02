import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
  private final WeightedQuickUnionUF uf;
  private boolean[] matrix;
  private final int msize;
  private int openCount = 0;

  /** creates n-by-n grid, with all sites initially blocked.
  * @param n dimension of the matrix.
  * @throws IllegalArgumentException if n <= 0
  */
  public Percolation(int n) {
    if (n < 1) {
      throw new IllegalArgumentException("n should be positive. Received " + n + " instead.");
    }

    msize = n;
    matrix = new boolean[n * n];
    for (int i = 0; i < n * n; i++) {
      matrix[i] = false;
    }

    uf = new WeightedQuickUnionUF(n * n + 2);

    for (int i = 0; i < n; i++) {
      uf.union(n * n, i);
    }
  }

  /** opens the site (row, col) if it is not open already.
  **/
  public void open(int row, int col) {
    if (row < 1 || row > msize) {
      throw new IllegalArgumentException("index should be between " + "1 and " + msize + ". Received " + row + " instead.");
    }

    if (col < 1 || col > msize) {
      throw new IllegalArgumentException("index should be between " + "1 and " + msize + ". Received " + col + " instead.");
    }

    int site = cartToIndex(row, col);

    if (matrix[site]) {
      return;
    }

    matrix[site] = true;    
    openCount++;

    if ((site + msize + 1 > msize * msize) && (uf.find(msize * msize) != uf.find(msize * msize + 1))) {
      uf.union(site, msize * msize + 1);
    }

    if (site - msize >= 0  && matrix[site - msize]) {
      uf.union(site, site - msize);
    }

    if (site + msize < msize * msize && matrix[site + msize]) {
      uf.union(site, site + msize);
    }

    if (site % msize != 0 && matrix[site - 1]) {
      uf.union(site, site - 1);
    }

    if (site % msize != msize - 1 && matrix[site + 1]) {
      uf.union(site, site + 1);
    }
  }

  // is the site (row, col) open?
  public boolean isOpen(int row, int col) {
    if (row < 1 || row > msize) {
      throw new IllegalArgumentException("index should be between " + "1 and " + msize + ". Received " + row + " instead.");
    }

    if (col < 1 || col > msize) {
      throw new IllegalArgumentException("index should be between " + "1 and " + msize + ". Received " + col + " instead.");
    }

    int site = cartToIndex(row, col);

    return matrix[site];
  }

  // is the site (row, col) full?
  public boolean isFull(int row, int col) {
    if (row < 1 || row > msize) {
      throw new IllegalArgumentException("index should be between " + "1 and " + msize + ". Received " + row + " instead.");
    }

    if (col < 1 || col > msize) {
      throw new IllegalArgumentException("index should be between " + "1 and " + msize + ". Received " + col + " instead.");
    }

    int site = cartToIndex(row, col);

    return ((uf.find(site) == uf.find(msize * msize)) && matrix[site]);


    
  }

  // returns the number of open sites
  public int numberOfOpenSites() {
    return openCount;
  }

  // does the system percolate?
  public boolean percolates() {
    return (uf.find(msize * msize) == uf.find(msize * msize + 1));
  }

  // convert cartesian coords to index of matrices
  private int cartToIndex(int row, int col) {
    return (row - 1) * msize + col - 1;
  }

  /** test client (optional).
  **/
  public static void main(String[] args) {
    String helperMsg = String.join("\n",
          "h: this helper message",
          "p: print the n*n matrix.",
          "o r c: open cell (r, c). return nothing.",
          "uf: dump the internal uf group table",
          "?o rc : return \"true\" if cell (r, c) is open. \"false\" otherwise.",
          "?p: return \"true\" if the matrix can percolate. \"false\" otherwise.",
          "?f r c: return \"true\" if matrix can percolate to cell (r, c). \"false\" otherwise",
          "?n: number of open sites.",
          "x: exit the program.");

    if (args.length == 0) {
      StdOut.println("missing matrix dimension");
      return;
    }
 
    int n = Integer.parseInt(args[0]);
    Percolation p = new Percolation(n);

    StdOut.println(helperMsg);

    StringBuilder builder = new StringBuilder();

    cmd_loop:  
    while (true) {
      // print the prompt
      StdOut.print("> ");
      StdOut.print();

      switch (StdIn.readString()) {
        case "p":
          builder.delete(0, builder.length());

          // print header
          builder.append("   ");
          for (int i = 0; i < n; i++) {
            builder.append(String.format("%2d ", i + 1));
          }
          builder.append("\n");

          // print matrix
          for (int r = 0; r < n; r++) {
            builder.append(String.format("%2d ", r + 1));

            for (int c = 0; c < n; c++) {
              builder.append(p.matrix[r * n + c] ? " X " : "   ");
            }
            builder.append("\n");
          }

          StdOut.print(builder.toString());
          break;
        case "uf":
          builder.delete(0, builder.length());

          for (int i = 0; i < n * n; i++) {
            builder.append(String.format("%2d ", i));
          }

          builder.append("\n");

          for (int i = 0; i < n * n; i++) {
            builder.append(String.format("%2d ", p.uf.find(i)));
          }

          StdOut.println(builder.toString());
          break;
        case "o":
          p.open(StdIn.readInt(), StdIn.readInt());
          break;
        case "x":
          break cmd_loop;
        case "?o":
          StdOut.println(p.isOpen(StdIn.readInt(), StdIn.readInt()));
          break;
        case "?f":
          StdOut.println(p.isFull(StdIn.readInt(), StdIn.readInt()));
          break;
        case "?p":
          StdOut.println(p.percolates());
          break;
        case "?n":
          StdOut.println(p.numberOfOpenSites());
          break;
        case "h": 
          StdOut.println(helperMsg);
          break;
        default:
          StdOut.println(helperMsg);
          break;
      }
    }
  }
}