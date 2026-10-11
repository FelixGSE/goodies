package goodies.geometry

import goodies.draw.Drawable
import processing.core.PApplet

/** Sphere: an [NSphere] in three dimensions. Throws IllegalArgumentException for any other dimension. */
class Sphere(
    center: Point,
    radius: Number,
) : NSphere(center, radius),
    Drawable {
    init {
        require(dimensions == SPATIAL_DIMENSIONS) { "A sphere can only have three dimensions" }
    }

    override fun draw(sketch: PApplet) {
        sketch.sphere(radius)
    }

    /** Builders for spheres. */
    companion object {
        fun unitSphere(): NEllipsoid = unitNSphere(SPATIAL_DIMENSIONS)
    }
}
