package goodies.geometry.interfaces

import goodies.geometry.Point

interface Shape {
    fun center(): Shape

    fun centerAt(point: Point): Shape

    fun normalize(): Shape

    val isClosed: Boolean
}
