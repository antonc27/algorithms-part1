/* *****************************************************************************
 *  Name: BruteCollinearPoints
 *  Date: 27/10/19
 *  Description: Examines 4 points at a time and checks whether they all lie on the same line segment, returning all such line segments.
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {
    private LineSegment[] segments = new LineSegment[2];
    private int segmentsCount = 0;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException("Null points");

        int n = points.length;
        for (int i = 0; i < n; i++) {
            Point pI = points[i];
            if (pI == null) throw new IllegalArgumentException("Null point inside points");
            for (int j = 0; j < n; j++) {
                if (i == j) continue;
                Point pJ = points[j];
                if (pI.compareTo(pJ) == 0) throw new IllegalArgumentException("Duplicate(s) in points");
                for (int k = 0; k < n; k++) {
                    if (k == i || k == j) continue;
                    Point pK = points[k];
                    if (pI.slopeTo(pJ) != pJ.slopeTo(pK)) continue;
                    for (int l = 0; l < n; l++) {
                        if (l == i || l == j || l == k) continue;
                        Point pL = points[l];
                        if (pJ.slopeTo(pK) == pK.slopeTo(pL)) {
                            if (pI.compareTo(pJ) < 0 && pJ.compareTo(pK) < 0 && pK.compareTo(pL) < 0) {
                                LineSegment ls = new LineSegment(pI, pL);
                                if (segmentsCount == segments.length) {
                                    changeCapacity(2 * segments.length);
                                }
                                segmentsCount++;
                                segments[segmentsCount - 1] = ls;
                            }
                        }
                    }
                }
            }
        }

        changeCapacity(segmentsCount);
    }

    // the number of line segments
    public int numberOfSegments() {
        return segmentsCount;
    }

    // the line segments
    public LineSegment[] segments() {
        return segments;
    }

    private void changeCapacity(int capacity) {
        LineSegment[] newLineSegments = new LineSegment[capacity];
        for (int i = 0; i < segmentsCount; i++) {
            newLineSegments[i] = segments[i];
        }
        segments = newLineSegments;
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
