package extensions.modules.shapes;

import goodies.utils.Conversions;
import processing.core.PApplet;
import processing.core.PVector;

public class NotReallyAnArc implements Shape {

    public PVector A;
    public PVector B;
    public PVector C;
    public Circle refCircle;
    public float refCircleAngleStart;
    public float refCircleAngleEnd;

    public NotReallyAnArc(PVector A, PVector B, PVector C, Circle refCircle) {
        this.A = A;
        this.B = B;
        this.C = C;
        this.refCircle = refCircle;
        this.refCircleAngleStart = Utils.angleForPointOnCircleInDegrees(refCircle, B);
        this.refCircleAngleEnd = Utils.angleForPointOnCircleInDegrees(refCircle, C);

        if (refCircleAngleEnd == 0 && refCircleAngleStart < 360) {
            this.refCircleAngleEnd = 360;
        }
    }

    public NotReallyAnArc(PVector A, PVector B, PVector C) {
        this.A = A;
        this.B = B;
        this.C = C;
    }

    public NotReallyAnArc() {
        this.A = new PVector(0, 0);
        this.B = new PVector(0, 0);
        this.C = new PVector(0, 0);
    }

    @Override
    public String toString() {
        return String.format("NotReallyAnArc(A=(%f,%f),B=(%f,%f),C=(%f,%f),start=%f,stop=%f)", A.x, A.y, B.x, B.y, C.x, C.y, this.refCircleAngleStart, this.refCircleAngleEnd);
    }


    public double a() {
        return (Math.toRadians(refCircleAngleEnd) - Math.toRadians(refCircleAngleStart)) * refCircle.radius;
    }


    public double b() {
        return segmentB().length();
    }


    public double c() {
        return segmentC().length();
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


    public PVector getA() {
        return A.copy();
    }


    public PVector getB() {
        return B.copy();
    }


    public PVector getC() {
        return C.copy();
    }


    public NotReallyAnArc shift(PVector mover) {
        PVector newA = A.copy().add(mover);
        PVector newB = B.copy().add(mover);
        PVector newC = C.copy().add(mover);
        Circle newRefCircle = refCircle.translate(mover);
        return new NotReallyAnArc(newA, newB, newC, newRefCircle);

    }


    public PVector computeRandomPointOnSide(String side) {

        return switch (side) {
            case "a" -> refCircle.computeRandomPointOnBetweenAngles(refCircleAngleStart, refCircleAngleEnd);
            case "b" -> segmentB().getRandomPointOn();
            case "c" -> segmentC().getRandomPointOn();
            default -> throw new IllegalArgumentException();
        };


    }


    public PVector computeMidPointOnSide(String side) {

        return switch (side) {
            case "a" -> refCircle.getPointOnCircleForAngle((refCircleAngleEnd + refCircleAngleStart) / 2);
            case "b" -> segmentB().getMidPoint();
            case "c" -> segmentC().getMidPoint();
            default -> throw new IllegalArgumentException();
        };


    }


    public PVector computeRandomGaussianPointOnSide(String side, float sigma) {

        return switch (side) {
            case "a" ->
                    refCircle.computeRandomGaussianPointOnBetweenAngles(refCircleAngleStart, refCircleAngleEnd, sigma);
            case "b" -> segmentB().computeRandomGaussianPointOnLine(sigma);
            case "c" -> segmentC().computeRandomGaussianPointOnLine(sigma);
            default -> throw new IllegalArgumentException();
        };

    }


    public Line segmentA() {
        return new Line(B, C);
    }

    public Line segmentC() {
        return new Line(A, B);
    }


    public Line segmentB() {
        return new Line(A, C);
    }


    public void draw(PApplet sketch) {
        sketch.line(A.x, A.y, B.x, B.y);
        sketch.line(A.x, A.y, C.x, C.y);

        float startInRadians = Conversions.doubleToFloat(Utils.degreeToRadians(refCircleAngleStart));
        float endInRadians = Conversions.doubleToFloat(Utils.degreeToRadians(refCircleAngleEnd));


        sketch.arc(refCircle.center.x,
                refCircle.center.y, refCircle.radius * 2, refCircle.radius * 2, startInRadians, endInRadians);
    }


    public void drawTriangleSubdivisionFill(PApplet sketch, int depth, float sigma) {

        arcTriangleSubdivision(sketch, this, depth, sigma);

    }


    private void arcTriangleSubdivision(PApplet sketch, Shape shape, int depth, float sigma) {
        String side;

        if (depth >= 0) {

            side = shape.getLongestSide();
            PVector pointOnSide = shape.computeRandomGaussianPointOnSide(side, 1);
            // FIXME: This needs to be done to resolve cannot resolve symbol error
            Shape left = null;
            Shape right = null;

            if (shape instanceof Triangle) {

                switch (side) {
                    case "a" -> {
                        new Line(shape.getA(), pointOnSide).draw(sketch);
                        left = new Triangle(shape.getA(), pointOnSide, shape.getC());
                        right = new Triangle(shape.getA(), shape.getB(), pointOnSide);
                    }
                    case "b" -> {
                        new Line(shape.getB(), pointOnSide).draw(sketch);
                        left = new Triangle(shape.getA(), shape.getB(), pointOnSide);
                        right = new Triangle(pointOnSide, shape.getB(), shape.getC());
                    }
                    case "c" -> {
                        new Line(shape.getC(), pointOnSide).draw(sketch);
                        left = new Triangle(pointOnSide, shape.getB(), shape.getC());
                        right = new Triangle(shape.getA(), pointOnSide, shape.getC());
                    }
                }
            } else if (shape instanceof NotReallyAnArc) {

                switch (side) {
                    case "a" -> {
                        new Line(shape.getA(), pointOnSide).draw(sketch);
                        left = new NotReallyAnArc(shape.getA(), pointOnSide, shape.getC(), refCircle);
                        right = new NotReallyAnArc(shape.getA(), shape.getB(), pointOnSide, refCircle);
                    }
                    case "b" -> {
                        new Line(shape.getB(), pointOnSide).draw(sketch);
                        left = new Triangle(shape.getA(), shape.getB(), pointOnSide);
                        right = new NotReallyAnArc(pointOnSide, shape.getB(), shape.getC(), refCircle);
                    }
                    case "c" -> {
                        new Line(shape.getC(), pointOnSide).draw(sketch);
                        left = new NotReallyAnArc(pointOnSide, shape.getB(), shape.getC(), refCircle);
                        right = new Triangle(shape.getA(), pointOnSide, shape.getC());
                    }
                    default -> throw new IllegalArgumentException();
                }
            }

            arcTriangleSubdivision(sketch, left, depth - 1, sigma);
            arcTriangleSubdivision(sketch, right, depth - 1, sigma);


        }


    }
}
