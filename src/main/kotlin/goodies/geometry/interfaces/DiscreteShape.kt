package goodies.geometry.interfaces

import goodies.algebra.GMatrix
import goodies.algebra.ROW
import goodies.geometry.Point

/** Shape given by a finite list of vertices. */
interface DiscreteShape {
    val vertices: List<Point>

    val vertexCount: Int get() = vertices.size
    val vertexMatrix: GMatrix get() = GMatrix(vertices, orientation = ROW)

    /** Returns this shape with [dimension] appended to the coordinates of every vertex. */
    fun addDimensions(vararg dimension: Number): Shape
}
