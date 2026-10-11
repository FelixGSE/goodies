package goodies.geometry

data class Triangle(
    val a: Point,
    val b: Point,
    val c: Point,
) : Polygon(listOf(a, b, c))
