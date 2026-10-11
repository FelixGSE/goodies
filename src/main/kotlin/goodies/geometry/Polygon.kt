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

    val directions: PolygonPath
        get() {
            return PolygonPath(directionsForListOfPoints(vertices, isClosed))
        }

    override val centroid: Point
        get() {
            return Point(GMatrix(vertices).rows.map { it.average() })
        }

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

    val perimeter: Float
        get() {
            return edges.map { it.length }.sum()
        }

    fun randomPointOn(): Point {
        val weights = edges.map { it.length / perimeter }
        return weightedSample(edges, weights).randomPointOn()
    }

//    fun getIntersections(otherPolygon: Polygon): List<Point> {
//        val edges1 = this.edges
//        val edges2 = otherPolygon.edges
//
//        val allEdges = edges1 + edges2
//
//        val intersectionPoints = mutableListOf<Point>()
//
//        for (i in allEdges.indices) {
//            val edge1 = allEdges[i]
//
//            for (j in i + 1 until allEdges.size) {
//                val edge2 = allEdges[j]
//                val intersection = edge1.computeSegmentIntersection(edge2)
//
//                if (intersection != null) {
//                    intersectionPoints.add(Point.fromPVector2D(intersection))
//                }
//            }
//        }
//
//        return intersectionPoints
//    }

    fun convexHull(): Polygon {
        if (vertices.size <= 3) {
            return Polygon(vertices.toList())
        }

        val hull = mutableListOf<Point>()

        var startPoint = vertices[0]
        for (vertex in vertices) {
            if (vertex.x < startPoint.x || (vertex.x == startPoint.x && vertex.y < startPoint.y)) {
                startPoint = vertex
            }
        }

        var currentVertex = startPoint
        do {
            hull.add(currentVertex)

            var nextVertex = vertices[0]
            for (vertex in vertices) {
                if (vertex == currentVertex) continue

                val cross =
                    (vertex.x - currentVertex.x) * (nextVertex.y - currentVertex.y) -
                        (vertex.y - currentVertex.y) * (nextVertex.x - currentVertex.x)

                if (nextVertex == currentVertex || cross > 0 || (
                        cross == 0.0F && vertex.distance(currentVertex) >
                            nextVertex.distance(
                                currentVertex,
                            )
                    )
                ) {
                    nextVertex = vertex
                }
            }

            currentVertex = nextVertex
        } while (currentVertex != startPoint)

        return Polygon(hull)
    }

    override val isConvex: Boolean
        get() {
            if (vertices.size <= 3) {
                return true
            }

            return convexHull().vertices.size == vertices.size
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
    ): List<Polygon> = polygonSpiral(this, iterations, lower, upper)

    private fun polygonSpiral(
        polygon: Polygon,
        iterations: Int,
        lower: Number = 0.1,
        upper: Number = 0.1,
    ): List<Polygon> {
        if (iterations == 0) {
            return listOf(polygon)
        }

        val scale = randomFloat(lower.toFloat(), upper.toFloat())

        val newPolygon = Polygon(polygon.edges.map { (it.scaleFromStart(scale).end) }.toList())

        return polygonSpiral(newPolygon, iterations - 1) + listOf(newPolygon)
    }

    override fun contains(point: Point): Boolean {
        var inside = false
        var i = 0
        var j = vertices.size - 1
        while (i < vertices.size) {
            if ((vertices[i].y > point.y) != (vertices[j].y > point.y) &&
                (
                    point.x <
                        (vertices[j].x - vertices[i].x) * (point.y - vertices[i].y) / (vertices[j].y - vertices[i].y) +
                        vertices[i].x
                )
            ) {
                inside = !inside
            }
            j = i++
        }
        return inside
    }
}
