package goodies.brush

import extensions.modules.shapes.Line
import goodies.geometry.Point
import goodies.geometry.PolygonPath
import org.apache.commons.math3.distribution.MultivariateNormalDistribution
import org.apache.commons.math3.distribution.NormalDistribution
import processing.core.PApplet
import kotlin.math.abs

class GaussBrush(
    private val brushPins: List<Point>,
    private val pinSizes: List<Float>,
    private val sizeNoiseScale: Float,
    private val sizeNoiseLowerBound: Float,
    private val sizeNoiseUpperBound: Float,
    private val pinOpacity: List<Float>,
    private val color: Int = 0,
) : Brush {
    private val numberOfPoints = brushPins.size

    companion object {
        fun initBrush(
            start: Point,
            numberOfPoints: Int,
            variance: Number,
            sizeMean: Number = 1.0,
            sizeStd: Number = 1.0,
            sizeNoiseScale: Number = 0.0002,
            sizeNoiseLowerBound: Number = 0,
            sizeNoiseUpperBound: Number = 0,
            opacityMean: Number = 1.0,
            opacityStd: Number = 1.0,
            color: Int = 0,
        ): GaussBrush {
            val covariance =
                arrayOf(
                    doubleArrayOf(variance.toDouble(), 0.0),
                    doubleArrayOf(0.0, variance.toDouble()),
                )
            val brushPins =
                List(numberOfPoints) {
                    MultivariateNormalDistribution(start.toDoubleArray(), covariance).sample().toTypedArray()
                }.map { Point(*it) }
            val pinSizes =
                List(numberOfPoints) {
                    abs(
                        NormalDistribution(
                            sizeMean.toDouble(),
                            sizeStd.toDouble(),
                        ).sample(),
                    ).toFloat()
                }
            val pinOpacity =
                List(numberOfPoints) {
                    abs(
                        NormalDistribution(
                            opacityMean.toDouble(),
                            opacityStd.toDouble(),
                        ).sample(),
                    ).toFloat()
                }
            return GaussBrush(
                brushPins,
                pinSizes,
                sizeNoiseScale.toFloat(),
                sizeNoiseLowerBound.toFloat(),
                sizeNoiseUpperBound.toFloat(),
                pinOpacity,
                color,
            )
        }
    }

    fun draw(
        sketch: PApplet,
        path: PolygonPath,
    ) {
        var currentPositions = brushPins.toMutableList()
        for (direction in path.directions) {
            for (i in 0 until numberOfPoints) {
                val currentPosition = currentPositions[i]
                val nextPosition = currentPosition + direction
                val line = Line(currentPosition.toPVector(), nextPosition.toPVector())
                val midpoint = line.midPoint
                val lineThicknessNoise =
                    PApplet.map(
                        sketch.noise(midpoint.x * sizeNoiseScale, midpoint.y * sizeNoiseScale),
                        0F,
                        1F,
                        sizeNoiseLowerBound,
                        sizeNoiseUpperBound,
                    )
                val weight = pinSizes[i] + lineThicknessNoise
                sketch.strokeWeight(pinSizes[i] + weight)
                sketch.stroke(color, pinOpacity[i])
                line.draw(sketch)
                currentPositions[i] = nextPosition
            }
        }
    }
}
