package goodies.draw

import goodies.geometry.Point
import processing.core.PApplet
import java.awt.Color

fun hexColorToRGBInt(hex: String): Int = Color.decode(hex).rgb

fun scaleToScreen(
    points: Collection<Point>,
    sketch: PApplet,
    margin: Number,
): Collection<Point> {
    val minX = points.minOf { it.x }
    val maxX = points.maxOf { it.x }
    val minY = points.minOf { it.y }
    val maxY = points.maxOf { it.y }

    return if (sketch.width > sketch.height) {
        val scale = sketch.height.toDouble() / (maxY - minY)
        points.map { it.normalize().scale(scale - margin.toFloat()) }
    } else {
        val scale = sketch.width.toDouble() / (maxX - minX)
        points.map { it.normalize().scale(scale - margin.toFloat()) }
    }
}

fun scaleToScreen2(
    points: Collection<Point>,
    sketch: PApplet,
    margin: Number,
): Float {
    val minX = points.minOf { it.x }
    val maxX = points.maxOf { it.x }
    val minY = points.minOf { it.y }
    val maxY = points.maxOf { it.y }

    return if (sketch.width > sketch.height) {
        sketch.height / (maxY - minY)
    } else {
        sketch.width / (maxX - minX)
    }
}
