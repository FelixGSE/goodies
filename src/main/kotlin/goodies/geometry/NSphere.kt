package goodies.geometry

open class NSphere(
    center: Point,
    radius: Number,
) : NEllipsoid(
        center,
        List(center.dimensions) { radius },
    ) {
    val radius: Float = radius.toFloat()
    val diameter: Float = 2 * radius.toFloat()

    companion object {
        fun unitNSphere(dimensions: Int): NEllipsoid = NSphere(Point(List(dimensions) { 0 }), 1)
    }
}
