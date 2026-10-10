package goodies.watercolor

import goodies.geometry.Line
import goodies.geometry.Point
import goodies.geometry.Polygon
import processing.core.PApplet
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

class WaterColorBlob(
    val basePolygon: Polygon,
) {
    val deformedPolygons: MutableList<Polygon> = mutableListOf()

    fun createDeformedPolygons(
        n: Int,
        depth: Int,
    ) {
        for (i in 0 until n) {
            deformedPolygons.add(deform(basePolygon, depth))
        }
    }

    fun draw(sketch: PApplet) {
        for (polygon in deformedPolygons) {
            polygon.draw(sketch)
        }
    }

    companion object {
        private val rng = java.util.Random()

        fun deform(
            polygon: Polygon,
            depth: Int,
        ): Polygon {
            val points = mutableListOf<Point>()
            for (edge in polygon.edges) {
                points.add(edge.start)
                subdivide(points, edge, depth - 1, 100f / (depth + 1), 100f / (depth + 1))
            }
            return Polygon(points)
        }

        private fun subdivide(
            points: MutableList<Point>,
            line: Line,
            depth: Int,
            lengthVariance: Float,
            angleVariance: Float,
        ) {
            if (depth < 0) return

            val midFactor = 0.3 + rng.nextDouble() * 0.4
            val midPoint = line.scaleFromStart(midFactor).end

            val displacement = (rng.nextGaussian() * lengthVariance).toFloat()
            val baseAngle =
                atan2(
                    (line.end.y - line.start.y).toDouble(),
                    (line.end.x - line.start.x).toDouble(),
                ).toFloat()
            val perpAngle =
                baseAngle + Math.PI.toFloat() / 2f +
                    (rng.nextGaussian() * angleVariance * Math.PI / 180.0).toFloat()

            val splitPoint =
                Point(
                    midPoint.x + cos(perpAngle.toDouble()).toFloat() * displacement,
                    midPoint.y + sin(perpAngle.toDouble()).toFloat() * displacement,
                )

            val left = Line(line.start, splitPoint)
            val right = Line(splitPoint, line.end)
            val nextLengthVariance = 100f / (2 * (depth.coerceAtLeast(1)) + 1)
            val nextAngleVariance = 100f / (depth.coerceAtLeast(1) + 1)

            subdivide(points, left, depth - 1, nextLengthVariance, nextAngleVariance)
            points.add(splitPoint)
            subdivide(points, right, depth - 1, nextLengthVariance, nextAngleVariance)
        }
    }
}
