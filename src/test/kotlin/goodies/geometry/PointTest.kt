package goodies.geometry

import org.apache.commons.math3.exception.MathArithmeticException
import org.apache.commons.math3.linear.MatrixUtils
import org.assertj.core.api.Assertions.*
import org.assertj.core.api.Assertions.withPrecision
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import processing.core.PVector
import java.util.stream.Stream

internal class PointTest {
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    inner class Construction {
        @Test
        fun `Empty constructor for Point`() {
            // Given
            val expected = Point()

            // When
            val result = expected.dimensions

            // Then
            assertThat(result).isEqualTo(0)
        }

        @ParameterizedTest
        @MethodSource("getPointInputFromNumbers")
        fun `Construct a Point object from Numbers`(
            x: Number,
            y: Number,
        ) {
            // Given
            val expected = Point(x, y)

            // When
            val result = Point(1.0, 1.0)

            // Then
            assertThat(result).isEqualTo(expected)
        }

        private fun getPointInputFromNumbers(): Stream<Arguments> =
            Stream.of(
                Arguments.of(1, 1),
                Arguments.of(1L, 1L),
                Arguments.of(1.0, 1.0),
                Arguments.of(1.0f, 1.0f),
            )

        @ParameterizedTest
        @MethodSource("getPointInputFromList")
        fun `Construct a Point object from A List`(coordinates: List<Number>) {
            // Given
            val expected = Point(coordinates)

            // When
            val result = Point(1.0, 1.0)

            // Then
            assertThat(result).isEqualTo(expected)
        }

        private fun getPointInputFromList(): Stream<List<Number>> =
            Stream.of(
                listOf(1, 1),
                listOf(1L, 1L),
                listOf(1.0, 1.0),
                listOf(1.0f, 1.0f),
            )

        @Test
        fun `Construct a Point object from RealVector`() {
            // Given
            val expected = Point(1, 1)

            // When
            val result = Point(MatrixUtils.createRealVector(doubleArrayOf(1.0, 1.0)))

            // Then
            assertThat(result).isEqualTo(expected)
        }

        @Test
        fun `randomPoint should throw IllegalArgumentException when input is negative`() {
            // Given

            // When
            val result = catchThrowable { Point.randomPoint(-1) }

            // Then
            assertThat(result)
                .isInstanceOf(IllegalArgumentException().javaClass)
                .hasMessageContaining("Dimensions must greater or equal than zero")
        }

        @Test
        fun `randomPoint should create an empty Point when Input is Zero`() {
            // Given
            val expected = Point()

            // When
            val result = Point.randomPoint(0)

            // Then
            assertThat(result).isEqualTo(expected)
        }

        @Test
        fun `randomPoint should create a Point with positive dimensions when input is greater than zero`() {
            // Given
            val inputDimensions = 2

            // When
            val result = Point.randomPoint(inputDimensions).dimensions

            // Then
            assertThat(result).isEqualTo(inputDimensions)
        }
    }

    @Nested
    inner class Properties {
        @Test
        fun `x should get the first coordinate of a Vector`() {
            // Given
            val point = Point(15.0f, 99.0f)
            val expected = 15.0f

            // When
            val result = point.x

            // Then
            assertThat(result).isEqualTo(expected)
        }

        @Test
        fun `Accessing property x should should throw IllegalArgumentException when Point has zero dimensions`() {
            // Given

            // When
            val result = catchThrowable { Point().x }

            assertThat(result)
                .isInstanceOf(IllegalArgumentException().javaClass)
                .hasMessageContaining("Point does not have enough dimensions")
        }

        @Test
        fun `y should get the second coordinate of a Vector`() {
            // Given
            val point = Point(15.0f, 99.0f)
            val expected = 99.0f

            // When
            val result = point.y

            // Then
            assertThat(result).isEqualTo(expected)
        }

        @Test
        fun `Accessing property y should should throw IllegalArgumentException when Point has only one dimension`() {
            // Given

            // When
            val result = catchThrowable { Point(1).y }

            assertThat(result)
                .isInstanceOf(IllegalArgumentException().javaClass)
                .hasMessageContaining("Point does not have enough dimensions")
        }

        @Test
        fun `z should get the second coordinate of a Vector`() {
            // Given
            val point = Point(15.0f, 99.0f, 255.0f)
            val expected = 255.0f

            // When
            val result = point.z

            // Then
            assertThat(result).isEqualTo(expected)
        }

        @Test
        fun `Accessing property z should should throw IllegalArgumentException when Point has only two dimensions`() {
            // Given

            // When
            val result = catchThrowable { Point(1, 2).z }

            assertThat(result)
                .isInstanceOf(IllegalArgumentException().javaClass)
                .hasMessageContaining("Point does not have enough dimensions")
        }
    }

    @Nested
    inner class Arithmetic {
        @Test
        fun `Minus operator should subtract two Points element wise`() {
            // Given
            val point = Point(15.0f, 15.0f)
            val anotherPoint = Point(5.0f, 5.0f)
            val expected = Point(10.0f, 10.0f)

            // When
            val result = point - anotherPoint

            // Then
            assertThat(result).isEqualTo(expected)
        }

        @Test
        fun `Minus operator should subtract a scalar from each Point element`() {
            // Given
            val point = Point(15.0f, 15.0f)
            val scalar = 5
            val expected = Point(10, 10)

            // When
            val result = point - scalar

            // Then
            assertThat(result).isEqualTo(expected)
        }

        @Test
        fun `Plus operator should add two Points element wise`() {
            // Given
            val point = Point(15.0f, 15.0f)
            val anotherPoint = Point(5.0f, 5.0f)
            val expected = Point(20.0f, 20.0f)

            // When
            val result = point + anotherPoint

            // Then
            assertThat(result).isEqualTo(expected)
        }

        @Test
        fun `Plus operator should add a scalar to each Point element`() {
            // Given
            val point = Point(15.0f, 15.0f)
            val scalar = 5
            val expected = Point(20, 20)

            // When
            val result = point + scalar

            // Then
            assertThat(result).isEqualTo(expected)
        }

        @Test
        fun `Times operator should multiply two Points element wise`() {
            // Given
            val point = Point(1, 1)
            val anotherPoint = Point(5.0f, 5.0f)
            val expected = Point(5, 5)

            // When
            val result = point * anotherPoint

            // Then
            assertThat(result).isEqualTo(expected)
        }

        @Test
        fun `Times operator should throw IllegalArgumentException when points do not have the same dimensions`() {
            // Given
            val point = Point(1, 1, 1)
            val anotherPoint = Point(5.0f, 5.0f)

            // When
            val result = catchThrowable { point * anotherPoint }

            // Then
            assertThat(result)
                .isInstanceOf(IllegalArgumentException().javaClass)
                .hasMessageContaining("Points dimension do not match")
        }

        @Test
        fun `Times operator should multiply a scalar with each element of Point`() {
            // Given
            val point = Point(1.0f, 1.0f)
            val scalar = 5
            val expected = Point(5, 5)

            // When
            val result = point * scalar

            // Then
            assertThat(result).isEqualTo(expected)
        }

        @Test
        fun `Div operator should divide two Points element wise`() {
            // Given
            val point = Point(5, 5)
            val anotherPoint = Point(5.0f, 5.0f)
            val expected = Point(1, 1)

            // When
            val result = point / anotherPoint

            // Then
            assertThat(result).isEqualTo(expected)
        }

        @Test
        fun `Div operator each element of Point a point by a scalar if right element is a Number`() {
            // Given
            val point = Point(5.0f, 5.0f)
            val scalar = 5
            val expected = Point(1, 1)

            // When
            val result = point / scalar

            // Then
            assertThat(result).isEqualTo(expected)
        }

        @Test
        fun `dot should compute the dot product between two vectors`() {
            // Given
            val point = Point(1, 0)
            val otherPoint = Point(0, 1)

            // When
            val result = point.dot(otherPoint)

            // Then
            assertThat(result).isEqualTo(0.0f)
        }
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    inner class Algebra {
        @Test
        fun `normalize should convert a given Point to a vector with length 1`() {
            // Given
            val point = Point(1, 2, 3, 4)

            // When
            val result = point.normalize().magnitude()

            // Then
            assertThat(result).isEqualTo(1.0f)
        }

        @Test
        fun `normalize should convert a Point with dimension 1 to a vector with length 1`() {
            // Given
            val point = Point(5)

            // When
            val result = point.normalize().magnitude()

            // Then
            assertThat(result).isEqualTo(1.0f)
        }

        @Test
        fun `normalize should should throw MathArithmeticException when Point is empty`() {
            // Given

            // When
            val result = catchThrowable { Point().normalize() }
            // Then
            assertThat(result).isInstanceOf(MathArithmeticException().javaClass).hasMessageContaining("zero norm")
        }
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    inner class Geometry {
        @Test
        fun `translate should translate a given vector in all dimension`() {
            // Given
            val x = 10
            val y = 10
            val point = Point(x, y)
            val xTranslation = 100
            val yTranslation = 100
            val expectedResult = Point(x + xTranslation, y + yTranslation)

            // When
            val result = point.translate(xTranslation, yTranslation)

            // When
            assertThat(result).isEqualTo(expectedResult)
        }

        @Test
        fun `translate should throw IllegalArgumentException when input doesn't match dimensions and is not a single number`() {
            // Given
            val point = Point(10, 10, 10)

            // When
            val result = catchThrowable { point.translate(10, 10) }

            assertThat(result)
                .isInstanceOf(IllegalArgumentException().javaClass)
                .hasMessageContaining("Translation does not match point dimension")
        }

        @Test
        fun `translate should translate all coordinates by the same factor when input is a single number`() {
            // Given
            val x = 10
            val y = 10
            val point = Point(x, y)
            val translation = 100
            val expectedResult = Point(x + translation, y + translation)

            // When
            val result = point.translate(translation)

            // When
            assertThat(result).isEqualTo(expectedResult)
        }

        @ParameterizedTest(name = "TestPoint: {0}")
        @MethodSource("getArgsForRotate")
        fun `rotate should rotate a Point clock wise by degree`(
            angle: Number,
            expected: Point,
        ) {
            // Given
            val point = Point(1, 0)
//            val angle = PApplet.radians(90f)
            // When
            val result = point.rotate(angle)

            // Then
            assertThat(result.toDoubleArray()).containsSequence(expected.toDoubleArray(), withPrecision(0.01))
        }

        private fun getArgsForRotate(): Stream<Arguments> =
            Stream.of(
                Arguments.of(90, Point(0, -1)),
                Arguments.of(180, Point(-1, 0)),
                Arguments.of(270, Point(0, 1)),
                Arguments.of(360, Point(1, 0)),
            )

        @ParameterizedTest(name = "TestPoint: {0}")
        @MethodSource("getInvalidPointsForRotate")
        fun `rotate should throw IllegalArgumentException when Point dimensions is not equal to 2`(point: Point) {
            // When

            val result = catchThrowable { point.rotate(90) }
            // Then
            assertThat(result)
                .isInstanceOf(IllegalArgumentException().javaClass)
                .hasMessageContaining("Invalid dimensions. Points must have dimension equal to 2 for this method")
        }

        private fun getInvalidPointsForRotate(): Stream<Point> = Stream.of(Point(1), Point(1, 2, 3))

        @ParameterizedTest(name = "TestPoint: {0}")
        @MethodSource("getArgsForRotateZ")
        fun `rotateZ should rotate a Point clock wise by degree`(
            angle: Number,
            expected: Point,
        ) {
            // Given
            val point = Point(1, 0, 0)
//            val angle = PApplet.radians(90f)
            // When
            val result = point.rotateZ(angle)

            // Then
            assertThat(result.toDoubleArray()).containsSequence(expected.toDoubleArray(), withPrecision(0.01))
        }

        private fun getArgsForRotateZ(): Stream<Arguments> =
            Stream.of(
                Arguments.of(90, Point(0, -1, 0)),
                Arguments.of(180, Point(-1, 0, 0)),
                Arguments.of(270, Point(0, 1, 0)),
                Arguments.of(360, Point(1, 0, 0)),
            )

        @ParameterizedTest(name = "TestPoint: {0}")
        @MethodSource("getInvalidPointsForRotationsInThreeDimensions")
        fun `rotateZ should throw IllegalArgumentException when Point dimensions is less than 3`(point: Point) {
            // When

            val result = catchThrowable { point.rotateZ(90) }
            // Then
            assertThat(result)
                .isInstanceOf(IllegalArgumentException().javaClass)
                .hasMessageContaining("Invalid dimensions. Point must have at least 3 dimensions for this method")
        }

        @ParameterizedTest(name = "TestPoint: {0}")
        @MethodSource("getArgsForRotateY")
        fun `rotateY should rotate a Point clock wise by degree`(
            angle: Number,
            expected: Point,
        ) {
            // Given
            val point = Point(1, 0, 0)
//            val angle = PApplet.radians(90f)
            // When
            val result = point.rotateY(angle)

            // Then
            assertThat(result.toDoubleArray()).containsSequence(expected.toDoubleArray(), withPrecision(0.01))
        }

        private fun getArgsForRotateY(): Stream<Arguments> =
            Stream.of(
                Arguments.of(90, Point(0, 0, -1)),
                Arguments.of(180, Point(-1, 0, 0)),
                Arguments.of(270, Point(0, 0, 1)),
                Arguments.of(360, Point(1, 0, 0)),
            )

        @ParameterizedTest(name = "TestPoint: {0}")
        @MethodSource("getInvalidPointsForRotationsInThreeDimensions")
        fun `rotateY should throw IllegalArgumentException when Point dimensions is less than 3`(point: Point) {
            // When

            val result = catchThrowable { point.rotateY(90) }
            // Then
            assertThat(result)
                .isInstanceOf(IllegalArgumentException().javaClass)
                .hasMessageContaining("Invalid dimensions. Point must have at least 3 dimensions for this method")
        }

        @ParameterizedTest(name = "TestPoint: {0}")
        @MethodSource("getArgsForRotateX")
        fun `rotateX should rotate a Point clock wise by degree`(
            angle: Number,
            expected: Point,
        ) {
            // Given
            val point = Point(0, 0, 1)
//            val angle = PApplet.radians(90f)
            // When
            val result = point.rotateX(angle)

            // Then
            assertThat(result.toDoubleArray()).containsSequence(expected.toDoubleArray(), withPrecision(0.01))
        }

        private fun getArgsForRotateX(): Stream<Arguments> =
            Stream.of(
                Arguments.of(90, Point(0, 1, 0)),
                Arguments.of(180, Point(0, 0, -1)),
                Arguments.of(270, Point(0, -1, 0)),
                Arguments.of(360, Point(0, 0, 1)),
            )

        private fun getInvalidPointsForRotationsInThreeDimensions(): Stream<Point> =
            Stream.of(Point(), Point(1), Point(1, 2))

        @Test
        fun `negate should create Vector pointing in the opposite direction with the same length`() {
            // Given
            val point = Point(2, 5)
            val expected = Point(-2, -5)
            // When
            val result = point.negate()

            // Then
            assertThat(result).isEqualTo(expected)
        }

        @Test
        fun `distance should compute the euclidean distance between two points`() {
            // Given
            val point = Point(2, 20)
            val otherPoint = Point(-4, 60)
            val expected = 40.4475f
            // When
            val result = point.distance(otherPoint)

            // Then
            assertThat(result).isEqualTo(expected)
        }
    }

    @Nested
    inner class Utils {
        @Test
        fun `swapPointEntries should swap to entries of a given Point`() {
            // Given
            val point = Point(1, 2, 3, 4)
            val expected = Point(4, 2, 3, 1)

            // When
            val result = point.swapEntries(0, 3)

            // Then
            assertThat(result).isEqualTo(expected)
        }

        @Test
        fun `copy should return a copy of a given Point having the same attributes but different memory address`() {
            // Given
            val point = Point(2, 2)

            // When
            val result = point.copy()

            // Then
            assertThat(result).isEqualTo(point).isNotEqualTo(result === point)
        }

        @Test
        fun `newWithLength should return new vector with the provided length pointing in the same direction`() {
            // Given
            val point = Point(2, 2)
            val newLength = 10f

            // When
            val result = point.newWithLength(newLength).magnitude()

            // Then
            assertThat(result).isEqualTo(newLength)
        }

        @Test
        fun `limit should return new vector with provided length in the same direction when magnitude is larger`() {
            // Given
            val point = Point(990, 990)
            val expectedLimit = 10f

            // When
            val result = point.limit(expectedLimit).magnitude()

            // Then
            assertThat(result).isEqualTo(expectedLimit)
        }

        @Test
        fun `limit should return a copy ot the original vector when limit is larger than the magnitude of the vector`() {
            // Given
            val point = Point(1, 1)
            val expectedLimit = 99

            // When
            val result = point.limit(expectedLimit)

            // Then
            assertThat(result).isEqualTo(point)
        }

        @Test
        fun `limit should throw IllegalArgumentException when limit is a negative number`() {
            // Given
            val point = Point(1, 1)
            val expectedLimit = -1

            // When
            val result = catchThrowable { point.limit(expectedLimit) }

            // Then
            assertThat(result)
                .isInstanceOf(IllegalArgumentException().javaClass)
                .hasMessageContaining("Length must be a positive number")
        }
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    inner class Conversions {
        @Test
        fun toRealVectorShouldConvertAPointToARealVector() {
            // Given
            val point = Point(1, 2, 3)

            val expected = MatrixUtils.createRealVector(doubleArrayOf(1.0, 2.0, 3.0))

            // When
            val result = point.toRealVector()

            // Then
            assertThat(result).isEqualTo(expected)
        }

        @Test
        fun `toDoubleArray should export Point coordinates to a DoubleArray`() {
            // Given
            val point = Point(1, 2, 3)

            val expected = doubleArrayOf(1.0, 2.0, 3.0)

            // When
            val result = point.toDoubleArray()

            // Then
            assertThat(result).isEqualTo(expected)
        }

        @Test
        fun `toFloatArray should convert a Point to a FloatArray`() {
            // Given
            val point = Point(1, 2, 3)

            val expected = floatArrayOf(1f, 2f, 3f)

            // When
            val result = point.toFloatArray()

            // Then
            assertThat(result).isEqualTo(expected)
        }

        @Test
        fun `toDoubleList should convert a Point to a List of Double`() {
            // Given
            val point = Point(1, 2, 3)

            val expected = listOf(1.0, 2.0, 3.0)

            // When
            val result = point.toDoubleList()

            // Then
            assertThat(result).isEqualTo(expected)
        }

        @Test
        fun `toFloatList should convert a Point to a List of Float`() {
            // Given
            val point = Point(1, 2, 3)

            val expected = listOf(1.0f, 2.0f, 3.0f)

            // When
            val result = point.toFloatList()

            // Then
            assertThat(result).isEqualTo(expected)
        }

        @Test
        fun `toPVector should convert a 2 Dimensional Point to a PVector with non Zero x y coordinates`() {
            // Given
            val point = Point(1, 2)

            val expected = PVector(1.0f, 2.0f)

            // When
            val result = point.toPVector()

            // Then
            assertThat(result).isEqualTo(expected)
        }

        @ParameterizedTest(name = "TestPoint: {0}")
        @MethodSource("getNonConvertiblePoints")
        fun `toPVector should throw IllegalArgumentException when Vector has dimension is not 2 or 3`(point: Point) {
            // When

            val result = catchThrowable { point.toPVector() }
            // Then
            assertThat(result)
                .isInstanceOf(IllegalArgumentException().javaClass)
                .hasMessageContaining("Can't convert Point to PVector - Invalid number of dimensions")
        }

        private fun getNonConvertiblePoints(): Stream<Point> = Stream.of(Point(1), Point(1, 2, 3, 4))
    }
}
