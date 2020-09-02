import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.DirectedCycle;
  
public class SAP {
  private final Digraph g;

  // constructor takes a digraph (not necessarily a DAG)
  public SAP(Digraph G) {
    if (G == null) {
      throw new IllegalArgumentException("can't operate on empty graph");
    }

    /*
    if ((new DirectedCycle(G)).hasCycle()) {
      StdOut.println(G);
      throw new IllegalArgumentException("cycle detected");
    } 
    */

    this.g = new Digraph(G);    
  }


  private int[] helper(int v, int w) {
    /*
    if (v == null || w == null) {
      throw new IllegalArgumentException("input ints cannot be null");
    }
    */

    if (v < 0 || v > g.V() || w < 0 || w > g.V()) {
      throw new IllegalArgumentException("input ints not in range of graph");
    }

    BreadthFirstDirectedPaths vg = new BreadthFirstDirectedPaths(g, v);
    BreadthFirstDirectedPaths wg = new BreadthFirstDirectedPaths(g, w);

    int minDist = Integer.MAX_VALUE;
    int minNode = -1;
    for (int i = 0; i < g.V(); i++) {
      if (!vg.hasPathTo(i) || !wg.hasPathTo(i)) {
        continue;
      }

      // StdOut.println("length(" + v + ", " + w + ") " + i + ": " + vg.distTo(i) + ", " + wg.distTo(i));
      int dist = vg.distTo(i) + wg.distTo(i);
      if (dist < minDist) {
        minDist = dist;
        minNode = i;
      }
    }

    return minDist != Integer.MAX_VALUE ? new int[]{minDist, minNode} : new int[0];
  }

  private int[] helperSubset(Iterable<Integer> v, Iterable<Integer> w) {
    if (v == null || w == null) {
      throw new IllegalArgumentException("input iterable<integer> cannot be null");
    }

    boolean vOneItem = true;
    for (Integer i : v) {
      if (i == null) {
        throw new IllegalArgumentException("input node cannot be null");
      }

      if (i < 0 || i > g.V()) {
        throw new IllegalArgumentException("input ints not in range of graph");
      }
      
      vOneItem = false;
    }

    boolean wOneItem = true;
    for (Integer i : w) {
      if (i == null) {
        throw new IllegalArgumentException("input node cannot be null");
      }

      if (i < 0 || i > g.V()) {
        throw new IllegalArgumentException("input ints not in range of graph");
      }

      wOneItem = false;
    }

    if (vOneItem || wOneItem) {
      return new int[0];
      // throw new IllegalArgumentException("input subset length can not be 0");
    }

    BreadthFirstDirectedPaths vg = new BreadthFirstDirectedPaths(g, v);
    BreadthFirstDirectedPaths wg = new BreadthFirstDirectedPaths(g, w);

    int minDist = Integer.MAX_VALUE;
    int minNode = -1;
    for (int i = 0; i < g.V(); i++) {
      if (!vg.hasPathTo(i) || !wg.hasPathTo(i)) {
        continue;
      }

      // StdOut.println("length(" + v + ", " + w + ") " + i + ": " + vg.distTo(i) + ", " + wg.distTo(i));
      int dist = vg.distTo(i) + wg.distTo(i);
      if (dist < minDist) {
        minDist = dist;
        minNode = i;
      }
    }

    return minDist != Integer.MAX_VALUE ? new int[]{minDist, minNode} : new int[0];
  }

  // length of shortest ancestral path between v and w; -1 if no such path
  public int length(int v, int w) {
    int[] node = helper(v, w);
    return node.length != 0 ? node[0] : -1;
  }

  // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
  public int ancestor(int v, int w) {
    int[] node = helper(v, w);
    return node.length != 0 ? node[1] : -1;
  }
  // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
  public int length(Iterable<Integer> v, Iterable<Integer> w) {
    int[] node = helperSubset(v, w);
    return node.length != 0 ? node[0] : -1;
  }


  // a common ancestor that participates in shortest ancestral path; -1 if no such path
  public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
    int[] node = helperSubset(v, w);
    return node.length != 0 ? node[1] : -1;
  }

  // do unit testing of this class
  public static void main(String[] args) {
    test1();
    test2();
    test3();
  }

  private static void test1() {
    /*
    Digraph g = new Digraph(12);
    g.addEdge(11, 9);
    g.addEdge(10, 9);
    g.addEdge(9, 5);
    g.addEdge(8, 5);
    g.addEdge(7, 3);
    g.addEdge(6, 3);
    g.addEdge(5, 1);
    g.addEdge(4, 1);
    g.addEdge(3, 1);
    g.addEdge(1, 0);
    g.addEdge(2, 0);

    SAP s = new SAP(g);
    assert s.length(3, 10) == 4;
    assert s.ancestor(3, 10) == 1;

    assert s.length(6, 1) == 2 : "s.length(6, 1) = " + s.length(6, 1);
    assert s.ancestor(6, 1) == 1;
    */
  }

  private static void test2() {
    /*
    Digraph g = new Digraph(6);
    g.addEdge(1, 0);
    g.addEdge(1, 2);
    g.addEdge(2, 3);
    g.addEdge(3, 4);
    g.addEdge(4, 5);
    g.addEdge(5, 0);

    SAP s = new SAP(g);
    assert s.length(1, 5) == 2;
    assert s.ancestor(1, 5) == 0;

    assert s.length(0, 0) == 0;
    assert s.ancestor(0, 0) == 0;
    */
  }

  private static void test3() {
    /*
    Digraph g = new Digraph(25);
    g.addEdge(24, 20);
    g.addEdge(23, 20);
    g.addEdge(22, 16);
    g.addEdge(21, 16);
    g.addEdge(20, 12);
    g.addEdge(19, 12);
    g.addEdge(18, 10);
    g.addEdge(17, 10);
    g.addEdge(16, 9);
    g.addEdge(15, 9);
    g.addEdge(14, 7);
    g.addEdge(13, 7);
    g.addEdge(12, 5);
    g.addEdge(11, 5);
    g.addEdge(10, 5);
    g.addEdge(9, 3);
    g.addEdge(8, 3);
    g.addEdge(7, 3);
    g.addEdge(6, 2);
    g.addEdge(5, 2);
    g.addEdge(4, 1);
    g.addEdge(3, 1);
    g.addEdge(2, 0);
    g.addEdge(1, 0);

    SAP s = new SAP(g);
    Integer[] suba = {13, 23, 24};
    Integer[] subb = {6, 16, 17};
    assert s.length(Arrays.asList(suba), Arrays.asList(subb)) == 4;
    assert s.ancestor(Arrays.asList(suba), Arrays.asList(subb)) == 3;
    */
  }
}