package goodies.geometry.interfaces

interface EllipsoidLike : Shape {
    fun addDimensions(
        vararg dimension: Number,
        newAxes: List<Float>,
    ): Shape
}
