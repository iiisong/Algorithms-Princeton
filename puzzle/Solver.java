import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;
import java.util.LinkedList;

public class Solver {
  private Node result; // use to store reference to final result

  private class Node {
    public Board b; // present board
    public Node parent; // previous Node 
    public int moves; // number of moves from initial
    public int distance; // manhattan distance

    public Node(Board b, Node parent) {
      this.b = b;
      this.parent = parent;
      moves = parent != null ? parent.moves + 1 : 0;
      distance = b.manhattan();
    }
  }

  // find a solution to the initial board (using the A* algorithm)
  public Solver(Board initial) {
    if (initial == null) {
      throw new IllegalArgumentException("input can not be null");
    }

    MinPQ<Node> pq = new MinPQ<>(
      (Node n1, Node n2) -> Integer.compare(n1.distance + n1.moves, n2.distance + n2.moves));

    pq.insert(new Node(initial, null));

    // create target board for twin 
    int[][] target = new int[initial.dimension()][initial.dimension()];

    for (int i = 0; i < target.length * target.length - 1; i++) {
      target[i / target.length][i % target.length] = i + 1;
    }
    target[target.length - 1][target.length - 1] = 0;

    // create twin
    Board twin = (new Board(target)).twin();

    // continue until find solution or if impossible case
    while (true) { 
      Node minBoard = pq.delMin(); // pop min item

      /*
      StdOut.println(String.format("~~~ %d ~~~", minBoard.distance));
      StdOut.println(minBoard.b.toString());
      */

      // test if result
      if (minBoard.b.isGoal()) {
        result = minBoard;
        break;
      }

      // catch impossible situations
      if (minBoard.b.equals(twin)) {
        break; // result remains null
      }

      // insert neighbors back onto pq
      for (Board i : minBoard.b.neighbors()) {
        // check if same as grandparent, prevents back and forth loop
        if (minBoard.parent != null && i.equals(minBoard.parent.b)) {
          continue;
        }

        // insert neighbor on pq
        pq.insert(new Node(i, minBoard));
      } 
    }

    // after finished, remove remaining items from priority queue
    while (pq.size() != 0) { 
      pq.delMin();
    }
  }

  // is the initial board solvable? (see below)
  public boolean isSolvable() {
    return result != null;
  }

  // min number of moves to solve initial board; -1 if unsolvable
  public int moves() {
    if (result == null) {
      return -1;
    }

    return result.moves;
  }

  // sequence of boards in a shortest solution; null if unsolvable
  public Iterable<Board> solution() {
    if (result == null) {
      return null;
    }

    Node temp = result;
    LinkedList<Board> stack = new LinkedList<>();

    while (temp != null) {
      stack.addFirst(temp.b);
      temp = temp.parent;
    }

    return stack;
  }

  // test client (see below) 
  public static void main(String[] args) {
    // create initial board from file
    In in = new In(args[0]);
    int n = in.readInt();
    int[][] tiles = new int[n][n];
    for (int i = 0; i < n; i++)
        for (int j = 0; j < n; j++)
            tiles[i][j] = in.readInt();
    Board initial = new Board(tiles);

    // solve the puzzle
    Solver solver = new Solver(initial);

    // print solution to standard output
    if (!solver.isSolvable())
        StdOut.println("No solution possible");
    else {
        StdOut.println("Minimum number of moves = " + solver.moves());
        for (Board board : solver.solution())
            StdOut.println(board);
    }
  }
}