package goodies.colors

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ColorPaletteTest {
    @Nested
    inner class PaletteATests {
        @Test
        fun `should have four colors`() {
            // Given / When
            val palette = PaletteA()

            // Then
            assertThat(palette.colors).hasSize(4)
        }

        @Test
        fun `should contain expected color values`() {
            // Given / When
            val palette = PaletteA()

            // Then
            assertThat(palette.colors).containsExactly(-661_305, -1_258_318, -2_181_461, -5_476_216)
        }

        @Test
        fun `should be iterable`() {
            // Given
            val palette = PaletteA()

            // When
            val colorList = palette.map { it }

            // Then
            assertThat(colorList).hasSize(4)
            assertThat(colorList).containsExactly(-661_305, -1_258_318, -2_181_461, -5_476_216)
        }

        @Test
        fun `iterator should return all colors in order`() {
            // Given
            val palette = PaletteA()
            val collected = mutableListOf<Int>()

            // When
            for (color in palette) {
                collected.add(color)
            }

            // Then
            assertThat(collected).isEqualTo(palette.colors)
        }
    }

    @Nested
    inner class CustomPaletteTests {
        @Test
        fun `should support custom palette creation`() {
            // Given
            val customPalette =
                object : ColorPalette {
                    override val colors = listOf(-1, -2, -3)
                }

            // When / Then
            assertThat(customPalette.colors).hasSize(3)
            assertThat(customPalette.toList()).containsExactly(-1, -2, -3)
        }
    }
}
