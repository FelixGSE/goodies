package goodies.grid

import extensions.modules.shapes.Quadrilateral
import goodies.geometry.Point
import processing.core.PApplet
import utils.linSpace

class Grid2D(
    yMin: Number,
    yMax: Number,
    yParts: Int,
    xMin: Number,
    xMax: Number,
    xParts: Int,
) {
    private val gridPoints: List<List<Point>>
    val nRows: Int
    val nCols: Int
    private val cells: List<List<Quadrilateral>>
    val nCells: Int

    init {
        val rowSpace = linSpace(start = yMin, stop = yMax, num = yParts)
        nRows = rowSpace.size
        val columnSpace = linSpace(start = xMin, stop = xMax, num = xParts)
        nCols = columnSpace.size
        gridPoints = cartesian(rowSpace, columnSpace)
        cells = getCells(gridPoints, nRows, nCols)
        nCells = cells.sumOf { it.size }
    }

    operator fun get(
        i: Int,
        j: Int,
    ): Point {
        assert(i in 0..nRows)
        assert(j in 0..nCols)
        return gridPoints[i][j]
    }

    fun getCell(
        i: Int,
        j: Int,
    ): Quadrilateral {
        assert(i >= 0 && i <= nRows - 1)
        assert(j >= 0 && j <= nCols - 1)
        return cells[i][j]
    }

    fun getFlatCells(): List<Quadrilateral> = cells.flatten()

    private fun getCells(
        gridPoints: List<List<Point>>,
        nRows: Int,
        nCols: Int,
    ): List<List<Quadrilateral>> {
        val cellGrid = mutableListOf<List<Quadrilateral>>()
        for (i in 0 until nRows - 1) {
            val row = mutableListOf<Quadrilateral>()
            for (j in 0 until nCols - 1) {
                val a = gridPoints[i][j].toPVector()
                val b = gridPoints[i][j + 1].toPVector()
                val c = gridPoints[i + 1][j + 1].toPVector()
                val d = gridPoints[i + 1][j].toPVector()
                row.add(Quadrilateral(a, b, c, d))
            }
            cellGrid.add(row.toList())
        }
        return cellGrid.toList()
    }

    private fun cartesian(
        rowSpace: List<Number>,
        columnSpace: List<Number>,
    ): List<List<Point>> {
        val cartesianGrid = mutableListOf<List<Point>>()
        for (rowCoordinate in rowSpace) {
            val row = mutableListOf<Point>()
            for (columnCoordinate in columnSpace) {
                row.add(Point(rowCoordinate, columnCoordinate))
            }
            cartesianGrid.add(row.toList())
        }
        return cartesianGrid.toList()
    }

    fun draw(sketch: PApplet) {
        for (point in gridPoints.flatten()) {
            point.draw(sketch)
        }
    }

    fun drawCells(sketch: PApplet) {
        for (cell in cells.flatten()) {
            cell.draw(sketch)
        }
    }
}
