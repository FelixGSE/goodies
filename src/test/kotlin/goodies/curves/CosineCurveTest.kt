package goodies.curves

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.Test

class CosineCurveTest {
    @Test
    fun `cosineCurve should create a cosine curve when input is within valid range`() {
        // Given
        val xStart = 0.0
        val xEnd = 4.0
        val stepSize = 0.1
        val amplitude = 2.0
        val ordinaryFrequency = 2.0
        val phase = 0.0
        val shift = 1.0

        // When
        val result =
            cosineCurve(
                xStart = xStart,
                xEnd = xEnd,
                stepSize = stepSize,
                amplitude = amplitude,
                ordinaryFrequency = ordinaryFrequency,
                phase = phase,
                shift = shift,
            )

        // Then
        assertThat(result).hasSize(40)
    }

    @Test
    fun `cosineCurve should throw an IllegalArgumentException when xStart is greater than xEnd`() {
        // Given
        val xStart = 10.0
        val xEnd = 5.0

        // When
        val thrown = catchThrowable { cosineCurve(xStart, xEnd) }

        // Then
        assertThat(thrown)
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("xStart must be less than xEnd")
    }

    @Test
    fun `cosineCurve should throw an IllegalArgumentException when stepSize is non-positive`() {
        // Given
        val xStart = 0.0
        val xEnd = 5.0
        val stepSize = 0.0 // Invalid stepSize
        val amplitude = 1.0
        val ordinaryFrequency = 1.0
        val phase = 0.0
        val shift = 0.0

        // When
        val thrown =
            catchThrowable {
                cosineCurve(
                    xStart = xStart,
                    xEnd = xEnd,
                    stepSize = stepSize,
                    amplitude = amplitude,
                    ordinaryFrequency = ordinaryFrequency,
                    phase = phase,
                    shift = shift,
                )
            }

        // Then
        assertThat(thrown)
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("stepSize must be greater than 0")
    }
}
