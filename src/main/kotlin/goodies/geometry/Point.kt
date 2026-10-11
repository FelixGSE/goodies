package goodies.geometry

import goodies.algebra.Graphics
import goodies.algebra.RotationMatrix
import goodies.algebra.RotationMatrixUtils
import goodies.projections.StereographicProjection
import goodies.sampler.SphereSampler.drawPointOnUnitNSphere
import org.apache.commons.math3.linear.MatrixUtils
import org.apache.commons.math3.linear.RealMatrix
import org.apache.commons.math3.linear.RealVector
import processing.core.PApplet
import processing.core.PVector
import java.util.Collections
import kotlin.math.acos
import kotlin.math.pow

/**
 * Immutable point, or vector, in any number of dimensions.
 *
 * Coordinates are stored as `Double` and read back as `Float`.
 */
class Point(
    vararg coordinates: Number,
) : Iterable<Float>,
    Graphics {
    private val coordinates = MatrixUtils.createRealVector(coordinates.map { it.toDouble() }.toDoubleArray())
    val dimensions: Int = coordinates.size

    val x: Float
        get() {
            require(dimensions > 0) { TOO_FEW_DIMENSIONS }
            return this[0]
        }

    val y: Float
        get() {
            require(dimensions > 1) { TOO_FEW_DIMENSIONS }
            return this[1]
        }

    val z: Float
        get() {
            require(dimensions > 2) { TOO_FEW_DIMENSIONS }
            return this[2]
        }

    constructor(coordinates: List<Number>) : this(*coordinates.toTypedArray())
    constructor(coordinates: RealVector) : this(*coordinates.toArray().toTypedArray())
    constructor(pVector: PVector) : this(*pVector.array().toTypedArray())

    override fun toString(): String = "Point(dimensions=$dimensions, coordinates=$coordinates)"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Point

        if (coordinates != other.coordinates) return false
        return dimensions == other.dimensions
    }

    override fun hashCode(): Int {
        var result = coordinates?.hashCode() ?: 0
        result = 31 * result + dimensions
        return result
    }

    override fun iterator(): Iterator<Float> = toFloatList().iterator()

    operator fun get(index: Int): Float = coordinates.getEntry(index).toFloat()

    fun slice(
        from: Int,
        to: Int,
    ): Point = Point(toFloatList().slice(from until to))

    fun slice(i: Int): Point = Point(toFloatList().slice(i until i))

    operator fun minus(other: Point): Point = Point(coordinates.subtract(other.toRealVector()))

    operator fun minus(number: Number): Point = Point(coordinates.toArray().map { it - number.toDouble() })

    operator fun plus(other: Point): Point = Point(coordinates.add(other.toRealVector()))

    operator fun plus(number: Number): Point = Point(coordinates.toArray().map { it + number.toDouble() })

    operator fun times(other: Point): Point {
        require(dimensions == other.dimensions) { "Points dimension do not match" }
        return Point(other.toDoubleArray().zip(coordinates.toArray()) { x, y -> x * y })
    }

    operator fun times(number: Number): Point = Point(coordinates.toArray().map { it * number.toDouble() })

    operator fun div(other: Point): Point = Point(other.toDoubleArray().zip(coordinates.toArray()) { x, y -> x / y })

    operator fun div(number: Number): Point = Point(coordinates.toArray().map { it / number.toDouble() })

    /**
     * Alias plus operator. Compliance with Processing PVector
     *
     */
    fun add(other: Point): Point = this + other

    /**
     * Alias minus operator. Compliance with Processing PVector
     *
     */
    fun sub(other: Point): Point = this - other

    /**
     * Alias times operator. Compliance with Processing PVector
     *
     */
    fun mult(other: Point): Point = this * other

    fun dot(point: Point): Float = coordinates.dotProduct(point.toRealVector()).toFloat()

    fun pow(exponent: Number): Point = Point(coordinates.toArray().map { it.pow(exponent.toDouble()) })

    fun cross(other: Point): Point {
        require(dimensions == SPATIAL_DIMENSIONS && other.dimensions == SPATIAL_DIMENSIONS) {
            "Points must have dimension equal to 3 for this method"
        }
        return Point(
            this[1] * other[2] - this[2] * other[1],
            this[2] * other[0] - this[0] * other[2],
            this[0] * other[1] - this[1] * other[0],
        )
    }

    fun translate(vararg translation: Number): Point {
        require(translation.size == dimensions) { TRANSLATION_DIMENSION_MISMATCH }
        return Point((0 until dimensions).map { this[it] + translation[it].toDouble() })
    }

    fun translate(translation: Number): Point = Point((0 until dimensions).map { this[it] + translation.toDouble() })

    fun translate(translation: Point): Point {
        require(translation.dimensions == dimensions) { TRANSLATION_DIMENSION_MISMATCH }
        return Point((0 until dimensions).map { this[it] + translation[it].toDouble() })
    }

    fun scale(factor: Number): Point = Point(coordinates.mapMultiply(factor.toDouble()))

    fun sum(): Float = coordinates.toArray().sum().toFloat()

    fun average(): Float = coordinates.toArray().average().toFloat()

    fun avg(): Float = average()

    fun max(): Float = toList().max()

    fun maxIndex(): Int {
        val coordinatesList = toList()
        return coordinatesList.indexOf(coordinatesList.max())
    }

    fun negate(): Point = this * -1

    fun distance(other: Point): Float = coordinates.getDistance(other.toRealVector()).toFloat()

    /**
     * Alias distance(). Compliance with Processing PVector
     *
     */
    fun dist(other: Point): Float = distance(other)

    fun magnitude(): Float = coordinates.norm.toFloat()

    /**
     * Alias for magnitude(). Compliance with Processing PVector
     *
     */
    fun mag(): Float = magnitude()

    fun magnitudeSquared(): Float = coordinates.toArray().sumOf { it * it }.toFloat()

    /**
     * Alias for magnitudeSquared(). Compliance with Processing PVector
     *
     */
    fun magSq(): Float = magnitudeSquared()

    fun normalize(): Point = Point(coordinates.unitVector())

    fun rotate(angle: Number): Point {
        require(dimensions == 2) { "Invalid dimensions. Points must have dimension equal to 2 for this method" }
        return rotateAxis(i = 0, j = 1, angle = angle.toFloat())
    }

    fun rotateZ(angle: Number): Point {
        require(dimensions >= SPATIAL_DIMENSIONS) { NOT_SPATIAL }
        return rotateAxis(i = 0, j = 1, angle = angle.toFloat())
    }

    fun rotateY(angle: Number): Point {
        require(dimensions >= SPATIAL_DIMENSIONS) { NOT_SPATIAL }
        return rotateAxis(i = 0, j = 2, angle = angle.toFloat())
    }

    fun rotateX(angle: Number): Point {
        require(dimensions >= SPATIAL_DIMENSIONS) { NOT_SPATIAL }
        return rotateAxis(i = 1, j = 2, angle = angle.toFloat())
    }

    fun rotateAxis(
        i: Int,
        j: Int,
        angle: Number,
    ): Point {
        val rotation =
            RotationMatrixUtils.getRotationMatrixForAxis(i = i, j = j, dimensions = dimensions, angle = angle.toFloat())
        return rotation.multiplyPoint(this)
    }

    fun rotateAxis(
        i: Int,
        angle: Number,
    ): Point = RotationMatrix(i, dimensions, angle).dot(this)

    fun rotateAxisAroundReferencePoint(
        i: Int,
        j: Int,
        angle: Number,
        referencePoint: Point,
    ): Point = (this - referencePoint).rotateAxis(i, j, angle) + referencePoint

    fun angleBetween(point: Point): Float = acos(normalize().dot(point.normalize()).toDouble()).toFloat()

    fun headings(): List<Float> = NSphere.unitNSphere(dimensions).anglesForPointOnSurface(normalize())

    fun heading(): Float {
        require(dimensions == 2) {
            "heading() is only defined for 2D points. For higher dimensions " +
                "use headings(). Function is implemented to comply with Processing PVector"
        }
        val headings = headings()

        check(headings.size == 1) { "headings() should return a list of size 1 for 2D points" }
        return headings[0]
    }

    fun projectDown(distance: Float = 1.0f): Point =
        Point(StereographicProjection.projectDownOneDimension(distance, coordinates))

    fun projectDownTo3D(distance: Float = 1.0f): Point {
        require(dimensions >= SPATIAL_DIMENSIONS) { "Point must have dimension 4 to be projected down to 3D" }
        var point = this.copy()
        repeat(dimensions - SPATIAL_DIMENSIONS) {
            point = point.projectDown(distance)
        }
        return point
    }

    fun newWithLength(length: Number): Point = Point(coordinates.unitVector().map { it * length.toDouble() })

    /**
     * Alias for newWithLength(). Compliance with Processing PVector.
     * Returns a new vector instead of modifying the original.
     */
    fun setMag(length: Number): Point {
        println(
            "[WARNING] setMag() returns a new Vector instead of modifying the old one. Overwrite you variable " +
                "with the result of setMag()",
        )
        return newWithLength(length)
    }

    fun limit(length: Number): Point {
        require(!(length.toFloat() < 0.0f)) { "Length must be a positive number" }
        return if (length.toFloat() >= this.magnitude()) {
            copy()
        } else {
            Point(coordinates.unitVector().map { it * length.toDouble() })
        }
    }

    fun lerp(
        other: Point,
        t: Float,
    ): Point {
        require(dimensions == other.dimensions) {
            "Vectors must be of the same dimension for lerp operation."
        }

        val resultVector = DoubleArray(dimensions)

        for (i in 0 until dimensions) {
            val value = this[i] * (1 - t) + other[i] * t
            resultVector[i] = value.toDouble()
        }

        return Point(resultVector.toList())
    }

    fun swapEntries(
        firstPositionOne: Int,
        secondPositionOne: Int,
    ): Point {
        val pointAsList = this.toDoubleArray().toMutableList()
        Collections.swap(pointAsList, firstPositionOne, secondPositionOne)
        return Point(pointAsList.toList())
    }

    fun extend(vararg values: Number): Point = Point(toDoubleArray().plus(values.map { it.toDouble() }).toList())

    override fun makeAffine(): Point = extend(1)

    fun copy(): Point = Point(coordinates.copy())

    fun toRealVector(): RealVector = coordinates.copy()

    fun toDoubleArray(): DoubleArray = coordinates.toArray()

    /**
     * Alias for toFloatArray(). Compliance with Processing PVector.
     *
     */
    fun array(): FloatArray = toFloatArray()

    fun toFloatArray(): FloatArray = coordinates.toArray().map { it.toFloat() }.toFloatArray()

    fun toDoubleList(): List<Double> = coordinates.toArray().toList()

    fun toFloatList(): List<Float> =
        coordinates
            .toArray()
            .map { it.toFloat() }
            .toFloatArray()
            .toList()

    fun toPVector(): PVector =
        when (dimensions) {
            2 -> {
                PVector(this[0], this[1])
            }

            SPATIAL_DIMENSIONS -> {
                PVector(this[0], this[1], this[2])
            }

            else -> {
                throw IllegalArgumentException("Can't convert Point to PVector - Invalid number of dimensions")
            }
        }

    fun draw(sketch: PApplet) {
        when (dimensions) {
            2 -> sketch.point(this[0], this[1])
            SPATIAL_DIMENSIONS -> sketch.point(this[0], this[1], this[2])
            else -> throw IllegalArgumentException("Can't convert Point to PVector - Too many dimensions")
        }
    }

    /** Builders for points. */
    companion object {
        fun zeros(dimensions: Int): Point = Point(List(dimensions) { 0 })

        fun randomPoint(dimensions: Int): Point {
            require(dimensions >= 0) { "Dimensions must greater or equal than zero" }

            return Point(*drawPointOnUnitNSphere(dimensions).toArray().toTypedArray())
        }

        fun random2D(): Point = Point.randomPoint(2)

        fun random3D(): Point = Point.randomPoint(SPATIAL_DIMENSIONS)

        /**
         * Generate a 2D GVector from an angle. For compliance with Processing PVector class
         *
         * @param angle The angle is in radians.
         * @return The GVector with magnitude 1 for the corresponding angle.
         */
        fun fromAngle(angle: Number): Point = NSphere.unitNSphere(2).pointOnSurface(angle)

        fun fromAngles(vararg angles: Number): Point = NSphere.unitNSphere(angles.size).pointOnSurface(*angles)

        fun fromPVector2D(pVector: PVector): Point = Point(pVector.x, pVector.y)

        fun fromPVector3D(pVector: PVector): Point = Point(pVector.x, pVector.y)
    }
}

private const val TRANSLATION_DIMENSION_MISMATCH = "Translation does not match point dimension"
private const val TOO_FEW_DIMENSIONS = "Point does not have enough dimensions"
private const val NOT_SPATIAL = "Invalid dimensions. Point must have at least 3 dimensions for this method"

fun RealMatrix.multiplyPoint(point: Point): Point = Point(this.preMultiply(point.toRealVector()))

fun Iterable<Number>.toPoint(): Point = Point(this.toList())

operator fun Number.plus(point: Point): Point = point + this

operator fun Number.minus(point: Point): Point = Point(point.map { this.toFloat() - it })

operator fun Number.times(point: Point): Point = point * this

operator fun Number.div(point: Point): Point = Point(point.map { this.toFloat() / it })
