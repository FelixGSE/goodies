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
        fun `should draw point on unit sphere with length 1`() {
            // Given / When
            val point = SphereSampler.drawPointOnUnitSphere()
            val length = SphereSampler.lengthOfVector(point)

            // Then
            assertThat(length).isCloseTo(1.0, offset(0.0001))
        }

        @Test
        fun `should produce 3D vector`() {
            // Given / When
            val point = SphereSampler.drawPointOnUnitSphere()

            // Then
            assertThat(point.dimension).isEqualTo(3)
        }
    }

    @Nested
    inner class UnitNSphere {
        @Test
        fun `should draw point on N-sphere with correct dimensionality`() {
            // Given
            val dimensions = 5

            // When
            val point = SphereSampler.drawPointOnUnitNSphere(dimensions)

            // Then
            assertThat(point.dimension).isEqualTo(dimensions)
        }

        @Test
        fun `should draw point on N-sphere with unit length`() {
            // Given / When
            val point = SphereSampler.drawPointOnUnitNSphere(4)
            val length = point.norm

            // Then
            assertThat(length).isCloseTo(1.0, offset(0.0001))
        }

        @Test
        fun `should draw point on 3-sphere with unit length`() {
            // Given / When
            val point = SphereSampler.drawPointOnUnitNSphere(3)
            val length = SphereSampler.lengthOfVector(point)

            // Then
            assertThat(length).isCloseTo(1.0, offset(0.0001))
        }
    }
}
