package goodies.sampler

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.data.Offset.offset
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SphereSamplerTest {
    @Nested
    inner class UnitSphere {
        @Test
        fun `drawPointOnUnitSphere should draw a point with length 1`() {
            // Given

            // When
            val point = SphereSampler.drawPointOnUnitSphere()

            // Then
            val length = SphereSampler.lengthOfVector(point)
            assertThat(length).isCloseTo(1.0, offset(0.0001))
        }

        @Test
        fun `drawPointOnUnitSphere should produce a 3D vector`() {
            // Given

            // When
            val point = SphereSampler.drawPointOnUnitSphere()

            // Then
            assertThat(point.dimension).isEqualTo(3)
        }
    }

    @Nested
    inner class UnitNSphere {
        @Test
        fun `drawPointOnUnitNSphere should draw a point with correct dimensionality`() {
            // Given
            val dimensions = 5

            // When
            val point = SphereSampler.drawPointOnUnitNSphere(dimensions)

            // Then
            assertThat(point.dimension).isEqualTo(dimensions)
        }

        @Test
        fun `drawPointOnUnitNSphere should draw a point with unit length when dimensions is 4`() {
            // Given

            // When
            val point = SphereSampler.drawPointOnUnitNSphere(4)

            // Then
            val length = point.norm
            assertThat(length).isCloseTo(1.0, offset(0.0001))
        }

        @Test
        fun `drawPointOnUnitNSphere should draw a point with unit length when dimensions is 3`() {
            // Given

            // When
            val point = SphereSampler.drawPointOnUnitNSphere(3)

            // Then
            val length = SphereSampler.lengthOfVector(point)
            assertThat(length).isCloseTo(1.0, offset(0.0001))
        }
    }
}
