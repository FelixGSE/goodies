package goodies.chaos

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.data.Offset.offset
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import processing.core.PApplet

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DeJongAttractorTest {

    @Nested
    inner class Construction {

        @Test
        fun `should create attractor with x and y at zero by default`() {
            // Given / When
            val attractor = DeJongAttractor(1f, 2f, 3f, 4f)

            // Then
            assertThat(attractor.x).isEqualTo(0f)
            assertThat(attractor.y).isEqualTo(0f)
            assertThat(attractor.a).isEqualTo(1f)
            assertThat(attractor.b).isEqualTo(2f)
            assertThat(attractor.c).isEqualTo(3f)
            assertThat(attractor.d).isEqualTo(4f)
        }

        @Test
        fun `should create attractor with explicit x and y`() {
            // Given / When
            val attractor = DeJongAttractor(0.5f, 0.5f, 1f, 2f, 3f, 4f)

            // Then
            assertThat(attractor.x).isEqualTo(0.5f)
            assertThat(attractor.y).isEqualTo(0.5f)
        }
    }

    @Nested
    inner class Iteration {

        @Test
        fun `next should compute new x and y using De Jong equations`() {
            // Given
            val attractor = DeJongAttractor(0f, 0f, 1.4f, -2.3f, 2.4f, -2.1f)

            // When
            val next = attractor.next()

            // Then
            // x_next = sin(a * 0) - cos(b * 0) = sin(0) - cos(0) = 0 - 1 = -1
            // y_next = sin(c * 0) - cos(d * 0) = sin(0) - cos(0) = 0 - 1 = -1
            assertThat(next.x).isCloseTo(-1f, offset(0.001f))
            assertThat(next.y).isCloseTo(-1f, offset(0.001f))
        }

        @Test
        fun `next should preserve parameters a, b, c, d`() {
            // Given
            val attractor = DeJongAttractor(1.4f, -2.3f, 2.4f, -2.1f)

            // When
            val next = attractor.next()

            // Then
            assertThat(next.a).isEqualTo(1.4f)
            assertThat(next.b).isEqualTo(-2.3f)
            assertThat(next.c).isEqualTo(2.4f)
            assertThat(next.d).isEqualTo(-2.1f)
        }

        @Test
        fun `next should not modify original attractor (immutability)`() {
            // Given
            val attractor = DeJongAttractor(0f, 0f, 1.4f, -2.3f, 2.4f, -2.1f)

            // When
            attractor.next()

            // Then
            assertThat(attractor.x).isEqualTo(0f)
            assertThat(attractor.y).isEqualTo(0f)
        }

        @Test
        fun `should iterate multiple steps`() {
            // Given
            val attractor = DeJongAttractor(1.4f, -2.3f, 2.4f, -2.1f)

            // When
            val step1 = attractor.next()
            val step2 = step1.next()
            val step3 = step2.next()

            // Then - all steps should have different x,y values (attractor is chaotic)
            assertThat(step1.x).isNotEqualTo(step2.x)
            assertThat(step2.x).isNotEqualTo(step3.x)
        }

        @Test
        fun `next should return correct values for non-zero starting point`() {
            // Given
            val attractor = DeJongAttractor(1f, 1f, 2f, 2f, 2f, 2f)

            // When
            val next = attractor.next()

            // Then
            // x_next = sin(2 * 1) - cos(2 * 1) = sin(2) - cos(2)
            val expectedX = PApplet.sin(2f) - PApplet.cos(2f)
            val expectedY = PApplet.sin(2f) - PApplet.cos(2f)
            assertThat(next.x).isCloseTo(expectedX, offset(0.001f))
            assertThat(next.y).isCloseTo(expectedY, offset(0.001f))
        }
    }

    @Nested
    inner class Properties {

        @Test
        fun `location should return PVector with x and y`() {
            // Given
            val attractor = DeJongAttractor(3.5f, -2.1f, 1f, 2f, 3f, 4f)

            // When
            val loc = attractor.location

            // Then
            assertThat(loc.x).isEqualTo(3.5f)
            assertThat(loc.y).isEqualTo(-2.1f)
        }

        @Test
        fun `data class equality should work`() {
            // Given
            val a1 = DeJongAttractor(0f, 0f, 1f, 2f, 3f, 4f)
            val a2 = DeJongAttractor(0f, 0f, 1f, 2f, 3f, 4f)

            // Then
            assertThat(a1).isEqualTo(a2)
        }
    }
}
