package goodies.projections

import org.apache.commons.math3.linear.MatrixUtils
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.data.Offset.offset
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import processing.core.PVector

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StereographicProjectionTest {
    @Nested
    inner class Project3Dto2D {
        @Test
        fun `should project 3D point to 2D`() {
            // Given
            val point = PVector(100f, 100f, 100f)

            // When
            val result = StereographicProjection.project3Dto2D(10f, point)

            // Then
            // scale = 1/(10 - 100) = -1/90
            assertThat(result.x).isCloseTo(100f * (-1f / 90f), offset(0.01f))
            assertThat(result.y).isCloseTo(100f * (-1f / 90f), offset(0.01f))
        }

        @Test
        fun `should project negative 3D point to 2D`() {
            // Given
            val point = PVector(-100f, -100f, -100f)

            // When
            val result = StereographicProjection.project3Dto2D(10f, point)

            // Then
            // scale = 1/(10 - (-100)) = 1/110
            assertThat(result.x).isCloseTo(-100f / 110f, offset(0.01f))
            assertThat(result.y).isCloseTo(-100f / 110f, offset(0.01f))
        }

        @Test
        fun `should project origin to origin`() {
            // Given
            val point = PVector(0f, 0f, 0f)

            // When
            val result = StereographicProjection.project3Dto2D(2f, point)

            // Then
            assertThat(result.x).isCloseTo(0f, offset(0.001f))
            assertThat(result.y).isCloseTo(0f, offset(0.001f))
        }
    }

    @Nested
    inner class ProjectDownOneDimension {
        @Test
        fun `should reduce dimensionality by one for RealVector`() {
            // Given
            val vectorData = doubleArrayOf(110.0, 110.0, 101.0, 110.0)
            val vectorToProject = MatrixUtils.createRealVector(vectorData)

            // When
            val result = StereographicProjection.projectDownOneDimension(10f, vectorToProject)

            // Then
            assertThat(result.dimension).isEqualTo(vectorData.size - 1)
        }

        @Test
        fun `should correctly project 3D RealVector to 2D`() {
            // Given
            val vector = MatrixUtils.createRealVector(doubleArrayOf(-1.0, -1.0, 1.0))

            // When
            val result = StereographicProjection.projectDownOneDimension(2f, vector)

            // Then
            // scale = 1/(2 - 1) = 1
            assertThat(result.getEntry(0)).isCloseTo(-1.0, offset(0.001))
            assertThat(result.getEntry(1)).isCloseTo(-1.0, offset(0.001))
        }

        @Test
        fun `should reduce dimensionality by one for List of Floats`() {
            // Given
            val point = listOf(1.0f, 2.0f, 3.0f, 4.0f)

            // When
            val result = StereographicProjection.projectDownOneDimension(10f, point)

            // Then
            assertThat(result).hasSize(3)
        }

        @Test
        fun `should correctly project List of Floats`() {
            // Given
            val point = listOf(6.0f, 8.0f, 0.0f)

            // When
            val result = StereographicProjection.projectDownOneDimension(2f, point)

            // Then
            // scale = 1/(2 - 0) = 0.5
            assertThat(result[0]).isCloseTo(3.0f, offset(0.001f))
            assertThat(result[1]).isCloseTo(4.0f, offset(0.001f))
        }
    }
}
