package goodies.utils

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class GeneralUtilsKtTest {
    @Test
    fun `sha512 should hash a string`() {
        // Given
        val input = "Some string"
        val expectedResult =
            "31fcbf83b5ba1c6a378a46645558a961ffb94fbb61f502d2f3becb12feae63d9" +
                "433851e8b83ea56d143bdffc83874db881f8e5e6ac2fc787ed628d4e7e7bf6f0"
        // When
        val result = sha512(input)

        // Then
        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `sha512 should hash a number`() {
        // Given

        // When

        // Then
    }

    @Test
    fun `sha512 should hash a byte array`() {
        // Given

        // When

        // Then
    }

    @Test
    fun `sha512 should hash a list`() {
        // Given

        // When

        // Then
    }

    @Test
    fun `sha512 should throw IllegalArgumentException when the argument is illegal`() {
        // Given

        // When

        // Then
    }
}
