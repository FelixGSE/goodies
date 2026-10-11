package goodies.geometry

import goodies.geometry.interfaces.ClosedShape
import goodies.geometry.interfaces.EllipsoidLike
import goodies.geometry.interfaces.ParametricShape
import goodies.geometry.interfaces.Shape
import kotlin.math.PI
import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

open class NEllipsoid(
    private val centerPoint: Point,
    private val axes: List<Number>,
) : ParametricShape,
    EllipsoidLike,
    ClosedShape {
    val dimensions: Int = centerPoint.dimensions

    val center: Point
        get() = centerPoint.copy()

    init {
        require(dimensions >= 2) { "Can only construct ellipse like objects with at least two dimensions" }
        require(dimensions == axes.size) { "Provided list of radia does not match the point dimensions" }
    }

    override val centroid: Point
        get() = center

    override fun center(): NEllipsoid = NEllipsoid(Point.zeros(dimensions), axes)

    override fun centerAt(point: Point): Shape = NEllipsoid(point, axes)

    override fun normalize(): NEllipsoid = NEllipsoid(centerPoint, Point(axes).normalize().toList())

    override val isConvex: Boolean
        get() = true

    override fun addDimensions(
        vararg dimension: Number,
        newAxes: List<Float>,
    ): Shape {
        require(dimension.size == newAxes.size) { "Number of dimensions must match number of new axes" }
        return NEllipsoid(centerPoint.extend(*dimension), newAxes + newAxes)
    }

    override fun contains(point: Point): Boolean = (point - center / Point(axes)).pow(2).sum() <= 1.0

    fun pointOnSurface(vararg angles: Number): Point = pointOnSurface(angles.map { it.toDouble() }.toList())

    fun pointOnSurface(angles: List<Number>): Point {
        val angleAsListOfDoubles = angles.map { it.toDouble() }
        require(angles.size == dimensions - 1) { "Number of angles must have be equal to dimensions - 1" }
        var result = Point(axes)
        val numberOfAngles = angles.size

        for (i in 0 until numberOfAngles) {
            val angle = angleAsListOfDoubles[i]
            val numberOfRemainingOnes = dimensions - 2 - i
            val numberOfNonOneEntries = dimensions - numberOfRemainingOnes

            val sines = List(numberOfNonOneEntries - 1) { sin(angle) }
            val cosine = listOf(cos(angle))
            val ones = List(numberOfRemainingOnes) { 1.0 }
            val column = Point(sines + cosine + ones)

            result *= column
        }

        return result
    }

    fun anglesForPointOnSurface(point: Point): List<Float> {
        val angles = mutableListOf<Float>()
        for (i in 0 until dimensions - 1) {
            val ssqDenominator = point.slice(from = 0, to = i + 2).map { it * it }.sum()
            if (ssqDenominator == 0.0F) {
                val angle = 0.0F
                angles.add(angle)
            } else {
                val numerator = point[i + 1]
                val denominator = sqrt(ssqDenominator)
                var angle = acos(numerator / denominator)
                if (i == 0 && point[i] < 0) {
                    angle = 2 * PI.toFloat() - angle
                }
                angles.add(angle)
            }
        }
        return angles.toList()
    }
}
