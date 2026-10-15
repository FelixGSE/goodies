package goodies.geometry

import goodies.draw.Drawable
import processing.core.PApplet

/** An [NSphere] in three dimensions. Throws IllegalArgumentException for any other dimension. */
class Sphere(
    center: Point,
    radius: Number,
) : NSphere(center, radius),
    Drawable {
    init {
        require(dimensions == SPATIAL_DIMENSIONS) {
            "Sphere needs a 3D center, got ${dimensions}D. Use Circle, or NSphere for any dimension"
        }
    }

    override fun draw(sketch: PApplet) {
        sketch.sphere(radius)
    }

    /** Builders for spheres. */
    companion object {
        /** Returns the unit sphere around the origin. */
        fun unitSphere(): NEllipsoid = unitNSphere(SPATIAL_DIMENSIONS)
    }
}
