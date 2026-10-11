package goodies.geometry

class Square(
    x: Number,
    y: Number,
    scale: Number,
) : Quadrilateral(
        unitVertices.map { it * scale }.map { it.translate(x, y) },
    ) {
    constructor(point: Point, scale: Number) : this(point.x, point.y, scale)
}
