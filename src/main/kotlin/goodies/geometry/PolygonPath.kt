package goodies.geometry

import goodies.geometry.interfaces.OpenShape
import goodies.geometry.interfaces.PolygonLikeShape
import goodies.geometry.interfaces.Shape
import processing.core.PApplet

class PolygonPath(override val vertices: List<Point>) : PolygonLikeShape, OpenShape, Iterable<Point> {
    constructor(vararg vertices: Point) : this(vertices.toList())

    override fun center(): PolygonPath {
        return PolygonPath(vertexMatrix.center().rows)
    }

    fun translate(translation: Point): PolygonPath {
        return PolygonPath(vertices.map { it + translation })
    }

    override fun centerAt(point: Point): Shape {
        return center().translate(point)
    }

    override fun normalize(): PolygonPath {
        return PolygonPath(vertexMatrix.normalize().rows)
    }

    val start: Point
        get() {
            return vertices.first()
        }

    val end: Point
        get() {
            return vertices.last()
        }

    override fun get(index: Int): Point {
        return super.get(index)
    }

    val directions: PolygonPath
        get() {
            val diffs = mutableListOf<Point>()
            for (i in 0 until vertices.size - 1) {
                val diff = vertices[i + 1] - vertices[i]
                diffs.add(diff)
            }
            return PolygonPath(diffs)
        }

    override fun iterator(): Iterator<Point> {
        return vertices.iterator()
    }

    fun draw(sketch: PApplet) {
        for (line in edges) {
            line.draw(sketch)
        }
    }
}
