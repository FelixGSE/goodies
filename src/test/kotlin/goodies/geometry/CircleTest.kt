package goodies.geometry

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.lang.Math.toRadians
import java.util.stream.Stream
import kotlin.math.sqrt

internal class CircleTest {
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    inner class Construction {

        @Test
        fun `Initialization of a Circle should throw IllegalArgumentException when dimension is not equal to two`() {
            // Given
            val center = Point(1, 2, 3)

            // When
            val result = catchThrowable { Circle(center, 10) }

            // Then
            assertThat(result).isInstanceOf(IllegalArgumentException().javaClass)
                .hasMessageContaining("A circle can only have two dimensions")

        }

    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    inner class Basics {

        @ParameterizedTest
        @MethodSource("getParametersForComputationOfPointsOnSphere")
        fun `pointOnSurface should compute a defined sample of points on a unit circle`(
            angle: Double, expected: Point
        ) {
            // Given
            val unitCircle = Circle.unitCircle()
            val angleAsRadians = toRadians(angle)
            // When
            val result = unitCircle.pointOnSurface(angleAsRadians)

            // Then
            assertThat(result.toDoubleArray()).containsSequence(expected.toDoubleArray(), withPrecision(0.01))

        }

        @ParameterizedTest
        @MethodSource("getParametersForComputationOfPointsOnSphere")
        fun `anglesForPointOnSurface should compute the angle for a point on a unit circle`(
            angle: Double, point: Point
        ) {
            // Given
            val unitCircle = Circle.unitCircle()
            val angleAsRadians = toRadians(angle)

            // When
            val result = unitCircle.anglesForPointOnSurface(point)[0]

            // Then
            assertThat(result).isEqualTo(angleAsRadians.toFloat(), withPrecision(0.0001f))

        }

        private fun getParametersForComputationOfPointsOnSphere(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(0, Point(0, 1)),
                Arguments.of(45, Point(sqrt(2.0) / 2, sqrt(2.0) / 2)),
                Arguments.of(90, Point(1, 0)),
                Arguments.of(135, Point(sqrt(2.0) / 2, -sqrt(2.0) / 2)),
                Arguments.of(180, Point(0, -1)),
                Arguments.of(225, Point(-sqrt(2.0) / 2, -sqrt(2.0) / 2)),
                Arguments.of(270, Point(-1, 0)),
                Arguments.of(315, Point(-sqrt(2.0) / 2, sqrt(2.0) / 2)),
            )
        }

    }
}
