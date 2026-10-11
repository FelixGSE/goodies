package goodies.geometry

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class NEllipsoidTest {
    @Nested
    inner class Construction {
        @Test
        fun `constructor should throw IllegalArgumentException when semi-axes and dimensions differ`() {
            // Given
            val center = Point(0, 0)
            val axes = listOf(1, 2, 3)

            // When
            val result = catchThrowable { NEllipsoid(center, axes) }

            // Then
            assertThat(result)
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessageContaining("Need 2 semi-axes, got 3")
        }
    }

    @Nested
    inner class PointOnSurface {
        @Test
        fun `pointOnSurface should throw IllegalArgumentException when the angle count is not dimensions minus one`() {
            // Given
            val ellipsoid = NEllipsoid(Point(0, 0, 0), listOf(1, 2, 3))

            // When
            val result = catchThrowable { ellipsoid.pointOnSurface(0.5) }

            // Then
            assertThat(result)
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessageContaining("Need 2 angles for 3 dimensions, got 1")
        }
    }
}
