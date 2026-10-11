package goodies.geometry

/** Axis-aligned square with corner ([x], [y]) and side length [scale]. */
class Square(
    x: Number,
    y: Number,
    scale: Number,
) : Quadrilateral(
        unitVertices.map { it * scale }.map { it.translate(x, y) },
    ) {
    constructor(point: Point, scale: Number) : this(point.x, point.y, scale)
}
