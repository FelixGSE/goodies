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

    /** Returns coordinate [index]. */
    operator fun get(index: Int): Float = coordinates.getEntry(index).toFloat()

    /** Returns the point of coordinates [from] up to, not including, [to]. */
    fun slice(
        from: Int,
        to: Int,
    ): Point = Point(toFloatList().slice(from until to))

    /** Returns an empty point: the range from [i] until [i] is empty. */
    fun slice(i: Int): Point = Point(toFloatList().slice(i until i))

    /** Returns the difference with [other], coordinate by coordinate. */
    operator fun minus(other: Point): Point = Point(coordinates.subtract(other.toRealVector()))

    /** Returns this point with [number] subtracted from every coordinate. */
    operator fun minus(number: Number): Point = Point(coordinates.toArray().map { it - number.toDouble() })

    /** Returns the sum with [other], coordinate by coordinate. */
    operator fun plus(other: Point): Point = Point(coordinates.add(other.toRealVector()))

    /** Returns this point with [number] added to every coordinate. */
    operator fun plus(number: Number): Point = Point(coordinates.toArray().map { it + number.toDouble() })

    /** Returns the product with [other], coordinate by coordinate. Requires equal dimensions. */
    operator fun times(other: Point): Point {
        require(dimensions == other.dimensions) { "Points dimension do not match" }
        return Point(other.toDoubleArray().zip(coordinates.toArray()) { x, y -> x * y })
    }

    /** Returns this point with every coordinate multiplied by [number]. */
    operator fun times(number: Number): Point = Point(coordinates.toArray().map { it * number.toDouble() })

    /** Returns [other] divided by this point, coordinate by coordinate: the operands are swapped. */
    operator fun div(other: Point): Point = Point(other.toDoubleArray().zip(coordinates.toArray()) { x, y -> x / y })

    /** Returns this point with every coordinate divided by [number]. */
    operator fun div(number: Number): Point = Point(coordinates.toArray().map { it / number.toDouble() })

    /** Alias for [plus], as in Processing PVector. */
    fun add(other: Point): Point = this + other

    /** Alias for [minus], as in Processing PVector. */
    fun sub(other: Point): Point = this - other

    /** Alias for [times], as in Processing PVector. */
    fun mult(other: Point): Point = this * other

    /** Returns the dot product with [point]. */
    fun dot(point: Point): Float = coordinates.dotProduct(point.toRealVector()).toFloat()

    /** Returns this point with every coordinate raised to [exponent]. */
    fun pow(exponent: Number): Point = Point(coordinates.toArray().map { it.pow(exponent.toDouble()) })

    /** Returns the cross product with [other]. Requires both to be 3D. */
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

    /** Returns this point moved by [translation], one value per axis. Requires one value per axis. */
    fun translate(vararg translation: Number): Point {
        require(translation.size == dimensions) { TRANSLATION_DIMENSION_MISMATCH }
        return Point((0 until dimensions).map { this[it] + translation[it].toDouble() })
    }

    /** Returns this point moved by [translation] along every axis. */
    fun translate(translation: Number): Point = Point((0 until dimensions).map { this[it] + translation.toDouble() })

    /** Returns this point moved by [translation]. Requires equal dimensions. */
    fun translate(translation: Point): Point {
        require(translation.dimensions == dimensions) { TRANSLATION_DIMENSION_MISMATCH }
        return Point((0 until dimensions).map { this[it] + translation[it].toDouble() })
    }

    /** Returns this point with every coordinate multiplied by [factor]. */
    fun scale(factor: Number): Point = Point(coordinates.mapMultiply(factor.toDouble()))

    /** Returns the sum of the coordinates. */
    fun sum(): Float = coordinates.toArray().sum().toFloat()

    /** Returns the mean of the coordinates. */
    fun average(): Float = coordinates.toArray().average().toFloat()

    /** Alias for [average]. */
    fun avg(): Float = average()

    /** Returns the largest coordinate. */
    fun max(): Float = toList().max()

    /** Returns the index of the first largest coordinate. */
    fun maxIndex(): Int {
        val coordinatesList = toList()
        return coordinatesList.indexOf(coordinatesList.max())
    }

    /** Returns the point with every coordinate negated. */
    fun negate(): Point = this * -1

    /** Returns the Euclidean distance to [other]. */
    fun distance(other: Point): Float = coordinates.getDistance(other.toRealVector()).toFloat()

    /** Alias for [distance], as in Processing PVector. */
    fun dist(other: Point): Float = distance(other)

    /** Returns the Euclidean length. */
    fun magnitude(): Float = coordinates.norm.toFloat()

    /** Alias for [magnitude], as in Processing PVector. */
    fun mag(): Float = magnitude()

    /** Returns the squared Euclidean length. */
    fun magnitudeSquared(): Float = coordinates.toArray().sumOf { it * it }.toFloat()

    /** Alias for [magnitudeSquared], as in Processing PVector. */
    fun magSq(): Float = magnitudeSquared()

    /** Returns the unit vector in the same direction. Throws MathArithmeticException for a zero vector. */
    fun normalize(): Point = Point(coordinates.unitVector())

    /** Returns this 2D point rotated by [angle] degrees, clockwise in standard axes. Requires 2D. */
    fun rotate(angle: Number): Point {
        require(dimensions == 2) { "Invalid dimensions. Points must have dimension equal to 2 for this method" }
        return rotateAxis(i = 0, j = 1, angle = angle.toFloat())
    }

    /** Returns this point rotated by [angle] degrees in the plane of axes 0 and 1. Requires 3D or more. */
    fun rotateZ(angle: Number): Point {
        require(dimensions >= SPATIAL_DIMENSIONS) { NOT_SPATIAL }
        return rotateAxis(i = 0, j = 1, angle = angle.toFloat())
    }

    /** Returns this point rotated by [angle] degrees in the plane of axes 0 and 2. Requires 3D or more. */
    fun rotateY(angle: Number): Point {
        require(dimensions >= SPATIAL_DIMENSIONS) { NOT_SPATIAL }
        return rotateAxis(i = 0, j = 2, angle = angle.toFloat())
    }

    /** Returns this point rotated by [angle] degrees in the plane of axes 1 and 2. Requires 3D or more. */
    fun rotateX(angle: Number): Point {
        require(dimensions >= SPATIAL_DIMENSIONS) { NOT_SPATIAL }
        return rotateAxis(i = 1, j = 2, angle = angle.toFloat())
    }

    /** Returns this point rotated by [angle] degrees in the plane of axes [i] < [j]. */
    fun rotateAxis(
        i: Int,
        j: Int,
        angle: Number,
    ): Point {
        val rotation =
            RotationMatrixUtils.getRotationMatrixForAxis(i = i, j = j, dimensions = dimensions, angle = angle.toFloat())
        return rotation.multiplyPoint(this)
    }

    /** Returns this point rotated by [angle] radians in plane [i] of [RotationMatrix.getCoordinates]. */
    fun rotateAxis(
        i: Int,
        angle: Number,
    ): Point = RotationMatrix(i, dimensions, angle).dot(this)

    /** Returns this point rotated by [angle] degrees in the plane of axes [i] < [j] around [referencePoint]. */
    fun rotateAxisAroundReferencePoint(
        i: Int,
        j: Int,
        angle: Number,
        referencePoint: Point,
    ): Point = (this - referencePoint).rotateAxis(i, j, angle) + referencePoint

    /** Returns the angle in radians between this vector and [point]. */
    fun angleBetween(point: Point): Float = acos(normalize().dot(point.normalize()).toDouble()).toFloat()

    /** Returns the hyperspherical angles in radians of this direction, as [NEllipsoid.anglesForPointOnSurface]. */
    fun headings(): List<Float> = NSphere.unitNSphere(dimensions).anglesForPointOnSurface(normalize())

    /** Returns the 2D heading in radians from the positive y axis towards positive x, unlike Processing. */
    fun heading(): Float {
        require(dimensions == 2) {
            "heading() is only defined for 2D points. For higher dimensions " +
                "use headings(). Function is implemented to comply with Processing PVector"
        }
        val headings = headings()

        check(headings.size == 1) { "headings() should return a list of size 1 for 2D points" }
        return headings[0]
    }

    /** Returns the stereographic projection one dimension down: leading coordinates over `distance - last`. */
    fun projectDown(distance: Float = 1.0f): Point =
        Point(StereographicProjection.projectDownOneDimension(distance, coordinates))

    /** Returns this point projected down to 3D by repeated [projectDown]. Requires 3D or more. */
    fun projectDownTo3D(distance: Float = 1.0f): Point {
        require(dimensions >= SPATIAL_DIMENSIONS) { "Point must have dimension 4 to be projected down to 3D" }
        var point = this.copy()
        repeat(dimensions - SPATIAL_DIMENSIONS) {
            point = point.projectDown(distance)
        }
        return point
    }

    /** Returns the vector in the same direction with length [length]. */
    fun newWithLength(length: Number): Point = Point(coordinates.unitVector().map { it * length.toDouble() })

    /** Alias for [newWithLength] that prints a warning: unlike Processing, it returns a new vector. */
    fun setMag(length: Number): Point {
        println(
            "[WARNING] setMag() returns a new Vector instead of modifying the old one. Overwrite you variable " +
                "with the result of setMag()",
        )
        return newWithLength(length)
    }

    /** Returns a copy if at most [length] long, else rescaled to [length]. Requires [length] >= 0. */
    fun limit(length: Number): Point {
        require(!(length.toFloat() < 0.0f)) { "Length must be a positive number" }
        return if (length.toFloat() >= this.magnitude()) {
            copy()
        } else {
            Point(coordinates.unitVector().map { it * length.toDouble() })
        }
    }

    /** Returns the linear interpolation `(1 - t) · this + t · other`. Requires equal dimensions. */
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

    /** Returns this point with the coordinates at the two positions swapped. */
    fun swapEntries(
        firstPositionOne: Int,
        secondPositionOne: Int,
    ): Point {
        val pointAsList = this.toDoubleArray().toMutableList()
        Collections.swap(pointAsList, firstPositionOne, secondPositionOne)
        return Point(pointAsList.toList())
    }

    /** Returns this point with [values] appended as new coordinates. */
    fun extend(vararg values: Number): Point = Point(toDoubleArray().plus(values.map { it.toDouble() }).toList())

    override fun makeAffine(): Point = extend(1)

    /** Returns a copy of this point. */
    fun copy(): Point = Point(coordinates.copy())

    /** Returns a copy as an Apache Commons Math [RealVector]. */
    fun toRealVector(): RealVector = coordinates.copy()

    /** Returns the coordinates as a new `DoubleArray`. */
    fun toDoubleArray(): DoubleArray = coordinates.toArray()

    /** Alias for [toFloatArray], as in Processing PVector. */
    fun array(): FloatArray = toFloatArray()

    /** Returns the coordinates as a new `FloatArray`. */
    fun toFloatArray(): FloatArray = coordinates.toArray().map { it.toFloat() }.toFloatArray()

    /** Returns the coordinates as a list of `Double`. */
    fun toDoubleList(): List<Double> = coordinates.toArray().toList()

    /** Returns the coordinates as a list of `Float`. */
    fun toFloatList(): List<Float> =
        coordinates
            .toArray()
            .map { it.toFloat() }
            .toFloatArray()
            .toList()

    /** Returns a Processing [PVector]. Requires 2D or 3D. */
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

    /** Draws this point on [sketch]. Requires 2D or 3D. */
    fun draw(sketch: PApplet) {
        when (dimensions) {
            2 -> sketch.point(this[0], this[1])
            SPATIAL_DIMENSIONS -> sketch.point(this[0], this[1], this[2])
            else -> throw IllegalArgumentException("Can't convert Point to PVector - Too many dimensions")
        }
    }

    /** Builders for points. */
    companion object {
        /** Returns the origin in [dimensions] dimensions. */
        fun zeros(dimensions: Int): Point = Point(List(dimensions) { 0 })

        /** Returns a uniformly random point on the unit sphere in [dimensions] dimensions, which must be >= 0. */
        fun randomPoint(dimensions: Int): Point {
            require(dimensions >= 0) { "Dimensions must greater or equal than zero" }

            return Point(*drawPointOnUnitNSphere(dimensions).toArray().toTypedArray())
        }

        /** Returns a uniformly random point on the unit circle. */
        fun random2D(): Point = Point.randomPoint(2)

        /** Returns a uniformly random point on the unit sphere in 3D. */
        fun random3D(): Point = Point.randomPoint(SPATIAL_DIMENSIONS)

        /**
         * Generate a 2D GVector from an angle. For compliance with Processing PVector class
         *
         * @param angle The angle is in radians.
         * @return The GVector with magnitude 1 for the corresponding angle.
         */
        fun fromAngle(angle: Number): Point = NSphere.unitNSphere(2).pointOnSurface(angle)

        /** Always throws IllegalArgumentException today: the sphere has as many dimensions as angles, one too many. */
        fun fromAngles(vararg angles: Number): Point = NSphere.unitNSphere(angles.size).pointOnSurface(*angles)

        /** Returns the 2D point at the x and y of [pVector]. */
        fun fromPVector2D(pVector: PVector): Point = Point(pVector.x, pVector.y)

        /** Returns the point at the x and y of [pVector]; z is dropped today. */
        fun fromPVector3D(pVector: PVector): Point = Point(pVector.x, pVector.y)
    }
}

private const val TRANSLATION_DIMENSION_MISMATCH = "Translation does not match point dimension"
private const val TOO_FEW_DIMENSIONS = "Point does not have enough dimensions"
private const val NOT_SPATIAL = "Invalid dimensions. Point must have at least 3 dimensions for this method"

/** Returns [point] as a row vector multiplied by this matrix. */
fun RealMatrix.multiplyPoint(point: Point): Point = Point(this.preMultiply(point.toRealVector()))

/** Returns the point with these numbers as coordinates. */
fun Iterable<Number>.toPoint(): Point = Point(this.toList())

/** Returns [point] with this number added to every coordinate. */
operator fun Number.plus(point: Point): Point = point + this

/** Returns this number minus each coordinate of [point]. */
operator fun Number.minus(point: Point): Point = Point(point.map { this.toFloat() - it })

/** Returns [point] with every coordinate multiplied by this number. */
operator fun Number.times(point: Point): Point = point * this

/** Returns this number divided by each coordinate of [point]. */
operator fun Number.div(point: Point): Point = Point(point.map { this.toFloat() / it })
