package goodies.geometry.interfaces

import goodies.geometry.Point

interface Shape {
    val isClosed: Boolean

    fun center(): Shape

    fun centerAt(point: Point): Shape

    fun normalize(): Shape
}
