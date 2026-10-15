package goodies.geometry

import goodies.support.FLOAT_EPSILON
import goodies.support.assertThat
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
        @MethodSource("constructFromNumbersCases")
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

        private fun constructFromNumbersCases(): Stream<Arguments> =
            Stream.of(
                Arguments.of(1, 1),
                Arguments.of(1L, 1L),
                Arguments.of(1.0, 1.0),
                Arguments.of(1.0f, 1.0f),
            )

        @ParameterizedTest
        @MethodSource("constructFromListCases")
        fun `Construct a Point object from A List`(coordinates: List<Number>) {
            // Given
            val expected = Point(coordinates)

            // When
            val result = Point(1.0, 1.0)

            // Then
            assertThat(result).isEqualTo(expected)
        }

        private fun constructFromListCases(): Stream<List<Number>> =
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
                .hasMessageContaining("randomPoint needs 0 or more dimensions, got -1")
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
                .hasMessageContaining("x needs 1 or more dimensions, got 0")
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
                .hasMessageContaining("y needs 2 or more dimensions, got 1")
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
                .hasMessageContaining("z needs 3 or more dimensions, got 2")
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
                .hasMessageContaining("Points need equal dimensions, got 3 and 2")
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
        fun `translate should throw IllegalArgumentException when dimensions differ and input is not a scalar`() {
            // Given
            val point = Point(10, 10, 10)

            // When
            val result = catchThrowable { point.translate(10, 10) }

            assertThat(result)
                .isInstanceOf(IllegalArgumentException().javaClass)
                .hasMessageContaining("translate needs 3 values, got 2")
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
        @MethodSource("rotateCases")
        fun `rotate should rotate a Point clock wise by degree`(
            angle: Number,
            expected: Point,
        ) {
            // Given
            val point = Point(1, 0)
            // When
            val result = point.rotate(angle)

            // Then
            assertThat(result.toDoubleArray()).containsSequence(expected.toDoubleArray(), withPrecision(0.01))
        }

        private fun rotateCases(): Stream<Arguments> =
            Stream.of(
                Arguments.of(90, Point(0, -1)),
                Arguments.of(180, Point(-1, 0)),
                Arguments.of(270, Point(0, 1)),
                Arguments.of(360, Point(1, 0)),
            )

        @ParameterizedTest(name = "TestPoint: {0}")
        @MethodSource("rotateInvalidDimensionsCases")
        fun `rotate should throw IllegalArgumentException when Point dimensions is not equal to 2`(point: Point) {
            // When

            val result = catchThrowable { point.rotate(90) }
            // Then
            assertThat(result)
                .isInstanceOf(IllegalArgumentException().javaClass)
                .hasMessageContaining("rotate needs 2 dimensions")
        }

        private fun rotateInvalidDimensionsCases(): Stream<Point> = Stream.of(Point(1), Point(1, 2, 3))

        @ParameterizedTest(name = "TestPoint: {0}")
        @MethodSource("rotateZCases")
        fun `rotateZ should rotate a Point clock wise by degree`(
            angle: Number,
            expected: Point,
        ) {
            // Given
            val point = Point(1, 0, 0)
            // When
            val result = point.rotateZ(angle)

            // Then
            assertThat(result.toDoubleArray()).containsSequence(expected.toDoubleArray(), withPrecision(0.01))
        }

        private fun rotateZCases(): Stream<Arguments> =
            Stream.of(
                Arguments.of(90, Point(0, -1, 0)),
                Arguments.of(180, Point(-1, 0, 0)),
                Arguments.of(270, Point(0, 1, 0)),
                Arguments.of(360, Point(1, 0, 0)),
            )

        @ParameterizedTest(name = "TestPoint: {0}")
        @MethodSource("rotateZInvalidDimensionsCases")
        fun `rotateZ should throw IllegalArgumentException when Point dimensions is less than 3`(point: Point) {
            // When

            val result = catchThrowable { point.rotateZ(90) }
            // Then
            assertThat(result)
                .isInstanceOf(IllegalArgumentException().javaClass)
                .hasMessageContaining("rotateZ needs 3 or more dimensions")
        }

        private fun rotateZInvalidDimensionsCases(): Stream<Point> = Stream.of(Point(), Point(1), Point(1, 2))

        @ParameterizedTest(name = "TestPoint: {0}")
        @MethodSource("rotateYCases")
        fun `rotateY should rotate a Point clock wise by degree`(
            angle: Number,
            expected: Point,
        ) {
            // Given
            val point = Point(1, 0, 0)
            // When
            val result = point.rotateY(angle)

            // Then
            assertThat(result.toDoubleArray()).containsSequence(expected.toDoubleArray(), withPrecision(0.01))
        }

        private fun rotateYCases(): Stream<Arguments> =
            Stream.of(
                Arguments.of(90, Point(0, 0, -1)),
                Arguments.of(180, Point(-1, 0, 0)),
                Arguments.of(270, Point(0, 0, 1)),
                Arguments.of(360, Point(1, 0, 0)),
            )

        @ParameterizedTest(name = "TestPoint: {0}")
        @MethodSource("rotateYInvalidDimensionsCases")
        fun `rotateY should throw IllegalArgumentException when Point dimensions is less than 3`(point: Point) {
            // When

            val result = catchThrowable { point.rotateY(90) }
            // Then
            assertThat(result)
                .isInstanceOf(IllegalArgumentException().javaClass)
                .hasMessageContaining("rotateY needs 3 or more dimensions")
        }

        private fun rotateYInvalidDimensionsCases(): Stream<Point> = Stream.of(Point(), Point(1), Point(1, 2))

        @ParameterizedTest(name = "TestPoint: {0}")
        @MethodSource("rotateXCases")
        fun `rotateX should rotate a Point clock wise by degree`(
            angle: Number,
            expected: Point,
        ) {
            // Given
            val point = Point(0, 0, 1)
            // When
            val result = point.rotateX(angle)

            // Then
            assertThat(result.toDoubleArray()).containsSequence(expected.toDoubleArray(), withPrecision(0.01))
        }

        private fun rotateXCases(): Stream<Arguments> =
            Stream.of(
                Arguments.of(90, Point(0, 1, 0)),
                Arguments.of(180, Point(0, 0, -1)),
                Arguments.of(270, Point(0, -1, 0)),
                Arguments.of(360, Point(0, 0, 1)),
            )

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

        @Test
        fun `projectDownTo3D should throw IllegalArgumentException when point has fewer than 3 dimensions`() {
            // Given
            val point = Point(1, 2)

            // When
            val result = catchThrowable { point.projectDownTo3D() }

            // Then
            assertThat(result)
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessageContaining("projectDownTo3D needs 3 or more dimensions, got 2")
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
            val result = point.swapEntries(firstPositionOne = 0, secondPositionOne = 3)

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
        fun `limit should return a copy of the original vector when limit exceeds its magnitude`() {
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
                .hasMessageContaining("Length must not be negative, got -1")
        }

        @Test
        fun `limit should return the zero vector when limit is zero`() {
            // Given
            val point = Point(3, 4)
            val expected = Point.zeros(2)

            // When
            val result = point.limit(0)

            // Then
            assertThat(result).isEqualTo(expected)
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
        @MethodSource("toPVectorInvalidDimensionsCases")
        fun `toPVector should throw IllegalArgumentException when Vector has dimension is not 2 or 3`(point: Point) {
            // When

            val result = catchThrowable { point.toPVector() }
            // Then
            assertThat(result)
                .isInstanceOf(IllegalArgumentException().javaClass)
                .hasMessageContaining("Can't convert Point to PVector - Invalid number of dimensions")
        }

        private fun toPVectorInvalidDimensionsCases(): Stream<Point> = Stream.of(Point(1), Point(1, 2, 3, 4))
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    inner class FromAngles {
        private val r = 0.7071067811865476

        @ParameterizedTest(name = "{0} -> {1}")
        @MethodSource("oneAngleCases")
        fun `fromAngles should return the point on the unit circle when given one angle`(
            angle: Double,
            expected: Point,
        ) {
            // Given
            val precision = withPrecision(FLOAT_EPSILON)

            // When
            val result = Point.fromAngles(angle)

            // Then
            assertThat(result).isCloseTo(expected, precision)
        }

        @ParameterizedTest(name = "{0} -> {1}")
        @MethodSource("twoAnglesCases")
        fun `fromAngles should return the point on the unit sphere when given two angles`(
            angles: List<Double>,
            expected: Point,
        ) {
            // Given
            val precision = withPrecision(FLOAT_EPSILON)

            // When
            val result = Point.fromAngles(*angles.toTypedArray())

            // Then
            assertThat(result).isCloseTo(expected, precision)
        }

        @ParameterizedTest(name = "{0} -> {1}")
        @MethodSource("sixAnglesCases")
        fun `fromAngles should return the point on the unit sphere in 7 dimensions when given six angles`(
            angles: List<Double>,
            expected: Point,
        ) {
            // Given
            val precision = withPrecision(FLOAT_EPSILON)

            // When
            val result = Point.fromAngles(*angles.toTypedArray())

            // Then
            assertThat(result).isCloseTo(expected, precision)
        }

        @ParameterizedTest(name = "{0} -> {1}")
        @MethodSource("dimensionCases")
        fun `fromAngles should return a point with as many dimensions as angles plus one`(
            angles: List<Double>,
            expected: Int,
        ) {
            // Given
            val input = angles.toTypedArray()

            // When
            val result = Point.fromAngles(*input)

            // Then
            assertThat(result.dimensions).isEqualTo(expected)
        }

        @ParameterizedTest(name = "{0} -> {1}")
        @MethodSource("unitDistanceCases")
        fun `fromAngles should return a point at distance one from the origin`(
            angles: List<Double>,
            expected: Double,
        ) {
            // Given
            val input = angles.toTypedArray()

            // When
            val result = Point.fromAngles(*input)

            // Then
            assertThat(result.magnitude().toDouble()).isCloseTo(expected, withPrecision(FLOAT_EPSILON))
        }

        @Test
        fun `fromAngles should throw IllegalArgumentException when no angle is given`() {
            // Given
            val expected = "at least one angle"

            // When
            val result = catchThrowable { Point.fromAngles() }

            // Then
            assertThat(result)
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessageContaining(expected)
        }

        private fun oneAngleCases(): Stream<Arguments> =
            Stream.of(
                Arguments.of(0.0, Point(0, 1)),
                Arguments.of(45.0, Point(r, r)),
                Arguments.of(90.0, Point(1, 0)),
                Arguments.of(180.0, Point(0, -1)),
                Arguments.of(270.0, Point(-1, 0)),
                Arguments.of(-90.0, Point(-1, 0)),
                Arguments.of(450.0, Point(1, 0)),
            )

        private fun twoAnglesCases(): Stream<Arguments> =
            Stream.of(
                Arguments.of(listOf(0.0, 0.0), Point(0, 0, 1)),
                Arguments.of(listOf(0.0, 90.0), Point(0, 1, 0)),
                Arguments.of(listOf(90.0, 90.0), Point(1, 0, 0)),
                Arguments.of(listOf(45.0, 90.0), Point(r, r, 0)),
                Arguments.of(listOf(90.0, 45.0), Point(r, 0, r)),
            )

        private fun sixAnglesCases(): Stream<Arguments> =
            Stream.of(
                Arguments.of(listOf(0.0, 0.0, 0.0, 0.0, 0.0, 0.0), Point(0, 0, 0, 0, 0, 0, 1)),
                Arguments.of(listOf(90.0, 90.0, 90.0, 90.0, 90.0, 90.0), Point(1, 0, 0, 0, 0, 0, 0)),
                Arguments.of(listOf(0.0, 0.0, 0.0, 0.0, 0.0, 90.0), Point(0, 0, 0, 0, 0, 1, 0)),
                Arguments.of(listOf(90.0, 90.0, 90.0, 90.0, 90.0, 45.0), Point(r, 0, 0, 0, 0, 0, r)),
            )

        private fun dimensionCases(): Stream<Arguments> =
            Stream.of(
                Arguments.of(listOf(30.0), 2),
                Arguments.of(listOf(30.0, 60.0), 3),
                Arguments.of(listOf(10.0, 20.0, 30.0, 40.0, 50.0, 60.0), 7),
            )

        private fun unitDistanceCases(): Stream<Arguments> =
            Stream.of(
                Arguments.of(listOf(30.0), 1.0),
                Arguments.of(listOf(30.0, 60.0), 1.0),
                Arguments.of(listOf(10.0, 20.0, 30.0, 40.0, 50.0, 60.0), 1.0),
            )
    }
}
