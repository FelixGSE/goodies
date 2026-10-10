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

class Line(
    private val a: Point,
    private val b: Point,
) : DiscreteShape,
    OpenShape,
    Geom,
    Drawable {
    constructor(points: Iterable<Point>) : this(points.first(), points.last()) {
        require(points.count() == 2) { "Iterable must contain exactly two points" }
    }

    init {
        require(a.dimensions == b.dimensions) { "Points A and B must have equal dimensions" }
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

    fun copy(): Line = Line(start, end)

    val dimensions: Int
        get() {
            return a.dimensions
        }

    val direction: Point
        get() {
            return b - a
        }

    override val vertices: List<Point>
        get() {
            return listOf(start, end)
        }

    val start: Point
        get() {
            return a.copy()
        }

    val end: Point
        get() {
            return b.copy()
        }

    val reversed: Line
        get() {
            return Line(end, start)
        }

    val length: Float
        get() {
            return a.distance(b)
        }

    override fun addDimensions(vararg dimension: Number): Line = Line(a.extend(*dimension), b.extend(*dimension))

    override fun center(): Line {
        val centered = GMatrix(vertices).center().rows
        return Line(centered.first(), centered.last())
    }

    override fun centerAt(point: Point): Shape = center().translate(point.toList())

    override fun normalize(): Line {
        val normalized = GMatrix(vertices).normalize().rows
        return Line(normalized.first(), normalized.last())
    }

    override fun translate(vararg translation: Number): Line =
        Line(
            a.translate(*translation),
            b.translate(*translation),
        )

    fun translate(translation: List<Number>): Line =
        Line(
            a.translate(*translation.toTypedArray()),
            b.translate(*translation.toTypedArray()),
        )

    override fun translate(translation: Number): Line =
        Line(
            a.translate(translation),
            b.translate(translation),
        )

    override fun scale(factor: Number): Line = Line(a.scale(factor), b.scale(factor))

    val midpoint: Point
        get() {
            return scaleFromStart(0.5).end
        }

    fun scaleFromStart(factor: Number): Line = Line(start, start + factor * (end - start))

    fun scaleFromEnd(factor: Number): Line = reversed.scaleFromStart(factor).reversed

    fun scaleFromBothSides(factor: Number): Line = center().scale(factor).translate(midpoint.x, midpoint.y)

    fun randomPointOn(): Point = scaleFromStart(Math.random()).end

    fun goldenRatioSplit(): List<Line> {
        val split = length / GOLDEN_RATIO
        val splitPoint = scaleFromStart(split).end
        return listOf(
            Line(start, splitPoint),
            Line(splitPoint, end),
        )
    }

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

    fun divideLineInRandomParts(n: Int): List<Line> =
        randomFloats(n - 1)
            .sorted()
            .map { scaleFromStart(it) }

    fun increaseByLength(addOn: Number): Line {
        val scalingFactor = 1 + addOn.toFloat() / length
        val newEnd = scaleFromStart(scalingFactor).end
        return Line(start, newEnd)
    }

    companion object {
        fun fromPVector2D(
            start: PVector,
            end: PVector,
        ): Line = Line(Point(start.x, start.y), Point(end.x, end.y))

        fun fromPVector3D(
            start: PVector,
            end: PVector,
        ): Line = Line(Point(start.x, start.y, start.z), Point(end.x, end.y, end.z))

        fun makePositiveRealLine(upper: Number): Line = Line(Point(0f, 0f), Point(upper, 0f))

        fun makeRealLine(
            lower: Float,
            upper: Float,
        ): Line {
            require(!(lower > upper)) { "Parameter lower must less or equal than upp" }
            return Line(Point(lower, 0f), Point(upper, 0f))
        }

        fun hLIne(
            sketch: PApplet,
            y: Float,
        ): Line = Line(Point(0f, y), Point(sketch.width.toFloat(), y))

        fun vLine(
            sketch: PApplet,
            x: Float,
        ): Line = Line(Point(x, 0f), Point(x, sketch.height.toFloat()))
    }

    override fun draw(sketch: PApplet) =
        when (dimensions) {
            2 -> {
                sketch.line(a.x, a.y, b.x, b.y)
            }

            3 -> {
                sketch.line(a.x, a.y, a.z, b.x, b.y, b.z)
            }

            else -> {
                throw IllegalArgumentException("Can only draw 2D and 3D objects")
            }
        }
}
