package goodies.algebra

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

internal class RotationMatrixTest {

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    inner class Properties {

        @Test
        fun `Multiplying several high dimensional RotationMatrices should still have a determinant of 1`() {
            // Given
            val matrix = RotationMatrix(0, 3, 4, 0.5)
                .dot(RotationMatrix(2, 3, 4, 0.5))
                .dot(RotationMatrix(1, 3, 4, 0.5))
                .dot(RotationMatrix(1, 2, 4, 0.5))

            // When

            val result = matrix.determinant

            // Then
            assertThat(result).isEqualTo(1.0F)
//
        }

//        @Test
//        fun `Rotation matrix is orthogonal`() {
//            // Given
//            val matrix = listOf(
//                RotationMatrix(0, 3, 4, 0.5),
//                RotationMatrix(2, 3, 4, 0.5),
//                RotationMatrix(1, 3, 4, 0.5),
//                RotationMatrix(1, 2, 4, 0.5),
//            ).map { it.toGMatrix() }.reduce { acc, gMatrix ->
//                acc.dot(gMatrix)
//            }
//
//            // When
//            val result = matrix.isOrthogonal
//            println(matrix.inverse.toRealMatrix())
//            println(matrix.transpose.toRealMatrix())
//
//            // Then
//            assertThat(result).isEqualTo(true)
// //
//        }
    }
}
