package extensions.modules.shapes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import processing.core.PVector;

public class TriangleTest {
    @Test
    public void testAngle() {

        PVector A = new PVector(100, 100);
        PVector B = new PVector(110, 100);
        PVector C = new PVector(105, 105);
        Triangle triangle = new Triangle(A, B, C);

        System.out.println(String.format("H A: %f", triangle.hA()));
        System.out.println(String.format("H B: %f", triangle.hB()));
        System.out.println(String.format("H C: %f", triangle.hC()));


        System.out.println(String.format("Test a: %f", triangle.a()));
        System.out.println(String.format("Test b: %f", triangle.b()));
        System.out.println(String.format("Test c: %f", triangle.c()));
        // System.out.println(String.format("Actual X: %f", triangle.aTest()));

        System.out.println(Math.pow(triangle.a(), 2));

// Code
        double angle = Math.acos((Math.pow(triangle.b(), 2) + Math.pow(triangle.c(), 2) - Math.pow(triangle.a(), 2) / (2 * triangle.b() * triangle.c())));
        double test = Math.acos((Math.pow(triangle.a(), 2) + Math.pow(triangle.b(), 2) - Math.pow(triangle.c(), 2)) / (2 * triangle.a() * triangle.b()));
//        double test = Math.acos((Math.pow(triangle.b(), 2) + Math.pow(triangle.c(), 2) - Math.pow(triangle.a(), 2) / (2 * triangle.b() * triangle.c())));
        System.out.println(String.format("Test : %f", test));
        System.out.println(String.format("Angle : %f", angle));
        System.out.println(String.format("alpha X: %f", triangle.alpha()));
        System.out.println(String.format("beta X: %f", triangle.beta()));
        System.out.println(String.format("gamma X: %f", triangle.gamma()));
        System.out.println(String.format("Inner Center X: %f", triangle.innerCircleCenter().x));
        System.out.println(String.format("Inner Center Y: %f", triangle.innerCircleCenter().y));

        // Given
        Circle inner = triangle.getInnerCircle();
        System.out.println(String.format("Inner Center Y: %f", inner.radius));
        // When

        // Then
        assertEquals(1, 1);
    }

    @Test
    public void containsShouldReturnFalseIfPointIsOutsideTriangle() {

        // Given
        PVector A = new PVector(0, 0);
        PVector B = new PVector(1, 0);
        PVector C = new PVector(1, 1);
        Triangle triangle = new Triangle(A, B, C);

        PVector testPoint = new PVector(100, 100);

        // When
        boolean result = triangle.contains(testPoint);

        // Then
        assertThat(result).isEqualTo(false);

    }

    @Test
    public void containsShouldReturnTrueIfPointIsInsideTriangle() {

        // Given
        PVector A = new PVector(0, 0);
        PVector B = new PVector(1, 0);
        PVector C = new PVector(1, 1);
        Triangle triangle = new Triangle(A, B, C);

        PVector testPoint = new PVector(0.1f, 0.1f);

        // When
        boolean result = triangle.contains(testPoint);

        // Then
        assertThat(result).isEqualTo(true);

    }

    @Test
    public void containsAllShouldReturnFalseIfAtLeastOnePointInAListOfPointsIsOutsideTriangle() {

        // Given
        PVector A = new PVector(0, 0);
        PVector B = new PVector(1, 0);
        PVector C = new PVector(1, 1);
        Triangle triangle = new Triangle(A, B, C);

        List<PVector> testPoints = List.of( new PVector(0.1f, 0.1f), new PVector(100, 100));

        // When
        boolean result = triangle.containsAll(testPoints);

        // Then
        assertThat(result).isEqualTo(false);

    }

    @Test
    public void containsAllShouldReturnTrueIfPointsAreInsideTriangle() {

        // Given
        PVector A = new PVector(0, 0);
        PVector B = new PVector(1, 0);
        PVector C = new PVector(1, 1);
        Triangle triangle = new Triangle(A, B, C);

        List<PVector> testPoints = List.of( new PVector(0.1f, 0.1f), new PVector(0.2f, 0.2f));

        // When
        boolean result = triangle.containsAll(testPoints);

        // Then
        assertThat(result).isEqualTo(true);

    }

    @Test
    public void randomCirclePackShouldThrowIllegalArgumentExceptionWhenPercentageIsInvalid() {

        // Given
        PVector A = new PVector(0, 0);
        PVector B = new PVector(100, 0);
        PVector C = new PVector(100, 100);
        Triangle triangle = new Triangle(A, B, C);

        // when
        Throwable thrown = catchThrowable(() -> {
            triangle.randomCirclePack(9,2,200);
        });

        // then
        assertThat(thrown)
                .isInstanceOf(IllegalArgumentException.class);

    }

    @Test
    public void randomCirclePackShouldFindAtLeastOneCircleInATriangle() {

        // Given
        PVector A = new PVector(0, 0);
        PVector B = new PVector(100, 0);
        PVector C = new PVector(100, 100);
        Triangle triangle = new Triangle(A, B, C);
        int circleCountUpperBound = 1300;

        // when
        List<Circle> result = triangle.randomCirclePack(50,2,circleCountUpperBound);

        // then
        assertThat(result.size()).isBetween(50,circleCountUpperBound);

    }

    @Test
    public void circlesInRandomCirclePackShouldNotIntersect() {

        // Given
        PVector A = new PVector(0, 0);
        PVector B = new PVector(100, 0);
        PVector C = new PVector(100, 100);
        Triangle triangle = new Triangle(A, B, C);

        // when
        List<Circle> result = triangle.randomCirclePack(50,2,2000);

        // then
        for(Circle circle : result ){

            List<Circle> filtered = result.stream()
                    .filter(cir -> !cir.equals(circle))
                    .collect(Collectors.toList());
            boolean tmpResult = circle.intersectAtLeastOne(filtered);

            assertThat(tmpResult).isEqualTo(false);
        }


    }


    @Test
    public void reflectATriangleOnSideA() {

        // Given
        PVector A = new PVector(-1, 1);
        PVector B = new PVector(1, 1);
        PVector C = new PVector(-1, -1);
        Triangle triangle = new Triangle(A, B, C);

        Triangle expected = new Triangle(new PVector(1,-1), B,C);

        // when
        Triangle result = triangle.reflectTriangleOnSide("a");

        // then
        assertThat(result).isEqualTo(expected);

    }

    @Test
    public void intersectShouldReturnTrueIfTwoTrianglesHaveIntersectionPoints() {

        // Given
        PVector A = new PVector(-3, -3);
        PVector B = new PVector(-3, 2);
        PVector C = new PVector(2, 2);
        Triangle triangle = new Triangle(A, B, C);

        PVector anotherA = new PVector(-2, 1);
        PVector anotherB = new PVector(2, 1);
        PVector anotherC = new PVector(0, -3);
        Triangle anotherTriangle = new Triangle(anotherA, anotherB, anotherC);


        // when
        boolean result = triangle.intersect(anotherTriangle);

        // then
        assertThat(result).isEqualTo(true);

    }

    @Test
    public void intersectShouldReturnFalseIfTwoTrianglesDoNotHaveIntersectionPoints() {

        // Given
        PVector A = new PVector(-3, -3);
        PVector B = new PVector(-3, 2);
        PVector C = new PVector(2, 2);
        Triangle triangle = new Triangle(A, B, C);

        PVector anotherA = new PVector(200, 100);
        PVector anotherB = new PVector(200, 200);
        PVector anotherC = new PVector(400, 500);
        Triangle anotherTriangle = new Triangle(anotherA, anotherB, anotherC);


        // when
        boolean result = triangle.intersect(anotherTriangle);

        // then
        assertThat(result).isEqualTo(false);

    }

    @Test
    public void intersectShouldReturnTrueIfSmallerTriangleIsInsideBiggerTriangle() {

        // Given
        PVector A = new PVector(-3, -3);
        PVector B = new PVector(-3, 2);
        PVector C = new PVector(2, 2);
        Triangle triangle = new Triangle(A, B, C);

        PVector anotherA = new PVector(-0.5f, -0.5f);
        PVector anotherB = new PVector(-0.5f, 0.1f);
        PVector anotherC = new PVector(0.5f, 0.5f);
        Triangle anotherTriangle = new Triangle(anotherA, anotherB, anotherC);


        // when
        boolean result = triangle.intersect(anotherTriangle);

        // then
        assertThat(result).isEqualTo(true);

    }


}