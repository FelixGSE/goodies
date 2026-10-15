package goodies.geometry

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.withPrecision
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

private const val EPSILON = 1e-3

private val goldenSplitLeavesAtDepthZero = doubleArrayOf(0.0, 0.0, 4.0, 0.0, 4.0, 2.0, 0.0, 2.0)

private val goldenSplitLeavesAtDepthOne =
    doubleArrayOf(
        9.888544,
        0.0,
        4.0,
        0.0,
        4.0,
        2.0,
        -5.888544,
        2.0,
        0.0,
        0.0,
        9.888544,
        0.0,
        -5.888544,
        2.0,
        0.0,
        2.0,
    )

private val goldenSplitLeavesAtDepthTwo =
    doubleArrayOf(
        9.888544,
        0.0,
        4.0,
        0.0,
        4.0,
        2.472136,
        149.18146,
        -17.65762,
        149.18146,
        -17.65762,
        4.0,
        2.472136,
        4.0,
        2.0,
        -5.888544,
        2.0,
        0.0,
        -0.47213602,
        -145.18146,
        19.65762,
        -5.888544,
        2.0,
        0.0,
        2.0,
        0.0,
        0.0,
        9.888544,
        0.0,
        -145.18146,
        19.65762,
        0.0,
        -0.47213602,
    )

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class QuadrilateralTest {
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    inner class GoldenRatioSubdivide {
        @ParameterizedTest(name = "{displayName} [{index}] depth={0}")
        @MethodSource("goldenRatioSubdivideCases")
        fun `goldenRatioSubdivide should split into the pinned leaf quadrilaterals`(
            depth: Int,
            expected: DoubleArray,
        ) {
            // Given
            val quadrilateral = Quadrilateral(listOf(Point(0, 0), Point(4, 0), Point(4, 2), Point(0, 2)))

            // When
            val result = quadrilateral.goldenRatioSubdivide(depth = depth)

            // Then
            val leafCoordinates =
                result
                    .traverseLeafNodes()
                    .flatMap { it.vertices }
                    .flatMap { it.toDoubleList() }
                    .toDoubleArray()
            assertThat(leafCoordinates).containsExactly(expected, withPrecision(EPSILON))
        }

        private fun goldenRatioSubdivideCases(): List<Arguments> =
            listOf(
                Arguments.of(0, goldenSplitLeavesAtDepthZero),
                Arguments.of(1, goldenSplitLeavesAtDepthOne),
                Arguments.of(2, goldenSplitLeavesAtDepthTwo),
            )
    }
}
