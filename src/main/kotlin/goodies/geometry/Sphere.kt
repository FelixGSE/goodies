package goodies.geometry

import goodies.draw.Drawable
import processing.core.PApplet

class Sphere(
    center: Point,
    radius: Number,
) : NSphere(center, radius),
    Drawable {
    init {
        if (dimensions != 3) {
            throw IllegalArgumentException("A sphere can only have three dimensions")
        }
    }

    override fun draw(sketch: PApplet) {
        sketch.sphere(radius)
    }

    companion object {
        fun unitSphere(): NEllipsoid = unitNSphere(3)
    }
}
