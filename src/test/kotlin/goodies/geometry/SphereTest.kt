package goodies.geometry

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.lang.Math.toRadians
import java.util.stream.Stream
import kotlin.math.sqrt

internal class SphereTest {

    val piQuarter = sqrt(2.0) / 2

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    inner class Construction {

        @ParameterizedTest
        @MethodSource("getCentersForInvalidSphereConstructions")
        fun `Initialization of a Sphere should throw IllegalArgumentException when dimension is not equal to three`(
            center: Point,
        ) {
            // Given

            // When
            val result = catchThrowable { Sphere(center, 10) }

            // Then
            assertThat(result).isInstanceOf(IllegalArgumentException().javaClass)
                .hasMessageContaining("A sphere can only have three dimensions")
        }

        private fun getCentersForInvalidSphereConstructions(): Stream<Point> {
            return Stream.of(
                Point(1, 1),
                Point(1, 2, 3, 4),
            )
        }
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    inner class Basics {

        @ParameterizedTest
        @MethodSource("getParametersForComputationOfPolarCoordinatesToCartesianCoordinates")
        fun `pointOnSurface should compute a defined sample of points on a unit sphere`(
            angles: List<Double>,
            expected: Point,
        ) {
            // Given
            val unitSphere = Sphere.unitSphere()
            val anglesAsRadians = angles.map { toRadians(it) }
            // When
            val result = unitSphere.pointOnSurface(anglesAsRadians)

            // Then
            assertThat(result.toDoubleArray()).containsSequence(expected.toDoubleArray(), withPrecision(0.01))
        }

        private fun getParametersForComputationOfPolarCoordinatesToCartesianCoordinates(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(listOf(0.0, 0.0), Point(0, 0, 1)),
                Arguments.of(listOf(0.0, 45.0), Point(0, piQuarter, piQuarter)),
                Arguments.of(listOf(0.0, 90.0), Point(0, 1, 0)),
                Arguments.of(listOf(0.0, 135.0), Point(0, piQuarter, -piQuarter)),
//                Arguments.of(listOf(0.0, 180.0), Point(0, 0, -1)),
//                Arguments.of(listOf(45.0, 0.0), Point(0, 0, 1)),
                Arguments.of(listOf(45.0, 45.0), Point(0.5, 0.5, piQuarter)),
                Arguments.of(listOf(45.0, 90.0), Point(piQuarter, piQuarter, 0)),
                Arguments.of(listOf(45.0, 135.0), Point(0.5, 0.5, -piQuarter)),
//                Arguments.of(listOf(45.0, 180.0), Point(0, 0, -1)),
//                Arguments.of(listOf(90.0, 0.0), Point(0, 0, 1)),
                Arguments.of(listOf(90.0, 45.0), Point(piQuarter, 0, piQuarter)),
                Arguments.of(listOf(90.0, 90.0), Point(1, 0, 0)),
                Arguments.of(listOf(90.0, 135.0), Point(piQuarter, 0, -piQuarter)),
//                Arguments.of(listOf(90.0, 180.0), Point(0, 0, -1)),
//                Arguments.of(listOf(135.0, 0.0), Point(0, 0, 1)),
                Arguments.of(listOf(135.0, 45.0), Point(0.5, -0.5, piQuarter)),
                Arguments.of(listOf(135.0, 90.0), Point(piQuarter, -piQuarter, 0)),
                Arguments.of(listOf(135.0, 135.0), Point(0.5, -0.5, -piQuarter)),
//                Arguments.of(listOf(135.0, 180.0), Point(0, 0, -1)),
//                Arguments.of(listOf(180.0, 0.0), Point(0, 0, 1)),
                Arguments.of(listOf(180.0, 45.0), Point(0, -piQuarter, piQuarter)),
                Arguments.of(listOf(180.0, 90.0), Point(0, -1, 0)),
                Arguments.of(listOf(180.0, 135.0), Point(0, -piQuarter, -piQuarter)),
//                Arguments.of(listOf(180.0, 180.0), Point(0, 0, -1)),
//                Arguments.of(listOf(225.0, 0.0), Point(0, 0, 1)),
                Arguments.of(listOf(225.0, 45.0), Point(-0.5, -0.5, piQuarter)),
                Arguments.of(listOf(225.0, 90.0), Point(-piQuarter, -piQuarter, 0)),
                Arguments.of(listOf(225.0, 135.0), Point(-0.5, -0.5, -piQuarter)),
//                Arguments.of(listOf(225.0, 180.0), Point(0, 0, -1)),
//                Arguments.of(listOf(270.0, 0.0), Point(0, 0, 1)),
                Arguments.of(listOf(270.0, 45.0), Point(-piQuarter, 0, piQuarter)),
                Arguments.of(listOf(270.0, 90.0), Point(-1, 0, 0)),
                Arguments.of(listOf(270.0, 135.0), Point(-piQuarter, 0, -piQuarter)),
//                Arguments.of(listOf(270.0, 180.0), Point(0, 0, -1)),
//                Arguments.of(listOf(315.0, 0.0), Point(0, 0, 1)),
                Arguments.of(listOf(315.0, 45.0), Point(-0.5, 0.5, piQuarter)),
                Arguments.of(listOf(315.0, 90.0), Point(-piQuarter, piQuarter, 0)),
                Arguments.of(listOf(315.0, 135.0), Point(-0.5, 0.5, -piQuarter)),
//                Arguments.of(listOf(315.0, 180.0), Point(0, 0, -1)),
            )
        }

        @ParameterizedTest
        @MethodSource("getParametersForComputationCartesianCoordinatesToPolarCoordinates")
        fun `anglesForPointOnSurface should compute the angles for a point on a unit sphere`(
            angle: List<Double>,
            point: Point,
        ) {
            // Given
            val unitCircle = Sphere.unitSphere()
            val angleAsRadians = angle.map { toRadians(it).toDouble() }.toDoubleArray()

            // When
            val result = unitCircle.anglesForPointOnSurface(point)

            // Then
            assertThat(result.map { it.toDouble() }.toDoubleArray()).containsSequence(
                angleAsRadians,
                withPrecision(0.01),
            )
        }

        private fun getParametersForComputationCartesianCoordinatesToPolarCoordinates(): Stream<Arguments> {
            return Stream.of(
                Arguments.of(listOf(0.0, 0.0), Point(0, 0, 1)),
                Arguments.of(listOf(0.0, 45.0), Point(0, piQuarter, piQuarter)),
                Arguments.of(listOf(0.0, 90.0), Point(0, 1, 0)),
                Arguments.of(listOf(0.0, 135.0), Point(0, piQuarter, -piQuarter)),
                Arguments.of(listOf(0.0, 0.0), Point(0, 0, -1)),
                Arguments.of(listOf(0.0, 0.0), Point(0, 0, 1)),
                Arguments.of(listOf(45.0, 45.0), Point(0.5, 0.5, piQuarter)),
                Arguments.of(listOf(45.0, 90.0), Point(piQuarter, piQuarter, 0)),
                Arguments.of(listOf(45.0, 135.0), Point(0.5, 0.5, -piQuarter)),
                Arguments.of(listOf(45.0, 180.0), Point(0, 0, -1)),
                Arguments.of(listOf(90.0, 0.0), Point(0, 0, 1)),
                Arguments.of(listOf(90.0, 45.0), Point(piQuarter, 0, piQuarter)),
                Arguments.of(listOf(90.0, 90.0), Point(1, 0, 0)),
                Arguments.of(listOf(90.0, 135.0), Point(piQuarter, 0, -piQuarter)),
                Arguments.of(listOf(90.0, 180.0), Point(0, 0, -1)),
                Arguments.of(listOf(135.0, 0.0), Point(0, 0, 1)),
                Arguments.of(listOf(135.0, 45.0), Point(0.5, -0.5, piQuarter)),
                Arguments.of(listOf(135.0, 90.0), Point(piQuarter, -piQuarter, 0)),
                Arguments.of(listOf(135.0, 135.0), Point(0.5, -0.5, -piQuarter)),
                Arguments.of(listOf(135.0, 180.0), Point(0, 0, -1)),
                Arguments.of(listOf(180.0, 0.0), Point(0, 0, 1)),
                Arguments.of(listOf(180.0, 45.0), Point(0, -piQuarter, piQuarter)),
                Arguments.of(listOf(180.0, 90.0), Point(0, -1, 0)),
                Arguments.of(listOf(180.0, 135.0), Point(0, -piQuarter, -piQuarter)),
                Arguments.of(listOf(180.0, 180.0), Point(0, 0, -1)),
                Arguments.of(listOf(225.0, 0.0), Point(0, 0, 1)),
                Arguments.of(listOf(225.0, 45.0), Point(-0.5, -0.5, piQuarter)),
                Arguments.of(listOf(225.0, 90.0), Point(-piQuarter, -piQuarter, 0)),
                Arguments.of(listOf(225.0, 135.0), Point(-0.5, -0.5, -piQuarter)),
                Arguments.of(listOf(225.0, 180.0), Point(0, 0, -1)),
                Arguments.of(listOf(270.0, 0.0), Point(0, 0, 1)),
                Arguments.of(listOf(270.0, 45.0), Point(-piQuarter, 0, piQuarter)),
                Arguments.of(listOf(270.0, 90.0), Point(-1, 0, 0)),
                Arguments.of(listOf(270.0, 135.0), Point(-piQuarter, 0, -piQuarter)),
                Arguments.of(listOf(270.0, 180.0), Point(0, 0, -1)),
                Arguments.of(listOf(315.0, 0.0), Point(0, 0, 1)),
                Arguments.of(listOf(315.0, 45.0), Point(-0.5, 0.5, piQuarter)),
                Arguments.of(listOf(315.0, 90.0), Point(-piQuarter, piQuarter, 0)),
                Arguments.of(listOf(315.0, 135.0), Point(-0.5, 0.5, -piQuarter)),
                Arguments.of(listOf(315.0, 180.0), Point(0, 0, -1)),
            )
        }
    }
}
