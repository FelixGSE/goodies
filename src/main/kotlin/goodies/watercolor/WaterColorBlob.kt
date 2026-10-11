package goodies.watercolor

import goodies.geometry.Line
import goodies.geometry.Point
import goodies.geometry.Polygon
import processing.core.PApplet
import java.util.Random
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
        repeat(n) {
            deformedPolygons.add(deform(basePolygon, depth))
        }
    }

    fun draw(sketch: PApplet) {
        for (polygon in deformedPolygons) {
            polygon.draw(sketch)
        }
    }

    companion object {
        private val rng = Random()

        fun deform(
            polygon: Polygon,
            depth: Int,
        ): Polygon {
            val points = mutableListOf<Point>()
            for (edge in polygon.edges) {
                points.add(edge.start)
                subdivide(
                    points = points,
                    line = edge,
                    depth = depth - 1,
                    lengthVariance = BASE_VARIANCE / (depth + 1),
                    angleVariance =
                        BASE_VARIANCE / (depth + 1),
                )
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
                    y = (line.end.y - line.start.y).toDouble(),
                    x = (line.end.x - line.start.x).toDouble(),
                ).toFloat()
            val perpAngle =
                baseAngle + Math.PI.toFloat() / 2f +
                    (rng.nextGaussian() * angleVariance * Math.PI / 180.0).toFloat()

            val splitPoint =
                Point(
                    midPoint.x + cos(perpAngle.toDouble()).toFloat() * displacement,
                    midPoint.y + sin(perpAngle.toDouble()).toFloat() * displacement,
                )

            val left = Line(a = line.start, b = splitPoint)
            val right = Line(a = splitPoint, b = line.end)
            val nextLengthVariance = BASE_VARIANCE / (2 * depth.coerceAtLeast(1) + 1)
            val nextAngleVariance = BASE_VARIANCE / (depth.coerceAtLeast(1) + 1)

            subdivide(
                points = points,
                line = left,
                depth = depth - 1,
                lengthVariance = nextLengthVariance,
                angleVariance = nextAngleVariance,
            )
            points.add(splitPoint)
            subdivide(
                points = points,
                line = right,
                depth = depth - 1,
                lengthVariance = nextLengthVariance,
                angleVariance = nextAngleVariance,
            )
        }
    }
}

private const val BASE_VARIANCE = 100f
