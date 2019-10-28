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

        for (int i = 0; i < n; i++) {
            Point pI = points[i];
            Arrays.sort(sorted, 0, n, pI.slopeOrder());

            int sameSlopeCount = 0;
            double slope = Double.NEGATIVE_INFINITY;
            for (int j = 1; j < n; j++) {
                double currSlope = pI.slopeTo(sorted[j]);
                if (currSlope == slope) {
                    sameSlopeCount++;
                } else {
                    if (sameSlopeCount >= 2) {
                        addSegmentDeduplicated(pI, sorted, j - sameSlopeCount - 1, j);
                    }

                    slope = currSlope;
                    sameSlopeCount = 0;
                }
            }

            // need to check last sameSlopeCount
            if (sameSlopeCount >= 2) {
                addSegmentDeduplicated(pI, sorted, n - sameSlopeCount - 1, n);
            }
        }

        changeCapacity(segmentsCount);
    }

    private void addSegmentDeduplicated(Point origin, Point[] sorted, int start, int end) {
        Point min = null;
        Point max = null;

        for (int k = start; k < end; k++) {
            Point p = sorted[k];
            if (min == null) {
                min = p;
                max = p;
                continue;
            }
            if (p.compareTo(min) < 0) min = p;
            if (p.compareTo(max) > 0) max = p;
        }

        if (min == null) throw new RuntimeException("Incorrect same slope count");

        // we're adding segment only if origin of slope sorting is a minimal point in the natural order
        if (min.compareTo(origin) >= 0) {
            addSegment(origin, max);
        }
    }

    private void addSegment(Point p, Point q) {
        LineSegment ls = new LineSegment(p, q);
        if (segmentsCount == segments.length) {
            changeCapacity(2 * segments.length);
        }
        segmentsCount++;
        segments[segmentsCount - 1] = ls;
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
