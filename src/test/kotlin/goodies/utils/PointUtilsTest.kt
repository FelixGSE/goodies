package goodies.utils

import goodies.geometry.Point
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class PointUtilsTest {
    @Test
    fun `listOfPointsToListOfLines should return n - 1 lines for a given set of points`() {
        // Given
        val points =
            listOf(
                Point.random2D(),
                Point.random2D(),
                Point.random2D(),
            )
        val expectedNumberOfLines = points.size - 1
        // When
        val result = listOfPointsToListOfLines(points).size

        // Then
        Assertions.assertThat(result).isEqualTo(expectedNumberOfLines)
    }
}
