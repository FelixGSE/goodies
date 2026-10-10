package goodies.grid

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import utils.linSpace

class Grid2DTest {

    @Nested
    inner class Construction {
        @Test
        fun `Grid2D should be initialized with correct dimensions`() {
            // Given
            val grid = Grid2D(0, 10, 4, 0, 10, 4)
            val expectedNRows = 4
            val expectedNCols = 4
            val expectedNCells = 9

            // When


            // Then
            Assertions.assertThat(grid.nRows).isEqualTo(expectedNRows)
            Assertions.assertThat(grid.nCols).isEqualTo(expectedNCols)
            Assertions.assertThat(grid.nCells).isEqualTo(expectedNCells)

        }


    }
}