import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.LinkedList;
import java.awt.Color;

public class KdTree {
  private int size = 0;
  private Node root = null;
  private enum Dimension {
    X,
    Y,
  }

  private class Node {
    public Point2D pt;
    public Node left = null;
    public Node right = null;
    public Dimension d;

    public Node(Point2D pt, Dimension d) {
      this.pt = pt;
      this.d = d;
    }
  }
  // construct an empty set of points 
  public KdTree() { 
  }

  // is the set empty?
  public boolean isEmpty() {  
    return size == 0;
  }

  // number of points in the set 
  public int size() { 
    return size;
  }

  private void insertRecursive(Node r, Point2D p) {
    boolean isRight; // tracks which side to go down
    if (r.pt.equals(p)) {
      size--;
      return;
    }
    // which side to go down 
    if (r.d == Dimension.X) {
      isRight = p.x() < r.pt.x() ? false : true;
    } else {
      isRight = p.y() < r.pt.y() ? false : true;
    }

    Dimension notrd = (r.d == Dimension.X) ? Dimension.Y : Dimension.X;

    // right path
    if (isRight) {
      // break condition: break out if end of branch
      if (r.right == null) {
        r.right = new Node(p, notrd);
        return;
      }

      insertRecursive(r.right, p);
    } else {
    // left path
      // break condition: break out if end of branch
      if (r.left == null) {
        r.left = new Node(p, notrd);
        return;
      }

      insertRecursive(r.left, p);
    }

    return;
  }

  // add the point to the set (if it is not already in the set)
  public void insert(Point2D p) { 
    if (p == null) {
      throw new IllegalArgumentException("arguement can not be null");
    }

    // empty set exception
    if (root == null) {
      root = new Node(p, Dimension.X);
      size++;
      return;
    }

    size++;

    insertRecursive(root, p);

    return;
  }

  private boolean containsRecursion(Node r, Point2D p) {
    boolean isRight; // tracks which side to go down
    if (r == null) {
      return false;
    }

    if (r.pt.equals(p)) {
      return true;
    }

    // which side to go down 
    if (r.d == Dimension.X) {
      isRight = p.x() < r.pt.x() ? false : true;
    } else {
      isRight = p.y() < r.pt.y() ? false : true;
    }

    // right path
    if (isRight) {
      return containsRecursion(r.right, p);
    } else {
      return containsRecursion(r.left, p);
    }
  }

  // does the set contain point p? 
  public boolean contains(Point2D p) { 
    if (p == null) {
      throw new IllegalArgumentException("arguement can not be null");
    }

    // no item corner case
    if (root == null) {
      return false;
    }

    return containsRecursion(root, p);
  }

  private static void drawNodePoint(Node n) {
    // save
    Color oldc = StdDraw.getPenColor();
    double oldr = StdDraw.getPenRadius();
 
    StdDraw.setPenColor(0, 0, 0);
    StdDraw.setPenRadius(0.008);
    StdDraw.point(n.pt.x(), n.pt.y());

    // restore
    StdDraw.setPenColor(oldc);
    StdDraw.setPenRadius(oldr);
  }

  private static void drawNodeDividingLine(Node n, RectHV box) {
    Color oldc = StdDraw.getPenColor();
    double oldr = StdDraw.getPenRadius();

    StdDraw.setPenRadius(0.002);

    if (n.d == Dimension.X) {
      StdDraw.setPenColor(Color.RED);
      StdDraw.line(n.pt.x(), box.ymin(), n.pt.x(), box.ymax());
    } else {
      StdDraw.setPenColor(Color.BLUE);
      StdDraw.line(box.xmin(), n.pt.y(), box.xmax(), n.pt.y());
    }

    StdDraw.setPenColor(oldc);
    StdDraw.setPenRadius(oldr);
  }

  private void drawNode(Node n, RectHV box) {
    if (n == null) {
      return;
    }

    // draw myself which is a point at center of the bounding box
    // draw point
    drawNodePoint(n);
    // draw dividing line
    drawNodeDividingLine(n, box);

    RectHV leftBox = n.d == Dimension.X ? 
                      new RectHV(box.xmin(), box.ymin(), n.pt.x(), box.ymax()) :
                      new RectHV(box.xmin(), box.ymin(), box.xmax(), n.pt.y());
    RectHV rightBox = n.d == Dimension.X ?
                      new RectHV(n.pt.x(), box.ymin(), box.xmax(), box.ymax()) :
                      new RectHV(box.xmin(), n.pt.y(), box.xmax(), box.ymax());

    // draw left child
    drawNode(n.left, leftBox);

    // draw right child
    drawNode(n.right, rightBox);
  }

  // draw all points to standard draw 
  public void draw() {
    drawNode(root, new RectHV(0.0, 0.0, 1.0, 1.0));
  }

  private LinkedList<Point2D> rangeRecursion(Node r, RectHV rect, LinkedList<Point2D> cumulative) {
    // break condition: end of branch
    if (r == null) {
      return cumulative;
    }

    // add item to cumulative
    if (rect.contains(r.pt)) {
      cumulative.add(r.pt);
    }

    // iterate through left pt
    if ((r.d == Dimension.X && rect.xmin() < r.pt.x()) ||
        (r.d == Dimension.Y && rect.ymin() < r.pt.y())) {
      cumulative = rangeRecursion(r.left, rect, cumulative);
    }
    // default right
    if ((r.d == Dimension.X && rect.xmax() >= r.pt.x()) ||
        (r.d == Dimension.Y && rect.ymax() >= r.pt.y())) {
      cumulative = rangeRecursion(r.right, rect, cumulative);
    }

    return cumulative;
  }

  // all points that are inside the rectangle (or on the boundary) 
  public Iterable<Point2D> range(RectHV rect) {
    if (rect == null) {
      throw new IllegalArgumentException("arguement can not be null");
    }

    LinkedList<Point2D> cumulative = new LinkedList<>();

    return rangeRecursion(root, rect, cumulative);
  }

  private Point2D nearestRecursion(Node r, Point2D goal, Point2D best, double bdist) {
    Node good;
    Node bad = null;

    // break condition: break if end of branch or bad side incompatible
    if (r == null) {
      return best;
    }


    // update best distance and best node
    double cdist = goal.distanceSquaredTo(r.pt);
    if (cdist < bdist) {
      best = r.pt;
      bdist = cdist;
    }

    // find good side
    if (r.d == Dimension.X) {
      good = goal.x() < r.pt.x() ? r.left : r.right;

    } else {
      good = goal.y() < r.pt.y() ? r.left : r.right;
    }

    // find if bad side also compatible
    if (r.d == Dimension.X && Math.pow(Math.abs(goal.x() - r.pt.x()), 2) < bdist) {
      bad = (good == r.left) ? r.right : r.left;
    }

    if (r.d == Dimension.Y && Math.pow(Math.abs(goal.y() - r.pt.y()), 2) < bdist) {
      bad = (good == r.left) ? r.right : r.left;
    }

    // update good sides
    best = nearestRecursion(good, goal, best, bdist);
    bdist = goal.distanceSquaredTo(best);

    // update both sides
    return nearestRecursion(bad, goal, best, bdist);
  }

  // a nearest neighbor in the set to point p; null if the set is empty 
  public Point2D nearest(Point2D p) { 
    if (p == null) {
      throw new IllegalArgumentException("arguement can not be null");
    }

    // set empty corner case
    if (root == null) {
      return null;
    }

    Point2D result = nearestRecursion(root, p, null, Double.POSITIVE_INFINITY);
    return result;
  }

  // unit testing of the methods (optional) 
  public static void main(String[] args) {
    runAssertTest();
    return;
    /*
    if (args.length == 0) {
      runAssertTest();
      return;
    }

    // initialize the two data structures with point from file
    String filename = args[0];
    In in = new In(filename);
    KdTree kdtree = new KdTree();

    // read tree points
    while (!in.isEmpty()) {
        double x = in.readDouble();
        double y = in.readDouble();
        Point2D p = new Point2D(x, y);
        kdtree.insert(p);
    }

    StdOut.println(kdtree.size());
    
    // enable double bufferring
    StdDraw.enableDoubleBuffering();
    // set canvas size
    StdDraw.setCanvasSize(512, 512);
    // a 5% margin all around
    StdDraw.setScale(-0.05, 1.05);

    while (true) {
      StdDraw.clear();
      // draw a border
      StdDraw.setPenColor(Color.GRAY);
      StdDraw.setPenRadius(0.002);
      StdDraw.rectangle(0.5, 0.5, 0.5, 0.5);

      // draw tree
      kdtree.draw();

      // show
      StdDraw.show();

      // pause
      StdDraw.pause(10);
    }
  */
  }
  private static void runAssertTest() {
    KdTree ps = new KdTree(); // create

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
    ps.insert(new Point2D(3, 3));
    assert ps.size() == 5;

    // test if item exists inside
    assert ps.contains(new Point2D(2, 4));
    assert !ps.contains(new Point2D(5, 5));
    
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
    for (Point2D pt : ps.range(new RectHV(2, 2, 4, 4))) {
      assert !(pt.x() < 2 || pt.y() < 2 || pt.x() > 4 || pt.y() > 4);
      count++;
    }

    assert count == 3;

    // test nearest

    assert ps.nearest(new Point2D(1, 2)).equals(new Point2D(1, 1));
    assert ps.nearest(new Point2D(3, 3)).equals(new Point2D(3, 3));

    // test nearest returns null in empty set
    KdTree emptySet = new KdTree();
    // assert emptySet.nearest(new Point2D(4, 4)) == null;

    // make sure size hasn't changed
    assert ps.size() == 5;
    assert !ps.isEmpty();

    // test range
    int count1 = 0;
    KdTree ps1 = new KdTree();
    ps1.insert(new Point2D(0.372, 0.497));
    ps1.insert(new Point2D(0.564, 0.413));
    ps1.insert(new Point2D(0.226, 0.577));
    ps1.insert(new Point2D(0.144, 0.179));
    ps1.insert(new Point2D(0.083, 0.51));
    ps1.insert(new Point2D(0.32, 0.708));
    ps1.insert(new Point2D(0.417, 0.362));
    ps1.insert(new Point2D(0.862, 0.825));
    ps1.insert(new Point2D(0.785, 0.725));
    ps1.insert(new Point2D(0.499, 0.208));
    for (Point2D pt : ps1.range(new RectHV(0.144, 0.036, 0.688, 0.517))) {
      assert !(pt.x() < 2 || pt.y() < 2 || pt.x() > 4 || pt.y() > 4);
    }

    assert count == 5;

    KdTree ps2 = new KdTree();
    ps2.insert(new Point2D(0.125, 0.75));
    ps2.insert(new Point2D(0.625, 0.875));
    ps2.insert(new Point2D(0.0, 0.25));
    ps2.insert(new Point2D(1, 0.125));
    ps2.insert(new Point2D(0.5, 0.5));
    assert(ps2.nearest(new Point2D(0.25, 1.0)).equals(new Point2D(0.125, 0.75)));
  }
}