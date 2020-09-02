import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;


// import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {
  private final ArrayList<LineSegment> segments = new ArrayList<>();

  private class MyComparator implements Comparator<Point> {
    private Comparator<Point> slopeOrder;

    public MyComparator(Point p0) {
      slopeOrder = p0.slopeOrder();
    }

    public int compare(Point p1, Point p2) {
      int v = slopeOrder.compare(p1, p2);
      return v != 0 ? v : p1.compareTo(p2);
    }
  }

  public FastCollinearPoints(Point[] points) {
    if (points == null) {
      throw new IllegalArgumentException("input cannot be empty");
    }
    
    // create copy of input array
    Point[] temp = Arrays.copyOf(points, points.length);

    // sort the rest of points by bottom left to top right 
    Arrays.sort(temp);

    for (int i = 0; i < temp.length; i++) {
      if (temp[i] == null) {
        throw new IllegalArgumentException("input point cannot be null");
      }

      if (i != temp.length - 1 && temp[i].equals(temp[i + 1])) {
        throw new IllegalArgumentException("input cannot have repeated values");
      }
    }

    for (int i = 0; i < points.length; i++) {
      // create copy of points
      temp = Arrays.copyOf(points, points.length);

      // swap iterated point with first point
      Point tempPt = temp[i];
      temp[i] = temp[0];
      temp[0] = tempPt;

      // sort the points by primary slopeto, secondary location; first item always negative infinity
      
      // printPts(temp);
      Arrays.sort(temp, 1, points.length, new MyComparator(temp[0]));

      // counter to track repeating slopes, indicator of points on same line
      int counter = 0;

      // iterate from 2 as test if point = point prev
      for (int j = 2; j < temp.length; j++) {
        if (temp[0].slopeTo(temp[j]) == temp[0].slopeTo(temp[j - 1])) {
          counter++;

          // last item part of group corner case
          if (j == temp.length - 1 && counter >= 2 && temp[0].compareTo(temp[j - counter]) < 0) {
            counter = 0;
            segments.add(new LineSegment(temp[0], temp[j]));
            // StdOut.println("Line segment: " + points[0] + " + " + points[j] + "; end corner");
          }

          continue;
        }

        // only trigger when change, 3 min because adding base point and mid fence
        if (counter >= 2 && temp[0].compareTo(temp[j - counter - 1]) < 0) {
          counter = 0;
          segments.add(new LineSegment(temp[0], temp[j - 1]));
          // StdOut.println("Line segment: " + points[0] + " + " + points[j - 1]);
        }

        counter = 0;
      }
    }
  }

  /* 
  private void printPts(Point[] pts) {
    StdOut.println("\n\n=============================");
    for (int i = 0; i < pts.length; i++) {
      StdOut.printf("%s | %s\n", pts[i], pts[0].slopeTo(pts[i]));
    }
    return;
  }
  */

  // the number of line segments
  public int numberOfSegments() {
    return segments.size();
  }

  // the line segments
  public LineSegment[] segments() {
    LineSegment[] result = new LineSegment[segments.size()];
    segments.toArray(result);
    return result;
  }


  public static void main(String[] args) {
    // read the n points from a file
    In in = new In(args[0]);
    int n = in.readInt();
    Point[] points = new Point[n];
    for (int i = 0; i < n; i++) {
      int x = in.readInt();
      int y = in.readInt();
      points[i] = new Point(x, y);
    }

    // draw the points
    StdDraw.enableDoubleBuffering();
    StdDraw.setXscale(0, 32768);
    StdDraw.setYscale(0, 32768);
    for (Point p : points) {
      p.draw();
    }
    StdDraw.show();

    // print and draw the line segments
    FastCollinearPoints collinear = new FastCollinearPoints(points);
    LineSegment[] result = collinear.segments();
    // StdOut.println(String.format("result.length=%d", result.length));
    for (LineSegment segment : result) {
      // StdOut.println(segment);
      segment.draw();
    }
    StdDraw.show();
  }   
} 
