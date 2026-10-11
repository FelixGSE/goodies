package goodies.geometry.interfaces

import goodies.algebra.GMatrix
import goodies.geometry.Point

interface DiscreteShape {
    val vertices: List<Point>

    val vertexCount: Int get() = vertices.size
    val vertexMatrix: GMatrix get() = GMatrix(vertices, orientation = "row")

    fun addDimensions(vararg dimension: Number): Shape
}
