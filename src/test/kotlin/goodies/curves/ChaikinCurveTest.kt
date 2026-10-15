package goodies.curves

import goodies.geometry.Point
import goodies.geometry.Polygon
import goodies.geometry.PolygonPath
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ChaikinCurveTest {
    @Nested
    inner class ClosedPolygon {
        @Test
        fun `ChaikinCurve should produce a polygon with more vertices than a closed input`() {
            // Given
            val square =
                Polygon(
                    Point(0f, 0f),
                    Point(1f, 0f),
                    Point(1f, 1f),
                    Point(0f, 1f),
                )

            // When
            val curve = ChaikinCurve(square, 1)

            // Then
            assertThat(curve.fittedPolygon.vertices.size).isGreaterThan(square.vertices.size)
        }

        @Test
        fun `ChaikinCurve should produce more vertices when given more iterations`() {
            // Given
            val triangle =
                Polygon(
                    Point(0f, 0f),
                    Point(1f, 0f),
                    Point(0.5f, 1f),
                )

            // When
            val curve1 = ChaikinCurve(triangle, 1)
            val curve2 = ChaikinCurve(triangle, 3)

            // Then
            assertThat(curve2.fittedPolygon.vertices.size)
                .isGreaterThan(curve1.fittedPolygon.vertices.size)
        }

        @Test
        fun `fittedPolygon should be closed when the input is closed`() {
            // Given
            val square =
                Polygon(
                    Point(0f, 0f),
                    Point(1f, 0f),
                    Point(1f, 1f),
                    Point(0f, 1f),
                )

            // When
            val curve = ChaikinCurve(square, 1)

            // Then
            assertThat(curve.fittedPolygon.isClosed).isTrue()
        }

        @Test
        fun `ChaikinCurve should still produce a valid polygon when iterations are zero`() {
            // Given
            val triangle =
                Polygon(
                    Point(0f, 0f),
                    Point(1f, 0f),
                    Point(0.5f, 1f),
                )

            // When
            val curve = ChaikinCurve(triangle, 0)

            // Then
            assertThat(curve.fittedPolygon.vertices).isNotEmpty()
        }
    }

    @Nested
    inner class OpenPath {
        @Test
        fun `ChaikinCurve should produce an open path when the input is open`() {
            // Given
            val path =
                PolygonPath(
                    listOf(Point(0f, 0f), Point(1f, 0f), Point(1f, 1f), Point(0f, 1f)),
                )

            // When
            val curve = ChaikinCurve(path, 1)

            // Then
            assertThat(curve.fittedPolygon.isClosed).isFalse()
        }

        @Test
        fun `ChaikinCurve should produce more vertices than an open input path`() {
            // Given
            val path =
                PolygonPath(
                    listOf(Point(0f, 0f), Point(1f, 0f), Point(2f, 1f)),
                )

            // When
            val curve = ChaikinCurve(path, 2)

            // Then
            assertThat(curve.fittedPolygon.vertices.size).isGreaterThan(path.vertices.size)
        }
    }
}
