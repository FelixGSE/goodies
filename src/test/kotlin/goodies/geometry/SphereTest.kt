package goodies.geometry

import goodies.support.assertThat
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
        @ParameterizedTest(name = "{displayName} [{index}] {0}")
        @MethodSource("constructorInvalidDimensionsCases")
        fun `Initialization of a Sphere should throw IllegalArgumentException when dimension is not equal to three`(
            center: Point,
        ) {
            // Given

            // When
            val result = catchThrowable { Sphere(center, 10) }

            // Then
            assertThat(result)
                .isInstanceOf(IllegalArgumentException().javaClass)
                .hasMessageContaining("Sphere needs a 3D center")
        }

        private fun constructorInvalidDimensionsCases(): Stream<Point> =
            Stream.of(
                Point(1, 1),
                Point(1, 2, 3, 4),
            )
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    inner class Basics {
        @ParameterizedTest(name = "{displayName} [{index}] {0}")
        @MethodSource("pointOnSurfaceZeroAzimuthCases")
        fun `pointOnSurface should compute the point on a unit sphere when azimuth is zero`(
            angles: List<Double>,
            expected: Point,
        ) {
            // Given
            val unitSphere = Sphere.unitSphere()
            val anglesAsRadians = angles.map { toRadians(it) }

            // When
            val result = unitSphere.pointOnSurface(anglesAsRadians)

            // Then
            assertThat(result).isCloseTo(expected, withPrecision(0.01))
        }

        private fun pointOnSurfaceZeroAzimuthCases(): Stream<Arguments> =
            Stream.of(
                Arguments.of(listOf(0.0, 0.0), Point(0, 0, 1)),
                Arguments.of(listOf(0.0, 45.0), Point(0, piQuarter, piQuarter)),
                Arguments.of(listOf(0.0, 90.0), Point(0, 1, 0)),
                Arguments.of(listOf(0.0, 135.0), Point(0, piQuarter, -piQuarter)),
            )

        @ParameterizedTest(name = "{displayName} [{index}] {0}")
        @MethodSource("pointOnSurfaceEasternAzimuthCases")
        fun `pointOnSurface should compute the point on a unit sphere when azimuth is 45 to 135 degrees`(
            angles: List<Double>,
            expected: Point,
        ) {
            // Given
            val unitSphere = Sphere.unitSphere()
            val anglesAsRadians = angles.map { toRadians(it) }

            // When
            val result = unitSphere.pointOnSurface(anglesAsRadians)

            // Then
            assertThat(result).isCloseTo(expected, withPrecision(0.01))
        }

        private fun pointOnSurfaceEasternAzimuthCases(): Stream<Arguments> =
            Stream.of(
                Arguments.of(listOf(45.0, 45.0), Point(0.5, 0.5, piQuarter)),
                Arguments.of(listOf(45.0, 90.0), Point(piQuarter, piQuarter, 0)),
                Arguments.of(listOf(45.0, 135.0), Point(0.5, 0.5, -piQuarter)),
                Arguments.of(listOf(90.0, 45.0), Point(piQuarter, 0, piQuarter)),
                Arguments.of(listOf(90.0, 90.0), Point(1, 0, 0)),
                Arguments.of(listOf(90.0, 135.0), Point(piQuarter, 0, -piQuarter)),
                Arguments.of(listOf(135.0, 45.0), Point(0.5, -0.5, piQuarter)),
                Arguments.of(listOf(135.0, 90.0), Point(piQuarter, -piQuarter, 0)),
                Arguments.of(listOf(135.0, 135.0), Point(0.5, -0.5, -piQuarter)),
            )

        @ParameterizedTest(name = "{displayName} [{index}] {0}")
        @MethodSource("pointOnSurfaceWesternAzimuthCases")
        fun `pointOnSurface should compute the point on a unit sphere when azimuth is 180 to 315 degrees`(
            angles: List<Double>,
            expected: Point,
        ) {
            // Given
            val unitSphere = Sphere.unitSphere()
            val anglesAsRadians = angles.map { toRadians(it) }

            // When
            val result = unitSphere.pointOnSurface(anglesAsRadians)

            // Then
            assertThat(result).isCloseTo(expected, withPrecision(0.01))
        }

        private fun pointOnSurfaceWesternAzimuthCases(): Stream<Arguments> =
            Stream.of(
                Arguments.of(listOf(180.0, 45.0), Point(0, -piQuarter, piQuarter)),
                Arguments.of(listOf(180.0, 90.0), Point(0, -1, 0)),
                Arguments.of(listOf(180.0, 135.0), Point(0, -piQuarter, -piQuarter)),
                Arguments.of(listOf(225.0, 45.0), Point(-0.5, -0.5, piQuarter)),
                Arguments.of(listOf(225.0, 90.0), Point(-piQuarter, -piQuarter, 0)),
                Arguments.of(listOf(225.0, 135.0), Point(-0.5, -0.5, -piQuarter)),
                Arguments.of(listOf(270.0, 45.0), Point(-piQuarter, 0, piQuarter)),
                Arguments.of(listOf(270.0, 90.0), Point(-1, 0, 0)),
                Arguments.of(listOf(270.0, 135.0), Point(-piQuarter, 0, -piQuarter)),
                Arguments.of(listOf(315.0, 45.0), Point(-0.5, 0.5, piQuarter)),
                Arguments.of(listOf(315.0, 90.0), Point(-piQuarter, piQuarter, 0)),
                Arguments.of(listOf(315.0, 135.0), Point(-0.5, 0.5, -piQuarter)),
            )

        @ParameterizedTest(name = "{displayName} [{index}] {0}")
        @MethodSource("pointOnSurfacePoleCases")
        fun `pointOnSurface should return a pole for any azimuth when polar angle is 0 or 180 degrees`(
            angles: List<Double>,
            expected: Point,
        ) {
            // Given
            val unitSphere = Sphere.unitSphere()
            val anglesAsRadians = angles.map { toRadians(it) }

            // When
            val result = unitSphere.pointOnSurface(anglesAsRadians)

            // Then
            assertThat(result).isCloseTo(expected, withPrecision(0.01))
        }

        private fun pointOnSurfacePoleCases(): Stream<Arguments> =
            Stream.of(
                Arguments.of(listOf(0.0, 180.0), Point(0, 0, -1)),
                Arguments.of(listOf(45.0, 0.0), Point(0, 0, 1)),
                Arguments.of(listOf(45.0, 180.0), Point(0, 0, -1)),
                Arguments.of(listOf(90.0, 0.0), Point(0, 0, 1)),
                Arguments.of(listOf(90.0, 180.0), Point(0, 0, -1)),
                Arguments.of(listOf(135.0, 0.0), Point(0, 0, 1)),
                Arguments.of(listOf(135.0, 180.0), Point(0, 0, -1)),
                Arguments.of(listOf(180.0, 0.0), Point(0, 0, 1)),
                Arguments.of(listOf(180.0, 180.0), Point(0, 0, -1)),
                Arguments.of(listOf(225.0, 0.0), Point(0, 0, 1)),
                Arguments.of(listOf(225.0, 180.0), Point(0, 0, -1)),
                Arguments.of(listOf(270.0, 0.0), Point(0, 0, 1)),
                Arguments.of(listOf(270.0, 180.0), Point(0, 0, -1)),
                Arguments.of(listOf(315.0, 0.0), Point(0, 0, 1)),
                Arguments.of(listOf(315.0, 180.0), Point(0, 0, -1)),
            )

        @ParameterizedTest(name = "{displayName} [{index}] {0}")
        @MethodSource("anglesForPointOnSurfaceZeroAzimuthCases")
        fun `anglesForPointOnSurface should compute the angles on a unit sphere when azimuth is zero`(
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

        private fun anglesForPointOnSurfaceZeroAzimuthCases(): Stream<Arguments> =
            Stream.of(
                Arguments.of(listOf(0.0, 0.0), Point(0, 0, 1)),
                Arguments.of(listOf(0.0, 45.0), Point(0, piQuarter, piQuarter)),
                Arguments.of(listOf(0.0, 90.0), Point(0, 1, 0)),
                Arguments.of(listOf(0.0, 135.0), Point(0, piQuarter, -piQuarter)),
                Arguments.of(listOf(0.0, 180.0), Point(0, 0, -1)),
            )

        @ParameterizedTest(name = "{displayName} [{index}] {0}")
        @MethodSource("anglesForPointOnSurfaceEasternAzimuthCases")
        fun `anglesForPointOnSurface should compute the angles on a unit sphere when azimuth is 45 to 135 degrees`(
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

        private fun anglesForPointOnSurfaceEasternAzimuthCases(): Stream<Arguments> =
            Stream.of(
                Arguments.of(listOf(45.0, 45.0), Point(0.5, 0.5, piQuarter)),
                Arguments.of(listOf(45.0, 90.0), Point(piQuarter, piQuarter, 0)),
                Arguments.of(listOf(45.0, 135.0), Point(0.5, 0.5, -piQuarter)),
                Arguments.of(listOf(90.0, 45.0), Point(piQuarter, 0, piQuarter)),
                Arguments.of(listOf(90.0, 90.0), Point(1, 0, 0)),
                Arguments.of(listOf(90.0, 135.0), Point(piQuarter, 0, -piQuarter)),
                Arguments.of(listOf(135.0, 45.0), Point(0.5, -0.5, piQuarter)),
                Arguments.of(listOf(135.0, 90.0), Point(piQuarter, -piQuarter, 0)),
                Arguments.of(listOf(135.0, 135.0), Point(0.5, -0.5, -piQuarter)),
            )

        @ParameterizedTest(name = "{displayName} [{index}] {0}")
        @MethodSource("anglesForPointOnSurfaceWesternAzimuthCases")
        fun `anglesForPointOnSurface should compute the angles on a unit sphere when azimuth is 180 to 315 degrees`(
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

        private fun anglesForPointOnSurfaceWesternAzimuthCases(): Stream<Arguments> =
            Stream.of(
                Arguments.of(listOf(180.0, 45.0), Point(0, -piQuarter, piQuarter)),
                Arguments.of(listOf(180.0, 90.0), Point(0, -1, 0)),
                Arguments.of(listOf(180.0, 135.0), Point(0, -piQuarter, -piQuarter)),
                Arguments.of(listOf(225.0, 45.0), Point(-0.5, -0.5, piQuarter)),
                Arguments.of(listOf(225.0, 90.0), Point(-piQuarter, -piQuarter, 0)),
                Arguments.of(listOf(225.0, 135.0), Point(-0.5, -0.5, -piQuarter)),
                Arguments.of(listOf(270.0, 45.0), Point(-piQuarter, 0, piQuarter)),
                Arguments.of(listOf(270.0, 90.0), Point(-1, 0, 0)),
                Arguments.of(listOf(270.0, 135.0), Point(-piQuarter, 0, -piQuarter)),
                Arguments.of(listOf(315.0, 45.0), Point(-0.5, 0.5, piQuarter)),
                Arguments.of(listOf(315.0, 90.0), Point(-piQuarter, piQuarter, 0)),
                Arguments.of(listOf(315.0, 135.0), Point(-0.5, 0.5, -piQuarter)),
            )
    }
}
