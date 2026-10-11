package goodies.utils

import org.apache.commons.math3.distribution.ParetoDistribution
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.withPrecision
import org.junit.jupiter.api.Test
import utils.itemPercentages
import kotlin.random.Random

internal class RandomUtilsTest {
    @Test
    fun `weightedSample should return items in proportion to their probabilities`() {
        // Given
        val rng = Random(42)
        val n = 10_000
        val tolerance = 0.02 // 4σ for n = 10 000, p = 0.5
        val options = listOf("one", "two", "three")
        val probabilities = listOf(0.3, 0.5, 0.2)
        val expected = probabilities.toDoubleArray()

        // When
        val result = List(n) { weightedSample(options, probabilities, rng) }

        // Then
        val percentages = itemPercentages(result)
        Assertions
            .assertThat(options.map { percentages.getValue(it) }.toDoubleArray())
            .containsExactly(expected, withPrecision(tolerance))
    }

    @Test
    fun `weightedSample should accept probabilities when their floating sum is just below one`() {
        // Given
        val options = listOf("one", "two", "three")
        val probabilities = listOf(0.7, 0.2, 0.1)

        // When
        val thrown = Assertions.catchThrowable { weightedSample(options, probabilities, Random(42)) }

        // Then
        Assertions.assertThat(thrown).isNull()
    }

    @Test
    fun `weightedSample should throw IllegalArgumentException when weights do not sum to one`() {
        // Given
        val options = listOf("one", "two", "three")
        val weights = listOf(3, 5, 2)

        // When
        val thrown = Assertions.catchThrowable { weightedSample(options, weights, Random(42)) }

        // Then
        Assertions
            .assertThat(thrown)
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("sum to one")
    }

    @Test
    fun `randomBitList with correct length`() {
        // Given
        val n = 100

        // When
        val result = randomBitList(n).size

        // Then
        Assertions.assertThat(result).isEqualTo(100)
    }

    @Test
    fun `sampleWithReplacementFromList with correct length`() {
        // Given
        val input = listOf("A", "B", "C")
        val n = 100

        // When
        val result = sampleWithReplacementFromList(input, n).size

        // Then
        Assertions.assertThat(result).isEqualTo(n)
    }

    @Test
    fun `weightedSampleWithReplacementFromList with correct length`() {
        // Given
        val options = listOf("one", "two", "three")
        val distribution = listOf(0.3, 0.5, 0.2)
        val n = 100

        // When
        val result = weightedSampleWithReplacementFromList(options, distribution, n).size

        // Then
        Assertions.assertThat(result).isEqualTo(n)
    }

    @Test
    fun foo() {
        repeat(101) {
            ParetoDistribution().sample()
        }
    }
}
