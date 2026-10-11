package goodies.geometry

import goodies.geometry.interfaces.OpenShape
import goodies.geometry.interfaces.PolygonLikeShape
import goodies.geometry.interfaces.Shape
import processing.core.PApplet

/** Open polyline through [vertices] in order. */
class PolygonPath(
    override val vertices: List<Point>,
) : PolygonLikeShape,
    OpenShape,
    Iterable<Point> {
    val start: Point
        get() = vertices.first()

    val end: Point
        get() = vertices.last()

    val directions: PolygonPath
        get() {
            val diffs = mutableListOf<Point>()
            for (i in 0 until vertices.size - 1) {
                val diff = vertices[i + 1] - vertices[i]
                diffs.add(diff)
            }
            return PolygonPath(diffs)
        }

    constructor(vararg vertices: Point) : this(vertices.toList())

    override fun center(): PolygonPath = PolygonPath(vertexMatrix.center().rows)

    /** Returns this path moved by [translation]. */
    fun translate(translation: Point): PolygonPath = PolygonPath(vertices.map { it + translation })

    override fun centerAt(point: Point): Shape = center().translate(point)

    override fun normalize(): PolygonPath = PolygonPath(vertexMatrix.normalize().rows)

    override fun iterator(): Iterator<Point> = vertices.iterator()

    /** Draws every edge on [sketch]. */
    fun draw(sketch: PApplet) {
        for (line in edges) {
            line.draw(sketch)
        }
    }
}
