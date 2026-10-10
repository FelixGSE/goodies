package extensions.modules.shapes;

import goodies.utils.Conversions;
import extensions.modules.utils.Draw;
import goodies.geometry.Point;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static extensions.modules.shapes.Utils.arrayWithRandomNumbers;
import static goodies.algebra.ConstantsKt.GOLDEN_RATIO;

public class Line {

  public PVector start;
  public PVector end;

  public Line(PVector start, PVector end) {
    this.start = start;
    this.end = end;
  }

  public Line(Point start, Point end) {
    this.start = start.toPVector();
    this.end = end.toPVector();
  }

  @Override
  public String toString() {
    return String.format("Line(A=(%f,%f),B=(%f,%f))", start.x, start.y, end.x, end.y);
  }

  public Line getShifted(PVector mover) {
    PVector newStart = start.copy().add(mover);
    PVector newEnd = end.copy().add(mover);
    return new Line(newStart, newEnd);
  }

  public void shiftMe(PVector mover) {
    this.start.add(mover);
    this.end.add(mover);
  }

  public Line getScaled(float factor) {
    return Utils.shortenLineFromBothEnds(this, factor);
  }


  public void scaleMe(float factor) {
    Line scaled = Utils.shortenLineFromBothEnds(this, factor);
    this.start = scaled.start;
    this.end = scaled.end;
  }
  ;
  public Line reversed(){
    return new Line(this.end.copy(),this.start.copy());
  }

  public List<Line> goldenRatioSplit() {
    var split =  length() / GOLDEN_RATIO;
    var splitPoint = scaleFromStart(split).end;
    return List.of(
            new Line(start.copy(), splitPoint.copy()),
            new Line(splitPoint.copy(), end.copy())
    );
  }


  public Line scaleFromStart(float factor) {
    PVector scaled = Utils.scaleLineFromOrigin(
            this
            , factor);
    return new Line(this.start.copy(), scaled);
  }
  ;


  public Line scaleFromEnd(float factor) {
    PVector scaled = Utils.scaleLineFromOrigin(
            new Line(this.end.copy(),this.start.copy())
            , factor);
    return new Line(scaled, this.end.copy());
  }
  ;

  public PVector getStart() {
    return start.copy();
  }

  public PVector getEnd() {
    return end.copy();
  }

  public Line getRotated(float degrees) {

    PVector endRotated = new Circle(getStart(), length()).getPointOnCircleForAngle(degrees);

    return new Line(getStart(), endRotated);
  }
  ;

  public void rotateMe(float degrees) {
    this.end = new Circle(start, length()).getPointOnCircleForAngle(degrees);
  }

  public float length() {
    return start.dist(end);
  }

  public ArrayList<PVector> divideInEqualParts(int n) {
    return Utils.divideLineIntoEqualParts(this, n);
  }

  public List<Line> divideInEqualPartsAsLineCollection(int n) {
    return Utils.pointCollectionToLineCollection(Utils.divideLineIntoEqualParts(this, n));
  }

  public List<PVector> divideLineInRandomPartsAsPointCollection(int n) {

    List<Double> randomSplits = arrayWithRandomNumbers(n - 1);
    Collections.sort(randomSplits);

    ArrayList<PVector> outputs = new ArrayList<>();
    outputs.add(this.start.copy());
    for (int i = 0; i <= randomSplits.size() - 1; i++) {
      PVector point = Utils.scaleLineFromOrigin(this, randomSplits.get(i));
      outputs.add(point);
    }
    ;
    outputs.add(this.end.copy());

    return outputs;
  }

  public List<Line> divideLineInRandomPartsAsLineCollection(int n) {

    return Utils.pointCollectionToLineCollection(divideLineInRandomPartsAsPointCollection(n));
  }

  public PVector getMidPoint() {
    return Utils.lineMidPoint(this);
  }
  ;

  public ArrayList<PVector> subdivide(int depth) {

    ArrayList<PVector> pointList = new ArrayList<>();
    pointList.add(getStart());
    subdivide(pointList, this, depth - 1, 1, 100f / (depth + 1), 100f / (depth + 1));
    pointList.add(getEnd());
    return pointList;
  }

  private void subdivide(
      ArrayList<PVector> pointList,
      Line line,
      int depth,
      float locationVariance,
      float lengthVariance,
      float angleVariance) {
    if (depth >= 0) {

      PVector randomPointOnLine = line.computeRandomGaussianPointOnLine(locationVariance);

      float length = Conversions.doubleToFloat(Math.abs(Utils.randomNormal(0, lengthVariance)));
      float angle =
          getStartPointRotation()
              + 90
              + Conversions.doubleToFloat(Utils.randomNormal(0, angleVariance));
      PVector splitPoint = new Circle(randomPointOnLine, length).getPointOnCircleForAngle(angle);

      Line left = new Line(line.getStart(), splitPoint.copy());
      Line right = new Line(splitPoint.copy(), line.getEnd());
      /* Add two new edges which are recursively subdivided */
      subdivide(
          pointList, left, depth - 1, locationVariance, 100 / (2 * depth + 1), 100 / (depth + 1));
      pointList.add(splitPoint.copy());
      subdivide(
          pointList, right, depth - 1, locationVariance, 100 / (2 * depth + 1), 100 / (depth + 1));
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Line line = (Line) o;
    return Objects.equals(start, line.start) && Objects.equals(end, line.end);
  }

  @Override
  public int hashCode() {
    return Objects.hash(start, end);
  }

  public static Line makePositiveRealLine(float upper) {

    return new Line(new PVector(0, 0), new PVector(upper, 0));
  }

  public static Line makeRealLine(float lower, float upper) {
    if (lower > upper) {
      throw new IllegalArgumentException("Parameter lower must less or equal than upp");
    }
    return new Line(new PVector(lower, 0), new PVector(upper, 0));
  }

  public PVector computeRandomGaussianPointOnLine(double sigma) {
    return Utils.randomGaussianPointOnLine(this, sigma);
  }
  ;

  public PVector getRandomPointOn() {
    return Utils.randomPointOnLine(this);
  }
  ;

  public PVector getRandomPointIn() {
    return Utils.randomPointOnLine(this);
  }
  ;

  public void draw(PApplet sketch) {
    sketch.line(start.x, start.y, end.x, end.y);
  }
  ;

  public void draw(PGraphics sketch) {
    sketch.line(start.x, start.y, end.x, end.y);
  }

  public void drawWithEndpoints(PApplet sketch, float size) {
    sketch.circle(start.x, start.y, size);
    sketch.circle(end.x, end.y, size);
    sketch.line(start.x, start.y, end.x, end.y);
  }
  ;

  public void drawMidPoint(PApplet sketch) {
    privateDrawMidPoint(sketch, 5);
  }
  ;

  public void drawMidPoint(PApplet sketch, float size) {
    privateDrawMidPoint(sketch, size);
  }
  ;

  private void privateDrawMidPoint(PApplet sketch, float size) {
    Draw.drawPoint(sketch, getMidPoint(), size);
  }
  ;

  public void drawRandomGaussianPointOnLine(PApplet sketch, float sigma) {
    privateDrawRandomGaussianPointOnLine(sketch, sigma, 5);
  }
  ;

  public void drawRandomGaussianPointOnLine(PApplet sketch, float sigma, float size) {
    privateDrawRandomGaussianPointOnLine(sketch, sigma, size);
  }
  ;

  private void privateDrawRandomGaussianPointOnLine(PApplet sketch, float sigma, float size) {
    PVector point = computeRandomGaussianPointOnLine(Conversions.floatToDouble(sigma));
    sketch.circle(point.x, point.y, size);
  }
  ;

  public void drawRandomPointOnLine(PApplet sketch) {
    privateDrawRandomPointOnLine(sketch, this, 5);
  }
  ;

  public void drawRandomPointOnLine(PApplet sketch, float size) {
    privateDrawRandomPointOnLine(sketch, this, size);
  }
  ;

  public void drawRandomPointOnLine(PApplet sketch, float size, float offset) {
    Line line = Utils.shortenLineFromBothEnds(this, offset);
    privateDrawRandomPointOnLine(sketch, line, size);
  }
  ;

  private void privateDrawRandomPointOnLine(PApplet sketch, Line line, float size) {
    PVector point = Utils.randomPointOnLine(line);
    sketch.circle(point.x, point.y, size);
  }

  public Line increaseByLength(float length) {

    float scalingFactor = 1 + length / length();
    PVector newEnd = Utils.scaleLineFromOrigin(this, scalingFactor);
    return new Line(getStart(), newEnd);
  }

  public float getStartPointRotation() {
    return new Circle(getMidPoint(), length()).getAngleForPointOnCircle(getStart());
  }
  ;

  public float getEndPointRotation() {
    return new Circle(getMidPoint(), length()).getAngleForPointOnCircle(getEnd());
  }

  public PVector reflectPointAcrossLine(PVector point) {

    float dx = getEnd().x - getStart().x;
    float dy = getEnd().y - getStart().y;
    if (dx == 0) {

      return new PVector(2 * getStart().x - point.x, point.y);

    } else if (dy == 0) {

      return new PVector(point.x, 2 * getStart().y - point.y);
    }

    float m = (getEnd().y - getStart().y) / dx;
    float c = (getEnd().x * getStart().y - getStart().x * getEnd().y) / dx;

    float d = (point.x + (point.y - c) * m) / (1 + PApplet.pow(m, 2));

    float newX = 2 * d - point.x;
    float newY = 2 * d * m - point.y + 2 * c;

    return new PVector(newX, newY);
  }

  public static Line getHorizontalLine(PApplet sketch, float y) {
    return new Line(new PVector(0, y), new PVector(sketch.width, y));
  }

  public static Line getverticalLine(PApplet sketch, float x) {
    return new Line(new PVector(x, 0), new PVector(x, sketch.height));
  }

  private static boolean onSegment(PVector p, PVector q, PVector r) {
    return q.x <= Math.max(p.x, r.x)
        && q.x >= Math.min(p.x, r.x)
        && q.y <= Math.max(p.y, r.y)
        && q.y >= Math.min(p.y, r.y);
  }

  // Taken from https://www.geeksforgeeks.org/check-if-two-given-line-segments-intersect/
  // To find orientation of ordered triplet (p, q, r).
  // The function returns following values
  // 0 --> p, q and r are collinear
  // 1 --> Clockwise
  // 2 --> Counterclockwise
  private static int orientation(PVector p, PVector q, PVector r) {
    // See https://www.geeksforgeeks.org/orientation-3-ordered-points/
    // for details of below formula.
    float val = (q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y);

    if (val == 0) return 0; // collinear

    return (val > 0) ? 1 : 2; // clock or counterclock wise
  }

  // Taken from: https://www.geeksforgeeks.org/check-if-two-given-line-segments-intersect/
  // The main function that returns true if line segment 'p1q1'
  // and 'p2q2' intersect.
  public boolean intersect(Line anotherLine) {

    PVector p1 = getStart();
    PVector q1 = getEnd();
    PVector p2 = anotherLine.getStart();
    PVector q2 = anotherLine.getEnd();
    // Find the four orientations needed for general and
    // special cases
    int o1 = orientation(p1, q1, p2);
    int o2 = orientation(p1, q1, q2);
    int o3 = orientation(p2, q2, p1);
    int o4 = orientation(p2, q2, q1);

    // General case
    if (o1 != o2 && o3 != o4) return true;

    // Special Cases
    // p1, q1 and p2 are collinear and p2 lies on segment p1q1
    if (o1 == 0 && onSegment(p1, p2, q1)) return true;

    // p1, q1 and q2 are collinear and q2 lies on segment p1q1
    if (o2 == 0 && onSegment(p1, q2, q1)) return true;

    // p2, q2 and p1 are collinear and p1 lies on segment p2q2
    if (o3 == 0 && onSegment(p2, p1, q2)) return true;

    // p2, q2 and q1 are collinear and q1 lies on segment p2q2
    if (o4 == 0 && onSegment(p2, q1, q2)) return true;

    return false; // Doesn't fall in any of the above cases
  }


  public PVector computeSegmentIntersection(Line otherLine) {
    float xDiffA = end.x - start.x;
    float yDiffA = end.y - start.y;
    float xDiffB = otherLine.end.x - otherLine.start.x;
    float yDiffB = otherLine.end.y - otherLine.start.y;

    float determinant = xDiffA * yDiffB - xDiffB * yDiffA;

    // Check if the lines are parallel or coincident
    if (Math.abs(determinant) < 1e-10) {
      return null;
    }

    float constantA = start.x * end.y - end.x * start.y;
    float constantB = otherLine.start.x * otherLine.end.y - otherLine.end.x * otherLine.start.y;

    float intersectX = (constantA * xDiffB - constantB * xDiffA) / determinant;
    float intersectY = (constantA * yDiffB - constantB * yDiffA) / determinant;

    PVector intersectionPoint = new PVector(intersectX, intersectY);

    // Check if the intersection point is within the line segments
    if (isWithinLineSegment(intersectionPoint) && otherLine.isWithinLineSegment(intersectionPoint)) {
      return intersectionPoint;
    }

    return null;
  }

  private boolean isWithinLineSegment(PVector point) {
    float minX = Math.min(start.x, end.x);
    float maxX = Math.max(start.x, end.x);
    float minY = Math.min(start.y, end.y);
    float maxY = Math.max(start.y, end.y);

    return point.x >= minX && point.x <= maxX && point.y >= minY && point.y <= maxY;
  }

}
