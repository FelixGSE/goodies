package extensions.modules.shapes;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;

import processing.core.PVector;


public class UtilsTests {
    @Test
    public void testRadiansToDegrees() {

        // Given
        double degress = Utils.radiansToDegree(1.5708);
        // When

        // Then

        assertEquals(90.00, degress, 0.01);
    }

    @Test
    public void testPointOnCircle() {

        PVector center = new PVector(0, 0);
        float radius = 1;
        Circle circle = new Circle(center, radius);
        double radians = Utils.degreeToRadians(90);

        // Given
        PVector pointOnCircle = Utils.pointOnCircle(circle, radians);
        // When

        // Then

        assertEquals(0, pointOnCircle.x, 0.001);
        assertEquals(1, pointOnCircle.y, 0.001);
    }

    @Test
    public void pointShouldBeRotatedClockWiseAroundTheUnitCircle() {
        PVector center = new PVector(0,0);
        float radius = 1;
        Circle circle = new Circle(center, radius);
        ArrayList<PVector> poles = circle.getPoles();

        PVector pointToRotate = new PVector(1,0);

        for(int i=0; i<poles.size(); i++){
            PVector result = Utils.roundPVector(Utils.rotatePointClockWiseAroundAReferencePoint(center,pointToRotate,i*90),4);
            PVector expected = Utils.roundPVector(poles.get(i),4);
            assertEquals(expected,result);
        }

    }

    @Test
    public void pointShouldBeRotatedCounterClockWiseAroundTheUnitCircle() {
        PVector center = new PVector(0,0);
        float radius = 1;
        Circle circle = new Circle(center, radius);
        ArrayList<PVector> poles = circle.getPoles();
        Collections.reverse(poles);

        PVector pointToRotate = new PVector(1,0);

        for(int i=0; i<poles.size(); i++){
            PVector result = Utils.roundPVector(Utils.rotatePointCounterClockWiseAroundAReferencePoint(center,pointToRotate,i*90),4);
            PVector expected = Utils.roundPVector(poles.get(i),4);
            assertEquals(expected,result);
        }

    }







    @Test
    public void testAngleForPointOnCircle() {

        PVector center = new PVector(0, 0);
        float radius = 1;
        Circle circle = new Circle(center, radius);

        PVector pointOnCircle = new PVector(0, 1);

        // Given
        double angleForPointOnCircle = Utils.angleForPointOnCircleInDegrees(circle, pointOnCircle);
        // When

        // Then

        assertEquals(Utils.degreeToRadians(90), angleForPointOnCircle, 0.001);

    }

    @Test
    public void testLineSubdivision() {
        long seed = 20;
        Random random = new Random();
        random.setSeed(seed);

        PVector start = new PVector(0, 0);
        PVector end = new PVector(1000, 0);
        Line lineToSplit = new Line(start, end);

        ArrayList<Line> listOfLines = Utils.divideLineIntoUniformRandomParts(lineToSplit, 1);

//        for(int i = 0; i<=listOfLines.size();i++){
//            Line line = listOfLines.get(i);
//            start = line.start;
//            end = line.end;
//            System.out.println(String.format("START X: %f, START Y: %f", start.x,start.y));
//            System.out.println(String.format("END X: %f, END Y: %f", end.x,end.y));
//        }

        // Given

        // When

        // Then

        assertEquals(4, listOfLines.size());

    }


    @Test
    public void shouldReturnNMinusOneLinesWhenConvertingListOfPointsToListOfLines() {
        // given
        PVector start = new PVector(0, 0);
        PVector end = new PVector(100, 0);
        Line line = new Line(start, end);
        int numberOfSplits = 3;
        ArrayList<PVector> listOfPoints = line.divideInEqualParts(numberOfSplits);

        // when
        List<Line> listOfLines = Utils.pointCollectionToLineCollection(listOfPoints);

        // then
        assertEquals(numberOfSplits, listOfLines.size());
    }

    @Test
    public void shouldReturnEndPointEqualsToStartPointWhenScalingFactorIsZero() {
        // given
        PVector start = new PVector(0, 0);
        PVector end = new PVector(100, 0);
        Line line = new Line(start, end);
        // when
        PVector result = Utils.scaleLineFromOrigin(line, 0);

        // then
        assertEquals(line.getStart(), result);
    }

    @Test
    public void shouldReturnNewEndpointEqualsToOldEndPointWhenScalingFactorIsEqualToOne() {
        // given
        PVector start = new PVector(0, 0);
        PVector end = new PVector(100, 0);
        Line line = new Line(start, end);
        // when
        PVector result = Utils.scaleLineFromOrigin(line, 1);

        // then
        assertEquals(line.getEnd(), result);
    }

    @Test
    public void shouldReturnAlongerLineWhenScalingFactorIsLargerThanOne() {
        // given
        PVector start = new PVector(0, 0);
        PVector end = new PVector(100, 0);
        Line line = new Line(start, end);
        PVector expected = new PVector(line.getEnd().x * 1.5f, line.getEnd().y * 1.5f);

        // when
        PVector result = Utils.scaleLineFromOrigin(line, 1.5);

        // then
        assertEquals(expected, result);
    }

    @Test
    public void lineShouldHaveSameLengthWhenWhenInputIsNegativeAsSamePositiveValue() {
        // given
        PVector start = new PVector(20, 10);
        PVector end = new PVector(100, 20);
        Line line = new Line(start, end);


        for (double i = -1; i <= 0; i = i + 0.5) {
            PVector scaled = Utils.scaleLineFromOrigin(line, i);
            PVector absScaled = Utils.scaleLineFromOrigin(line, Math.abs(i));

            float scaledDist = start.dist(scaled);
            float absScaledDist = start.dist(absScaled);

            assertEquals(absScaledDist, scaledDist, 0.01);

        }

    }


    @Test
    public void shouldReturnLineInOppositeDirectionWhenScalingFactorIsNegative() {
        // given
        PVector start = new PVector(20, 20);
        PVector end = new PVector(10, 10);
        Line line = new Line(start, end);


        // when
        PVector result = Utils.scaleLineFromOrigin(line, -1);

        // then
        assertEquals(new PVector(30, 30), result);
    }

}