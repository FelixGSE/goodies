package goodies.geometry

/** Sphere in any number of dimensions: an [NEllipsoid] with every semi-axis equal to [radius]. */
open class NSphere(
    center: Point,
    radius: Number,
) : NEllipsoid(
        center,
        List(center.dimensions) { radius },
    ) {
    val radius: Float = radius.toFloat()
    val diameter: Float = 2 * radius.toFloat()

    /** Builders for spheres. */
    companion object {
        /** Returns the unit sphere around the origin in [dimensions] dimensions. */
        fun unitNSphere(dimensions: Int): NEllipsoid = NSphere(Point(List(dimensions) { 0 }), 1)
    }
}
