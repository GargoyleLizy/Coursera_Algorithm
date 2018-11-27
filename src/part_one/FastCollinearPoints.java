package part_one;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by zhenyali on 12/11/18.
 */
public class FastCollinearPoints {
    private Point[] currentPoints;
    private LineSegment[] segments;

    public FastCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException("Points can not be null");
        for (Point point : points) {
            if (point == null) {
                throw new IllegalArgumentException("Points can not contain null value");
            }
        }
        for (int i = 0; i < points.length - 1; i++) {
            for (int j = i + 1; j < points.length; j++) {
                if (points[i].equals(points[j])) {
                    throw new IllegalArgumentException("Points can not contain repeat element");
                }
            }
        }
        currentPoints = points;
        findSegments();
    }

    public int numberOfSegments() {
        if (segments == null) {
            return 0;
        }
        return segments.length;
    }

    public LineSegment[] segments() {
        return segments;
    }

    public static void main(String[] args) {
        /* YOUR CODE HERE */
        Point x1y1 = new Point(1, 1);
        Point x1y2 = new Point(1, 2);
        Point x2y1 = new Point(2, 1);
        Point x0y0 = new Point(0, 0);
        Point x1y3 = new Point(1, 3);
        Point[] firstTryArray = new Point[5];
        firstTryArray[0] = x1y1;
        firstTryArray[1] = x1y2;
        firstTryArray[2] = x2y1;
        firstTryArray[3] = x0y0;
        firstTryArray[4] = x1y3;
        FastCollinearPoints firstTry = new FastCollinearPoints(firstTryArray);
        System.out.println("First try should have no result: " + firstTry.numberOfSegments());

        Point x1y0 = new Point(1, 0);
        Point[] secondTryArray = new Point[5];
        secondTryArray[0] = x1y2;
        secondTryArray[1] = x1y1;
        secondTryArray[2] = x1y3;
        secondTryArray[3] = x1y0;
        secondTryArray[4] = x0y0;
        FastCollinearPoints secondTry = new FastCollinearPoints(secondTryArray);
        System.out.println("Second try: " + secondTry.numberOfSegments());
        for (LineSegment line : secondTry.segments()) {
            System.out.println("second try find line: " + line);
        }

        Point[] horizontalLine = new Point[5];
        horizontalLine[0] = new Point(0, 1);
        horizontalLine[1] = new Point(-1, 1);
        horizontalLine[2] = new Point(2, 1);
        horizontalLine[3] = new Point(4, 1);
        horizontalLine[4] = new Point(2, 2);
        FastCollinearPoints thirdTry = new FastCollinearPoints(horizontalLine);
        System.out.println("Third try: " + thirdTry.numberOfSegments());
        for (LineSegment line : thirdTry.segments()) {
            System.out.println("third try find line: " + line);
        }

        Point[] verticalLine = new Point[8];
        verticalLine[0] = new Point(2329, 9995);
        verticalLine[1] = new Point(2329, 3548);
        verticalLine[2] = new Point(2329, 11424);
        verticalLine[3] = new Point(2329, 5456);
        verticalLine[4] = new Point(14919, 2993);
        verticalLine[5] = new Point(14919, 10875);
        verticalLine[6] = new Point(14919, 19559);
        verticalLine[7] = new Point(14919, 4680);
        FastCollinearPoints verticalTry = new FastCollinearPoints(verticalLine);
        System.out.println("vertical try: " + verticalTry.numberOfSegments());
        for (LineSegment line : verticalTry.segments()) {
            System.out.println("vertical try find line: " + line);
        }

        Point[] slashLine = new Point[5];
        slashLine[0] = new Point(0, 0);
        slashLine[1] = new Point(-1, -1);
        slashLine[2] = new Point(2, 2);
        slashLine[3] = new Point(4, 4);
        slashLine[4] = new Point(5, 6);
        FastCollinearPoints forthTry = new FastCollinearPoints(slashLine);
        System.out.println("Forth try: " + forthTry.numberOfSegments());
        for (LineSegment line : forthTry.segments()) {
            System.out.println("forth try find line: " + line);
        }
    }

    private void findSegments() {
        ArrayList<LineSegment> answer = new ArrayList<>();
        for (int i = 0; i < currentPoints.length - 3; i++) {
            Point originPoint = currentPoints[i];
            Point[] restPoints = Arrays.copyOfRange(currentPoints, i + 1, currentPoints.length);
            Arrays.sort(restPoints, originPoint.slopeOrder());
            ArrayList<LineSegment> foundLines = findSegmentsInSortedArray(restPoints, originPoint);
            answer.addAll(foundLines);
        }
        if (answer.size() == 0) {
            segments = null;
        } else {
            segments = new LineSegment[answer.size()];
            for (int i = 0; i < answer.size(); i++) {
                segments[i] = answer.get(i);
            }
        }
    }

    private ArrayList<LineSegment> findSegmentsInSortedArray(Point[] sortedArray, Point originPoint) {
        double currentSlope = originPoint.slopeTo(sortedArray[0]);
        int currentStartIndex = 0;
        ArrayList<LineSegment> foundLineSegments = new ArrayList<>();

        for (int i = 1; i < sortedArray.length; i++) {
            if (originPoint.slopeTo(sortedArray[i]) != currentSlope) {
                // check if previous line is long enough.
                if (i - currentStartIndex >= 3) {
                    //if it is long enough
                    Point[] foundArray = Arrays.copyOfRange(sortedArray, currentStartIndex, i);
                    foundLineSegments.add(groupPointsToLine(foundArray, originPoint));
                }
                // then start over
                currentSlope = originPoint.slopeTo(sortedArray[i]);
                currentStartIndex = i;
            }
        }
        // after doing all, we need to check again, if the last few points form a line
        if (sortedArray.length - currentStartIndex >= 3) {
            Point[] foundArray = Arrays.copyOfRange(sortedArray, currentStartIndex, sortedArray.length);
            foundLineSegments.add(groupPointsToLine(foundArray, originPoint));
        }
        return foundLineSegments;
    }

    private LineSegment groupPointsToLine(Point[] foundArray, Point originPoint) {
        Point max = originPoint, min = originPoint;
        for (int i = 0; i < foundArray.length; i++) {
            if (foundArray[i].compareTo(max) > 0) {
                max = foundArray[i];
            }
            if (foundArray[i].compareTo(min) < 0) {
                min = foundArray[i];
            }
        }
        return new LineSegment(max, min);
    }
}
