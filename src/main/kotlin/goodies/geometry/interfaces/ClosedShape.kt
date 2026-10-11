package goodies.geometry.interfaces

import goodies.geometry.Point

/** Shape with an inside: it has a centroid, can be convex and can contain points. */
interface ClosedShape : Shape {
    val centroid: Point
    override val isClosed: Boolean get() = true
    val isConvex: Boolean

    /** Returns true when [point] lies inside this shape. */
    fun contains(point: Point): Boolean

    /** Returns true when every point in [point] lies inside this shape. */
    fun containsAll(point: List<Point>): Boolean = point.all { contains(it) }
}
