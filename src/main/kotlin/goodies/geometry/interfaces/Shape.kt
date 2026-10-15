package goodies.geometry.interfaces

import goodies.geometry.Point

/** Base of every geometric shape. */
interface Shape {
    val isClosed: Boolean

    /** Returns this shape moved so that its centre is at the origin. */
    fun center(): Shape

    /** Returns this shape moved so that its centre is at [point]. */
    fun centerAt(point: Point): Shape

    /** Returns a normalised copy of this shape; each shape defines how. */
    fun normalize(): Shape
}
