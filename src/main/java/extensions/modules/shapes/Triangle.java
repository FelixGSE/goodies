package extensions.modules.shapes;

import goodies.utils.Conversions;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@EqualsAndHashCode
public class Triangle implements Shape,  Iterable<PVector> {

    public PVector A;
    public PVector B;
    public PVector C;
    @Getter
    @Setter
    private List<PVector> vertices;
    public float globalRotation;

    public Triangle(PVector A, PVector B, PVector C) {
        this.A = A;
        this.B = B;
        this.C = C;
        setVertices(List.of(
                A, B, C
        ));
    }

    // FIXME: Some hack
    public Triangle() {
        this.A = new PVector(0, 0);
        this.B = new PVector(0, 0);
        this.C = new PVector(0, 0);
        setVertices(List.of(
                A, B, C
        ));
    }

    public Iterator<PVector> iterator() {
        return getVertices().iterator();
    }

    public String toString() {
        return String.format("Triangle(A=(%f,%f),B=(%f,%f),C=(%f,%f))", A.x, A.y, B.x, B.y, C.x, C.y);
    }


    public Triangle copy(){
        return new Triangle(getA().copy(),getB().copy(),getC().copy());

    }

    public PVector getA() {
        return A.copy();
    }

    public PVector getB() {
        return B.copy();
    }

    public PVector getC() {
        return C.copy();
    }

    public Triangle getScaled(float factor) {

        PVector newA = Utils.scaleLineFromOrigin(lineFromInnerCircleToA(), factor);
        PVector newB = Utils.scaleLineFromOrigin(lineFromInnerCircleToB(), factor);
        PVector newC = Utils.scaleLineFromOrigin(lineFromInnerCircleToC(), factor);

        return new Triangle(newA, newB, newC);
    }


    public void scaleMe(float factor) {
        this.A = Utils.scaleLineFromOrigin(lineFromInnerCircleToA(), factor);
        this.B = Utils.scaleLineFromOrigin(lineFromInnerCircleToB(), factor);
        this.C = Utils.scaleLineFromOrigin(lineFromInnerCircleToC(), factor);
    }


    public Triangle getShifted(PVector mover) {
        PVector newA = A.copy().add(mover);
        PVector newB = B.copy().add(mover);
        PVector newC = C.copy().add(mover);
        return new Triangle(newA, newB, newC);
    }


    public void shiftMe(PVector mover) {
        this.A.add(mover);
        this.B.add(mover);
        this.C.add(mover);
    }


    public Triangle getRotated(float degrees) {

        float radians = Conversions.doubleToFloat(Utils.degreeToRadians(degrees));
        PVector newA = Utils.rotatePointClockWiseAroundAReferencePoint(innerCircleCenter(), getA(), radians);
        PVector newB = Utils.rotatePointClockWiseAroundAReferencePoint(innerCircleCenter(), getB(), radians);
        PVector newC = Utils.rotatePointClockWiseAroundAReferencePoint(innerCircleCenter(), getC(), radians);
        return new Triangle(newA, newB, newC);

    }


    public void rotateMe(float degrees) {
        float radians = Conversions.doubleToFloat(Utils.degreeToRadians(degrees));
        this.A.rotate(radians);
        this.B.rotate(radians);
        this.C.rotate(radians);
    }


    public Triangle getJittered(float radius) {

        PVector newA = Utils.getCircularJitteredPoint(this.A, radius);
        PVector newB = Utils.getCircularJitteredPoint(this.B, radius);
        PVector newC = Utils.getCircularJitteredPoint(this.C, radius);

        return new Triangle(newA, newB, newC);

    }


    public void jitterMe(float radius) {
        this.A.add(Utils.getCircularJitteredPoint(this.A, radius));
        this.B.add(Utils.getCircularJitteredPoint(this.B, radius));
        this.C.add(Utils.getCircularJitteredPoint(this.C, radius));
    }


    public double a() {
        return Conversions.floatToDouble(C.dist(this.B));

    }


    public double b() {

        return Conversions.floatToDouble(A.dist(this.C));

    }


    public double c() {

        return Conversions.floatToDouble(A.dist(this.B));

    }

    public PVector getVertex(String vertex){
        if(Objects.equals(vertex, "a")){
            return getA();
        } else if (Objects.equals(vertex, "b")){
            return getB();
        } else if (Objects.equals(vertex, "c")){
            return getC();
        } else {
            throw new IllegalArgumentException("Invalid side");
        }

    }


    public String getShortestSide() {

        float[] segmentLengths = new float[]{Conversions.doubleToFloat(a()), Conversions.doubleToFloat(b()), Conversions.doubleToFloat(c())};

        int index = 0;
        float min = segmentLengths[index];

        for (int i = 1; i < segmentLengths.length; i++) {
            if (segmentLengths[i] <= min) {
                min = segmentLengths[i];
                index = i;
            }
        }
        if (index == 0) {
            return "a";
        } else if (index == 1) {
            return "b";
        } else {
            return "c";
        }


    }


    public String getLongestSide() {

        float[] segmentLengths = new float[]{Conversions.doubleToFloat(a()), Conversions.doubleToFloat(b()), Conversions.doubleToFloat(c())};


        int index = 0;
        float max = segmentLengths[index];

        for (int i = 1; i < segmentLengths.length; i++) {
            if (segmentLengths[i] >= max) {
                max = segmentLengths[i];
                index = i;
            }
        }
        if (index == 0) {
            return "a";
        } else if (index == 1) {
            return "b";
        } else {
            return "c";
        }
    }


    public float hA() {
        return Conversions.doubleToFloat(c() * Math.sin(beta()));
    }


    public float hB() {
        return Conversions.doubleToFloat(a() * Math.sin(gamma()));
    }


    public float hC() {
        return Conversions.doubleToFloat(b() * Math.sin(alpha()));
    }


    public double U() {
        return a() + b() + c();
    }


    public double area() {
        return (a() * hA()) / 2;
    }


    public double alpha() {
        return Math.acos((Math.pow(b(), 2) + Math.pow(c(), 2) - Math.pow(a(), 2)) / (2 * b() * c()));
    }


    public double beta() {
        return Math.acos((Math.pow(a(), 2) + Math.pow(c(), 2) - Math.pow(b(), 2)) / (2 * a() * c()));
    }


    public double gamma() {
        return Math.acos((Math.pow(a(), 2) + Math.pow(b(), 2) - Math.pow(c(), 2)) / (2 * a() * b()));

    }


    public double sumOfAngles() {

        return alpha() + beta() + gamma();

    }


    public Line segmentA() {
        return new Line(B, C);

    }


    public Line segmentB() {
        return new Line(A, C);

    }


    public Line segmentC() {
        return new Line(A, B);

    }


    public PVector innerCircleCenter() {
        double x = (a() * A.x + b() * B.x + c() * C.x) / (a() + b() + c());
        double y = (a() * A.y + b() * B.y + c() * C.y) / (a() + b() + c());
        return new PVector(Conversions.doubleToFloat(x), Conversions.doubleToFloat(y));

    }


    public double innerCircleRadius() {
        return 2 * area() / U();
    }


    public Circle getInnerCircle() {
        return new Circle(innerCircleCenter(), Conversions.doubleToFloat(innerCircleRadius()));

    }


    public Line lineFromInnerCircleToA() {
        return new Line(A, innerCircleCenter());

    }


    public Line lineFromInnerCircleToB() {
        return new Line(B, innerCircleCenter());

    }


    public Line lineFromInnerCircleToC() {
        return new Line(C, innerCircleCenter());

    }


    public float distanceFromInnerCircleCenterToA() {
        return lineFromInnerCircleToA().length();
    }

    public float distanceFromInnerCircleCenterToB() {
        return lineFromInnerCircleToB().length();
    }

    public float distanceFromInnerCircleCenterToC() {
        return lineFromInnerCircleToC().length();
    }

    public PVector pointOnInnerCirclePointToA() {
        Circle inner = getInnerCircle();
        double ratio = inner.radius / distanceFromInnerCircleCenterToA();
        Line line = new Line(inner.center, A);
        return Utils.scaleLineFromOrigin(line, ratio);
    }

    public PVector pointOnInnerCirclePointToB() {
        Circle inner = getInnerCircle();
        double ratio = inner.radius / distanceFromInnerCircleCenterToB();
        Line line = new Line(inner.center, B);
        return Utils.scaleLineFromOrigin(line, ratio);
    }

    public PVector pointOnInnerCirclePointToC() {
        Circle inner = getInnerCircle();
        double ratio = inner.radius / distanceFromInnerCircleCenterToC();
        Line line = new Line(inner.center, C);
        return Utils.scaleLineFromOrigin(line, ratio);
    }

    public PVector computeRandomGaussianPointOnSide(String side, float sigma) {

        return switch (side) {
            case "a" -> segmentA().computeRandomGaussianPointOnLine(sigma);
            case "b" -> segmentB().computeRandomGaussianPointOnLine(sigma);
            case "c" -> segmentC().computeRandomGaussianPointOnLine(sigma);
            default -> throw new IllegalArgumentException();
        };

    }

    public PVector computeMidPointOnSide(String side) {

        return switch (side) {
            case "a" -> segmentA().getMidPoint();
            case "b" -> segmentB().getMidPoint();
            case "c" -> segmentC().getMidPoint();
            default -> throw new IllegalArgumentException();
        };


    }

    public PVector computeRandomPointOn() {

        String[] sides = {"a", "b", "c"};

        String side = Utils.getRandom(sides);

        return switch (side) {
            case "a" -> segmentA().getRandomPointOn();
            case "b" -> segmentB().getRandomPointOn();
            case "c" -> segmentC().getRandomPointOn();
            default -> throw new IllegalArgumentException();
        };


    }


    public PVector computeRandomPointOnSide(String side) {

        return switch (side) {
            case "a" -> segmentA().getRandomPointOn();
            case "b" -> segmentB().getRandomPointOn();
            case "c" -> segmentC().getRandomPointOn();
            default -> throw new IllegalArgumentException();
        };


    }

    public Line getSegment(String side) {

        return switch (side) {
            case "a" -> segmentA();
            case "b" -> segmentB();
            case "c" -> segmentC();
            default -> throw new IllegalArgumentException();
        };


    }


    public PVector computeRandomPointIn() {

        return Utils.randomPointInTriangle(this);

    }


    public void draw(PApplet myParent) {

        myParent.triangle(this.A.x, this.A.y, this.B.x, this.B.y, this.C.x, this.C.y);

    }


    public void drawRandomPointInside(PApplet sketch) {
        privateDrawRandomPointInside(sketch, 10, 1);
    }


    public void drawRandomPointInside(PApplet sketch, float size) {
        privateDrawRandomPointInside(sketch, size, 1);
    }


    public void drawRandomPointInside(PApplet sketch, float size, float offSet) {
        privateDrawRandomPointInside(sketch, size, offSet);
    }


    private void privateDrawRandomPointInside(PApplet sketch, float size, float offSet) {
        Triangle triangle = getScaled(offSet);
        PVector point = triangle.computeRandomPointIn();
        sketch.circle(point.x, point.y, size);
    }


    public void drawRandomCurveFill(PApplet sketch) {
        privateDrawRandomCurveFill(sketch, 3, 1);
    }


    public void drawRandomCurveFill(PApplet sketch, int n) {
        privateDrawRandomCurveFill(sketch, n, 1);
    }

    public void drawRandomCurveFill(PApplet sketch, int n, float offset) {
        privateDrawRandomCurveFill(sketch, 4, offset);
    }


    public void privateDrawRandomCurveFill(PApplet sketch, int n, float offSet) {
        if (n < 3) {
            throw new IllegalArgumentException();
        }
        Triangle newTriangle = getScaled(offSet);
        PVector pointA = newTriangle.computeRandomPointIn();
        PVector pointB = newTriangle.computeRandomPointIn();
        PVector pointC = newTriangle.computeRandomPointIn();
        PVector pointD = newTriangle.computeRandomPointIn();
        sketch.curve(pointA.x, pointA.y, pointB.x, pointB.y, pointC.x, pointC.y, pointD.x, pointD.y);


    }

    // FIXME: CurveVertex is not really doing anything :(
//    public void privateDrawRandomCurveFill(PApplet sketch, int n, float offSet) {
//        if(n < 3){
//            throw new IllegalArgumentException();
//        }
//        Triangle newTriangle = makeScale(offSet);
//        sketch.beginShape();
//        for(int i=0; i<=n;i++){
//
//            PVector point = newTriangle.computeRandomPointInTriangle();
//            sketch.curveVertex(point.x,point.y);
//
//        }
//        sketch.endShape();
//
//    };

    public void drawRandomLineFill(PApplet sketch) {
        privateDrawRandomLineFill(sketch, 1);
    }


    public void drawRandomLineFill(PApplet sketch, float offset) {
        privateDrawRandomLineFill(sketch, offset);
    }


    public void privateDrawRandomLineFill(PApplet sketch, float offSet) {

        Triangle newTriangle = getScaled(offSet);
        PVector pointA = newTriangle.computeRandomPointIn();
        PVector pointB = newTriangle.computeRandomPointIn();
        sketch.line(pointA.x, pointA.y, pointB.x, pointB.y);


    }


    public void drawTriangleSubdivisionFill(PApplet sketch, int depth) {

        arcTriangleSubdivision(sketch, this, depth);

    }

    private void arcTriangleSubdivision(PApplet sketch, Triangle triangle, int depth) {
        if (Math.random() < 0.01) {
            return;
        }
        if (depth >= 0) {

//            String side = Utils.getRandomTriangleSide();
            String side = triangle.getLongestSide();
            PVector pointOnSide = triangle.getSegment(side).computeRandomGaussianPointOnLine(1);

            // FIXME: This needs to be done to resolve cannot resolve symbol error
            Triangle left = new Triangle();
            Triangle right = new Triangle();
//            sketch.stroke(sketch.random(255), sketch.random(255), sketch.random(255));
            switch (side) {
                case "a" -> {
                    new Line(triangle.A, pointOnSide).draw(sketch);
                    left = new Triangle(triangle.A, pointOnSide, triangle.C);
                    right = new Triangle(triangle.A, triangle.B, pointOnSide);
                }
                case "b" -> {
                    new Line(triangle.B, pointOnSide).draw(sketch);
                    left = new Triangle(triangle.A, triangle.B, pointOnSide);
                    right = new Triangle(pointOnSide, triangle.B, triangle.C);
                }
                case "c" -> {
                    new Line(triangle.C, pointOnSide).draw(sketch);
                    left = new Triangle(pointOnSide, triangle.B, triangle.C);
                    right = new Triangle(triangle.A, pointOnSide, triangle.C);
                }
                default -> throw new IllegalArgumentException();
            }

            System.out.printf("Left - Depth: %d%n", depth);
            arcTriangleSubdivision(sketch, left, depth - 1);

            System.out.printf("Right - Depth: %d%n", depth);
            arcTriangleSubdivision(sketch, right, depth - 1);

        }

    }

    public List<Circle> randomCirclePack(float percentage, float decay, int circleCountUpperBound) {

        if( percentage > 100 | percentage < 0 ){
            throw new IllegalArgumentException("Percentage must be between 0 and 100");
        }
        List<Circle> circles = new ArrayList<>();
        for(int i = 0; i <= circleCountUpperBound; i++){
            float circleArea = (float) (area() * ( percentage / 100 ) / (i * decay ));
            float circleRadiusForArea = Circle.getRadiusForArea(circleArea);

            for(int j = 0; j <= 2000; j++){
                PVector candidateCenter = computeRandomPointIn();
                Circle candidateCircle = new Circle(candidateCenter, circleRadiusForArea);
                List<PVector> testPoints = candidateCircle.getRegularPolygon(16).getVertices();
                if(containsAll(testPoints)){
                    if( circles.isEmpty()){
                        circles.add(candidateCircle);

                    } else if(!candidateCircle.intersectAtLeastOne(circles))
                        circles.add(candidateCircle);

                    }
                }

            }
        return circles;
    }

    /**
     * Test whether a given point lies inside the triangle. Code copied from SO. Not tested if it covers all edge cases
     * https://stackoverflow.com/questions/8721406/how-to-determine-if-a-point-is-inside-a-2d-convex-polygon
     * @param  point The point that should be tested to lie inside the polygon
     * @return true  If the point lies inside the triangle
     */
    public boolean contains(PVector point) {
        int i;
        int j;
        boolean result = false;
        for (i = 0, j = getVertices().size() - 1; i < getVertices().size(); j = i++) {
            if ((getVertices().get(i).y > point.y) != (getVertices().get(j).y > point.y) &&
                    (point.x < (getVertices().get(j).x - getVertices().get(i).x) * (point.y - getVertices().get(i).y) / (getVertices().get(j).y - getVertices().get(i).y) + getVertices().get(i).x)) {
                result = !result;
            }
        }
        return result;
    }

    public boolean containsAll(List<PVector> test) {

        for(PVector point :  test){
            if ( !contains(point) ){
                return false;
            }
        }
        return true;

    }

    public Triangle reflectTriangleOnSide(String side) {
        if (Objects.equals(side, "a")) {
            PVector newA = segmentA().reflectPointAcrossLine(getA());
            return new Triangle(newA, getB(), getC());
        } else if (Objects.equals(side, "b")) {
            PVector newB = segmentB().reflectPointAcrossLine(getB());
            return new Triangle(getA(), newB, getC());
        } else if (Objects.equals(side, "c")) {
            PVector newC = segmentC().reflectPointAcrossLine(getC());
            return new Triangle(getA(), getB(), newC);
        } else {
            throw new IllegalArgumentException("Illegal side");
        }
    }

    public Triangle reflectTriangleOnSideWithJitter(String side, float radius) {
        if (Objects.equals(side, "a")) {
            PVector newA = Utils.getCircularJitteredPoint(segmentA().reflectPointAcrossLine(getA()),radius);
            return new Triangle(newA, getB(), getC());
        } else if (Objects.equals(side, "b")) {
            PVector newB = Utils.getCircularJitteredPoint(segmentB().reflectPointAcrossLine(getB()),radius);
            return new Triangle(getA(), newB, getC());
        } else if (Objects.equals(side, "c")) {
            PVector newC = Utils.getCircularJitteredPoint(segmentC().reflectPointAcrossLine(getC()),radius);
            return new Triangle(getA(), getB(), newC);
        } else {
            throw new IllegalArgumentException("Illegal side");
        }
    }



    public boolean intersect(Triangle anotherTriangle) {

        boolean intersect = false;
        List<String> sides = List.of("a","b","c");

        for(String side : sides){
            Line segment = getSegment(side);
            for(String testSide : sides){


                if(segment.intersect(anotherTriangle.getSegment(testSide))){
                    return true;
                }
            }

            }
        return intersect;
        }








    }
