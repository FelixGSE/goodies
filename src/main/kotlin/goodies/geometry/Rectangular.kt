package goodies.geometry

/** Axis-aligned rectangle with corner ([x], [y]), [width] and [height]. */
open class Rectangular(
    x: Number,
    y: Number,
    width: Number,
    height: Number,
) : Quadrilateral(
        unitVertices.map { Point(it.x * width.toFloat(), it.y * height.toFloat()) }.map { it.translate(x, y) },
    ) {
    constructor(point: Point, width: Number, height: Number) : this(point.x, point.y, width, height)
}
