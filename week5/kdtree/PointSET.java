/* *****************************************************************************
 *  Name: PointSET
 *  Date: 11/11/19
 *  Description: Represents a set of points in the unit square
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

public class PointSET {

    private TreeSet<Point2D> bst;

    // construct an empty set of points
    public PointSET() {
        bst = new TreeSet<>(Point2D::compareTo);
    }

    // is the set empty?
    public boolean isEmpty() {
        return bst.isEmpty();
    }

    // number of points in the set
    public int size() {
        return bst.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        bst.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        return bst.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p : bst) {
            p.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        List<Point2D> list = new LinkedList<>();
        for (Point2D p : bst) {
            if (rect.contains(p)) {
                list.add(p);
            }
        }
        return list;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (isEmpty()) return null;
        double minDist = Double.MAX_VALUE;
        Point2D nearest = null;
        for (Point2D other : bst) {
            double dist = p.distanceTo(other);
            if (dist < minDist) {
                minDist = dist;
                nearest = other;
            }
        }
        return nearest;
    }

    private static void assertTrue(boolean cond, String msg) {
        if (!cond) {
            throw new RuntimeException(msg);
        }
    }

    private static int count(Iterable<Point2D> iter) {
        int count = 0;
        for (Point2D ignored : iter) {
            count++;
        }
        return count;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        PointSET set = new PointSET();

        assertTrue(set.isEmpty(), "emptyness");
        assertTrue(set.size() == 0, "empty size");

        Point2D p = new Point2D(0, 0);
        assertTrue(!set.contains(p), "contains before an insert");
        set.insert(p);
        assertTrue(set.contains(p), "contains after an insert");

        assertTrue(!set.isEmpty(), "non empty after an insert");
        assertTrue(set.size() == 1, "size after an insert");

        Point2D p1 = new Point2D(1, 1);
        set.insert(p1);

        RectHV inside = new RectHV(0.2, 0.2, 0.8, 0.8);
        assertTrue(count(set.range(inside)) == 0, "inside rect");

        RectHV outside = new RectHV(-1.0, -1.0, 2.0, 2.0);
        assertTrue(count(set.range(outside)) == 2, "outside rect");

        Point2D p2 = new Point2D(0.3, 0.3);
        assertTrue(set.nearest(p2).equals(p), "nearest is (0, 0)");

        Point2D p3 = new Point2D(0.8, 0.8);
        assertTrue(set.nearest(p3).equals(p1), "nearest is (1, 1)");
    }
}
