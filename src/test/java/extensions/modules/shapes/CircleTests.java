package extensions.modules.shapes;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import processing.core.PVector;

public class CircleTests {
    @Test
    public void testGCircle() {

        // Given
        PVector center = new PVector(0, 0);
        float radius = 1;

        Circle circle = new Circle(center, radius);
        double area = circle.getArea();
        double approximateCircleArea = 3.141592653589793;

        PVector p = circle.getPointOnCircleForAngle(400);
        PVector p2 = circle.getPointOnCircleForAngle(40);
        System.out.println(String.format("P1 x: %f", p.x));
        System.out.println(String.format("P1 y: %f", p.y));
        System.out.println(String.format("P2 x: %f", p2.x));
        System.out.println(String.format("P2 y: %f", p2.y));

        // When

        // Then

        assertEquals(approximateCircleArea, area, 0.0000000001);
    }

    @Test
    public void testEqualArcSubdivide() {

        PVector center = new PVector(0, 0);
        float radius = 1;
        Circle circle = new Circle(center, radius);

        ArrayList<NotReallyAnArc> listOfArcs = circle.equalArcSubdivide(4);

        int n = 4;
        ArrayList<PVector> degreeLine = Utils.makePositiveRealLine(360).divideInEqualParts(n);
        ArrayList<NotReallyAnArc> arcList = new ArrayList<>();

        for (int i = 0; i <= degreeLine.size() - 2; i++) {
            System.out.println(String.format("Angle lower: %f", degreeLine.get(i).x));
            System.out.println(String.format("Angle upper: %f", degreeLine.get(i + 1).x));

            PVector A = circle.center.copy();
            double lowerAngleInRadians = Utils.degreeToRadians(degreeLine.get(i).x);
            PVector B = Utils.pointOnCircle(circle, lowerAngleInRadians);

            System.out.println(String.format("B X: %f", B.x));
            System.out.println(String.format("B Y: %f", B.y));


            double upperAngleInRadians = Utils.degreeToRadians(degreeLine.get(i + 1).x);
            PVector C = Utils.pointOnCircle(circle, upperAngleInRadians);

            System.out.println(String.format("C X: %f", C.x));
            System.out.println(String.format("C Y: %f", C.y));

            NotReallyAnArc currentArc = new NotReallyAnArc(A, B, C, circle);
            arcList.add(currentArc);
        }

        assertEquals(4, listOfArcs.size());
        // 1
//        assertEquals(1.000000,listOfArcs.get(0).B.x,0.1);
//        assertEquals(0.000000,listOfArcs.get(0).B.y,0.1);
//        assertEquals(0.000000,listOfArcs.get(0).C.x,0.1);
//        assertEquals(1.000000,listOfArcs.get(0).C.y,0.1);
//        // 2
//        assertEquals(0.000000,listOfArcs.get(1).B.x,0.1);
//        assertEquals(1.000000,listOfArcs.get(1).B.y,0.1);
//        assertEquals(-1.000000,listOfArcs.get(1).C.x,0.1);
//        assertEquals(0.000000,listOfArcs.get(1).C.y,0.1);
//        // 3
//        assertEquals(-1.000000,listOfArcs.get(2).B.x,0.1);
//        assertEquals(0.000000,listOfArcs.get(2).B.y,0.1);
//        assertEquals(0.000000,listOfArcs.get(2).C.x,0.1);
//        assertEquals(1.000000,listOfArcs.get(2).C.y,0.1);
//        //4
//        assertEquals(0.000000,listOfArcs.get(3).B.x,0.1);
//        assertEquals(1.000000,listOfArcs.get(3).B.y,0.1);
//        assertEquals(1.000000,listOfArcs.get(3).C.x,0.1);
//        assertEquals(0.000000,listOfArcs.get(3).C.y,0.1);

    }

    @Test
    public void testPointMapping() {

        PVector center = new PVector(400, 200);
        float radius = 100;
        Circle circle = new Circle(center, radius);

        for (int i = 0; i < 360; i = i + 1) {
            double radians = Math.toRadians(i);
            PVector point = Utils.pointOnCircle(circle, radians);
            double degrees = Utils.angleForPointOnCircleInDegrees(circle, point);
            assertEquals(i, degrees, 0.01);
        }

    }

    @Test
    public void shouldThrowIllegalArgumentExecptionWhenGetPolygonIsCalledWithSplitLessThanThree() {

        // Given
        // PVector center = new PVector(0, 0);
        // float radius = 1;
        // Circle circle = new Circle(center, radius);
        // assertEquals(true, true);
    }

    @Test
    public void shouldReturnPolesOfTheCircle() {

        // Given
        PVector center = new PVector(0, 0);
        float radius = 1;
        Circle circle = new Circle(center, radius);

        List<PVector> poles = List.of(
                new PVector(1, 0),
                new PVector(0, 1),
                new PVector(-1, 0),
                new PVector(0, -1)


        );

        // when
        ArrayList<PVector> result = circle.getPoles();

        // then
        for(int i=0; i<=3;i++){
            PVector rounded = Utils.roundPVector(result.get(i),4);
            assertEquals(poles.get(i),rounded );
        }


    }


    @Test
    public void shouldGetAPolygonWithSameNumberOfVerticesAsInput() {

        // Given
        PVector center = new PVector(0, 0);
        float radius = 1;
        Circle circle = new Circle(center, radius);

        // when
        Polygon result = circle.getRegularPolygon(4);

        // then
        assertEquals(4, result.getN());
    }

    @Test
    public void shouldPreserveDistanceToPolygonVerticesEqualToCircleRadius() {
        // given
        PVector center = new PVector(0, 0);
        float radius = 1;
        Circle circle = new Circle(center, radius);

        // when
        Polygon result = circle.getRegularPolygon(4);

        // then
        for (PVector vertex : result.getVertices()) {

            float distance = new Line(center, vertex).length();
            assertEquals(radius, distance, 0.0001);

        }

    }

    @Test
    public void shouldMakeAPolygonWithVerticesOnPolsOfCircleWhenInputIsFour() {
        // given
        PVector center = new PVector(0, 0);
        float radius = 1;
        Circle circle = new Circle(center, radius);


        // when
        Polygon result = circle.getRegularPolygon(4);


        // then
        for (PVector vertex : result.getVertices()) {

            float distance = new Line(center, vertex).length();
            assertEquals(radius, distance, 0.0001);

        }

    }

    @Test
    public void shouldReturnPointEqualTo90DegreesWhenInputIs450() {
        // given
        PVector center = new PVector(0, 0);
        float radius = 1;
        Circle circle = new Circle(center, radius);
        PVector point = Utils.roundPVector(circle.getPointOnCircleForAngle(90),4);

        // when
        PVector result = Utils.roundPVector(circle.getPointOnCircleForAngle(450),4);

        //
        assertEquals(point,result);

    }

    @Test
    public void shouldCreateBoundingBox() {
        // given
        PVector center = new PVector(0, 0);
        float radius = 100;
        Circle circle = new Circle(center, radius);
        Square square = circle.getBoundingBox();

        assertThat(square.getCenter()).isEqualTo(center);


    }



    @Test
    public void intersectShouldReturnFalseIfTwoCirclesDoNotOverlap() {
        // given
        Circle circleA = new Circle(new PVector(0,0), 10);
        Circle circleB = new Circle(new PVector(100,100), 10);

        // when
        boolean result = circleA.intersect(circleB);

        // then
        assertThat(result).isEqualTo(false);

    }

    @Test
    public void intersectShouldReturnTrueIfTwoCirclesDoOverlap() {
        // given
        Circle circleA = new Circle(new PVector(0,0), 50);
        Circle circleB = new Circle(new PVector(0,5), 1);

        // when
        boolean result = circleA.intersect(circleB);

        // then
        assertThat(result).isEqualTo(true);

    }

    @Test
    public void intersectShouldReturnTrueIfSmallerCircleIsFullyInsideBiggerCircle() {
        // given
        Circle circleA = new Circle(new PVector(0,5), 1);
        Circle circleB = new Circle(new PVector(0,0), 50);

        // when
        boolean result = circleA.intersect(circleB);

        // then
        assertThat(result).isEqualTo(true);

    }

    @Test
    public void intersectAtLeastOneShouldReturnTrueIfAtLeastOneCircleOverlaps() {
        // given
        Circle circleA = new Circle(new PVector(0,0), 50);

        List<Circle> circles = List.of(
                new Circle(new PVector(0,5), 10),
                new Circle(new PVector(100,100), 10)
        );

        // when
        boolean result = circleA.intersectAtLeastOne(circles);

        // then
        assertThat(result).isEqualTo(true);

    }

    @Test
    public void intersectAtLeastOneShouldReturnFalseIfNoCircleOverlaps() {
        // given
        Circle circleA = new Circle(new PVector(0,0), 10);

        List<Circle> circles = List.of(
                new Circle(new PVector(500,500), 10),
                new Circle(new PVector(100,100), 10)
        );

        // when
        boolean result = circleA.intersectAtLeastOne(circles);

        // then
        assertThat(result).isEqualTo(false);

    }

    @Test
    public void intersectAtLeastOneShouldThrowIllegalArgumentExceptionWhenListIsEmpty() {
        // given
        Circle circleA = new Circle(new PVector(0,0), 10);

        List<Circle> circles = new ArrayList<>();

        // when
        Throwable thrown = catchThrowable(() -> {
            circleA.intersectAtLeastOne(circles);
        });

        // then
        assertThat(thrown)
                .isInstanceOf(IllegalArgumentException.class);
    }

}

