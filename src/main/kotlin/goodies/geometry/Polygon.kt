package goodies.geometry

import goodies.algebra.GMatrix
import goodies.geometry.interfaces.ClosedShape
import goodies.geometry.interfaces.Geom
import goodies.geometry.interfaces.PolygonLikeShape
import goodies.utils.directionsForListOfPoints
import goodies.utils.randomFloat
import goodies.utils.weightedSample
import processing.core.PApplet
import processing.core.PConstants.CLOSE

open class Polygon(
    override val vertices: List<Point>,
) : PolygonLikeShape,
    ClosedShape,
    Geom {
    val directions: PolygonPath
        get() = PolygonPath(directionsForListOfPoints(vertices, isClosed))

    override val centroid: Point
        get() = Point(GMatrix(vertices).rows.map { it.average() })

    val perimeter: Float
        get() = edges.map { it.length }.sum()

    override val isConvex: Boolean
        get() {
            if (vertices.size <= TRIANGLE_VERTICES) {
                return true
            }

            return convexHull().vertices.size == vertices.size
        }

    constructor(vararg vertices: Point) : this(vertices.toList())

    override fun translate(vararg translation: Number): Polygon {
        TODO("Not yet implemented")
    }

    override fun translate(translation: Number): Polygon = Polygon(vertices.map { it.translate(translation) })

    override fun scale(factor: Number): Polygon = Polygon(vertices.map { it.scale(factor) })

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Polygon) return false

        return vertices == other.vertices
    }

    override fun hashCode(): Int = vertices.hashCode()

    override fun center(): Polygon = Polygon(vertexMatrix.center().rows)

    override fun centerAt(point: Point): Polygon = center().translate(point)

    override fun normalize(): Polygon = Polygon(GMatrix(vertices).normalize().rows)

    fun translate(translation: Point): Polygon = Polygon(vertices.map { it + translation })

    fun rotateAroundReferencePoint(
        angle: Number,
        referencePoint: Point,
    ): Polygon =
        Polygon(
            vertices.map {
                it - referencePoint
            },
        ).rotate(angle).translate(referencePoint)

    fun rotate(angle: Number): Polygon = Polygon(vertices.map { it.rotate(angle) })

    fun randomPointOn(): Point {
        val weights = edges.map { it.length / perimeter }
        return weightedSample(edges, weights).randomPointOn()
    }

    fun convexHull(): Polygon {
        if (vertices.size <= TRIANGLE_VERTICES) {
            return Polygon(vertices.toList())
        }

        val hull = mutableListOf<Point>()
        val startPoint = vertices.minWith(compareBy<Point>({ it.x }, { it.y }))
        var currentVertex = startPoint
        do {
            hull.add(currentVertex)
            currentVertex = nextHullVertex(currentVertex)
        } while (currentVertex != startPoint)

        return Polygon(hull)
    }

    private fun nextHullVertex(current: Point): Point {
        var next = vertices[0]
        for (vertex in vertices) {
            if (vertex != current && isBetterHullCandidate(vertex, current, next)) {
                next = vertex
            }
        }
        return next
    }

    private fun isBetterHullCandidate(
        candidate: Point,
        current: Point,
        next: Point,
    ): Boolean {
        if (next == current) {
            return true
        }
        val cross =
            (candidate.x - current.x) * (next.y - current.y) -
                (candidate.y - current.y) * (next.x - current.x)
        val isCollinearAndFarther = cross == 0.0F && candidate.distance(current) > next.distance(current)
        return cross > 0 || isCollinearAndFarther
    }

    fun interiorAngles(): List<Float> {
        val n = vertices.size
        val angles = mutableListOf<Float>()

        for (i in 0 until n) {
            val p1 = vertices[i]
            val p2 = vertices[(i + 1) % n]
            val p3 = vertices[(i + 2) % n]

            val v1 = Point(p1.x - p2.x, p1.y - p2.y)
            val v2 = Point(p3.x - p2.x, p3.y - p2.y)
            val angle = v1.angleBetween(v2)
            angles.add(angle)
        }

        return angles
    }

    fun draw(sketch: PApplet) {
        sketch.beginShape()
        for (vertex in vertices) {
            sketch.vertex(vertex.x, vertex.y)
        }
        sketch.endShape(CLOSE)
    }

    fun polygonSpiral(
        iterations: Int,
        lower: Number = 0.1,
        upper: Number = 0.1,
    ): List<Polygon> = polygonSpiral(polygon = this, iterations = iterations, lower = lower, upper = upper)

    private fun polygonSpiral(
        polygon: Polygon,
        iterations: Int,
        lower: Number = 0.1,
        upper: Number = 0.1,
    ): List<Polygon> {
        if (iterations == 0) {
            return listOf(polygon)
        }

        val scale = randomFloat(a = lower.toFloat(), b = upper.toFloat())

        val newPolygon = Polygon(polygon.edges.map { it.scaleFromStart(scale).end }.toList())

        return polygonSpiral(newPolygon, iterations - 1) + listOf(newPolygon)
    }

    override fun contains(point: Point): Boolean {
        var isInside = false
        var i = 0
        var j = vertices.size - 1
        while (i < vertices.size) {
            if (vertices[i].y > point.y != vertices[j].y > point.y &&
                point.x <
                (vertices[j].x - vertices[i].x) * (point.y - vertices[i].y) / (vertices[j].y - vertices[i].y) +
                vertices[i].x
            ) {
                isInside = !isInside
            }
            j = i++
        }
        return isInside
    }
}

private const val TRIANGLE_VERTICES = 3
