package extensions.modules.shapes;

import extensions.modules.grid.Grid;
import goodies.utils.Conversions;
import extensions.modules.utils.RandomUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static processing.core.PApplet.println;

@ToString
@EqualsAndHashCode
public class Quadrilateral implements ClipObject {

    public PVector A;
    public PVector B;
    public PVector C;
    public PVector D;
    @Getter
    @Setter
    public List<PVector> vertices;

    @Getter
    @Setter
    public List<Line> edges;

    public Quadrilateral(PVector A, PVector B, PVector C, PVector D) {
        this.A = A;
        this.B = B;
        this.C = C;
        this.D = D;
        setVertices(List.of(A, B, C, D));
        setEdges(
                List.of(
                        new Line(A, B),
                        new Line(B, C),
                        new Line(C, D),
                        new Line(D, A))
        );
    }

    public Quadrilateral() {
        this.A = new PVector(0, 0);
        this.B = new PVector(0, 0);
        this.C = new PVector(0, 0);
        this.D = new PVector(0, 0);
        setVertices(List.of(A, B, C, D));
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


    public PVector getD() {
        return D.copy();
    }

    public double a() {
        return Conversions.floatToDouble(A.dist(this.B));

    }


    public Quadrilateral rotate(float radians) {
        List<PVector> newVertices = new ArrayList<PVector>();
        for (var vertex : vertices) {
            newVertices.add(vertex.rotate(radians));
        }
        return new Quadrilateral(newVertices.get(0), newVertices.get(1), newVertices.get(2), newVertices.get(3));
    }


    public double b() {

        return Conversions.floatToDouble(B.dist(this.C));

    }


    public double c() {

        return Conversions.floatToDouble(C.dist(this.D));

    }


    public double d() {

        return Conversions.floatToDouble(D.dist(this.A));

    }


    public Line segmentA() {
        return new Line(A, B);

    }


    public Line segmentB() {
        return new Line(B, C);

    }


    public Line segmentC() {
        return new Line(C, D);

    }


    public Line segmentD() {
        return new Line(D, A);

    }

    public Quadrilateral center() {

        var avgX = (float) Stream.of(A.x, B.x, C.x, D.x).mapToDouble(d -> d).average().orElse(0.0);
        var avgY = (float) Stream.of(A.y, B.y, C.y, D.y).mapToDouble(d -> d).average().orElse(0.0);
        var avgPvector = new PVector(avgX, avgY);
        var newA = A.copy().sub(avgPvector);
        var newB = B.copy().sub(avgPvector);
        var newC = C.copy().sub(avgPvector);
        var newD = D.copy().sub(avgPvector);
        return new Quadrilateral(newA, newB, newC, newD);
    }

    public Quadrilateral centerAt(PVector point) {
        var center = center();
        var newA = center.A.copy().add(point);
        var newB = center.B.copy().add(point);
        var newC = center.C.copy().add(point);
        var newD = center.D.copy().add(point);
        return new Quadrilateral(newA, newB, newC, newD);
    }

    public Quadrilateral scaleY(float factor) {
        var newC = new PVector(C.copy().x, C.copy().y * factor);
        var newD = new PVector(D.copy().x, D.copy().y * factor);
        return new Quadrilateral(A.copy(), B.copy(), newC, newD);
    }

    public Quadrilateral getJitter(float radius) {

        PVector newA = Utils.getCircularJitteredPoint(this.A.copy(), radius);
        PVector newB = Utils.getCircularJitteredPoint(this.B.copy(), radius);
        PVector newC = Utils.getCircularJitteredPoint(this.C.copy(), radius);
        PVector newD = Utils.getCircularJitteredPoint(this.D.copy(), radius);

        return new Quadrilateral(newA, newB, newC, newD);

    }


    public Quadrilateral scale(float factor) {
        var scaled = vertices.stream().map(it -> it.copy().mult(factor)).toList();
        return new Quadrilateral(scaled.get(0), scaled.get(1), scaled.get(2), scaled.get(3));
    }


    public Quadrilateral translate(PVector translation) {
        var scaled = vertices.stream().map(it -> it.copy().add(translation)).toList();
        return new Quadrilateral(scaled.get(0), scaled.get(1), scaled.get(2), scaled.get(3));
    }

    public PVector getRandomPointIn() {

        if (RandomUtils.zeroOrOne() == 1) {
            return new Triangle(getA(), getB(), getC()).computeRandomPointIn();
        } else {
            return new Triangle(getC(), getD(), getA()).computeRandomPointIn();
        }
    }

    public Line getSegment(String side) {
        return switch (side) {
            case "a" -> segmentA();
            case "b" -> segmentB();
            case "c" -> segmentC();
            case "d" -> segmentD();
            default -> throw new IllegalArgumentException("Invalid Side");
        };
    }

    public List<Float> computeCentroid() {
        float centroidX = 0;
        float centroidY = 0;
        for (PVector coords : getVertices()) {
            centroidX += coords.x;
            centroidY += coords.y;
        }
        return List.of(centroidX / getVertices().size(), centroidY / getVertices().size());


    }


    public String getOppositeSide(String side) {

        return switch (side) {
            case "a" -> "c";
            case "b" -> "d";
            case "c" -> "a";
            case "d" -> "b";
            default -> throw new IllegalArgumentException("Invalid side");
        };

    }

    public String getLongestSide() {

        float[] segmentLengths = new float[]{Conversions.doubleToFloat(a()), Conversions.doubleToFloat(b()), Conversions.doubleToFloat(c()), Conversions.doubleToFloat(d())};


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
        } else if (index == 2) {
            return "c";

        } else {
            return "d";
        }
    }


    public static Quadrilateral fromSketch(PApplet sketch) {
        PVector A = new PVector(0, 0);
        PVector B = new PVector(sketch.width, 0);
        PVector C = new PVector(sketch.width, sketch.height);
        PVector D = new PVector(0, sketch.height);


        return new Quadrilateral(A, B, C, D);

    }

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

        for (PVector point : test) {
            if (!contains(point)) {
                return false;
            }
        }
        return true;

    }


    public void draw(PApplet sketch) {

        for (var edge : edges) {
            edge.draw(sketch);
        }


//        sketch.quad(A.x,A.y,B.x,B.y,C.x,C.y,D.x,D.y);
    }

    public void drawQuad(PApplet sketch) {

//        for (var edge : edges) {
//            edge.draw(sketch);
//        }


        sketch.quad(A.x, A.y, B.x, B.y, C.x, C.y, D.x, D.y);
    }

    public void drawQuadSubdivision(PApplet sketch, int depth) {

        drawQuadSubdivisionSplit(sketch, this, depth);

    }

    public static String getRandomSide() {
        List<String> sides = List.of("a", "b", "c", "d");
        int index = new Random().nextInt(3);
        return sides.get(index);
    }


    public static String getRandomVertex() {
        List<String> sides = List.of("A", "B", "C", "D");
        int index = new Random().nextInt(3);
        return sides.get(index);
    }

    public static List<String> getVertexStrings() {
        return List.of("A", "B", "C", "D");
    }

    ;

    public void drawRandomLineFill(PApplet sketch) {

        List<String> sides = List.of("a", "b", "c", "d");
        int index = new Random().nextInt(3);
        String side = sides.get(index);
        PVector pointOnSide = this.getSegment(side).getRandomPointOn();

        String oppositeSide = this.getOppositeSide(side);
        PVector pointOnOppositeSite = this.getSegment(oppositeSide).getRandomPointOn();
//        PVector p = new PVector(10,10).mult(10);
        new Line(pointOnSide, pointOnOppositeSite).draw(sketch);

    }

    public double area() {
        return new Triangle(A, B, C).area() + new Triangle(C, D, A).area();
    }

    public List<Circle> randomCirclePack(float percentage, float decay, int circleCountUpperBound) {

        if (percentage > 100 | percentage < 0) {
            throw new IllegalArgumentException("Percentage must be between 0 and 100");
        }
        List<Circle> circles = new ArrayList<>();
        for (int i = 0; i <= circleCountUpperBound; i++) {
            float circleArea = (float) (area() * (percentage / 100) / (i * decay));
            float circleRadiusForArea = Circle.getRadiusForArea(circleArea);
            for (int j = 0; j <= 2000; j++) {
                PVector candidateCenter = computeRandomPointIn();
                Circle candidateCircle = new Circle(candidateCenter, circleRadiusForArea);
                List<PVector> testPoints = candidateCircle.getRegularPolygon(16).getVertices();
                if (containsAll(testPoints)) {
                    if (circles.isEmpty()) {
                        circles.add(candidateCircle);

                    } else if (!candidateCircle.intersectAtLeastOne(circles))
                        circles.add(candidateCircle);

                }
            }

        }
        return circles;
    }


    private void drawQuadSubdivisionSplit(PApplet sketch, Quadrilateral quad, int depth) {
        if (Math.random() < 0.01) {
            return;
        }
        if (depth >= 0) {
            String side;

            side = quad.getLongestSide();
            PVector pointOnSide = quad.getSegment(side).computeRandomGaussianPointOnLine(1);
            String oppositeSide = quad.getOppositeSide(side);
            PVector pointOnOppositeSite = quad.getSegment(oppositeSide).computeRandomGaussianPointOnLine(1);

            Quadrilateral left;
            Quadrilateral right;

            switch (side) {
                case "a" -> {
                    new Line(pointOnSide, pointOnOppositeSite).draw(sketch);
                    left = new Quadrilateral(pointOnSide, quad.getB(), quad.getC(), pointOnOppositeSite);
                    right = new Quadrilateral(quad.getA(), pointOnSide, pointOnOppositeSite, quad.getD());
                }
                case "b" -> {
                    new Line(pointOnSide, pointOnOppositeSite).draw(sketch);
                    left = new Quadrilateral(pointOnOppositeSite, pointOnSide, quad.getC(), quad.getD());
                    right = new Quadrilateral(quad.getA(), quad.getB(), pointOnSide, pointOnOppositeSite);
                }
                case "c" -> {
                    new Line(pointOnSide, pointOnOppositeSite).draw(sketch);
                    left = new Quadrilateral(quad.getA(), pointOnOppositeSite, pointOnSide, quad.getD());
                    right = new Quadrilateral(pointOnOppositeSite, quad.getB(), quad.getC(), pointOnSide);
                }
                default -> {
                    new Line(pointOnSide, pointOnOppositeSite).draw(sketch);
                    left = new Quadrilateral(quad.getA(), quad.getB(), pointOnOppositeSite, pointOnSide);
                    right = new Quadrilateral(pointOnSide, pointOnOppositeSite, quad.getC(), quad.getD());
                }
            }


            drawQuadSubdivisionSplit(sketch, left, depth - 1);


            drawQuadSubdivisionSplit(sketch, right, depth - 1);


        }

    }

    @Override
    public boolean pointIsInside(PVector point) {
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

    @Override
    public Grid getEqualGridPartition(int parts) {

        return new Grid(A.x, B.x, parts, B.y, C.y, parts, 2);
    }

    @Override
    public PVector computeRandomPointIn() {
        if (Math.random() < 0.5) {
            return new Triangle(A, B, C).computeRandomPointIn();
        } else {
            return new Triangle(C, D, A).computeRandomPointIn();
        }

    }
}

// Triangle split
// Band Split
// Box Split
// Random Fill
// RandomShade