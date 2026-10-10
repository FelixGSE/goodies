package goodies.geometry.interfaces

import goodies.geometry.Point

interface ClosedShape : Shape {
    val centroid: Point
    override val isClosed: Boolean get() = true
    val isConvex: Boolean

    fun contains(point: Point): Boolean

    fun containsAll(point: List<Point>): Boolean = point.all { contains(it) }
}
