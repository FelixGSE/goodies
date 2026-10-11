package goodies.geometry.interfaces

/** Ellipsoid-like shape that can gain dimensions. */
interface EllipsoidLike : Shape {
    fun addDimensions(
        vararg dimension: Number,
        newAxes: List<Float>,
    ): Shape
}
