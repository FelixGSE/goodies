package goodies.geometry

open class NSphere(center: Point, radius: Number) : NEllipsoid(
    center,
    List(center.dimensions) { radius },
) {
    val radius = radius.toFloat()
    val diameter = 2 * radius.toFloat()

    companion object {

        fun unitNSphere(dimensions: Int): NEllipsoid {
            return NSphere(Point(List(dimensions) { 0 }), 1)
        }
    }
}
