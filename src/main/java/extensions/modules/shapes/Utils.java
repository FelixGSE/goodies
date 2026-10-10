package extensions.modules.shapes;

import goodies.utils.Conversions;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class Utils {


    @Deprecated
    public static float round(double number, int digits) {
        double digit = Math.pow(10, digits);
        double rounded = Math.round(number * digit) / digit;
        return Conversions.doubleToFloat(rounded);

    }

    ;

    @Deprecated
    public static PVector roundPVector(PVector pVector, int digits) {

        double x = Utils.round(pVector.x, digits);
        double y = Utils.round(pVector.y, digits);

        return new PVector(Conversions.doubleToFloat(x), Conversions.doubleToFloat(y));

    }

    @Deprecated
    public static ArrayList<Line> listOfPointsToListOfLines(ArrayList<PVector> listOfPoints) {
        ArrayList<Line> listOfLines = new ArrayList<>();
        for (int i = 0; i < listOfPoints.size() - 1; i++) {
            PVector start = listOfPoints.get(i);
            PVector end = listOfPoints.get(i + 1);
            listOfLines.add(new Line(start, end));
        }
        return listOfLines;
    }

    @Deprecated
    public static double degreeToRadians(double degree) {
        return degree * Math.PI / 180;
    }

    ;

    @Deprecated
    public static double radiansToDegree(double radians) {
        return radians * 180 / Math.PI;
    }

    ;;;



    ;
    @Deprecated
    // Should be part
    public static PVector pointOnCircle(Circle circle, double angle) {

        double x = circle.center.x + circle.radius * Math.cos(angle);
        double y = circle.center.y + circle.radius * Math.sin(angle);

        return new PVector(Conversions.doubleToFloat(x), Conversions.doubleToFloat(y));
    }

    ;


    public static PVector randomPointInCircle(Circle circle) {

        double r = circle.radius * Math.sqrt(Math.random());
        double theta = Math.random() * 2 * Math.PI;

        float x = Conversions.doubleToFloat(circle.center.x + r * Math.cos(theta));
        float y = Conversions.doubleToFloat(circle.center.y + r * Math.sin(theta));

        return new PVector(x, y);

    }

    ;

    public static PVector randomPointOnCircle(Circle circle) {

        double theta = Math.random() * 2 * Math.PI;

        return pointOnCircle(circle, theta);
    }

    ;

    public static PVector randomPointOnCircleBetweenAngles(Circle circle, float start, float stop) {

        PVector degrees = Utils.makePositiveRealLine(start, stop).getRandomPointIn();

        return pointOnCircle(circle, Utils.degreeToRadians(Conversions.floatToDouble(degrees.x)));
    }

    ;

    public static PVector randomGaussianPointOnCircleBetweenAngles(Circle circle, float start, float stop, float sigma) {

        PVector degrees = Utils.makePositiveRealLine(start, stop).computeRandomGaussianPointOnLine(sigma);

        return pointOnCircle(circle, Utils.degreeToRadians(Conversions.floatToDouble(degrees.x)));
    }

    ;


    public static float angleForPointOnCircleInDegrees(Circle circle, PVector point) {
        double degrees = Math.toDegrees(Math.atan2(point.y - circle.center.y, point.x - circle.center.x));
        if (degrees < 0) {
            return 360 + Conversions.doubleToFloat(degrees);
        } else {
            return Conversions.doubleToFloat(degrees);
        }
    }

    ;

    public static float angleForPointOnCircleInRadians(Circle circle, PVector point) {
        double degrees = Utils.angleForPointOnCircleInDegrees(circle, point);

        return Conversions.doubleToFloat(Math.toRadians(degrees));
    }

    ;

    public static PVector oppositePointOnCircle(Circle circle, PVector B) {

        float x = 2 * circle.center.x - B.x;
        float y = 2 * B.y - circle.center.y;

        return new PVector(x, y);
    }

    ;


    public static boolean pointIsInCircle(Circle circle, PVector point) {

        return circle.center.dist(point) <= circle.radius;

    }

    ;

    public static boolean pointIsOutsideOfCircle(Circle circle, PVector point) {

        return circle.center.dist(point) >= circle.radius;

    }

    ;

    public static boolean pointIsOnCircle(Circle circle, PVector point) {

        return circle.center.dist(point) == circle.radius;


    }

    ;

    @Deprecated
    public static Line symmetricOrthogonalLine(Line line, PVector point, float distance) {
        Circle circle = new Circle(line.start, line.length());
        float degrees = angleForPointOnCircleInDegrees(circle, line.end);
        float newDegrees = degrees + 90;
        Circle newCircle = new Circle(point, distance);
        PVector newStart = newCircle.getPointOnCircleForAngle(newDegrees);
        PVector newEnd = newCircle.getOppositePointOnCircleForAngle(newDegrees);
        return new Line(newStart, newEnd);

    }

    ;

    public static PVector lineMidPoint(Line line) {
        float x = Conversions.doubleToFloat(0.5 * line.start.x + 0.5 * line.end.x);
        float y = Conversions.doubleToFloat(0.5 * line.start.y + 0.5 * line.end.y);

        return new PVector(x, y);

    }

    ;

    @Deprecated
    public static PVector scaleLineFromOrigin(Line line, double t) {

        float x = Conversions.doubleToFloat(line.start.x + t * (line.end.x - line.start.x));
        float y = Conversions.doubleToFloat(line.start.y + t * (line.end.y - line.start.y));

        return new PVector(x, y);

    }

    public static PVector scaleLineFromEnd(Line line, double t) {

        float x = Conversions.doubleToFloat(line.start.x + t * (line.end.x - line.start.x));
        float y = Conversions.doubleToFloat(line.start.y + t * (line.end.y - line.start.y));

        return new PVector(x, y);

    }

    ;

    public static Line shortenLineFromBothEnds(Line line, double t) {
        double split = t / 2;

        PVector startPoint = scaleLineFromOrigin(line, split);
        PVector endPoint = scaleLineFromOrigin(line, 1 - split);

        return new Line(startPoint, endPoint);

    }

    ;

    public static PVector lineIntersection(Line x, Line y) {

        float denominator = (x.start.x - x.end.x) * (y.start.y - y.end.y) - (x.start.y - x.end.y) * (y.start.x - y.end.x);
        if(denominator == 0){
            return null;
        }
        float pX = (x.start.x * x.end.y - x.start.y * x.end.x) * (y.start.x - y.end.x) - (x.start.x - x.end.x) * (y.start.x * y.end.y - y.start.y * y.end.x);
        float pY = (x.start.x * x.end.y - x.start.y * x.end.x) * (y.start.y - y.end.y) - (x.start.y - x.end.y) * (y.start.x * y.end.y - y.start.y * y.end.x);

        return new PVector(pX / denominator, pY / denominator);
    }

    ;

    public static Line makePositiveRealLine(float upper) {
        return new Line(new PVector(0, 0), new PVector(upper, 0));
    }

    ;

    public static Line makePositiveRealLine(float lower, float upper) {
        return new Line(new PVector(lower, 0), new PVector(upper, 0));
    }

    ;

    public static ArrayList<PVector> divideLineIntoEqualParts(Line line, int n) {

        double a = 1.0 / n;

        ArrayList<PVector> outputs = new ArrayList<>();

        for (int i = n; i >= 0; i--) {
            double scalingFactor = 1 - i * a;
            PVector point = scaleLineFromOrigin(line, scalingFactor);
            outputs.add(point);
        }
        ;

        return outputs;
    }

    ;


    public static ArrayList<PVector> divideLineInRandomParts(Line line, int n) {

        ArrayList<Double> randomSplits = arrayWithRandomNumbers(n - 1);
        Collections.sort(randomSplits);

        ArrayList<PVector> outputs = new ArrayList<>();
        outputs.add(line.start);
        for (int i = 0; i <= randomSplits.size() - 1; i++) {
            PVector point = Utils.scaleLineFromOrigin(line, randomSplits.get(i));
            outputs.add(point);
        }
        ;
        outputs.add(line.end);
        return outputs;
    }

    ;

    public static List<Line> pointCollectionToLineCollection(List<PVector> pointArray) {
        ArrayList<Line> outputs = new ArrayList<>();

        for (int i = 0; i <= pointArray.size() - 2; i++) {
            Line currentLine = new Line(pointArray.get(i), pointArray.get(i + 1));
            outputs.add(currentLine);
        }
        ;

        return outputs;
    }

    ;

    public static ArrayList<PVector> lineCollectionToPointCollection(List<Line> lineArray) {
        ArrayList<PVector> outputs = new ArrayList<>();
        for (Line line : lineArray) {
            PVector start = line.getStart();
            PVector end = line.getEnd();
            outputs.add(start);
            outputs.add(end);
        }

        ;

        return outputs;
    }

    ;

    public static PVector uniformRandomPointOnLine(PVector A, PVector B) {

        double randomSplit = Math.random();
        float x = Conversions.doubleToFloat((1 - randomSplit) * A.x + randomSplit * B.x);
        float y = Conversions.doubleToFloat((1 - randomSplit) * A.y + randomSplit * B.y);

        return new PVector(x, y);

    }

    ;

    public static ArrayList<Double> arrayWithRandomNumbers(int n) {
        ArrayList<Double> arrayWithRandomNumbers = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            double rand = Math.random();
            arrayWithRandomNumbers.add(rand);
        }
        ;

        return arrayWithRandomNumbers;
    }

    ;

    public static PVector randomPointOnLine(Line line) {
        return scaleLineFromOrigin(line, Math.random());

    }

    ;

    public static double randomNormal(double mean, double std) {
        Random random = new Random();
        return random.nextGaussian() * std + mean;

    }

    ;

    public static PVector randomGaussianPointOnLine(Line line, double sigma) {
        double MU = 0.0;
        int sigmaBoundFactor = 4;
//        double IM = 0.5;
        double X = Utils.randomNormal(MU, sigma);
//        double Z = (X - MU) / sigma;
        double leftClip = MU - sigmaBoundFactor * sigma;
        double rightClip = MU + sigmaBoundFactor * sigma;
        double scalingFactor = 0;
        if (X < leftClip) {
            scalingFactor = 0.0;
        } else if (X > rightClip) {
            scalingFactor = 1.0;
        } else {
            double len = Math.abs(leftClip - rightClip);
            scalingFactor = (X + len / 2) / (len);
        }

        return Utils.scaleLineFromOrigin(line, scalingFactor);
    }

    ;


    public static ArrayList<Line> divideLineIntoUniformRandomParts(Line line, int depth) {

        ArrayList<Line> lines = new ArrayList<>();
        lines.add(line);

        uniformSubdivideLine(lines, line, depth);

        return lines;

    }

    ;

    private static void uniformSubdivideLine(ArrayList<Line> lineList, Line line, int depth) {
        if (depth >= 0) {
            PVector midpoint = line.getMidPoint();

            /* Move the midpoint by a Gaussian variance */
            float nx = Conversions.doubleToFloat(midpoint.x + Math.random());
            float ny = Conversions.doubleToFloat(midpoint.y + Math.random());


            /* Add two new edges which are recursively subdivided */
            Line leftLine = new Line(new PVector(line.start.x, line.start.y), new PVector(nx, ny));
            uniformSubdivideLine(lineList, leftLine, depth - 1);
            lineList.add(leftLine);
            Line rightLine = new Line(new PVector(nx, ny), new PVector(line.end.x, line.end.y));
            uniformSubdivideLine(lineList, rightLine, depth - 1);

        }
    }

    ;


    public static PVector randomPointInTriangle(Triangle triangle) {
        double r1 = Math.random();
        double r2 = Math.random();

        double sqrtR1 = Math.sqrt(r1);

        double x = (1 - sqrtR1) * triangle.A.x + (sqrtR1 * (1 - r2)) * triangle.B.x + (sqrtR1 * r2) * triangle.C.x;
        double y = (1 - sqrtR1) * triangle.A.y + (sqrtR1 * (1 - r2)) * triangle.B.y + (sqrtR1 * r2) * triangle.C.y;

        return new PVector(Conversions.doubleToFloat(x), Conversions.doubleToFloat(y));

    }

    ;;;;


//    public static PVector perpendicularPointOnCircle(Circle circle, PVector point, boolean clockwise){
//
//        if(clockwise){
//            float counterClockwiseIndicator = 1;
//        } else {
//            float counterClockwiseIndicator = -1;
//        }
//
//        float dx = circle.center.x - point.x;
//        float dy = circle.center.y - point.y;
//
//        float d = doubleToFloat(Math.sqrt(dx*dx + dy*dy));
//
//        float newDX = dx / d;
//        float newDY = dy / d;
//
//        float finalX = point.x + counterClockwiseIndicator * (circle.radius/2)*newDX;
//        float finalY = point.y - counterClockwiseIndicator * (circle.radius/2)*newDY;
//
//        return PVector(finalX,finalY);
//    }
//
//    ;

    public static PVector pointOnEllipse(Ellipse ellipse, double angle) {

        double x = ellipse.center.x + ellipse.width * Math.cos(angle);
        double y = ellipse.center.y + ellipse.height * Math.sin(angle);

        return new PVector(Conversions.doubleToFloat(x), Conversions.doubleToFloat(y));
    }

    ;

    public static PVector scalePointFromReferencePoint(ArrayList<PVector> points, PVector referencePoint, float scalingFactor) {
        // translate point back to origin:
        return new PVector(0, 0);


    }

    ;

    public static ArrayList<PVector> scaleObjectFromReferencePoint(ArrayList<PVector> points, PVector referencePoint, float scalingFactor) {
        // translate point back to origin:
        ArrayList<PVector> finalPoints = new ArrayList<PVector>();
        return finalPoints;


    }

    ;

    public static PVector rotatePointClockWiseAroundAReferencePoint(PVector referencePoint, PVector pointToRotate, float degrees) {
        float radians = Conversions.doubleToFloat(Utils.degreeToRadians(degrees));
        float s = Conversions.doubleToFloat(Math.sin(radians));
        float c = Conversions.doubleToFloat(Math.cos(radians));

        // translate point back to origin:
        float dx = pointToRotate.x - referencePoint.x;
        float dy = pointToRotate.y - referencePoint.y;

        // rotate point
        float xnew = dx * c - dy * s + referencePoint.x;
        float ynew = dx * s + dy * c + referencePoint.y;

        return new PVector(xnew, ynew);
    }

    public static PVector rotatePointCounterClockWiseAroundAReferencePoint(PVector referencePoint, PVector pointToRotate, float degrees) {
        float radians = Conversions.doubleToFloat(Utils.degreeToRadians(degrees));
        float s = Conversions.doubleToFloat(Math.sin(radians));
        float c = Conversions.doubleToFloat(Math.cos(radians));

        // translate point back to origin:
        float dx = pointToRotate.x - referencePoint.x;
        float dy = pointToRotate.y - referencePoint.y;

        // rotate point
        float xnew = dx * c + dy * s + referencePoint.x;
        float ynew = -dx * s + dy * c + referencePoint.y;

        return new PVector(xnew, ynew);
    }

    public static PVector rotatePointOnEllipse(Ellipse ellipse, PVector point) {
        double radians = radiansToDegree(ellipse.rotation);
        float sinA = Conversions.doubleToFloat(Math.sin(radians));
        float cosA = Conversions.doubleToFloat(Math.cos(radians));

        float dX = point.x - ellipse.center.x;
        float dY = point.y - ellipse.center.y;

        float newX = ellipse.center.x + dX * cosA - dY * sinA;
        float newY = ellipse.center.y + dX * sinA - dY * cosA;

        return new PVector(newX, newY);
    }

    ;

    public static PVector randomPointInArc(Ellipse ellipse, float start, float stop, float orbit_min, float orbit_max) {

        PApplet container = new PApplet();

        double a = container.random(start / 360, stop / 360) * 2 * Math.PI;
        float randomMin = Conversions.doubleToFloat(Math.pow(orbit_min, 2));
        float randomMax = Conversions.doubleToFloat(Math.pow(orbit_max, 2));
        double R = Math.sqrt(container.random(randomMin, randomMax));

        float x = Conversions.doubleToFloat(R * Math.cos(a) * ellipse.width);
        float y = Conversions.doubleToFloat(R * Math.sin(a) * ellipse.height);
        PVector pointOnEllipse = new PVector(x, y);

        if (ellipse.rotation > 0) {
            Ellipse innerEllipse = new Ellipse(ellipse.center, ellipse.width, ellipse.height, ellipse.rotation);
            PVector v = rotatePointOnEllipse(innerEllipse, pointOnEllipse);
            return v.add(ellipse.center);
        } else {
            pointOnEllipse.add(ellipse.center);
            return pointOnEllipse;
        }
    }

    ;

    public static void planetoPolarCoordinates(Circle circle, PVector PVector) {
        return;
    }

    ;


    public static ArrayList<PVector> shiftVertexList(ArrayList<PVector> vertexList, PVector shift) {
        ArrayList<PVector> shifted = new ArrayList<>();

        for (int i = 0; i <= vertexList.size(); i++) {
            PVector shiftedVertex = vertexList.get(i).copy().add(shift);
            shifted.add(shiftedVertex);
        }

        return shifted;
    }

    ;

    public static String getRandom(String[] array) {
        int rnd = new java.util.Random().nextInt(array.length);
        return array[rnd];
    }

    ;

    public static String getRandomTriangleSide() {
        String[] sides = {"a", "b", "c"};
        return Utils.getRandom(sides);

    }

    ;

    public static PVector getCircularJitteredPoint(PVector point, float radius) {
        return new Circle(point, radius).computeRandomPointIn();

    }

    public static PVector getVericalJitteredPoint(PVector point, float lower, float upper) {
        return new PVector();

    }

    public static PVector getHorizontalJitteredPoint(PVector point, float lower, float upper) {
        return new PVector();

    }



    public ArrayList<PVector> makeGrid(PApplet sketch, int n){
        List<Line> horizontalCoords = Line.makePositiveRealLine(sketch.width).divideInEqualPartsAsLineCollection(n);
        List<Line> verticalCoords = Line.makePositiveRealLine(sketch.height).divideInEqualPartsAsLineCollection(n);

        ArrayList<PVector> locations = new ArrayList<PVector>();
        for(int i=0; i<n; i++){
            for(int j=0; j<n;j++){
                PVector location = new PVector(horizontalCoords.get(i).getMidPoint().x,verticalCoords.get(j).getMidPoint().x);
                locations.add(location);
            }

        }

        return locations;
    };

}

