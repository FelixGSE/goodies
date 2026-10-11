package goodies.geometry.interfaces

import goodies.geometry.Line
import goodies.geometry.Point
import goodies.geometry.PolygonPath
import goodies.utils.listOfPointsToListOfLines

interface PolygonLikeShape :
    Shape,
    DiscreteShape {
    operator fun get(index: Int): Point = vertices[index].copy()

    override fun addDimensions(vararg dimension: Number): Shape = PolygonPath(vertices.map { it.extend(*dimension) })

    val edges: List<Line>
        get() {
            return listOfPointsToListOfLines(vertices, closed = isClosed)
        }

    val edgeCount: Int
        get() {
            return edges.size
        }

    fun longestEdgeIndex(): Int {
        val edgeLengths = edges.map { it.length }
        return edgeLengths.indexOf(edgeLengths.max())
    }

    fun longestEdge(): Line = edges[longestEdgeIndex()]
}
