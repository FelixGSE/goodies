package goodies.curves

import goodies.geometry.Point
import kotlin.math.cos

fun cosineCurve(
    xStart: Number = 0.0,
    xEnd: Number = 10.0,
    stepSize: Number = 0.1,
    amplitude: Number = 1.0,
    ordinaryFrequency: Number = 1.0,
    phase: Number = 0.0,
    shift: Number = 0.0,
): List<Point> {
    val xStartValue = xStart.toDouble()
    val xEndValue = xEnd.toDouble()
    val stepSizeValue = stepSize.toDouble()

    require(xStartValue < xEndValue) { "xStart must be less than xEnd" }
    require(stepSizeValue > 0) { "stepSize must be greater than 0" }

    val curvePoints = mutableListOf<Point>()

    var x = xStartValue
    while (x <= xEndValue) {
        val y = amplitude.toDouble() * cos(ordinaryFrequency.toDouble() * x + phase.toDouble()) + shift.toDouble()
        curvePoints.add(Point(x, y))
        x += stepSizeValue
    }

    return curvePoints
}
