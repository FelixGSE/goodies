package goodies.geometry.interfaces

import goodies.geometry.Line
import goodies.geometry.Point
import goodies.geometry.PolygonPath
import goodies.utils.listOfPointsToListOfLines

interface PolygonLikeShape :
    Shape,
    DiscreteShape {
    val edges: List<Line>
        get() = listOfPointsToListOfLines(vertices, closed = isClosed)

    val edgeCount: Int
        get() = edges.size

    operator fun get(index: Int): Point = vertices[index].copy()

    override fun addDimensions(vararg dimension: Number): Shape = PolygonPath(vertices.map { it.extend(*dimension) })

    fun longestEdgeIndex(): Int {
        val edgeLengths = edges.map { it.length }
        return edgeLengths.indexOf(edgeLengths.max())
    }

    fun longestEdge(): Line = edges[longestEdgeIndex()]
}
