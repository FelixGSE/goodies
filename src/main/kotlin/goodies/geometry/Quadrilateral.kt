package goodies.geometry

import ShapeTree
import goodies.containers.ShapeTreeNode
import processing.core.PApplet

open class Quadrilateral(vertices: List<Point>) : Polygon(vertices) {

    constructor(vararg vertices: Point) : this(vertices.toList())

    constructor(first: Point, second: Point, third: Point, fourth: Point) : this(listOf(first, second, third, fourth))

    init {
        require(vertices.size == 4) { "Quadrilateral must have 4 vertices" }
    }


    val a: Point
        get() {
            return vertices[0]
        }
    val b: Point
        get() {
            return vertices[1]
        }
    val c: Point
        get() {
            return vertices[2]
        }
    val d: Point
        get() {
            return vertices[3]
        }

    private fun oppositeSideIndex(side: Int): Int {
        return when (side) {
            0 -> 2
            1 -> 3
            2 -> 0
            3 -> 1
            else -> throw IllegalArgumentException("Invalid side")
        }
    }

    private fun goldenRationSplit(quadrilateral: Quadrilateral): Triple<Int, Point, Point> {
        val longestSideIndex = quadrilateral.longestEdgeIndex()
        val pointOnSide = quadrilateral.edges[longestSideIndex].goldenRatioSplit().toList().first().end
        val oppositeSide = quadrilateral.oppositeSideIndex(longestSideIndex)
        val pointOnOppositeSite = quadrilateral.edges[oppositeSide].goldenRatioSplit().first().end
        return Triple(longestSideIndex, pointOnSide, pointOnOppositeSite)
    }

    fun goldenRatioSubdivide(depth: Int): ShapeTree<Quadrilateral> {
        return ShapeTree(quadSubdivide(this, depth, ::goldenRationSplit))
    }

    private fun quadSubdivide(
        quadrilateral: Quadrilateral,
        depth: Int,
        splitFunction: (Quadrilateral) -> Triple<Int, Point, Point>,
    ): ShapeTreeNode<Quadrilateral> {
        if (depth == 0) {
            return ShapeTreeNode(quadrilateral)
        }

        val (longestSideIndex, pointOnSide, pointOnOppositeSite) = splitFunction(quadrilateral)

        val (left, right) = when (longestSideIndex) {
            0 -> {
                Pair(
                    Quadrilateral(
                        pointOnSide,
                        quadrilateral.b,
                        quadrilateral.c,
                        pointOnOppositeSite,
                    ),
                    Quadrilateral(
                        quadrilateral.a,
                        pointOnSide,
                        pointOnOppositeSite,
                        quadrilateral.d,
                    ),
                )
            }

            1 -> {
                Pair(
                    Quadrilateral(
                        pointOnOppositeSite,
                        pointOnSide,
                        quadrilateral.c,
                        quadrilateral.d,
                    ),
                    Quadrilateral(
                        quadrilateral.a,
                        quadrilateral.b,
                        pointOnSide,
                        pointOnOppositeSite,
                    ),
                )
            }

            2 -> {
                Pair(
                    Quadrilateral(
                        quadrilateral.a,
                        pointOnOppositeSite,
                        pointOnSide,
                        quadrilateral.d,
                    ),
                    Quadrilateral(
                        pointOnOppositeSite,
                        quadrilateral.b,
                        quadrilateral.c,
                        pointOnSide,
                    ),
                )
            }

            else -> {
                Pair(
                    Quadrilateral(
                        quadrilateral.a,
                        quadrilateral.b,
                        pointOnOppositeSite,
                        pointOnSide,
                    ),
                    Quadrilateral(
                        pointOnSide,
                        pointOnOppositeSite,
                        quadrilateral.c,
                        quadrilateral.d,
                    ),
                )
            }
        }

        val leftChild = quadSubdivide(left, depth - 1, splitFunction)
        val rightChild = quadSubdivide(right, depth - 1, splitFunction)

        return ShapeTreeNode(quadrilateral, leftChild, rightChild)


    }


    companion object {
        fun fromSketch(sketch: PApplet): Quadrilateral {
            return Quadrilateral(
                listOf(
                    Point(0f, 0f),
                    Point(sketch.width.toFloat(), 0f),
                    Point(sketch.width.toFloat(), sketch.height.toFloat()),
                    Point(0f, sketch.height.toFloat()),
                ),
            )
        }
    }
}
