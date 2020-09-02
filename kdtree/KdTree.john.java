public class KdTree {
  private enum Dimension {
    X;
    Y;
  }

  private class Node {
    public Point2D p;
    public Dimension d;
    public Node left = null, right = null;

    public Node(Point2D p, Dimension d) {
      this.p = p;
      this.d = d;
    }
  }

  private Node root = null;
  private int size = 0;

  public KdTree() { // construct an empty set of points 
  }

  public boolean isEmpty() { // is the set empty?
    return root == null;
  }

  public int size() { // number of points in the set 
    return this.size;
  }

  private void insertRecursive(Node n, Point2D p) {
    Node side;
    Dimension newd;

    if (n.d == Dimension.X) {
      side = p.x() < n.p.x() ? n.left : n.right;
      newd = Dimension.Y;
    } else {
      side = p.y() < n.p.y() ? n.left : n.right;
      newd = Dimension.X;
    }

    if (side == null) {
      n.side = new Node(p, newd);
      return;
    }

    return insertRecursive(side, p);
  }

  public void insert(Point2D p) { // add the point to the set (if it is not already in the set)
    if (root == null) {
      root = new Node(p, Dimension.X);
      return;
    }

    return insertRecursive(root, p);
  }

  private boolean containsRecursive(Node n, Point2D p) {
    if (n == null) {
      return false;
    }

    if (n.p.equals(p)) {
      return true;
    }

    return (n.left != null && n.left.containsRecursive(p)) ||
            (n.right != null && n.right.containsRecursive(p));
  }

  public boolean contains(Point2D p){ // does the set contain point p?
    return containsRecursive(root, p);
  }

  private void rangeRecursive(Node n, RectHV rect, LinkedList<Point2D> cumulator) {
    if (n == null) {
      return;
    }

    if (rect.contains(n.p)) {
      cumulator.add(n.p);
    }

    if ((n.d == Dimension.X && rect.xmin() < n.p.x()) ||
        (n.d == Dimension.Y && rect.ymin() < n.p.y())) {
      rangeRecursive(n.left, rect, cumulator);
    }

    if ((n.d == Dimension.X && rect.xmax() > n.p.x()) ||
        (n.d == Dimension.Y && rect.ymax() > n.p.y())) {
      rangeRecursive(n.right, rect, cumulator);
    }
  }

  // all points that are inside the rectangle (or on the boundary) 
  public Iterable<Point2D> range(RectHV rect) {
    LinkedList<Point2D> cumulator = new LinkedList<>();
    rangeRecursive(root, rect, cumulator);
    return cumulator;
  }

  private void drawRecursive(Node n) {

  }

  public void draw() { // draw all points to standard draw 
    
  }

  private Node nearestRecursive(Point2D p, Node me, Node bestNode, double bestDist) {
    double d = p.distanceSquaredTo(me.p); // my distance

    if (d < bestDist) {
      bestNode = me;
      bestDist = d;
    }

    // determine which is the good side
    Node goodSide;
    if (me.d == Dimension.X) {
      goodSide = p.x() < me.p.x() ? n.left : n.right;
    } else {
      goodSide = p.y() < me.p.y() ? n.left : n.right;      
    }

    // always descend into good side
    bestNode = nearestRecursive(p, goodSide, bestNode, bestDist);
    bestDist = p.distanceSquaredTo(bestNode.p);

    Node badSide = goodSize == n.left ? n.right : n.left;
    // decide whether to descend into bad side or not
    if ((me.d == Dimension.X && Math.abs(p.x() - me.x()) < bestDist) ||
        (me.d == Dimension.Y && Math.abs(p.y() - me.y()) < bestDist)) {
      bestNode = nearestRecursive(p, badSide, bestNode, bestDist);
    }
        
    return bestNode;
  }

  public Point2D nearest(Point2D p) { // a nearest neighbor in the set to point p; null if the set is empty 
    if (p == null) {
      throw new IllegalArgumentException();
    }

    if (root == null) {
      return null;
    }

    Node result = nearestRecursive(p, root, null, Double.MAX_VALUE);
    return result.p;
  }

  public static void main(String[] args) { // unit testing of the methods (optional) 
    KdTree ps = new KdTree(); //create

    // test if empty
    assert ps.isEmpty();
    assert ps.size() == 0;

    // add some points
    ps.insert(new Point2D(3, 3));
    ps.insert(new Point2D(2, 4));
    ps.insert(new Point2D(4, 2));
    ps.insert(new Point2D(1, 1));
    ps.insert(new Point2D(5, 4));

    // test size
    assert !ps.isEmpty();
    assert ps.size() == 5;

    // insert repeate, test set functionality
    ps.insert(new Point2d(3, 3));
    assert ps.size() == 4;

    // test if item exists inside
    assert ps.contains(new Point2d(2, 4));
    assert !ps.contains(new Point2d(5, 5));
    
    /* visual representation
    5

    4       x           x

    3           x

    2               x
        
    1   x

    0   1   2   3   4   5
    */

    // test range
    int count = 0;
    for (Point2d pt : ps.range(new RectHV(2, 2, 4, 4))) {
      assert !(pt.x < 2 || pt.y < 2 || pt.x > 4 || pt.y > 4);
      count++;
    }
    assert count == 4;

    // test nearest
    assert ps.nearest(new Point2d(1, 2)) == new Point2d(1, 1);
    assert ps.nearest(new Point2d(3, 3)) == new Point2d(3, 3);

    // test nearest returns null in empty set
    KdTree empty_set = new KdTree();
    assert empty_set.nearest(new Point2d(4, 4)) == null;

    // make sure size hasn't changed
    assert ps.size() == 4;
    assert !ps.isEmpty;
  }
}