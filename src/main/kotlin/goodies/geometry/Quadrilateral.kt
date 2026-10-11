package goodies.geometry

import ShapeTree
import goodies.containers.ShapeTreeNode
import processing.core.PApplet

/** Polygon with exactly four vertices a, b, c and d. */
open class Quadrilateral(
    vertices: List<Point>,
) : Polygon(vertices) {
    init {
        require(vertices.size == QUADRILATERAL_VERTICES) { "Quadrilateral must have 4 vertices" }
    }

    val a: Point
        get() = vertices[0]
    val b: Point
        get() = vertices[1]
    val c: Point
        get() = vertices[2]
    val d: Point
        get() = vertices[3]

    constructor(vararg vertices: Point) : this(vertices.toList())

    constructor(first: Point, second: Point, third: Point, fourth: Point) : this(listOf(first, second, third, fourth))

    private fun oppositeSideIndex(side: Int): Int {
        require(side in 0 until QUADRILATERAL_VERTICES) { "Invalid side" }
        return (side + 2) % QUADRILATERAL_VERTICES
    }

    private fun goldenRationSplit(quadrilateral: Quadrilateral): Triple<Int, Point, Point> {
        val longestSideIndex = quadrilateral.longestEdgeIndex()
        val pointOnSide =
            quadrilateral.edges[longestSideIndex]
                .goldenRatioSplit()
                .toList()
                .first()
                .end
        val oppositeSide = quadrilateral.oppositeSideIndex(longestSideIndex)
        val pointOnOppositeSite =
            quadrilateral.edges[oppositeSide]
                .goldenRatioSplit()
                .first()
                .end
        return Triple(first = longestSideIndex, second = pointOnSide, third = pointOnOppositeSite)
    }

    /**
     * Splits this quadrilateral [depth] times, each time across its longest edge and the opposite edge at their
     * [Line.goldenRatioSplit] points.
     *
     * Returns the tree of all pieces; the leaves are the final ones.
     */
    fun goldenRatioSubdivide(depth: Int): ShapeTree<Quadrilateral> =
        ShapeTree(quadSubdivide(this, depth, ::goldenRationSplit))

    private fun quadSubdivide(
        quadrilateral: Quadrilateral,
        depth: Int,
        splitFunction: (Quadrilateral) -> Triple<Int, Point, Point>,
    ): ShapeTreeNode<Quadrilateral> {
        if (depth == 0) {
            return ShapeTreeNode(quadrilateral)
        }

        val (longestSideIndex, pointOnSide, pointOnOppositeSite) = splitFunction(quadrilateral)

        val (left, right) =
            splitAt(
                quadrilateral = quadrilateral,
                longestSideIndex = longestSideIndex,
                onSide = pointOnSide,
                onOppositeSide = pointOnOppositeSite,
            )

        val leftChild = quadSubdivide(left, depth - 1, splitFunction)
        val rightChild = quadSubdivide(right, depth - 1, splitFunction)

        return ShapeTreeNode(value = quadrilateral, left = leftChild, right = rightChild)
    }

    private fun splitAt(
        quadrilateral: Quadrilateral,
        longestSideIndex: Int,
        onSide: Point,
        onOppositeSide: Point,
    ): Pair<Quadrilateral, Quadrilateral> {
        val a = quadrilateral.a
        val b = quadrilateral.b
        val c = quadrilateral.c
        val d = quadrilateral.d
        return when (longestSideIndex) {
            0 -> {
                Quadrilateral(listOf(onSide, b, c, onOppositeSide)) to
                    Quadrilateral(listOf(a, onSide, onOppositeSide, d))
            }

            1 -> {
                Quadrilateral(listOf(onOppositeSide, onSide, c, d)) to
                    Quadrilateral(listOf(a, b, onSide, onOppositeSide))
            }

            2 -> {
                Quadrilateral(listOf(a, onOppositeSide, onSide, d)) to
                    Quadrilateral(listOf(onOppositeSide, b, c, onSide))
            }

            else -> {
                Quadrilateral(listOf(a, b, onOppositeSide, onSide)) to
                    Quadrilateral(listOf(onSide, onOppositeSide, c, d))
            }
        }
    }

    /** Builders for quadrilaterals. */
    companion object {
        /** Returns the rectangle that covers [sketch]. */
        fun fromSketch(sketch: PApplet): Quadrilateral =
            Quadrilateral(
                listOf(
                    Point(0f, 0f),
                    Point(sketch.width.toFloat(), 0f),
                    Point(sketch.width.toFloat(), sketch.height.toFloat()),
                    Point(0f, sketch.height.toFloat()),
                ),
            )
    }
}

private const val QUADRILATERAL_VERTICES = 4
