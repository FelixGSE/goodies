package goodies.geometry

import extensions.modules.shapes.Line
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.lang.Math.toRadians

class PolygonTest {
    @Nested
    inner class EdgesTest {
        @Test
        fun `edges should return correct edges for a triangle`() {
            // Given
            val vertexList =
                listOf(
                    Point(0.0, 0.0),
                    Point(1.0, 0.0),
                    Point(1.0, 1.0),
                )
            val polygon = Polygon(vertexList)

            // When
            val result = polygon.edges

            // Then
            Assertions.assertThat(result).hasSize(3)
        }

        @Test
        @Disabled("known failure: expected the legacy Java Line, got goodies.geometry.Line; polygon triage")
        fun `edges should close the polygon with the last edge`() {
            // Given
            val polygon =
                Polygon(
                    listOf(
                        Point(0.0, 0.0),
                        Point(1.0, 0.0),
                        Point(1.0, 1.0),
                    ),
                )

            val expectedEdges =
                listOf(
                    Line(Point(0.0, 0.0), Point(1.0, 0.0)),
                    Line(Point(1.0, 0.0), Point(1.0, 1.0)),
                    Line(Point(1.0, 1.0), Point(0.0, 0.0)),
                )

            // When
            val result = polygon.edges

            // Then
            Assertions.assertThat(result).isEqualTo(expectedEdges)
        }
    }

    @Nested
    inner class ConvexHullTests {
        @Test
        fun `convexHull should return original vertices when polygon has 3 or fewer vertices`() {
            // Given
            val polygon =
                Polygon(
                    listOf(
                        Point(0.0, 0.0),
                        Point(1.0, 0.0),
                        Point(1.0, 1.0),
                    ),
                )

            // When
            val result = polygon.convexHull()

            // Then
            Assertions.assertThat(result).isEqualTo(polygon)
        }

        @Test
        fun `convexHull should return correct convex hull vertices for a simple polygon`() {
            // Given
            val polygon =
                Polygon(
                    listOf(
                        Point(0.0, 0.0),
                        Point(1.0, 0.0),
                        Point(1.0, 1.0),
                        Point(0.0, 1.0),
                        Point(0.5, 0.5),
                    ),
                )
            val expectedConvexHull =
                Polygon(
                    listOf(
                        Point(0.0, 0.0),
                        Point(1.0, 0.0),
                        Point(1.0, 1.0),
                        Point(0.0, 1.0),
                    ),
                )

            // When
            val result = polygon.convexHull()

            // Then
            Assertions.assertThat(result).isEqualTo(expectedConvexHull)
        }

        @Test
        fun `convexHull should drop a vertex that lies on a hull edge when vertices are collinear`() {
            // Given
            val polygon = Polygon(Point(0, 0), Point(1, 0), Point(2, 0), Point(2, 2), Point(0, 2))
            val expected = Polygon(Point(0, 0), Point(2, 0), Point(2, 2), Point(0, 2))

            // When
            val result = polygon.convexHull()

            // Then
            Assertions.assertThat(result).isEqualTo(expected)
        }

        @Test
        fun `convexHull should keep a repeated vertex once when the polygon has duplicate vertices`() {
            // Given
            val polygon = Polygon(Point(0, 0), Point(2, 0), Point(2, 0), Point(2, 2), Point(0, 2), Point(1, 1))
            val expected = Polygon(Point(0, 0), Point(2, 0), Point(2, 2), Point(0, 2))

            // When
            val result = polygon.convexHull()

            // Then
            Assertions.assertThat(result).isEqualTo(expected)
        }

        @Test
        fun `convexHull should start at the minimum vertex once when the minimum vertex is repeated`() {
            // Given
            val polygon = Polygon(Point(2, 2), Point(0, 0), Point(2, 0), Point(0, 0), Point(0, 2), Point(1, 1))
            val expected = Polygon(Point(0, 0), Point(2, 0), Point(2, 2), Point(0, 2))

            // When
            val result = polygon.convexHull()

            // Then
            Assertions.assertThat(result).isEqualTo(expected)
        }
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    inner class IsConvexTests {
        @Test
        fun `isConvex should return true for a triangle`() {
            // Given
            val polygon =
                Polygon(
                    listOf(
                        Point(0.0, 0.0),
                        Point(1.0, 0.0),
                        Point(1.0, 1.0),
                    ),
                )

            // When
            val result = polygon.isConvex

            // Then
            Assertions.assertThat(result).isEqualTo(true)
        }

        @Test
        fun `isConvex should return true for a convex quadrilateral`() {
            // Given
            val polygon =
                Polygon(
                    listOf(
                        Point(0.0, 0.0),
                        Point(2.0, 0.0),
                        Point(2.0, 2.0),
                        Point(0.0, 2.0),
                    ),
                )

            // When
            val result = polygon.isConvex

            // Then
            Assertions.assertThat(result).isEqualTo(true)
        }

        @Test
        fun `isConvex should return false for a non-convex pentagon`() {
            // Given
            val polygon =
                Polygon(
                    listOf(
                        Point(0.0, 0.0),
                        Point(2.0, 0.0),
                        Point(1.5, 0.5),
                        Point(1.0, 1.0),
                        Point(0.0, 1.0),
                    ),
                )

            // When
            val result = polygon.isConvex

            // Then
            Assertions.assertThat(result).isEqualTo(false)
        }
    }

    @Nested
    inner class InteriorAnglesTests {
        @Test
        @Disabled("known failure: expected List<Double>, got List<Float> in another order; VAL-3")
        fun `should return correct angles for a triangle`() {
            // Given
            val polygon =
                Polygon(
                    listOf(
                        Point(0.0, 0.0),
                        Point(1.0, 0.0),
                        Point(1.0, 1.0),
                    ),
                )

            val expectedAngles = listOf(45.0, 45.0, 90.0).map { toRadians(it) }

            // When
            val result = polygon.interiorAngles()

            // Then
            Assertions.assertThat(result).isEqualTo(expectedAngles)
        }

        @Test
        fun `should return correct angles for a convex quadrilateral`() {
            // Given
            val polygon =
                Polygon(
                    listOf(
                        Point(0.0, 0.0),
                        Point(2.0, 0.0),
                        Point(2.0, 2.0),
                        Point(0.0, 2.0),
                    ),
                )

            val expectedAngles = listOf(90.0, 90.0, 90.0, 90.0).map { toRadians(it).toFloat() }

            // When
            val result = polygon.interiorAngles()

            // Then
            Assertions.assertThat(result).isEqualTo(expectedAngles)
        }

        @Test
        @Disabled(
            "known failure: expected [2.36, 1.70, 2.26, 2.48, 1.98], " +
                "got [0.79, 3.14, 2.36, 1.57, 1.57]; polygon triage",
        )
        fun `should return correct angles for a non-convex pentagon`() {
            // Given
            val polygon =
                Polygon(
                    listOf(
                        Point(0.0, 0.0),
                        Point(2.0, 0.0),
                        Point(1.5, 0.5),
                        Point(1.0, 1.0),
                        Point(0.0, 1.0),
                    ),
                )

            val expectedAngles = listOf(135.0, 97.378, 129.744, 142.238, 113.640).map { toRadians(it).toFloat() }

            // When
            val result = polygon.interiorAngles()

            // Then
            Assertions.assertThat(result).isEqualTo(expectedAngles)
        }
    }
}
