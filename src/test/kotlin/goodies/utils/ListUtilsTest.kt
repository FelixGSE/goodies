package goodies.utils

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import utils.cartesianProduct
import utils.floatRange
import utils.linSpace

internal class ListUtilsTest {

    inner class FloatRangeTest {
        @Test
        fun `floatRange should do foo`() {
            // Given
            val from = 1.0
            val to = 10.0
            val step = 2.5
            val expected = listOf(1.0F, 3.5F, 6.0F, 8.5F )

            // When
            val result = floatRange(from, to, step)

            // Then
            Assertions.assertThat(result).isEqualTo(expected)

        }


    }

    @Nested
    inner class LineSpaceTest {
        @Test
        fun `linSpace should do foo`() {
            // Given
            val from = 0.0
            val to = 10.0
            val step = 5
            val expected = listOf(0.0F, 2.5F, 5.0F, 7.5F, 10.0F )

            // When
            val result = linSpace(from, to, step)

            // Then
            Assertions.assertThat(result).isEqualTo(expected)

        }


    }

    @Test
    fun `cartesianProduct should compute a 2 dimensional Cartesian Product`(
    ) {

        // Given
        val a = listOf(0, 1, 2)
        val b = listOf(0, 1, 2)
        val expected = listOf(
            listOf(0, 0),
            listOf(0, 1),
            listOf(0, 2),
            listOf(1, 0),
            listOf(1, 1),
            listOf(1, 2),
            listOf(2, 0),
            listOf(2, 1),
            listOf(2, 2),
        )

        // When
        val result = cartesianProduct(a, b)
        // Then
        Assertions.assertThat(result).isEqualTo(expected)
    }


}
