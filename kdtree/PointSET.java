import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import java.util.LinkedList;
import java.util.TreeSet;
import java.awt.Color;

public class PointSET {
  private final TreeSet<Point2D> ps = new TreeSet<>();

  // construct an empty set of points 
  public PointSET() { 
  }

  // is the set empty?
  public boolean isEmpty() {  
    return ps.isEmpty();
  }

  // number of points in the set 
  public int size() { 
    return ps.size();
  }

  // add the point to the set (if it is not already in the set)
  public void insert(Point2D p) { 
    if (p == null) {
      throw new IllegalArgumentException();
    }

    ps.add(p);
  }

  // does the set contain point p? 
  public boolean contains(Point2D p) {
    if (p == null) {
      throw new IllegalArgumentException("arguement can not be null");
    }
    
    return ps.contains(p);
  }

  private static void drawNodePoint(Point2D pt) {
    // save
    Color oldc = StdDraw.getPenColor();
    double oldr = StdDraw.getPenRadius();
 
    StdDraw.setPenColor(0, 0, 0);
    StdDraw.setPenRadius(0.008);
    StdDraw.point(pt.x(), pt.y());

    // restore
    StdDraw.setPenColor(oldc);
    StdDraw.setPenRadius(oldr);
  }

  // draw all points to standard draw 
  public void draw() {
    for (Point2D pt : ps) {
      drawNodePoint(pt);
    }

    return;
  }

  // all points that are inside the rectangle (or on the boundary) 
  public Iterable<Point2D> range(RectHV rect) {
    if (rect == null) {
      throw new IllegalArgumentException("arguement can not be null");
    }

    LinkedList<Point2D> inside = new LinkedList<>();
    for (Point2D pt : ps) {
      if (rect.contains(pt)) {
        inside.add(pt);
      }
    }

    return inside;
  }

  // a nearest neighbor in the set to point p; null if the set is empty 
  public Point2D nearest(Point2D p) { 
    if (p == null) {
      throw new IllegalArgumentException("arguement can not be null");
    }

    if (ps.isEmpty()) {
      return null;
    }

    double minDist = Double.POSITIVE_INFINITY;
    Point2D best = null;

    for (Point2D pt : ps) {
      double ptDist = pt.distanceSquaredTo(p);
      if (ptDist < minDist) {
        minDist = ptDist;
        best = pt;
      } 
    }

    return best;
  }

  public static void main(String[] args) { // unit testing of the methods (optional) 
    PointSET ps = new PointSET(); // create

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
    PointSET emptySet = new PointSET();
    assert emptySet.nearest(new Point2D(4, 4)) == null;

    // make sure size hasn't changed
    assert ps.size() == 5;
    assert !ps.isEmpty();
  }
}