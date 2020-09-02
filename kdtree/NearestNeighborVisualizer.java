/******************************************************************************
 *  Compilation:  javac NearestNeighborVisualizer.java
 *  Execution:    java NearestNeighborVisualizer input.txt
 *  Dependencies: PointSET.java KdTree.java
 *
 *  Read points from a file (specified as a command-line argument) and
 *  draw to standard draw. Highlight the closest point to the mouse.
 *
 *  The nearest neighbor according to the brute-force algorithm is drawn
 *  in red; the nearest neighbor using the kd-tree algorithm is drawn in blue.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdDraw;

public class NearestNeighborVisualizer {

    public static void main(String[] args) {

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

        Point2D query = null;

        // process nearest neighbor queries
        StdDraw.enableDoubleBuffering();
        while (true) {
            if (StdDraw.isMousePressed()) {
                // the location (x, y) of the mouse
                double x = StdDraw.mouseX();
                double y = StdDraw.mouseY();
                query = new Point2D(x, y);
            }

            // draw all of the points
            StdDraw.clear();

            // draw kdtree
            kdtree.draw();

            if (query != null) {
                StdDraw.setPenColor(StdDraw.ORANGE);
                StdDraw.setPenRadius(0.02);
                query.draw();

                // draw in blue the nearest neighbor (using kd-tree algorithm)
                Point2D center = kdtree.nearest(query);
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.setPenRadius(0.005);
                StdDraw.circle(center.x(), center.y(), 0.01);
            }

            StdDraw.show();
            StdDraw.pause(10);
        }
    }
}