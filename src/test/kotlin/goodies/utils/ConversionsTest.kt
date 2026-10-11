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
        fun `should convert RealVector to list of floats`() {
            // Given
            val realVector = MatrixUtils.createRealVector(doubleArrayOf(1.5, 2.5, 3.5))

            // When
            val result = Conversions.realVectorToListOfFloats(realVector)

            // Then
            assertThat(result).containsExactly(1.5f, 2.5f, 3.5f)
        }

        @Test
        fun `should convert list of floats to RealVector`() {
            // Given
            val listOfFloats = listOf(1.0f, 2.0f, 3.0f)

            // When
            val result = Conversions.listOfFloatsToRealArray(listOfFloats)

            // Then
            assertThat(result.toArray()).containsExactly(1.0, 2.0, 3.0)
        }

        @Test
        fun `should convert 2D RealVector to PVector`() {
            // Given
            val realVector = MatrixUtils.createRealVector(doubleArrayOf(3.0, 4.0))

            // When
            val result = Conversions.realVectorToPVector(realVector)

            // Then
            assertThat(result.x).isCloseTo(3.0f, offset(0.001f))
            assertThat(result.y).isCloseTo(4.0f, offset(0.001f))
        }

        @Test
        fun `should convert 3D RealVector to PVector`() {
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
        fun `should throw for RealVector with invalid dimensions`() {
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
        fun `should convert 2D PVector to RealVector`() {
            // Given
            val pVector = PVector(5.0f, 6.0f)

            // When
            val result = Conversions.PVector2DToRealVector(pVector)

            // Then
            assertThat(result.toArray()).containsExactly(5.0, 6.0)
        }

        @Test
        fun `should convert 3D PVector to RealVector`() {
            // Given
            val pVector = PVector(5.0f, 6.0f, 7.0f)

            // When
            val result = Conversions.PVector3DToRealVector(pVector)

            // Then
            assertThat(result.toArray()).containsExactly(5.0, 6.0, 7.0)
        }

        @Test
        fun `should convert PVector to 2D double array`() {
            // Given
            val pVector = PVector(1.0f, 2.0f)

            // When
            val result = Conversions.PVectorToDoubleArray(pVector, 2)

            // Then
            assertThat(result).containsExactly(1.0, 2.0)
        }

        @Test
        fun `should convert PVector to 3D double array`() {
            // Given
            val pVector = PVector(1.0f, 2.0f, 3.0f)

            // When
            val result = Conversions.PVectorToDoubleArray(pVector, 3)

            // Then
            assertThat(result).containsExactly(1.0, 2.0, 3.0)
        }

        @Test
        fun `should throw for invalid dimensions in PVectorToDoubleArray`() {
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
        fun `should convert list of floats to PVector 2D`() {
            // Given
            val list = listOf(1.0f, 2.0f)

            // When
            val result = Conversions.listOfFloatsToPVector(list)

            // Then
            assertThat(result.x).isEqualTo(1.0f)
            assertThat(result.y).isEqualTo(2.0f)
        }

        @Test
        fun `should convert list of floats to PVector 3D`() {
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
        fun `should convert list of floats to double array`() {
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
        fun `should convert double array to list of floats`() {
            // Given
            val arr = doubleArrayOf(1.5, 2.5, 3.5)

            // When
            val result = Conversions.doubleArrayToListOfFloats(arr)

            // Then
            assertThat(result).containsExactly(1.5f, 2.5f, 3.5f)
        }

        @Test
        fun `should convert double array to PVector 2D`() {
            // Given
            val arr = doubleArrayOf(4.0, 5.0)

            // When
            val result = Conversions.doubleArrayToPVector(arr)

            // Then
            assertThat(result.x).isCloseTo(4.0f, offset(0.001f))
            assertThat(result.y).isCloseTo(5.0f, offset(0.001f))
        }

        @Test
        fun `should convert list of list of floats to double arrays`() {
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
        fun `should convert double to float`() {
            assertThat(Conversions.doubleToFloat(3.14)).isCloseTo(3.14f, offset(0.001f))
        }

        @Test
        fun `should convert float to double`() {
            assertThat(Conversions.floatToDouble(3.14f)).isCloseTo(3.14, offset(0.001))
        }
    }
}
