package goodies.geometry

/** Polygon with the three vertices [a], [b] and [c]. */
data class Triangle(
    val a: Point,
    val b: Point,
    val c: Point,
) : Polygon(listOf(a, b, c))
