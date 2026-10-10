package goodies.chaos

import processing.core.PApplet
import processing.core.PVector

data class DeJongAttractor(val x: Float, val y: Float, val a: Float, val b: Float, val c: Float, val d: Float) {

    constructor(a: Float, b: Float, c: Float, d: Float) : this(0f, 0f, a, b, c, d)

    val location: PVector
        get() = PVector(x, y)

    fun next(): DeJongAttractor {
        val xNext = PApplet.sin(a * y) - PApplet.cos(b * x)
        val yNext = PApplet.sin(c * x) - PApplet.cos(d * y)
        return DeJongAttractor(xNext, yNext, a, b, c, d)
    }

    fun draw(sketch: PApplet, maxIterations: Int, scale: Float) {
        sketch.pushMatrix()
        sketch.translate(location.x, location.y)
        var current = this
        for (i in 0..maxIterations) {
            current = current.next()
            sketch.point(current.x * scale, current.y * scale)
        }
        sketch.popMatrix()
    }
}
