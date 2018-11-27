package part_one;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.SET;

import java.util.Iterator;

/**
 * Created by zhenyali on 22/11/18.
 */
public class PointSET {
    private RedBlackBST<Point2D, Boolean> redBlackBST;

    // construct an empty set of points
    public PointSET() {
        redBlackBST = new RedBlackBST<>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return redBlackBST.isEmpty();
    }

    // number of points in the set
    public int size() {
        return redBlackBST.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if(p==null){
            throw new IllegalArgumentException("insert argument can not be null");
        }
        redBlackBST.put(p, true);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if(p==null){
            throw new IllegalArgumentException("contain argument can not be null");
        }
        return redBlackBST.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p : redBlackBST.keys()) {
            p.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if(rect==null){
            throw new IllegalArgumentException("range argument can not be null");
        }
        return new Iterable<Point2D>() {
            @Override
            public Iterator<Point2D> iterator() {
                SET<Point2D> insiders = new SET<>();
                for (Point2D p : redBlackBST.keys()) {
                    if (rect.contains(p)) {
                        insiders.add(p);
                    }
                }
                return insiders.iterator();
            }
        };
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D targetP) {
        if(targetP==null){
            throw new IllegalArgumentException("nearest argument can not be null");
        }
        Point2D champion = null;
        double championDistance = Double.POSITIVE_INFINITY;
        for (Point2D p : redBlackBST.keys()) {
            if (champion == null) {
                champion = p;
                championDistance = targetP.distanceTo(champion);
            } else {
                if (targetP.distanceTo(p) < championDistance) {
                    champion = p;
                    championDistance = targetP.distanceTo(p);
                }
            }
        }
        return champion;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        PointSET testSet = new PointSET();
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
}
