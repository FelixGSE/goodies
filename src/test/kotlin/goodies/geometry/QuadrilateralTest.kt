package goodies.geometry

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.withPrecision
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

private const val EPSILON = 1e-3

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
    @Nested
    inner class GoldenRatioSubdivide {
        @Test
        fun `goldenRatioSubdivide should split into the pinned leaf quadrilaterals when depth is two`() {
            // Given
            val quadrilateral = Quadrilateral(listOf(Point(0, 0), Point(4, 0), Point(4, 2), Point(0, 2)))
            val expected = goldenSplitLeavesAtDepthTwo

            // When
            val result = quadrilateral.goldenRatioSubdivide(depth = 2)

            // Then
            val leafCoordinates =
                result
                    .traverseLeafNodes()
                    .flatMap { it.vertices }
                    .flatMap { it.toDoubleList() }
                    .toDoubleArray()
            assertThat(leafCoordinates).containsExactly(expected, withPrecision(EPSILON))
        }
    }
}
