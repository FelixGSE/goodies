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
        ): Polygon = deform(polygon = polygon, depth = depth, random = rng)

        internal fun deform(
            polygon: Polygon,
            depth: Int,
            random: Random,
        ): Polygon {
            val subdivider = EdgeSubdivider(random)
            val initialVariance = BASE_VARIANCE / (depth + 1)
            val variance = SubdivisionVariance(length = initialVariance, angle = initialVariance)
            for (edge in polygon.edges) {
                subdivider.points.add(edge.start)
                subdivider.subdivide(line = edge, depth = depth - 1, variance = variance)
            }
            return Polygon(subdivider.points)
        }

        private class EdgeSubdivider(
            private val random: Random,
        ) {
            val points = mutableListOf<Point>()

            fun subdivide(
                line: Line,
                depth: Int,
                variance: SubdivisionVariance,
            ) {
                if (depth < 0) return

                val splitPoint = displacedSplitPoint(line = line, variance = variance)
                val left = Line(a = line.start, b = splitPoint)
                val right = Line(a = splitPoint, b = line.end)
                val next =
                    SubdivisionVariance(
                        length = BASE_VARIANCE / (2 * depth.coerceAtLeast(1) + 1),
                        angle = BASE_VARIANCE / (depth.coerceAtLeast(1) + 1),
                    )

                subdivide(line = left, depth = depth - 1, variance = next)
                points.add(splitPoint)
                subdivide(line = right, depth = depth - 1, variance = next)
            }

            private fun displacedSplitPoint(
                line: Line,
                variance: SubdivisionVariance,
            ): Point {
                val midFactor = MIN_SPLIT_FRACTION + random.nextDouble() * SPLIT_FRACTION_SPREAD
                val midPoint = line.scaleFromStart(midFactor).end

                val displacement = (random.nextGaussian() * variance.length).toFloat()
                val baseAngle =
                    atan2(
                        y = (line.end.y - line.start.y).toDouble(),
                        x = (line.end.x - line.start.x).toDouble(),
                    ).toFloat()
                val perpAngle =
                    baseAngle + Math.PI.toFloat() / 2f +
                        (random.nextGaussian() * variance.angle * Math.PI / 180.0).toFloat()

                return Point(
                    midPoint.x + cos(perpAngle.toDouble()).toFloat() * displacement,
                    midPoint.y + sin(perpAngle.toDouble()).toFloat() * displacement,
                )
            }
        }

        private data class SubdivisionVariance(
            val length: Float,
            val angle: Float,
        )
    }
}

private const val BASE_VARIANCE = 100f
private const val MIN_SPLIT_FRACTION = 0.3
private const val SPLIT_FRACTION_SPREAD = 0.4
