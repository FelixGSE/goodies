package goodies.curves

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.test.assertFailsWith

class SignCurveGeneratorTest {
    @Test
    fun `signCurve should create a sign curve when input is within valid range`() {
        // Given
        val xStart = 0.0
        val xEnd = 5.0
        val stepSize = 0.1
        val amplitude = 2.0
        val ordinaryFrequency = 1.0
        val phase = 0.0
        val shift = 1.0

        // When
        val result = signCurve(xStart, xEnd, stepSize, amplitude, ordinaryFrequency, phase, shift)

        // Then
        val expectedSize = ((xEnd - xStart) / stepSize + 1).toInt()
        assertThat(result).hasSize(expectedSize)
    }

    @Test
    fun `signCurve should create an empty list when xStart is greater than xEnd`() {
        // Given
        val xStart = 10.0
        val xEnd = 5.0

        // When & Then
        assertFailsWith<IllegalArgumentException> {
            signCurve(xStart, xEnd)
        }
    }

    @Test
    fun `signCurve should throw an IllegalArgumentException when stepSize is non-positive`() {
        // Given
        val xStart = 0.0
        val xEnd = 5.0
        val stepSize = 0.0
        val amplitude = 1.0
        val ordinaryFrequency = 1.0
        val phase = 0.0
        val shift = 0.0

        // When & Then
        assertFailsWith<IllegalArgumentException> {
            signCurve(xStart, xEnd, stepSize, amplitude, ordinaryFrequency, phase, shift)
        }
    }
}
