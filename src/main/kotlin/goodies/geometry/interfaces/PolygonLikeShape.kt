package goodies.geometry.interfaces

import goodies.geometry.Line
import goodies.geometry.Point
import goodies.geometry.PolygonPath
import goodies.utils.listOfPointsToListOfLines

/** Shape of vertices joined by straight edges; a closed one also joins the last vertex to the first. */
interface PolygonLikeShape :
    Shape,
    DiscreteShape {
    val edges: List<Line>
        get() = listOfPointsToListOfLines(vertices, closed = isClosed)

    val edgeCount: Int
        get() = edges.size

    /** Returns a copy of vertex [index]. */
    operator fun get(index: Int): Point = vertices[index].copy()

    override fun addDimensions(vararg dimension: Number): Shape = PolygonPath(vertices.map { it.extend(*dimension) })

    /** Returns the index of the first longest edge. */
    fun longestEdgeIndex(): Int {
        val edgeLengths = edges.map { it.length }
        return edgeLengths.indexOf(edgeLengths.max())
    }

    /** Returns the first longest edge. */
    fun longestEdge(): Line = edges[longestEdgeIndex()]
}
