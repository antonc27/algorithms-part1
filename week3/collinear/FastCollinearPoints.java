/* *****************************************************************************
 *  Name: FastCollinearPoints
 *  Date: 27/10/19
 *  Description: Finds colllinear points with n^2 * log(n) time in the worst case
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class FastCollinearPoints {
    private LineSegment[] segments = new LineSegment[2];
    private int segmentsCount = 0;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException("Null points");
        int n = points.length;

        for (int i = 0; i < n; i++) {
            if (points[i] == null) throw new IllegalArgumentException("Null point inside points");
        }

        Point[] sorted = points.clone();
        Point[] found = new Point[4];

        for (int i = 0; i < n; i++) {
            Point pI = points[i];
            Arrays.sort(sorted, 0, n, pI.slopeOrder());

            int sameSlopeCount = 0;
            double slope = Double.NEGATIVE_INFINITY;
            for (int j = 1; j < n; j++) {
                double currSlope = pI.slopeTo(sorted[j]);
                if (currSlope == slope) {
                    sameSlopeCount++;
                    if (sameSlopeCount == 2) {
                        found[0] = pI;
                        for (int k = j-2; k <= j; k++) {
                            found[k - (j - 2) + 1] = sorted[k];
                        }
                        sameSlopeCount = 0;
                        Arrays.sort(found, 0, 4);

                        LineSegment ls = new LineSegment(found[0], found[3]);
                        if (segmentsCount == segments.length) {
                            changeCapacity(2 * segments.length);
                        }
                        segmentsCount++;
                        segments[segmentsCount - 1] = ls;
                    }
                } else {
                    slope = currSlope;
                    sameSlopeCount = 0;
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
        return segments.clone();
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
