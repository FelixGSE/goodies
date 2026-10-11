package goodies.utils

import goodies.geometry.Line
import goodies.geometry.Point

fun listOfPointsToListOfLines(
    points: List<Point>,
    closed: Boolean = false,
): List<Line> {
    val lines = mutableListOf<Line>()
    for (i in 0 until points.size - 1) {
        val currentPoint = points[i]
        val nextPoint = points[i + 1]
        lines.add(Line(currentPoint, nextPoint))
    }
    if (closed) {
        lines.add(Line(points.last(), points.first()))
    }
    return lines.toList()
}

fun directionsForListOfPoints(
    points: List<Point>,
    closed: Boolean = false,
): List<Point> {
    val diffs = mutableListOf<Point>()
    for (i in 0 until points.size - 1) {
        val diff = points[i + 1] - points[i]
        diffs.add(diff)
    }
    if (closed) {
        diffs.add(points.first() - points.last())
    }
    return diffs
}

fun maxOfListOfPoints(points: List<Point>): Point {
    var max = points.first()
    for (point in points) {
        if (point.x > max.x) {
            max = point
        }
        if (point.y > max.y) {
            max = point
        }
    }
    return max
}
