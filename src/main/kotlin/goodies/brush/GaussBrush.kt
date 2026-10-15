package goodies.brush

import extensions.modules.shapes.Line
import goodies.geometry.Point
import goodies.geometry.PolygonPath
import processing.core.PApplet

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

    fun draw(
        sketch: PApplet,
        path: PolygonPath,
    ) {
        val currentPositions = brushPins.toMutableList()
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
            val covariance = arrayOf(doubleArrayOf(variance.toDouble(), 0.0), doubleArrayOf(0.0, variance.toDouble()))
            val brushPins = samplePins(start.toDoubleArray(), covariance, numberOfPoints)
            val pinSizes = absoluteNormalSamples(mean = sizeMean, std = sizeStd, count = numberOfPoints)
            val pinOpacity = absoluteNormalSamples(mean = opacityMean, std = opacityStd, count = numberOfPoints)
            return GaussBrush(
                brushPins = brushPins,
                pinSizes = pinSizes,
                sizeNoiseScale = sizeNoiseScale.toFloat(),
                sizeNoiseLowerBound = sizeNoiseLowerBound.toFloat(),
                sizeNoiseUpperBound = sizeNoiseUpperBound.toFloat(),
                pinOpacity = pinOpacity,
                color = color,
            )
        }
    }
}
