package goodies.utils

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.withPrecision
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

private const val EPSILON = 1e-4f

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class MapUtilsTest {
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    inner class Map2 {
        @ParameterizedTest(name = "{displayName} [{index}] {0} {1} value={2}")
        @MethodSource("map2Cases")
        fun `map2 should map a value from 0 to 1 onto 0 to 10 along the eased curve`(
            type: EaseFunctionType,
            easingType: EasingType,
            value: Float,
            expected: Float,
        ) {
            // Given
            val start = 0f
            val stop = 1f
            val targetStart = 0f
            val targetStop = 10f

            // When
            val result =
                map2(
                    value = value,
                    start1 = start,
                    stop1 = stop,
                    start2 = targetStart,
                    stop2 = targetStop,
                    type = type,
                    easingType = easingType,
                )

            // Then
            assertThat(result).isCloseTo(expected, withPrecision(EPSILON))
        }

        private fun map2Cases(): Stream<Arguments> =
            listOf(
                easedRows(EaseFunctionType.LINEAR, 2.5f, 7.5f, 2.5f, 7.5f, 2.5f, 7.5f),
                easedRows(EaseFunctionType.QUADRATIC, 0.625f, 5.625f, 4.375f, 9.375f, 1.25f, 8.75f),
                easedRows(EaseFunctionType.CUBIC, 0.15625f, 4.21875f, 5.78125f, 9.84375f, 0.625f, 9.375f),
                easedRows(EaseFunctionType.QUARTIC, 0.03906f, 3.16406f, 6.83594f, 9.96094f, 0.3125f, 9.6875f),
                easedRows(EaseFunctionType.QUINTIC, 0.00977f, 2.37305f, 7.62695f, 9.99023f, 0.15625f, 9.84375f),
                easedRows(EaseFunctionType.SINUSOIDAL, 0.7612f, 6.17317f, 3.82683f, 9.23879f, 1.46447f, 8.53553f),
                easedRows(EaseFunctionType.EXPONENTIAL, 0.05524f, 1.76777f, 8.23223f, 9.94476f, 0.15625f, 9.84375f),
                easedRows(EaseFunctionType.CIRCULAR, 0.31754f, 3.38562f, 6.61438f, 9.68246f, 0.66987f, 9.33013f),
                easedRows(EaseFunctionType.SQRT, 5.0f, 8.66025f, 1.33975f, 5.0f, 3.53553f, 6.46447f),
            ).flatten().stream()

        private fun easedRows(
            type: EaseFunctionType,
            vararg expected: Float,
        ): List<Arguments> =
            EasingType.entries.flatMapIndexed { easing, easingType ->
                listOf(0.25f, 0.75f).mapIndexed { half, value ->
                    Arguments.of(type, easingType, value, expected[2 * easing + half])
                }
            }
    }
}
