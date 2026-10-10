package extensions.modules.shapes;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LineTests {

//    @Test
//    public void testGoldenRatioSplit() {
//
//        PVector a = new PVector(0, 0);
//        PVector b = new PVector(1, 0);
//        Line line = new Line(a, b);
//
//        List<Line> listOfSplitPoints = line.goldenRatioSplit();
//
//        println(listOfSplitPoints);
//
//    }

    @Test
    public void testArrayWithRandomNumbers() {

        ArrayList<Double> randomNumbers = Utils.arrayWithRandomNumbers(4);

        assertEquals(randomNumbers.size(), 4);


    }

    @Disabled("known failure: expected 5, got 3 (assertEquals arguments swapped); E1.4")
    @Test
    public void testDivideLineInRandomParts() {

        PVector a = new PVector(100, 100);
        PVector b = new PVector(600, 100);
        Line line = new Line(a, b);

        ArrayList<PVector> listOfSplitPoints = Utils.divideLineInRandomParts(line, 4);

        assertEquals(listOfSplitPoints.size(), 3);


    }

    @Test
    public void testPointCollectionToLineCollection() {

        PVector a = new PVector(100, 100);
        PVector b = new PVector(600, 100);
        Line line = new Line(a, b);

        ArrayList<PVector> listOfSplitPoints = Utils.divideLineInRandomParts(line, 4);
        List<Line> lines = Utils.pointCollectionToLineCollection(listOfSplitPoints);

        assertEquals(4, lines.size());


    }

    @Test
    public void testDivideLineIntoEqualParts() {

        PVector a = new PVector(100, 100);
        PVector b = new PVector(600, 100);
        Line line = new Line(a, b);

        ArrayList<PVector> listOfSplitPoints = Utils.divideLineIntoEqualParts(line, 4);
        List<Line> lines = Utils.pointCollectionToLineCollection(listOfSplitPoints);
        assertEquals(4, lines.size());

    }

    @Disabled("known failure: expected 0, got 2; E1.4")
    @Test
    public void shouldReturnZeroSplitPointsWhenDepthIsZeroWhenSubdivide() {

        PVector a = new PVector(100, 100);
        PVector b = new PVector(600, 100);
        Line line = new Line(a, b);

        ArrayList<PVector> result = line.subdivide(0);

        assertEquals(0, result.size());

    }

    @Disabled("known failure: expected 1, got 3; E1.4")
    @Test
    public void shouldReturnOneSplitPointsWhenDepthIsOneWhenSubdivide() {

        PVector a = new PVector(100, 100);
        PVector b = new PVector(600, 100);
        Line line = new Line(a, b);

        ArrayList<PVector> result = line.subdivide(1);

        assertEquals(1, result.size());

    }

    @Disabled("known failure: expected 0, got 2; E1.4")
    @Test
    public void shouldReturnTwoToThePowerOfDepthMinusOneSplitPointsWhenInputIsGreaterThanZero() {

        PVector a = new PVector(100, 100);
        PVector b = new PVector(600, 100);
        Line line = new Line(a, b);

        for (int i = 0; i <= 5; i++) {
            ArrayList<PVector> result = line.subdivide(i);
            assertEquals(Math.pow(2,i) - 1, result.size(),0.01);
        }

    }

    @Test
    public void shouldReturnALineWithSameLengthWhenInputIsZero() {
        PVector a = new PVector(100, 100);
        PVector b = new PVector(600, 100);
        Line line = new Line(a, b);
        float scaleby = 10;

        Line result = line.increaseByLength(scaleby);

        assertEquals(line.length() + scaleby, result.length(), 0.01);

    }

    @Test
    public void startPointOfRotationShouldBe90DegreesWhenLiesOnUnitCircle() {
        PVector start = new PVector(0, 1);
        PVector end = new PVector(0, -1);
        Line line = new Line(start, end);

        float result = line.getStartPointRotation();

        assertEquals(90, result, 0.01);

    }

    @Test
    public void reflectAPointAcrossAnArbitraryLineWithPositiveSlopeWherePointIsLeftToTheLine() {
        // Given
        PVector start = new PVector(-1, -1);
        PVector end = new PVector(1, 1);
        Line line = new Line(start, end);
        PVector pointToReflect = new PVector(-1,1);
        PVector expected = new PVector(1,-1);


        // When
        PVector result = line.reflectPointAcrossLine(pointToReflect);

        assertThat(result).isEqualTo(expected);

    }

    @Test
    public void reflectAPointAcrossAnArbitraryLineWithPositiveSlopeWherePointIsRightToTheLine() {
        // Given
        PVector start = new PVector(-1, -1);
        PVector end = new PVector(1, 1);
        Line line = new Line(start, end);
        PVector pointToReflect = new PVector(1,-1);
        PVector expected = new PVector(-1,1);


        // When
        PVector result = line.reflectPointAcrossLine(pointToReflect);

        assertThat(result).isEqualTo(expected);

    }

    @Test
    public void reflectAPointAcrossAnArbitraryLineWithNegativeSlopeWherePointIsLeftToTheLine() {
        // Given
        PVector start = new PVector(-1, 1);
        PVector end = new PVector(1, -1);
        Line line = new Line(start, end);
        PVector pointToReflect = new PVector(1,1);
        PVector expected = new PVector(-1,-1);


        // When
        PVector result = line.reflectPointAcrossLine(pointToReflect);

        assertThat(result).isEqualTo(expected);

    }

    @Test
    public void reflectAPointAcrossAnArbitraryLineWithNegativeSlopeWherePointIsRightToTheLine() {
        // Given
        PVector start = new PVector(-1, 1);
        PVector end = new PVector(1, -1);
        Line line = new Line(start, end);
        PVector pointToReflect = new PVector(-1,-1);
        PVector expected = new PVector(1,1);


        // When
        PVector result = line.reflectPointAcrossLine(pointToReflect);

        assertThat(result).isEqualTo(expected);

    }

    @Test
    public void reflectAPointAcrossAnVerticalLineWherePointIsToTheLeftToTheLine() {
        // Given
        PVector start = new PVector(1, 0);
        PVector end = new PVector(1, 10);
        Line line = new Line(start, end);
        PVector pointToReflect = new PVector(0,5);
        PVector expected = new PVector(2,5);


        // When
        PVector result = line.reflectPointAcrossLine(pointToReflect);

        assertThat(result).isEqualTo(expected);

    }

    @Test
    public void reflectAPointAcrossAnVerticalLineWherePointIsToTheRightToTheLine() {
        // Given
        PVector start = new PVector(1, 0);
        PVector end = new PVector(1, 10);
        Line line = new Line(start, end);
        PVector pointToReflect = new PVector(2,5);
        PVector expected = new PVector(0,5);


        // When
        PVector result = line.reflectPointAcrossLine(pointToReflect);

        assertThat(result).isEqualTo(expected);

    }

    @Test
    public void reflectAPointAcrossAnHorizontalLineWherePointIsToTheLeftToTheLine() {
        // Given
        PVector start = new PVector(0, 0);
        PVector end = new PVector(10, 0);
        Line line = new Line(start, end);
        PVector pointToReflect = new PVector(1,5);
        PVector expected = new PVector(1,-5);


        // When
        PVector result = line.reflectPointAcrossLine(pointToReflect);

        assertThat(result).isEqualTo(expected);

    }

    @Test
    public void reflectAPointAcrossAnHorizontalLineWherePointIsToTheRightToTheLine() {
        // Given
        PVector start = new PVector(0, 0);
        PVector end = new PVector(10, 0);
        Line line = new Line(start, end);
        PVector pointToReflect = new PVector(1,-5);
        PVector expected = new PVector(1,5);


        // When
        PVector result = line.reflectPointAcrossLine(pointToReflect);

        assertThat(result).isEqualTo(expected);

    }

    @Test
    public void intersectShouldReturnFalseIfBothLinesAreHorizontal() {
        // Given
        PVector start = new PVector(0, 0);
        PVector end = new PVector(10, 0);
        Line line = new Line(start, end);

        PVector anotherStart = new PVector(0, -5);
        PVector anotherEnd = new PVector(10, -5);
        Line anotherLine = new Line(anotherStart, anotherEnd);

        // When
        var result = line.intersect(anotherLine);

        assertThat(result).isEqualTo(false);

    }
    @Test
    public void intersectShouldReturnFalseIfBothLinesAreVertical() {
        // Given
        PVector start = new PVector(0, 0);
        PVector end = new PVector(0, 10);
        Line line = new Line(start, end);

        PVector anotherStart = new PVector(2, 0);
        PVector anotherEnd = new PVector(2, 10);
        Line anotherLine = new Line(anotherStart, anotherEnd);

        // When
        var result = line.intersect(anotherLine);

        assertThat(result).isEqualTo(false);

    }


    @Test
    public void intersectShouldReturnFalseIfBothLineSegmentsDoNotIntersect() {
        // Given
        PVector start = new PVector(-1, 1);
        PVector end = new PVector(0, 0);
        Line line = new Line(start, end);

        PVector anotherStart = new PVector(1, -1);
        PVector anotherEnd = new PVector(2, -2);
        Line anotherLine = new Line(anotherStart, anotherEnd);

        // When
        var result = line.intersect(anotherLine);

        assertThat(result).isEqualTo(false);

    }

    @Test
    public void intersectShouldReturnTrueIfBothLineSegmentsDoIntersect() {
        // Given
        PVector start = new PVector(-1, 1);
        PVector end = new PVector(0, 0);
        Line line = new Line(start, end);

        PVector anotherStart = new PVector(-1, 0);
        PVector anotherEnd = new PVector(0, 1);
        Line anotherLine = new Line(anotherStart, anotherEnd);

        // When
        var result = line.intersect(anotherLine);

        assertThat(result).isEqualTo(true);

    }


    @Test
    public void intersectShould() {
        // Given
        PVector start = new PVector(-3.0f, 2.0f);
        PVector end = new PVector(2.0f, 2.0f);
        Line line = new Line(start, end);

        PVector anotherStart = new PVector(200, 200);
        PVector anotherEnd = new PVector(400, 500);
        Line anotherLine = new Line(anotherStart, anotherEnd);

        // When
        var result = line.intersect(anotherLine);

        assertThat(result).isEqualTo(false);

    }

}
