package goodies.utils

import org.apache.commons.math3.linear.MatrixUtils
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.assertj.core.data.Offset.offset
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import processing.core.PVector

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ConversionsTest {
    @Nested
    inner class RealVectorConversions {
        @Test
        fun `realVectorToListOfFloats should convert a RealVector to a list of floats`() {
            // Given
            val realVector = MatrixUtils.createRealVector(doubleArrayOf(1.5, 2.5, 3.5))

            // When
            val result = Conversions.realVectorToListOfFloats(realVector)

            // Then
            assertThat(result).containsExactly(1.5f, 2.5f, 3.5f)
        }

        @Test
        fun `listOfFloatsToRealArray should convert a list of floats to a RealVector`() {
            // Given
            val listOfFloats = listOf(1.0f, 2.0f, 3.0f)

            // When
            val result = Conversions.listOfFloatsToRealArray(listOfFloats)

            // Then
            assertThat(result.toArray()).containsExactly(1.0, 2.0, 3.0)
        }

        @Test
        fun `realVectorToPVector should convert a 2D RealVector to a PVector`() {
            // Given
            val realVector = MatrixUtils.createRealVector(doubleArrayOf(3.0, 4.0))

            // When
            val result = Conversions.realVectorToPVector(realVector)

            // Then
            assertThat(result.x).isCloseTo(3.0f, offset(0.001f))
            assertThat(result.y).isCloseTo(4.0f, offset(0.001f))
        }

        @Test
        fun `realVectorToPVector should convert a 3D RealVector to a PVector`() {
            // Given
            val realVector = MatrixUtils.createRealVector(doubleArrayOf(1.0, 2.0, 3.0))

            // When
            val result = Conversions.realVectorToPVector(realVector)

            // Then
            assertThat(result.x).isCloseTo(1.0f, offset(0.001f))
            assertThat(result.y).isCloseTo(2.0f, offset(0.001f))
            assertThat(result.z).isCloseTo(3.0f, offset(0.001f))
        }

        @Test
        fun `realVectorToPVector should throw IllegalArgumentException when the RealVector has invalid dimensions`() {
            // Given
            val realVector = MatrixUtils.createRealVector(doubleArrayOf(1.0))

            // When
            val throwable = catchThrowable { Conversions.realVectorToPVector(realVector) }

            // Then
            assertThat(throwable)
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessageContaining("PVector needs 2 or 3 values, got 1")
        }
    }

    @Nested
    inner class PVectorConversions {
        @Test
        fun `PVector2DToRealVector should convert a 2D PVector to a RealVector`() {
            // Given
            val pVector = PVector(5.0f, 6.0f)

            // When
            val result = Conversions.PVector2DToRealVector(pVector)

            // Then
            assertThat(result.toArray()).containsExactly(5.0, 6.0)
        }

        @Test
        fun `PVector3DToRealVector should convert a 3D PVector to a RealVector`() {
            // Given
            val pVector = PVector(5.0f, 6.0f, 7.0f)

            // When
            val result = Conversions.PVector3DToRealVector(pVector)

            // Then
            assertThat(result.toArray()).containsExactly(5.0, 6.0, 7.0)
        }

        @Test
        fun `PVectorToDoubleArray should convert a PVector to a 2D double array`() {
            // Given
            val pVector = PVector(1.0f, 2.0f)

            // When
            val result = Conversions.PVectorToDoubleArray(pVector, 2)

            // Then
            assertThat(result).containsExactly(1.0, 2.0)
        }

        @Test
        fun `PVectorToDoubleArray should convert a PVector to a 3D double array`() {
            // Given
            val pVector = PVector(1.0f, 2.0f, 3.0f)

            // When
            val result = Conversions.PVectorToDoubleArray(pVector, 3)

            // Then
            assertThat(result).containsExactly(1.0, 2.0, 3.0)
        }

        @Test
        fun `PVectorToDoubleArray should throw IllegalArgumentException when dimensions are invalid`() {
            // Given
            val pVector = PVector(1.0f, 2.0f)

            // When
            val throwable = catchThrowable { Conversions.PVectorToDoubleArray(pVector, 4) }

            // Then
            assertThat(throwable)
                .isInstanceOf(IllegalArgumentException::class.java)
                .hasMessageContaining("PVector has 2 or 3 dimensions, got 4")
        }
    }

    @Nested
    inner class ListAndArrayConversions {
        @Test
        fun `listOfFloatsToPVector should convert a list of two floats to a 2D PVector`() {
            // Given
            val list = listOf(1.0f, 2.0f)

            // When
            val result = Conversions.listOfFloatsToPVector(list)

            // Then
            assertThat(result.x).isEqualTo(1.0f)
            assertThat(result.y).isEqualTo(2.0f)
        }

        @Test
        fun `listOfFloatsToPVector should convert a list of three floats to a 3D PVector`() {
            // Given
            val list = listOf(1.0f, 2.0f, 3.0f)

            // When
            val result = Conversions.listOfFloatsToPVector(list)

            // Then
            assertThat(result.x).isEqualTo(1.0f)
            assertThat(result.y).isEqualTo(2.0f)
            assertThat(result.z).isEqualTo(3.0f)
        }

        @Test
        fun `listOfFloatsToDoubleArray should convert a list of floats to a double array`() {
            // Given
            val list = listOf(1.5f, 2.5f)

            // When
            val result = Conversions.listOfFloatsToDoubleArray(list)

            // Then
            assertThat(result).hasSize(2)
            assertThat(result[0]).isCloseTo(1.5, offset(0.001))
            assertThat(result[1]).isCloseTo(2.5, offset(0.001))
        }

        @Test
        fun `doubleArrayToListOfFloats should convert a double array to a list of floats`() {
            // Given
            val arr = doubleArrayOf(1.5, 2.5, 3.5)

            // When
            val result = Conversions.doubleArrayToListOfFloats(arr)

            // Then
            assertThat(result).containsExactly(1.5f, 2.5f, 3.5f)
        }

        @Test
        fun `doubleArrayToPVector should convert a double array to a 2D PVector`() {
            // Given
            val arr = doubleArrayOf(4.0, 5.0)

            // When
            val result = Conversions.doubleArrayToPVector(arr)

            // Then
            assertThat(result.x).isCloseTo(4.0f, offset(0.001f))
            assertThat(result.y).isCloseTo(5.0f, offset(0.001f))
        }

        @Test
        fun `listOfListOfFloatsToDoubleArrays should convert a list of lists of floats to double arrays`() {
            // Given
            val lists = listOf(listOf(1.0f, 2.0f), listOf(3.0f, 4.0f))

            // When
            val result = Conversions.listOfListOfFloatsToDoubleArrays(lists)

            // Then
            assertThat(result.size).isEqualTo(2)
            assertThat(result[0]).containsExactly(1.0, 2.0)
            assertThat(result[1]).containsExactly(3.0, 4.0)
        }
    }

    @Nested
    inner class TypeConversions {
        @Test
        fun `doubleToFloat should convert a double to a float`() {
            // Given
            val input = 3.14

            // When
            val result = Conversions.doubleToFloat(input)

            // Then
            assertThat(result).isCloseTo(3.14f, offset(0.001f))
        }

        @Test
        fun `floatToDouble should convert a float to a double`() {
            // Given
            val input = 3.14f

            // When
            val result = Conversions.floatToDouble(input)

            // Then
            assertThat(result).isCloseTo(3.14, offset(0.001))
        }
    }
}
