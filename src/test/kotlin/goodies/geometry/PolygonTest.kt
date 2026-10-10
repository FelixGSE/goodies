import extensions.modules.shapes.Line
import goodies.geometry.Point
import goodies.geometry.Polygon
import org.assertj.core.api.Assertions
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
            val vertexList = listOf(
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
        fun `edges should close the polygon with the last edge`() {
            // Given
            val polygon = Polygon(
                listOf(
                    Point(0.0, 0.0),
                    Point(1.0, 0.0),
                    Point(1.0, 1.0),
                ),
            )

            val expectedEdges = listOf(
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

    @Test
    fun `convexHull should return original vertices when polygon has 3 or fewer vertices`() {
        // Given
        val polygon = Polygon(
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
        val polygon = Polygon(
            listOf(
                Point(0.0, 0.0),
                Point(1.0, 0.0),
                Point(1.0, 1.0),
                Point(0.0, 1.0),
                Point(0.5, 0.5),
            ),
        )
        val expectedConvexHull = Polygon(
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

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    inner class IsConvexTests {

        @Test
        fun `isConvex should return true for a triangle`() {
            // Given
            val polygon = Polygon(
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
            val polygon = Polygon(
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
            val polygon = Polygon(
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
        fun `should return correct angles for a triangle`() {
            // Given
            val polygon = Polygon(
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
            val polygon = Polygon(
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
        fun `should return correct angles for a non-convex pentagon`() {
            // Given
            val polygon = Polygon(
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
