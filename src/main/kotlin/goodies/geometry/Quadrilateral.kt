package goodies.geometry

import ShapeTree
import goodies.containers.ShapeTreeNode
import processing.core.PApplet

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
        get() = vertices.last()

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
            when (longestSideIndex) {
                0 -> {
                    Pair(
                        first =
                            Quadrilateral(
                                first = pointOnSide,
                                second = quadrilateral.b,
                                third = quadrilateral.c,
                                fourth = pointOnOppositeSite,
                            ),
                        second =
                            Quadrilateral(
                                first = quadrilateral.a,
                                second = pointOnSide,
                                third = pointOnOppositeSite,
                                fourth = quadrilateral.d,
                            ),
                    )
                }

                1 -> {
                    Pair(
                        first =
                            Quadrilateral(
                                first = pointOnOppositeSite,
                                second = pointOnSide,
                                third = quadrilateral.c,
                                fourth = quadrilateral.d,
                            ),
                        second =
                            Quadrilateral(
                                first = quadrilateral.a,
                                second = quadrilateral.b,
                                third = pointOnSide,
                                fourth = pointOnOppositeSite,
                            ),
                    )
                }

                2 -> {
                    Pair(
                        first =
                            Quadrilateral(
                                first = quadrilateral.a,
                                second = pointOnOppositeSite,
                                third = pointOnSide,
                                fourth = quadrilateral.d,
                            ),
                        second =
                            Quadrilateral(
                                first = pointOnOppositeSite,
                                second = quadrilateral.b,
                                third = quadrilateral.c,
                                fourth = pointOnSide,
                            ),
                    )
                }

                else -> {
                    Pair(
                        first =
                            Quadrilateral(
                                first = quadrilateral.a,
                                second = quadrilateral.b,
                                third = pointOnOppositeSite,
                                fourth = pointOnSide,
                            ),
                        second =
                            Quadrilateral(
                                first = pointOnSide,
                                second = pointOnOppositeSite,
                                third = quadrilateral.c,
                                fourth = quadrilateral.d,
                            ),
                    )
                }
            }

        val leftChild = quadSubdivide(left, depth - 1, splitFunction)
        val rightChild = quadSubdivide(right, depth - 1, splitFunction)

        return ShapeTreeNode(value = quadrilateral, left = leftChild, right = rightChild)
    }

    companion object {
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
