package goodies.watercolor

import goodies.geometry.Point
import goodies.geometry.Polygon
import goodies.support.FLOAT_EPSILON
import goodies.support.assertThat
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.withPrecision
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.util.Random

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WaterColorBlobTest {
    private val square =
        Polygon(
            Point(0f, 0f),
            Point(100f, 0f),
            Point(100f, 100f),
            Point(0f, 100f),
        )

    @Nested
    inner class Construction {
        @Test
        fun `should create blob with base polygon`() {
            // Given / When
            val blob = WaterColorBlob(square)

            // Then
            assertThat(blob.basePolygon).isEqualTo(square)
            assertThat(blob.deformedPolygons).isEmpty()
        }
    }

    @Nested
    inner class Deformation {
        @Test
        fun `should create deformed polygons`() {
            // Given
            val blob = WaterColorBlob(square)

            // When
            blob.createDeformedPolygons(n = 3, depth = 2)

            // Then
            assertThat(blob.deformedPolygons).hasSize(3)
        }

        @Test
        fun `deformed polygons should have more vertices than base`() {
            // Given
            val blob = WaterColorBlob(square)

            // When
            blob.createDeformedPolygons(n = 1, depth = 3)

            // Then
            assertThat(blob.deformedPolygons[0].vertices.size)
                .isGreaterThan(square.vertices.size)
        }

        @Test
        fun `deform with higher depth should produce more vertices`() {
            // Given / When
            val low = WaterColorBlob.deform(square, 1)
            val high = WaterColorBlob.deform(square, 4)

            // Then
            assertThat(high.vertices.size).isGreaterThan(low.vertices.size)
        }

        @Test
        fun `deform should give every edge two to the power of depth vertices`() {
            // Given
            val depth = 3
            val expected = 32

            // When
            val result = WaterColorBlob.deform(square, depth)

            // Then
            assertThat(result.vertices).hasSize(expected)
        }

        @Test
        fun `deform should keep the base vertices at the start of each edge run`() {
            // Given
            val depth = 3
            val baseIndices = listOf(0, 8, 16, 24)
            val expected = square.vertices

            // When
            val result = WaterColorBlob.deform(square, depth)

            // Then
            assertThat(baseIndices.map { result.vertices[it] }).isEqualTo(expected)
        }

        @Test
        fun `deform should place the split points drawn from a seeded random`() {
            // Given
            val depth = 2
            val random = Random(42)
            val expected = seededDepthTwoVertices()

            // When
            val result = WaterColorBlob.deform(square, depth, random)

            // Then
            assertThat(result.vertices).zipSatisfy(expected) { actual, point ->
                assertThat(actual).isCloseTo(point, withPrecision(FLOAT_EPSILON))
            }
        }

        private fun seededDepthTwoVertices(): List<Point> =
            listOf(
                Point(0.0f, 0.0f),
                Point(26.11647f, 17.395475f),
                Point(81.922f, 28.817259f),
                Point(34.389088f, -2.035223f),
                Point(100.0f, 0.0f),
                Point(95.12327f, 30.053091f),
                Point(73.32323f, 63.205086f),
                Point(103.83777f, 80.601616f),
                Point(100.0f, 100.0f),
                Point(103.29025f, 97.257324f),
                Point(76.44338f, 88.81793f),
                Point(40.65124f, 103.56044f),
                Point(0.0f, 100.0f),
                Point(32.82447f, 84.21611f),
                Point(-2.716027f, 58.390476f),
                Point(-49.187428f, 77.137695f),
            )

        @Test
        fun `deform should return the base vertices when depth is zero`() {
            // Given
            val depth = 0
            val expected = square.vertices

            // When
            val result = WaterColorBlob.deform(square, depth)

            // Then
            assertThat(result.vertices).isEqualTo(expected)
        }

        @Test
        fun `deformed polygon should not equal base polygon`() {
            // Given / When
            val deformed = WaterColorBlob.deform(square, 2)

            // Then
            assertThat(deformed).isNotEqualTo(square)
        }
    }
}
