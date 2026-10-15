package goodies.geometry.interfaces

/** Ellipsoid-like shape that can gain dimensions. */
interface EllipsoidLike : Shape {
    /**
     * Returns this shape with [dimension] appended to its centre and [newAxes] as the semi-axes of the new dimensions.
     */
    fun addDimensions(
        vararg dimension: Number,
        newAxes: List<Float>,
    ): Shape
}
