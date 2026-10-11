package goodies.geometry

import goodies.geometry.interfaces.Native2D

/** An [NSphere] in two dimensions. Throws IllegalArgumentException for any other dimension. */
class Circle(
    center: Point,
    radius: Number,
) : NSphere(center, radius),
    Native2D {
    init {
        require(dimensions == 2) {
            "Circle needs a 2D center, got ${dimensions}D. Use Sphere, or NSphere for any dimension"
        }
    }

    override fun toString(): String = "Circle{center=${center}radius=$radius}"

    /** Returns a circle with a copy of the centre and the same radius. */
    fun copy(): Circle = Circle(center.copy(), radius)

    /** Builders for circles. */
    companion object {
        /** Returns the unit circle around the origin. */
        fun unitCircle(): NEllipsoid = unitNSphere(2)
    }
}
