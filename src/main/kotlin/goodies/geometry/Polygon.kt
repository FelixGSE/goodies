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

/** Closed polygon through [vertices] in order; the last vertex joins the first. */
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

    /** Returns this polygon moved by [translation]. */
    fun translate(translation: Point): Polygon = Polygon(vertices.map { it + translation })

    /** Returns this 2D polygon rotated by [angle] degrees around [referencePoint]. */
    fun rotateAroundReferencePoint(
        angle: Number,
        referencePoint: Point,
    ): Polygon =
        Polygon(
            vertices.map {
                it - referencePoint
            },
        ).rotate(angle).translate(referencePoint)

    /** Returns this 2D polygon with every vertex rotated by [angle] degrees around the origin, as [Point.rotate]. */
    fun rotate(angle: Number): Polygon = Polygon(vertices.map { it.rotate(angle) })

    /**
     * Returns a random point on the boundary: an edge drawn with probability proportional to its length, then a uniform
     * point on it.
     */
    fun randomPointOn(): Point {
        val weights = edges.map { it.length / perimeter }
        return weightedSample(edges, weights).randomPointOn()
    }

    /**
     * Returns the convex hull of the 2D vertices by gift wrapping (Jarvis march), O(n h) for n vertices and h hull
     * vertices.
     *
     * The hull starts at the vertex with the smallest x, then smallest y, and keeps neither collinear nor repeated
     * vertices. A polygon with at most three vertices is returned as it is.
     */
    fun convexHull(): Polygon {
        if (vertices.size <= TRIANGLE_VERTICES) {
            return Polygon(vertices.toList())
        }

        val hull = mutableListOf<Point>()
        val startPoint = startVertex()
        var currentVertex = startPoint
        do {
            hull.add(currentVertex)
            currentVertex = nextHullVertex(currentVertex)
        } while (currentVertex != startPoint)

        return Polygon(hull)
    }

    private fun startVertex(): Point =
        vertices.reduce { start, vertex ->
            val isLowerOnSameX = vertex.x == start.x && vertex.y < start.y
            if (vertex.x < start.x || isLowerOnSameX) vertex else start
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

    /**
     * Returns the unsigned angle in radians at each vertex, starting with the second one; a reflex angle comes out as
     * its complement to a full turn.
     */
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

    /** Draws this 2D polygon on [sketch] as one closed shape. */
    fun draw(sketch: PApplet) {
        sketch.beginShape()
        for (vertex in vertices) {
            sketch.vertex(vertex.x, vertex.y)
        }
        sketch.endShape(CLOSE)
    }

    /**
     * Returns nested polygons, each through points at a fraction along the edges of the one before.
     *
     * Only the first level draws its fraction at random from [lower, upper]; the deeper levels always use 0.1.
     * DESIGN: CODE-1 asks for [lower, upper] on every level.
     *
     * The result has [iterations] + 1 entries, innermost first; the innermost appears twice and this polygon is not
     * included.
     *
     * Costs O(k² + k·n) for k [iterations] on n vertices: each level copies the list of the levels below it.
     */
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
