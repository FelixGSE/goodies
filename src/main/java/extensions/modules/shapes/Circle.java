package extensions.modules.shapes;

import extensions.modules.grid.Grid;
import goodies.utils.Conversions;
import lombok.Getter;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
public class Circle implements ClipObject {

    public PVector center;
    public float radius;
    public float diameter;
    public float globalRotation;

    @Override
    public String toString() {
        return "Circle{" +
                "center=" + center +
                "radius=" + radius +
                '}';
    }

    public Circle(PVector center, float radius) {
        this.center = center;
        this.radius = radius;
        this.diameter = 2 * radius;

    }

    @Deprecated
    public Circle copy() {

        return new Circle(getCenter(), radius);

    }

    public Circle translate(PVector mover) {
        PVector newCenter = center.copy().add(mover);
        return new Circle(newCenter, radius);

    }

    @Deprecated
    public PVector getCenter() {
        return center.copy();

    }

    @Deprecated
    public void shiftMe(PVector mover) {
        this.center.add(mover);
    }

    @Deprecated
    public Circle getJitter(float radius) {
        return new Circle(Utils.getCircularJitteredPoint(this.center, radius), this.radius);

    }

    @Deprecated
    public boolean pointIsInside(PVector point) {
        return getCenter().dist(point) < radius;
    }

    @Deprecated
    public static Circle getUnitCircle() {

        return new Circle(new PVector(0, 0), 1);
    }

    @Deprecated
    public void jitterMe(float radius) {
        this.center = Utils.getCircularJitteredPoint(this.center, radius);
    }


    public Circle getScaled(float scalingFactor) {
        return new Circle(center, radius * scalingFactor);
    }


    public void scaleMe(float scalingFactor) {
        this.radius = this.radius * scalingFactor;
    }


    public float getArea() {

        return PApplet.pow(this.radius, 2) * PApplet.PI;

    }


    public float getCircumference() {

        return 2 * PApplet.PI * radius;

    }


    public ArrayList<PVector> getPoles() {

        ArrayList<PVector> poles = new ArrayList<PVector>();

        for (int i = 0; i <= 360; i = i + 90) {
            poles.add(getPointOnCircleForAngle(i));
        }
        return poles;

    }

    public void getChordQuadrilateral() {

    }

    public Square getBoundingBox() {
        return Square.getSquare(getCenter(), radius);

    }

    public Grid getEqualGridPartition(int parts) {

        List<List<Float>> gridCoordinates = getBoundingBox().getEqualGridPartition(parts).getCoordinates();
        List<List<Integer>> coordinateIndices = getBoundingBox().getEqualGridPartition(parts).getCoordinateIndices();
        List<List<Float>> newGridCoordinates = new ArrayList<>();
        List<List<Integer>> newCoordinateIndices = new ArrayList<>();

        for (int i = 0; i <= gridCoordinates.size() - 1; i++) {
            if (pointIsInside(Conversions.listOfFloatsToPVector(gridCoordinates.get(i)))) {
                newGridCoordinates.add(gridCoordinates.get(i));
                newCoordinateIndices.add(coordinateIndices.get(i));
            }

        }


//        List<List<Float>> filteredCoordinates = gridCoordinates.stream().filter(x -> pointIsInside(Conversions.listOfFloatsToPVector(x))).collect(toList());

        return new Grid(newCoordinateIndices, newGridCoordinates);

    }


    public Polygon getRegularPolygon(int numberOfVertices) {
        if (numberOfVertices < 3) {
            throw new IllegalArgumentException();
        }

        ArrayList<PVector> polygonVertices = new ArrayList<PVector>();

        float splits = 360 / ((float) numberOfVertices - 1);

        for (int i = 0; i < numberOfVertices; i++) {

            PVector point = getPointOnCircleForAngle(i * splits);
            polygonVertices.add(point);

        }

        return new Polygon(polygonVertices);

    }

    public Polygon getRegularPolygon(int numberOfVertices, boolean closed) {
        if (numberOfVertices < 3) {
            throw new IllegalArgumentException();
        }

        ArrayList<PVector> polygonVertices = new ArrayList<PVector>();

        float splits = 360 / ((float) numberOfVertices - 1);

        for (int i = 0; i < numberOfVertices; i++) {

            PVector point = getPointOnCircleForAngle(i * splits);
            polygonVertices.add(point);

        }

        return new Polygon(polygonVertices, closed);

    }


    public PVector getPointOnCircleForAngle(float degrees) {
        double radians = Utils.degreeToRadians(Conversions.floatToDouble(degrees));
        return Utils.pointOnCircle(this, radians);
    }

    public PVector getOppositePointOnCircleForAngle(float degrees) {
        double radians = Utils.degreeToRadians(Conversions.floatToDouble(degrees));
        PVector pointOnCircle = Utils.pointOnCircle(this, radians);
        float newX = this.center.x - (pointOnCircle.x - this.center.x);
        float newY = this.center.y - (pointOnCircle.y - this.center.y);

        return new PVector(newX, newY);
    }


    public PVector computeRandomGaussianPointOnBetweenAngles(float start, float stop, float sigma) {

        return Utils.randomGaussianPointOnCircleBetweenAngles(this, start, stop, sigma);

    }


    public PVector computeRandomPointOnBetweenAngles(float start, float stop) {
        return Utils.randomPointOnCircleBetweenAngles(this, start, stop);
    }


    public PVector computeRandomPointIn() {
        return Utils.randomPointInCircle(this);
    }


    public PVector computeRandomPointOn() {
        return Utils.randomPointOnCircle(this);
    }


    public ArrayList<NotReallyAnArc> equalArcSubdivide(int n) {

        ArrayList<PVector> degreeLine = Utils.makePositiveRealLine(360).divideInEqualParts(n);
        ArrayList<NotReallyAnArc> arcList = new ArrayList<>();
        for (int i = 0; i <= degreeLine.size() - 2; i++) {

            PVector A = this.center.copy();

            double lowerAngleInRadians = Utils.degreeToRadians(degreeLine.get(i).x);
            PVector B = Utils.pointOnCircle(this, lowerAngleInRadians);

            double upperAngleInRadians = Utils.degreeToRadians(degreeLine.get(i + 1).x);
            PVector C = Utils.pointOnCircle(this, upperAngleInRadians);

            NotReallyAnArc currentArc = new NotReallyAnArc(A, B, C, this);
            arcList.add(currentArc);
        }

        return arcList;

    }


    public void drawEqualArcTriangleSubdivide(PApplet sketch, int n, int depth, float sigma) {

        ArrayList<NotReallyAnArc> arcList = equalArcSubdivide(n);
        for (NotReallyAnArc notReallyAnArc : arcList) {
            notReallyAnArc.draw(sketch);
            notReallyAnArc.drawTriangleSubdivisionFill(sketch, depth, sigma);

        }

    }


    public void draw(PApplet sketch) {

        sketch.circle(center.x, center.y, radius * 2);
    }


    public void drawRandomPointIn(PApplet sketch) {

        sketch.circle(center.x, center.y, radius * 2);
    }


    public float getAngleForPointOnCircle(PVector point) {
        double degrees = Math.toDegrees(Math.atan2(point.y - this.center.y, point.x - this.center.x));
        if (degrees < 0) {
            return 360 + Conversions.doubleToFloat(degrees);
        } else {
            return Conversions.doubleToFloat(degrees);
        }
    }

    public static float getRadiusForArea(float area) {
        return PApplet.sqrt(area / PApplet.PI);
    }

    public boolean intersect(Circle circle) {
        float centerXSquared = PApplet.pow(getCenter().x - circle.getCenter().x, 2);
        float centerYSquared = PApplet.pow(getCenter().y - circle.getCenter().y, 2);

        float distanceBetweenCenters = PApplet.sqrt(centerXSquared + centerYSquared);
        if (pointIsInside(circle.getCenter())) {
            return true;
        } else if (circle.pointIsInside(getCenter())) {
            return true;
        } else if (PApplet.abs(getRadius() - circle.getRadius()) <= distanceBetweenCenters && distanceBetweenCenters <= getRadius() + circle.getRadius()) {
            return true;
        } else {
            return false;
        }
//        boolean eq =
//        return eq;
//        ABS(R0 - R1) <= SQRT((x0 - x1)^2 + (y0 - y1)^2) <= (R0 + R1)
//        (R0 - R1)^2 <= (x0 - x1)^2 + (y0 - y1)^2 <= (R0 + R1)^2
//        if (pointIsInside(circle.getCenter())) {
//
//            return true;
//        } else
//            return distanceBetweenCenters <= (getRadius() + circle.getRadius()) && (distanceBetweenCenters >= PApplet.abs(getRadius() - circle.getRadius()));

    }

    public boolean intersectAtLeastOne(List<Circle> circles) {
        if (circles.isEmpty()) {
            throw new IllegalArgumentException("Got empty list");
        }

        for (Circle circle : circles) {

            if (intersect(circle)) {
                return true;
            }


        }
        return false;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Circle circle = (Circle) o;
        return Float.compare(circle.radius, radius) == 0 && center.equals(circle.center);
    }

    @Override
    public int hashCode() {
        return Objects.hash(center, radius);
    }
}
