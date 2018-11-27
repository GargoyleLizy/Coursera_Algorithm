package part_one;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

import java.util.Iterator;

/**
 * Created by zhenyali on 22/11/18.
 */
public class KdTree {
    private Node root = null;

    // construct an empty set of points
    public KdTree() {

    }

    // is the set empty?
    public boolean isEmpty() {
        return this.size() == 0;
    }

    // number of points in the set
    public int size() {
        if (root == null) {
            return 0;
        } else {
            return root.size;
        }
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("insert argument can not be null");
        }
        root = insert(root, p, true);
    }

    private Node insert(Node n, Point2D p, boolean isComparingX) {
        if (n == null) {
            return new Node(p, 1);
        }
        boolean isEqual = false;
        if (isComparingX) {
            if (p.x() > n.pointKey.x()) {
                n.right = insert(n.right, p, !isComparingX);
            } else if (p.x() < n.pointKey.x()) {
                n.left = insert(n.left, p, !isComparingX);
            } else {
                if (p.y() == n.pointKey.y()) {
                    isEqual = true;
                } else {
                    n.left = insert(n.left, p, !isComparingX);
                }
            }
        } else {
            if (p.y() > n.pointKey.y()) {
                n.right = insert(n.right, p, !isComparingX);
            } else if (p.y() < n.pointKey.y()) {
                n.left = insert(n.left, p, !isComparingX);
            } else {
                if (p.x() == n.pointKey.x()) {
                    isEqual = true;
                } else {
                    n.left = insert(n.left, p, !isComparingX);
                }
            }
        }
        if (!isEqual) {
            n.size++;
        }
        return n;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("contain argument can not be null");
        }
        return contains(root, p, true);
    }

    private boolean contains(Node n, Point2D p, boolean isComparingX) {
        if (n == null) {
            return false;
        } else {
            if (n.pointKey.equals(p)) {
                return true;
            }
            if (isComparingX) {
                if (p.x() > n.pointKey.x()) {
                    return contains(n.right, p, !isComparingX);
                } else {
                    return contains(n.left, p, !isComparingX);
                }
            } else {
                if (p.y() > n.pointKey.y()) {
                    return contains(n.right, p, !isComparingX);
                } else {
                    return contains(n.left, p, !isComparingX);
                }
            }
        }
    }


    // draw all points to standard draw
    public void draw() {
        draw(root, true, new RectHV(0, 0, 1, 1));
    }

    private static double POINT_RADIUS = 0.01;

    private void draw(Node n, boolean isEven, RectHV box) {
        if (n != null) {
            // draw node
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.filledCircle(n.pointKey.x(), n.pointKey.y(), POINT_RADIUS);
            // draw line
            if (isEven) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(n.pointKey.x(), box.ymin(), n.pointKey.x(), box.ymax());
            } else {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(box.xmin(), n.pointKey.y(), box.xmax(), n.pointKey.y());
            }
            // draw its children
            RectHV leftBox, rightBox;
            if (isEven) {
                leftBox = new RectHV(box.xmin(), box.ymin(), n.pointKey.x(), box.ymax());
                rightBox = new RectHV(n.pointKey.x(), box.ymin(), box.xmax(), box.ymax());
            } else {
                leftBox = new RectHV(box.xmin(), box.ymin(), box.xmax(), n.pointKey.y());
                rightBox = new RectHV(box.xmin(), n.pointKey.y(), box.xmax(), box.ymax());
            }
            draw(n.left, !isEven, leftBox);
            draw(n.right, !isEven, rightBox);
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException("range argument can not be null");
        }
        return new Iterable<Point2D>() {
            @Override
            public Iterator<Point2D> iterator() {
                SET<Point2D> insiders = new SET<>();
                rangeSearch(root, true, rect, insiders);
                return insiders.iterator();
            }
        };
    }

    private void rangeSearch(Node n, boolean isEven, RectHV rect, SET<Point2D> insiderSet) {
        if (n != null) {
            if (rect.contains(n.pointKey)) {
                insiderSet.add(n.pointKey);
                rangeSearch(n.left, !isEven, rect, insiderSet);
                rangeSearch(n.right, !isEven, rect, insiderSet);
            } else {
                if (isEven) {
                    if (rect.xmax() < n.pointKey.x()) {
                        rangeSearch(n.left, !isEven, rect, insiderSet);
                    } else if (rect.xmin() > n.pointKey.x()) {
                        rangeSearch(n.right, !isEven, rect, insiderSet);
                    } else {
                        rangeSearch(n.left, !isEven, rect, insiderSet);
                        rangeSearch(n.right, !isEven, rect, insiderSet);
                    }
                } else {
                    if (rect.ymax() < n.pointKey.y()) {
                        rangeSearch(n.left, !isEven, rect, insiderSet);
                    } else if (rect.ymin() > n.pointKey.y()) {
                        rangeSearch(n.right, !isEven, rect, insiderSet);
                    } else {
                        rangeSearch(n.left, !isEven, rect, insiderSet);
                        rangeSearch(n.right, !isEven, rect, insiderSet);
                    }
                }
            }
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("nearest argument can not be null");
        }
        if (isEmpty()) {
            return null;
        }
        Point2D champion = root.pointKey;
        return nearest(root, true, p, champion);
    }

    private Point2D nearest(Node node, boolean isEven, Point2D p, Point2D championP) {
        if (node != null) {
            if (node.pointKey.distanceTo(p) < p.distanceTo(championP)) {
                championP = node.pointKey;
            }
            if (isEven) {
                if (p.x() < node.pointKey.x()) {
                    championP = nearest(node.left, !isEven, p, championP);
                    // Then check if we need to check right sub tree
                    if (p.distanceTo(championP) >= node.pointKey.x() - p.x()) {
                        championP = nearest(node.right, !isEven, p, championP);
                    }
                } else {
                    championP = nearest(node.right, !isEven, p, championP);
                    if (p.distanceTo(championP) >= p.x() - node.pointKey.x()) {
                        championP = nearest(node.left, !isEven, p, championP);
                    }
                }
            } else {
                if (p.y() < node.pointKey.y()) {
                    championP = nearest(node.left, !isEven, p, championP);
                    if (p.distanceTo(championP) >= node.pointKey.y() - p.y()) {
                        championP = nearest(node.right, !isEven, p, championP);
                    }
                } else {
                    championP = nearest(node.right, !isEven, p, championP);
                    if (p.distanceTo(championP) >= p.y() - node.pointKey.y()) {
                        championP = nearest(node.left, !isEven, p, championP);
                    }
                }
            }
        }
        return championP;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        KdTree testSet = new KdTree();
        System.out.println("At start is empty: " + testSet.isEmpty());
        testSet.insert(new Point2D(1,1));
        System.out.println("Then not empty: " + testSet.isEmpty() + "; size 1: " + testSet.size());
        System.out.println("Contains (1,1): " + testSet.contains(new Point2D(1,1)));
        testSet.insert(new Point2D(2,2));
        System.out.println("Nearest (3,3) is (2,2): "+ testSet.nearest(new Point2D(3,3)));
        System.out.println("(1,1) inside (0,0)(0,1)(1,1)(1,0) :");
        for(Point2D p: testSet.range(new RectHV(0,0,1,1))){
            System.out.println("Contains " + p);
        }
    }

    private class Node {
        private Point2D pointKey;           // key
        private Node left, right;  // links to left and right subtrees
        private int size;          // subtree count

        public Node(Point2D pointKey, int size) {
            this.pointKey = pointKey;
            this.size = size;
        }
    }
}
