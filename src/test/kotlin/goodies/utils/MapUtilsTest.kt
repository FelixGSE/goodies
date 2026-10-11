package goodies.utils

import goodies.utils.EaseFunctionType.*
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.withPrecision
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

private const val EPSILON = 1e-4f

private fun unitRows(
    type: EaseFunctionType,
    easeIn: Pair<Float, Float>,
    easeOut: Pair<Float, Float>,
    easeInOut: Pair<Float, Float>,
): List<Arguments> =
    listOf(
        EasingType.EASE_IN to easeIn,
        EasingType.EASE_OUT to easeOut,
        EasingType.EASE_IN_OUT to easeInOut,
    ).flatMap { (easingType, expected) ->
        listOf(
            Arguments.of(type, easingType, 0.25f, expected.first),
            Arguments.of(type, easingType, 0.75f, expected.second),
        )
    }

private val unitRangeRows: List<Arguments> =
    listOf(
        unitRows(
            type = LINEAR,
            easeIn = 2.5f to 7.5f,
            easeOut = 2.5f to 7.5f,
            easeInOut = 2.5f to 7.5f,
        ),
        unitRows(
            type = QUADRATIC,
            easeIn = 0.625f to 5.625f,
            easeOut = 4.375f to 9.375f,
            easeInOut = 1.25f to 8.75f,
        ),
        unitRows(
            type = CUBIC,
            easeIn = 0.15625f to 4.21875f,
            easeOut = 5.78125f to 9.84375f,
            easeInOut = 0.625f to 9.375f,
        ),
        unitRows(
            type = QUARTIC,
            easeIn = 0.03906f to 3.16406f,
            easeOut = 6.83594f to 9.96094f,
            easeInOut = 0.3125f to 9.6875f,
        ),
        unitRows(
            type = QUINTIC,
            easeIn = 0.00977f to 2.37305f,
            easeOut = 7.62695f to 9.99023f,
            easeInOut = 0.15625f to 9.84375f,
        ),
        unitRows(
            type = SINUSOIDAL,
            easeIn = 0.7612f to 6.17317f,
            easeOut = 3.82683f to 9.23879f,
            easeInOut = 1.46447f to 8.53553f,
        ),
        unitRows(
            type = EXPONENTIAL,
            easeIn = 0.05524f to 1.76777f,
            easeOut = 8.23223f to 9.94476f,
            easeInOut = 0.15625f to 9.84375f,
        ),
        unitRows(
            type = CIRCULAR,
            easeIn = 0.31754f to 3.38562f,
            easeOut = 6.61438f to 9.68246f,
            easeInOut = 0.66987f to 9.33013f,
        ),
        unitRows(
            type = SQRT,
            easeIn = 5.0f to 8.66025f,
            easeOut = 1.33975f to 5.0f,
            easeInOut = 3.53553f to 6.46447f,
        ),
    ).flatten()

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

        @ParameterizedTest(name = "{displayName} [{index}] {0} -> {1} value={2}")
        @MethodSource("shiftedRangeCases")
        fun `map2 should map a value between ranges that do not start at 0`(
            source: ClosedFloatingPointRange<Float>,
            target: ClosedFloatingPointRange<Float>,
            value: Float,
            expected: Float,
        ) {
            // Given
            val type = QUADRATIC
            val easingType = EasingType.EASE_IN

            // When
            val result =
                map2(
                    value = value,
                    start1 = source.start,
                    stop1 = source.endInclusive,
                    start2 = target.start,
                    stop2 = target.endInclusive,
                    type = type,
                    easingType = easingType,
                )

            // Then
            assertThat(result).isCloseTo(expected, withPrecision(EPSILON))
        }

        private fun map2Cases(): List<Arguments> = unitRangeRows

        private fun shiftedRangeCases(): List<Arguments> =
            listOf(
                Arguments.of(2f..4f, -5f..5f, 2.5f, -4.375f),
                Arguments.of(2f..4f, -5f..5f, 3.5f, 0.625f),
                Arguments.of(-1f..1f, 1f..3f, 0f, 1.5f),
                Arguments.of(10f..0f, 100f..0f, 7.5f, 93.75f),
            )
    }
}
