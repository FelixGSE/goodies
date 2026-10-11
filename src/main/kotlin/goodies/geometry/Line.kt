package goodies.geometry

import goodies.algebra.GMatrix
import goodies.algebra.GOLDEN_RATIO
import goodies.draw.Drawable
import goodies.geometry.interfaces.DiscreteShape
import goodies.geometry.interfaces.Geom
import goodies.geometry.interfaces.OpenShape
import goodies.geometry.interfaces.Shape
import goodies.utils.randomFloats
import processing.core.PApplet
import processing.core.PVector

/** Straight segment from a to b in any number of dimensions; both ends have the same dimension. */
class Line(
    private val a: Point,
    private val b: Point,
) : DiscreteShape,
    OpenShape,
    Geom,
    Drawable {
    init {
        require(a.dimensions == b.dimensions) { "Points A and B must have equal dimensions" }
    }

    val dimensions: Int
        get() = a.dimensions

    val direction: Point
        get() = b - a

    val start: Point
        get() = a.copy()

    val end: Point
        get() = b.copy()

    override val vertices: List<Point>
        get() = listOf(start, end)

    val reversed: Line
        get() = Line(a = end, b = start)

    val length: Float
        get() = a.distance(b)

    val midpoint: Point
        get() = scaleFromStart(0.5).end

    constructor(points: Iterable<Point>) : this(points.first(), points.last()) {
        require(points.count() == 2) { "Iterable must contain exactly two points" }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Line) return false

        if (a != other.a) return false
        return b == other.b
    }

    override fun hashCode(): Int {
        var result = a.hashCode()
        result = 31 * result + b.hashCode()
        return result
    }

    override fun toString(): String = "Line(a=$a, b=$b)"

    /** Returns a segment with copies of both ends. */
    fun copy(): Line = Line(a = start, b = end)

    override fun addDimensions(vararg dimension: Number): Line =
        Line(a = a.extend(*dimension), b = b.extend(*dimension))

    override fun center(): Line {
        val centered = GMatrix(vertices).center().rows
        return Line(a = centered.first(), b = centered.last())
    }

    override fun centerAt(point: Point): Shape = center().translate(point.toList())

    override fun normalize(): Line {
        val normalized = GMatrix(vertices).normalize().rows
        return Line(a = normalized.first(), b = normalized.last())
    }

    override fun translate(vararg translation: Number): Line =
        Line(
            a = a.translate(*translation),
            b = b.translate(*translation),
        )

    /** Returns this segment moved by [translation], one value per axis. */
    fun translate(translation: List<Number>): Line =
        Line(
            a = a.translate(*translation.toTypedArray()),
            b = b.translate(*translation.toTypedArray()),
        )

    override fun translate(translation: Number): Line =
        Line(
            a = a.translate(translation),
            b = b.translate(translation),
        )

    override fun scale(factor: Number): Line = Line(a = a.scale(factor), b = b.scale(factor))

    /** Returns the segment from start to `start + factor · (end - start)`. */
    fun scaleFromStart(factor: Number): Line = Line(a = start, b = start + factor * (end - start))

    /** Returns the segment from `end + factor · (start - end)` to end. */
    fun scaleFromEnd(factor: Number): Line = reversed.scaleFromStart(factor).reversed

    /** Returns [center] scaled by [factor] and moved by the x and y of [midpoint]; two dimensions only. */
    fun scaleFromBothSides(factor: Number): Line = center().scale(factor).translate(midpoint.x, midpoint.y)

    /** Returns a uniformly random point on the segment, drawn with `Math.random`. */
    fun randomPointOn(): Point = scaleFromStart(Math.random()).end

    /**
     * Returns two segments that meet at `scaleFromStart(length / GOLDEN_RATIO).end`.
     *
     * The factor is not divided by the length, so the split point leaves the segment once the length exceeds the golden
     * ratio.
     */
    fun goldenRatioSplit(): List<Line> {
        val split = length / GOLDEN_RATIO
        val splitPoint = scaleFromStart(split).end
        return listOf(
            Line(a = start, b = splitPoint),
            Line(a = splitPoint, b = end),
        )
    }

    /** Returns the n + 1 segments from start to `k / n` of the way to end, for k = 0 to n. */
    fun divideInEqualParts(n: Int): List<Line> {
        val a = 1.0 / n

        val outputs = mutableListOf<Line>()

        for (i in n downTo 0) {
            val scalingFactor = 1 - i * a
            val line = scaleFromStart(scalingFactor)
            outputs.add(line)
        }

        return outputs
    }

    /** Returns n - 1 segments from start to random fractions of the way to end, shortest first. */
    fun divideLineInRandomParts(n: Int): List<Line> =
        randomFloats(n - 1)
            .sorted()
            .map { scaleFromStart(it) }

    /** Returns this segment with its end moved by [addOn] along the segment. */
    fun increaseByLength(addOn: Number): Line {
        val scalingFactor = 1 + addOn.toFloat() / length
        val newEnd = scaleFromStart(scalingFactor).end
        return Line(a = start, b = newEnd)
    }

    override fun draw(sketch: PApplet) {
        when (dimensions) {
            2 -> sketch.line(a.x, a.y, b.x, b.y)
            SPATIAL_DIMENSIONS -> sketch.line(a.x, a.y, a.z, b.x, b.y, b.z)
            else -> throw IllegalArgumentException("Can only draw 2D and 3D objects")
        }
    }

    /** Builders for lines. */
    companion object {
        /** Returns the 2D segment between the x and y of [start] and [end]. */
        fun fromPVector2D(
            start: PVector,
            end: PVector,
        ): Line = Line(a = Point(start.x, start.y), b = Point(end.x, end.y))

        /** Returns the 3D segment between [start] and [end]. */
        fun fromPVector3D(
            start: PVector,
            end: PVector,
        ): Line = Line(a = Point(start.x, start.y, start.z), b = Point(end.x, end.y, end.z))

        /** Returns the segment from the origin to ([upper], 0). */
        fun makePositiveRealLine(upper: Number): Line = Line(a = Point(0f, 0f), b = Point(upper, 0f))

        /**
         * Returns the segment from ([lower], 0) to ([upper], 0). Throws IllegalArgumentException when [lower] >
         * [upper].
         */
        fun makeRealLine(
            lower: Float,
            upper: Float,
        ): Line {
            require(!(lower > upper)) { "Parameter lower must less or equal than upp" }
            return Line(a = Point(lower, 0f), b = Point(upper, 0f))
        }

        /** Returns the horizontal segment at [y] across the width of [sketch]. */
        fun hLIne(
            sketch: PApplet,
            y: Float,
        ): Line = Line(a = Point(0f, y), b = Point(sketch.width.toFloat(), y))

        /** Returns the vertical segment at [x] across the height of [sketch]. */
        fun vLine(
            sketch: PApplet,
            x: Float,
        ): Line = Line(a = Point(x, 0f), b = Point(x, sketch.height.toFloat()))
    }
}
