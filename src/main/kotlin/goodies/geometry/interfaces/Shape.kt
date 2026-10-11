package goodies.geometry.interfaces

import goodies.geometry.Point

/** Base of every geometric shape. */
interface Shape {
    val isClosed: Boolean

    fun center(): Shape

    fun centerAt(point: Point): Shape

    fun normalize(): Shape
}
