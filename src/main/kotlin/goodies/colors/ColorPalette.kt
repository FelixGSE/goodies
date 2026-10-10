package goodies.colors

import processing.core.PApplet

interface ColorPalette : Iterable<Int> {
    val colors: List<Int>

    override fun iterator(): Iterator<Int> = colors.iterator()

    fun drawSample(sketch: PApplet, size: Int) {
        val width = sketch.width.toFloat()
        var nextX = 0f
        var nextY = size.toFloat()

        for (color in colors) {
            if (nextX >= width) {
                nextX = 0f
                nextY += size
            }
            nextX += size
            sketch.fill(color.toFloat())
            sketch.circle(nextX, nextY, size.toFloat())
        }
    }
}
