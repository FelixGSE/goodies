package goodies.curves

import goodies.geometry.Line
import goodies.geometry.Point
import goodies.geometry.Polygon
import goodies.geometry.PolygonPath
import goodies.geometry.interfaces.PolygonLikeShape
import processing.core.PApplet

class ChaikinCurve(polygon: PolygonLikeShape, iterations: Int) {

    val fittedPolygon: PolygonLikeShape = fitChaikinCurve(polygon.edges, iterations, polygon.isClosed)

    fun draw(sketch: PApplet) {
        sketch.beginShape()
        for (vertex in fittedPolygon.vertices) {
            sketch.vertex(vertex.x, vertex.y)
        }
        sketch.endShape()
    }

    companion object {
        private fun shortenLine(line: Line, ratio: Double = 0.5): Line {
            val split = ratio / 2.0
            val newStart = line.scaleFromStart(split).end
            val newEnd = line.scaleFromStart(1.0 - split).end
            return Line(newStart, newEnd)
        }

        private fun fitChaikinCurve(
            initialEdges: List<Line>,
            iterations: Int,
            closed: Boolean
        ): PolygonLikeShape {
            var lines = initialEdges

            for (i in 0..iterations) {
                val shorts = lines.map { shortenLine(it) }

                val newLines = mutableListOf<Line>()
                for (j in 0 until shorts.size - 1) {
                    val thisLine = shorts[j]
                    val nextLine = shorts[j + 1]
                    val connectingLine = Line(thisLine.end, nextLine.start)
                    newLines.add(thisLine)
                    newLines.add(connectingLine)
                    if (j == shorts.size - 2) {
                        newLines.add(nextLine)
                    }
                }
                if (closed) {
                    val endLine = Line(shorts.last().end, newLines.first().start)
                    newLines.add(endLine)
                }
                lines = newLines
            }

            val points = linesToPoints(lines)
            return if (closed) Polygon(points) else PolygonPath(points)
        }

        private fun linesToPoints(lines: List<Line>): List<Point> {
            if (lines.isEmpty()) return emptyList()
            val points = mutableListOf(lines.first().start)
            for (line in lines) {
                points.add(line.end)
            }
            return points
        }
    }
}
