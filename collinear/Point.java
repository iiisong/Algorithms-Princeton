/******************************************************************************
 *  Compilation:  javac Point.java
 *  Execution:    java Point
 *  Dependencies: none
 *  
 *  An immutable data type for points in the plane.
 *  For use on Coursera, Algorithms Part I programming assignment.
 *
 ******************************************************************************/
import edu.princeton.cs.algs4.StdDraw;

import java.util.Arrays;
import java.util.Comparator;

// import edu.princeton.cs.algs4.StdOut;

public class Point implements Comparable<Point> {

  private final int x;     // x-coordinate of this point
  private final int y;     // y-coordinate of this point

  /**
   * Initializes a new point.
   *
   * @param  x the <em>x</em>-coordinate of the point
   * @param  y the <em>y</em>-coordinate of the point
   */
  public Point(int x, int y) {
    /* DO NOT MODIFY */
    this.x = x;
    this.y = y;
  }

  /**
   * Draws this point to standard draw.
   */
  public void draw() {
    /* DO NOT MODIFY */
    // StdDraw.setPenRadius(0.008);
    StdDraw.point(x, y);
  }

  /**
   * Draws the line segment between this point and the specified point
   * to standard draw.
   *
   * @param that the other point
   */
  public void drawTo(Point that) {
    /* DO NOT MODIFY */
    StdDraw.line(this.x, this.y, that.x, that.y);
  }

  /**
   * Returns the slope between this point and the specified point.
   * Formally, if the two points are (x0, y0) and (x1, y1), then the slope
   * is (y1 - y0) / (x1 - x0). For completeness, the slope is defined to be
   * +0.0 if the line segment connecting the two points is horizontal;
   * Double.POSITIVE_INFINITY if the line segment is vertical;
   * and Double.NEGATIVE_INFINITY if (x0, y0) and (x1, y1) are equal.
   *
   * @param  that the other point
   * @return the slope between this point and the specified point
   */
  public double slopeTo(Point that) {
    // vertical and same pt corner cases
    if (x == that.x) {
      if (y == that.y) {
        return Double.NEGATIVE_INFINITY;
      }

      return Double.POSITIVE_INFINITY;
    }

    if (y == that.y) {
      return 0.0;
    }

    return (double) (that.y - y)/(that.x - x);
  }

  /**
   * Compares two points by y-coordinate, breaking ties by x-coordinate.
   * Formally, the invoking point (x0, y0) is less than the argument point
   * (x1, y1) if and only if either y0 < y1 or if y0 = y1 and x0 < x1.
   *
   * @param  that the other point
   * @return the value <tt>0</tt> if this point is equal to the argument
   *         point (x0 = x1 and y0 = y1);
   *         a negative integer if this point is less than the argument
   *         point; and a positive integer if this point is greater than the
   *         argument point
   */
  public int compareTo(Point that) {
    if (y == that.y) {
      return x - that.x;
    }

    return y - that.y;
  }

  /**
   * Compares two points by the slope they make with this point.
   * The slope is defined as in the slopeTo() method.
   *
   * @return the Comparator that defines this ordering on points
   */
  public Comparator<Point> slopeOrder() {
    // return (Point p1, Point p2) -> Double.compare(this.slopeTo(p1), this.slopeTo(p2));
    return (Point p1, Point p2) -> Double.compare(this.slopeTo(p1), this.slopeTo(p2));
  }

  /**
   * Returns a string representation of this point.
   * This method is provide for debugging;
   * your program should not rely on the format of the string representation.
   *
   * @return a string representation of this point
   */

  public String toString() {
    /* DO NOT MODIFY */
    return "(" + x + ", " + y + ")";
  }


  /**
   * Unit tests the Point data type.
   */
  public static void main(String[] args) {
    Point[] pts = new Point[5];
    pts[0] = new Point(0, 0);
    pts[1] = new Point(1, 1);
    pts[2] = new Point(1, 2);
    pts[3] = new Point(2, 2);
    pts[4] = new Point(2, 1);

    // test slopes
    assert pts[0].slopeTo(pts[1]) == 1;
    assert pts[0].slopeTo(pts[3]) == 1;
    assert pts[1].slopeTo(pts[4]) == +0.0;
    assert pts[0].slopeTo(pts[2]) == 2;
    assert pts[0].slopeTo(pts[4]) == 1/2;
    assert pts[1].slopeTo(pts[2]) == Double.POSITIVE_INFINITY;
    assert pts[1].slopeTo(pts[1]) == Double.NEGATIVE_INFINITY;

    // test compareTo
    assert pts[0].compareTo(pts[1]) < 0;
    assert pts[1].compareTo(pts[0]) > 0;
    assert pts[1].compareTo(pts[2]) < 0;
    assert pts[2].compareTo(pts[1]) > 0;
    assert pts[1].compareTo(pts[1]) == 0;

    // test slope order
    Point[] pts2 = {new Point(3, 0), new Point(1, 2), new Point(1, 0), 
                    new Point(1, 1), new Point(2, 0), new Point(1, 3)};
    Point p0 = new Point(0, 0);
    Arrays.sort(pts2, p0.slopeOrder());
    Point[] pts2Result = {new Point(1, 0), new Point(2, 0), new Point(3, 0), 
                          new Point(1, 1), new Point(1, 2), new Point(1, 3)};

    assert Arrays.equals(pts2, pts2Result) : "pts2 not same as pts2Result. pts2:" + 
                                              Arrays.toString(pts2) + ", pts2Resul:" + Arrays.toString(pts2Result);
    return;
  }
}