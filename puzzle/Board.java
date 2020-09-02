import java.util.Iterator;
import java.util.LinkedList;

public class Board {
  private final int[][] b;
  private final int dim;

  // create a board from an n-by-n array of tiles,
  // where tiles[row][col] = tile at (row, col)
  public Board(int[][] tiles) {
    if (tiles == null) {
      throw new IllegalArgumentException("board can't be null");
    }

    dim = tiles.length;
    b = deepCopy(tiles);
  }
  /*
  public Board(Board initial) {
    this(deepCopy(initial.b));
  }
  */

  // copies every value in array, including subarrays
  private int[][] deepCopy(int[][] arr) {
    if (arr == null) {
      throw new IllegalArgumentException();
    }

    int[][] result = new int[arr.length][arr.length];
    for (int i = 0; i < result.length; i++) {
      for (int j = 0; j < result.length; j++) {
        result[i][j] = arr[i][j];
      }
    }

    return result;
  }             
  // string representation of this board
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append(dim).append("\n");
    
    for (int i = 0; i < dim; i++) {
      for (int j = 0; j < dim; j++) {
        builder.append(String.format("%2d ", b[i][j]));

        if ((j + 1) % dim == 0) {
          builder.append("\n");
        }
      }
    }

    return builder.toString();
  }

  // board dimension n
  public int dimension() {
    return dim;
  }

  // number of tiles out of place
  public int hamming() {
    int hamDist = 0;

    for (int i = 0; i < dim; i++) {
      for (int j = 0; j < dim; j++) {
        if (b[i][j] == 0) {
          continue;
        }

        if (b[i][j] != i * dim + j + 1) {
          hamDist++;
        }

      }
    }

    return hamDist;
  }

  // sum of Manhattan distances between tiles and goal
  public int manhattan() {
    int mannDist = 0;

    for (int i = 0; i < dim; i++) {
      for (int j = 0; j < dim; j++) {
        int cValue = b[i][j]; // cell value

        if (cValue == 0) {
          continue;
        }

        mannDist += Math.abs(i - (cValue - 1) / dim); // vert dist difference
        mannDist += Math.abs(j - (cValue - 1) % dim); // horiz dist difference
      }
    }

    return mannDist;
  }

  // is this board the goal board?
  public boolean isGoal() {
    return hamming() == 0;
  }

  // does this board equal y?
  public boolean equals(Object y) {
    if (y == null || y.getClass() != getClass()) {
      return false;
    }

    Board other = (Board) y;

    if (dim != other.dimension()) {
      return false;
    }

    for (int i = 0; i < dim; i++) {
      for (int j = 0; j < dim; j++) {
        if (b[i][j] != other.b[i][j]) {
          return false;
        }
      }
    }

    return true;
  }

  private void swap(int[][] m, int r1, int c1, int r2, int c2) {
    int temp = m[r1][c1];
    m[r1][c1] = m[r2][c2];
    m[r2][c2] = temp;

    return;
  }

  // all neighboring boards
  public Iterable<Board> neighbors() {
    LinkedList<Board> nbs = new LinkedList<>();

loop:    
    for (int i = 0; i < dim; i++) {
      for (int j = 0; j < dim; j++) {
        int[][] temp;

        if (b[i][j] != 0) { // only operate on empty cell, skip otherwise
          continue;
        }

        if (i > 0) { // swap upper
          temp = deepCopy(b);
          swap(temp, i, j, i - 1, j);
          nbs.add(new Board(temp));
        }

        if (j < dim - 1) { // swap right
          temp = deepCopy(b);
          swap(temp, i, j, i, j + 1);
          nbs.add(new Board(temp));
        }

        if (i < dim - 1) { // swap down
          temp = deepCopy(b);
          swap(temp, i, j, i + 1, j);
          nbs.add(new Board(temp));
        }

        if (j > 0) { // swap left
          temp = deepCopy(b);
          swap(temp, i, j, i, j - 1);
          nbs.add(new Board(temp));
        }

        break loop;
      }
    }

    return nbs;
  }

  // a board that is obtained by exchanging any pair of tiles
  public Board twin() {
    if (dim < 2) {
      throw new IllegalArgumentException();
    }

    int[][] m = deepCopy(b);
    // note: format in (y, x)
    if (m[0][0] != 0) { // swap (0, 0) with (0, 1) if (0,0) is not empty
      if (m[0][1] != 0) { // swap with (0, 1) cell if not empty
        swap(m, 0, 0, 0, 1);   
      } else { // swap with (0, 0) with (1, 0) otherwise
        swap(m, 0, 0, 1, 0); 
      }
    } else { // if (0, 0) is empty, swap with (0, 1) with (0, 2)
      swap(m, 0, 1, 0, 2);
    }

    return new Board(m);
  }

  /*
  public int tileAt(int i, int j) {
        return b[i][j];
    }
  */

  // unit testing (not graded)
  public static void main(String[] args) {
    Board b = new Board(new int[][]{{0, 1, 3}, {4, 2, 5}, {7, 8, 6}});
    assert b.hamming() == 4 : "hamming expecting 4, recv " + b.hamming();

    Board goal = new Board(new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 0}});
    assert goal.manhattan() == 0 : "goal.manhattan()=" + goal.manhattan();
    assert goal.hamming() == 0;
    assert goal.isGoal();

    b = new Board(new int[][]{{1, 2, 3}, {4, 0, 6}, {7, 5, 8}});
    assert b.dimension() == 3;
    assert b.manhattan() == 2 : "b.manhattan() expecting 2, got " + b.manhattan();
    assert b.hamming() == 2 : "b.hamming() expeting 2, got " + b.hamming();
    assert !b.isGoal();

    assert b.toString().equals("3\n 1  2  3 \n 4  0  6 \n 7  5  8 \n") : 
                            "b.toString()=\"" + b.toString() + "\"";

    // testing neighbors. taking advantage that neighbors are always returned
    // in the order of upper, right, bottom, and left.
    Iterator<Board> it = b.neighbors().iterator();

    assert it.next().equals(new Board(new int[][]{{1, 0, 3}, {4, 2, 6}, {7, 5, 8}}));
    assert it.next().equals(new Board(new int[][]{{1, 2, 3}, {4, 6, 0}, {7, 5, 8}}));
    assert it.next().equals(new Board(new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 0, 8}}));
    assert it.next().equals(new Board(new int[][]{{1, 2, 3}, {0, 4, 6}, {7, 5, 8}}));
    assert !it.hasNext();

    assert b != goal;

    b = new Board(new int[][]{{0,  1,  3}, {4,  2,  5},  
                               {7,  8,  6}});
  }
}