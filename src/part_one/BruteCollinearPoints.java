package part_one;

/**
 * Created by zhenyali on 1/10/18.
 */
public class BruteCollinearPoints {
    private Point[] currentPoints;
    private LineSegment[] segments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
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

    // the number of line segments
    public int numberOfSegments() {
        if(segments == null){
            return 0;
        }
        return segments.length;
    }

    // the line segments
    public LineSegment[] segments() {
        if(segments == null){
            return new LineSegment[0];
        }
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
        BruteCollinearPoints firstTry = new BruteCollinearPoints(firstTryArray);
        System.out.println("First try should have no result: " + firstTry.numberOfSegments());

        Point x1y0 = new Point(1,0);
        Point[] secondTryArray = new Point[5];
        secondTryArray[0] = x1y2;
        secondTryArray[1] = x1y1;
        secondTryArray[2] = x1y3;
        secondTryArray[3] = x1y0;
        secondTryArray[4] = x0y0;
        BruteCollinearPoints secondTry = new BruteCollinearPoints(secondTryArray);
        System.out.println("Second try: " + secondTry.numberOfSegments());
        for(LineSegment line :secondTry.segments()){
            System.out.println("second try find line: " + line);
        }

        Point[] horizontalLine = new Point[5];
        horizontalLine[0] = new Point(0,1);
        horizontalLine[1] = new Point(-1,1);
        horizontalLine[2] = new Point(2,1);
        horizontalLine[3] = new Point(4,1);
        horizontalLine[4] = new Point(2,2);
        BruteCollinearPoints thirdTry = new BruteCollinearPoints(horizontalLine);
        System.out.println("Third try: " + thirdTry.numberOfSegments());
        for(LineSegment line :thirdTry.segments()){
            System.out.println("third try find line: " + line);
        }

        Point[] slashLine = new Point[5];
        slashLine[0] = new Point(0,0);
        slashLine[1] = new Point(-1,-1);
        slashLine[2] = new Point(2,2);
        slashLine[3] = new Point(4,4);
        slashLine[4] = new Point(5,6);
        BruteCollinearPoints forthTry = new BruteCollinearPoints(slashLine);
        System.out.println("Forth try: " + forthTry.numberOfSegments());
        for(LineSegment line :forthTry.segments()){
            System.out.println("forth try find line: " + line);
        }
    }

    private void addNewLineSegment(LineSegment newLine) {
        if (segments == null) {
            segments = new LineSegment[1];
            segments[0] = newLine;
        } else {

            LineSegment[] temp = new LineSegment[segments.length + 1];
            System.arraycopy(segments, 0, temp, 0, segments.length);
            temp[segments.length] = newLine;
            segments = temp;
        }
    }

    private void findSegments() {
        for (int i = 0; i < currentPoints.length - 3; i++) {
            for (int j = i + 1; j < currentPoints.length - 2; j++) {
                for (int k = j + 1; k < currentPoints.length - 1; k++) {
                    for (int l = k + 1; l < currentPoints.length; l++) {
                        LineSegment line = isPointsFormLine(currentPoints[i], currentPoints[j], currentPoints[k], currentPoints[l]);
                        if (line != null) {
                            addNewLineSegment(line);
                        }
                    }
                }
            }
        }
    }

    private LineSegment isPointsFormLine(Point p, Point q, Point r, Point s) {
        Double slopePQ = p.slopeTo(q);
        Double slopeQR = q.slopeTo(r);
        if (!slopePQ.equals(slopeQR)) {
            return null;
        }
        Double slopeRS = r.slopeTo(s);
        if (!slopePQ.equals(slopeRS)) {
            return null;
        }
        //find max and min
        Point max = p, min = p;
        if (p.compareTo(q) > 0) {
            min = q;
        } else {
            max = q;
        }
        if (max.compareTo(r) < 0) {
            max = r;
        }
        if (min.compareTo(r) > 0) {
            min = r;
        }
        if (max.compareTo(s) < 0) {
            max = s;
        }
        if (min.compareTo(s) > 0) {
            min = s;
        }
        return new LineSegment(max, min);
    }
}
