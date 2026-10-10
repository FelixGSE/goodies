package goodies.utils

import goodies.utils.weightedSample
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.withPrecision
import org.junit.jupiter.api.Test
import utils.itemPercentages

internal class RandomUtilsTest {

    @Test
    fun `weightedSample sample weighted samples from a given list`(
    ) {

        // Given
        val repetitions = 10000
        val options = listOf("one", "two", "three")
        val distribution = listOf(0.3, 0.5, 0.2)
        val samples = mutableListOf<String>()

        // When
        for (i in 0..repetitions) {
            samples.add(weightedSample(options, distribution))
        }

        val percentages = itemPercentages(samples)
        val result = listOf(
            percentages["one"],
            percentages["two"],
            percentages["three"]
        ).toTypedArray().map { it!!.toDouble() }.toDoubleArray()
        val expected = distribution.toTypedArray()

        // Then
        Assertions.assertThat(result)
            .containsSequence(expected, withPrecision(0.01))
    }

    @Test
    fun `randomBitList with correct length`(
    ) {

        // Given
        val n = 100

        // When
        val result = randomBitList(n).size

        // Then
        Assertions.assertThat(result).isEqualTo(100)

    }

    @Test
    fun `sampleWithReplacementFromList with correct length`(
    ) {

        // Given
        val input = listOf("A", "B", "C")
        val n = 100

        // When
        val result = sampleWithReplacementFromList(input, n).size

        // Then
        Assertions.assertThat(result).isEqualTo(n)

    }

    @Test
    fun `weightedSampleWithReplacementFromList with correct length`(
    ) {

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
    fun foo(){
        for(i in 0..100){
            val a = org.apache.commons.math3.distribution.ParetoDistribution().sample()
            println(a)
        }
    }

}

