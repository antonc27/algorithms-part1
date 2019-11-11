/* *****************************************************************************
 *  Name: KdTree
 *  Date: 11/11/19
 *  Description: Represents a set of points in the unit square, supports efficient implementation of range search and nearest-neighbor search
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.LinkedList;
import java.util.List;

public class KdTree {

    private enum NodeType {
        HORIZ, VERT
    }

    private enum Direction {
        UNKNOWN, LEFT, RIGHT
    }

    private class Node {
        Point2D point;
        NodeType type;
        RectHV rect;

        Node left = null;
        Node right = null;

        Node(Point2D point, NodeType type, RectHV rect) {
            this.point = point;
            this.type = type;
            this.rect = rect;
        }
    }

    private Node root = null;
    private int size = 0;

    // construct an empty set of points
    public KdTree() {

    }

    // is the set empty?
    public boolean isEmpty() {
        return (root == null);
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Insert: Null argument");

        if (contains(p)) return;

        if (root == null) {
            RectHV whole = new RectHV(0, 0, 1, 1);
            root = new Node(p, NodeType.VERT, whole);
        } else {
            insertRec(null, Direction.UNKNOWN, root, p);
        }

        size++;
    }

    private void insertRec(Node parent, Direction direction, Node current, Point2D p) {
        if (current == null) {
            RectHV newRect = splitRect(parent.rect, parent.point, parent.type, direction);
            NodeType newType = (parent.type == NodeType.HORIZ) ? NodeType.VERT : NodeType.HORIZ;

            Node newNode = new Node(p, newType, newRect);

            switch (direction) {
                case LEFT:
                    assert parent.left == null;
                    parent.left = newNode;
                    break;
                case RIGHT:
                    assert parent.right == null;
                    parent.right = newNode;
                    break;
                default:
                    throw new RuntimeException("UNKNOWN case on rec insert");
            }
        } else {
            Point2D currP = current.point;
            switch (current.type) {
                case VERT:
                    if (p.x() < currP.x()) {
                        insertRec(current, Direction.LEFT, current.left, p);
                    } else {
                        insertRec(current, Direction.RIGHT, current.right, p);
                    }
                    break;
                case HORIZ:
                    if (p.y() < currP.y()) {
                        insertRec(current, Direction.LEFT, current.left, p);
                    } else {
                        insertRec(current, Direction.RIGHT, current.right, p);
                    }
                    break;
                default:
                    throw new RuntimeException("Bad node type on rec insert");
            }
        }
    }

    private RectHV splitRect(RectHV toSplit, Point2D p, NodeType type, Direction direction) {
        switch (type) {
            case VERT:
                if (direction == Direction.LEFT) {
                    return new RectHV(toSplit.xmin(), toSplit.ymin(), p.x(), toSplit.ymax());
                } else if (direction == Direction.RIGHT) {
                    return new RectHV(p.x(), toSplit.ymin(), toSplit.xmax(), toSplit.ymax());
                }
                break;
            case HORIZ:
                if (direction == Direction.LEFT) {
                    return new RectHV(toSplit.xmin(), toSplit.ymin(), toSplit.xmax(), p.y());
                } else if (direction == Direction.RIGHT) {
                    return new RectHV(toSplit.xmin(), p.y(), toSplit.xmax(), toSplit.ymax());
                }
                break;
        }
        throw new RuntimeException("Unsplittable rect");
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Contains: Null argument");

        return findRec(root, p) != null;
    }

    private Point2D findRec(Node current, Point2D p) {
        if (current == null) {
            return null;
        } else {
            Point2D currP = current.point;
            if (currP.equals(p)) return currP;

            switch (current.type) {
                case VERT:
                    if (p.x() < currP.x()) {
                        return findRec(current.left, p);
                    } else {
                        return findRec(current.right, p);
                    }
                case HORIZ:
                    if (p.y() < currP.y()) {
                        return findRec(current.left, p);
                    } else {
                        return findRec(current.right, p);
                    }
                default:
                    throw new RuntimeException("Bad node type on rec find");
            }
        }
    }

    // draw all points to standard draw
    public void draw() {
        drawRec(root);
    }

    private void drawRec(Node current) {
        if (current == null) return;

        drawRec(current.left);
        drawNode(current);
        drawRec(current.right);
    }

    private void drawNode(Node current) {
        RectHV rect = current.rect;
        Point2D p = current.point;

        StdDraw.setPenRadius();
        switch (current.type) {
            case VERT:
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(p.x(), rect.ymin(), p.x(), rect.ymax());
                break;
            case HORIZ:
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(rect.xmin(), p.y(), rect.xmax(), p.y());
                break;
        }

        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(StdDraw.BLACK);
        p.draw();
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("Range: Null argument");

        List<Point2D> found = new LinkedList<>();
        rangeRec(root, rect, found);
        return found;
    }

    private void rangeRec(Node current, RectHV rect, List<Point2D> found) {
        if (current == null) return;

        if (rect.intersects(current.rect)) {
            if (rect.contains(current.point)) {
                found.add(current.point);
            }

            rangeRec(current.left, rect, found);
            rangeRec(current.right, rect, found);
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Nearest: Null argument");

        return new Point2D(0, 0);
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
        KdTree set = new KdTree();

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

        assertTrue(set.size() == 2, "size after an another insert");
        assertTrue(set.contains(p1), "contains after an another insert");

        RectHV inside = new RectHV(0.2, 0.2, 0.8, 0.8);
        assertTrue(count(set.range(inside)) == 0, "inside rect");

        RectHV outside = new RectHV(-1.0, -1.0, 2.0, 2.0);
        assertTrue(count(set.range(outside)) == 2, "outside rect");
    }
}
