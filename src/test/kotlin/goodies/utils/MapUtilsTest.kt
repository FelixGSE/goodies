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

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class MapUtilsTest {
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    inner class Map2 {
        @ParameterizedTest(name = "{displayName} [{index}] {0} {1} value={2}")
        @MethodSource("map2PolynomialCases", "map2CurveCases")
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

        private fun map2PolynomialCases(): List<Arguments> =
            listOf(
                unitRows(LINEAR, EasingType.EASE_IN, 0f, 2.5f, 7.5f, 10f),
                unitRows(LINEAR, EasingType.EASE_OUT, 0f, 2.5f, 7.5f, 10f),
                unitRows(LINEAR, EasingType.EASE_IN_OUT, 0f, 2.5f, 7.5f, 10f),
                unitRows(QUADRATIC, EasingType.EASE_IN, 0f, 0.625f, 5.625f, 10f),
                unitRows(QUADRATIC, EasingType.EASE_OUT, 0f, 4.375f, 9.375f, 10f),
                unitRows(QUADRATIC, EasingType.EASE_IN_OUT, 0f, 1.25f, 8.75f, 10f),
                unitRows(CUBIC, EasingType.EASE_IN, 0f, 0.15625f, 4.21875f, 10f),
                unitRows(CUBIC, EasingType.EASE_OUT, 0f, 5.78125f, 9.84375f, 10f),
                unitRows(CUBIC, EasingType.EASE_IN_OUT, 0f, 0.625f, 9.375f, 10f),
                unitRows(QUARTIC, EasingType.EASE_IN, 0f, 0.03906f, 3.16406f, 10f),
                unitRows(QUARTIC, EasingType.EASE_OUT, 0f, 6.83594f, 9.96094f, 10f),
                unitRows(QUARTIC, EasingType.EASE_IN_OUT, 0f, 0.3125f, 9.6875f, 10f),
                unitRows(QUINTIC, EasingType.EASE_IN, 0f, 0.00977f, 2.37305f, 10f),
                unitRows(QUINTIC, EasingType.EASE_OUT, 0f, 7.62695f, 9.99023f, 10f),
                unitRows(QUINTIC, EasingType.EASE_IN_OUT, 0f, 0.15625f, 9.84375f, 10f),
            ).flatten()

        private fun map2CurveCases(): List<Arguments> =
            listOf(
                unitRows(SINUSOIDAL, EasingType.EASE_IN, 0f, 0.7612f, 6.17317f, 10f),
                unitRows(SINUSOIDAL, EasingType.EASE_OUT, 0f, 3.82683f, 9.23879f, 10f),
                unitRows(SINUSOIDAL, EasingType.EASE_IN_OUT, 0f, 1.46447f, 8.53553f, 10f),
                unitRows(EXPONENTIAL, EasingType.EASE_IN, 0.00977f, 0.05524f, 1.76777f, 10f),
                unitRows(EXPONENTIAL, EasingType.EASE_OUT, 0f, 8.23223f, 9.94476f, 9.99023f),
                unitRows(EXPONENTIAL, EasingType.EASE_IN_OUT, 0.00488f, 0.15625f, 9.84375f, 9.99512f),
                unitRows(CIRCULAR, EasingType.EASE_IN, 0f, 0.31754f, 3.38562f, 10f),
                unitRows(CIRCULAR, EasingType.EASE_OUT, 0f, 6.61438f, 9.68246f, 10f),
                unitRows(CIRCULAR, EasingType.EASE_IN_OUT, 0f, 0.66987f, 9.33013f, 10f),
                unitRows(SQRT, EasingType.EASE_IN, 0f, 5f, 8.66025f, 10f),
                unitRows(SQRT, EasingType.EASE_OUT, 0f, 1.33975f, 5f, 10f),
                unitRows(SQRT, EasingType.EASE_IN_OUT, 0f, 3.53553f, 6.46447f, 10f),
            ).flatten()

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

        private fun shiftedRangeCases(): List<Arguments> =
            listOf(
                Arguments.of(2f..4f, -5f..5f, 2.5f, -4.375f),
                Arguments.of(2f..4f, -5f..5f, 3.5f, 0.625f),
                Arguments.of(-1f..1f, 1f..3f, 0f, 1.5f),
                Arguments.of(10f..0f, 100f..0f, 7.5f, 93.75f),
            )
    }
}

private val UNIT_VALUES = listOf(0f, 0.25f, 0.75f, 1f)

private fun unitRows(
    type: EaseFunctionType,
    easingType: EasingType,
    vararg expected: Float,
): List<Arguments> =
    UNIT_VALUES.zip(expected.toList()) { value, expectedValue -> Arguments.of(type, easingType, value, expectedValue) }
