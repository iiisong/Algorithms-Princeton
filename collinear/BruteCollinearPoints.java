import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.Arrays;

// import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {
  private final ArrayList<LineSegment> segments = new ArrayList<>();

  // finds all line segments containing 4 points
  public BruteCollinearPoints(Point[] points) {
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

    for (int p = 0; p < temp.length - 3; p++) {
      for (int q = p + 1; q < temp.length - 2; q++) {
        for (int r = q + 1; r < temp.length - 1; r++) {
          for (int s = r + 1; s < temp.length; s++) {
            if (temp[p].slopeTo(temp[q]) == temp[p].slopeTo(temp[r]) && 
                temp[p].slopeTo(temp[r]) == temp[p].slopeTo(temp[s])) {
              segments.add(new LineSegment(temp[p], temp[s]));

              // StdOut.println(p + ", " + q + ", " + r + ", " + s);
            }
          }
        }
      }
    }
  }

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

  /*
  private void printPts(Point[] pts) {
    for (int i = 0; i < pts.length; i++) {
      StdOut.printf("%s | %s\n", pts[i], pts[0].slopeTo(pts[i]));
    }
    StdOut.println("\n\n=============================");
    return;
  }
  */

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
    BruteCollinearPoints collinear = new BruteCollinearPoints(points);
    LineSegment[] result = collinear.segments();
    // StdOut.println(String.format("result.length=%d", result.length));
    for (LineSegment segment : result) {
      // StdOut.println(segment);
      segment.draw();
    }
    StdDraw.show();
  }   
}