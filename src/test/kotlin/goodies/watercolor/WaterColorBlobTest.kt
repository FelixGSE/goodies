package goodies.watercolor

import goodies.geometry.Point
import goodies.geometry.Polygon
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

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
            blob.createDeformedPolygons(3, 2)

            // Then
            assertThat(blob.deformedPolygons).hasSize(3)
        }

        @Test
        fun `deformed polygons should have more vertices than base`() {
            // Given
            val blob = WaterColorBlob(square)

            // When
            blob.createDeformedPolygons(1, 3)

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
        fun `deformed polygon should not equal base polygon`() {
            // Given / When
            val deformed = WaterColorBlob.deform(square, 2)

            // Then
            assertThat(deformed).isNotEqualTo(square)
        }
    }
}
