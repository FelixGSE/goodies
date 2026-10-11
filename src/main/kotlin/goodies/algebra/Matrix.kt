package goodies.algebra

import goodies.geometry.Point
import org.apache.commons.math3.linear.RealMatrix

/** Linear map that multiplies matrices and points. */
interface Matrix {
    val determinant: Float

    /** Returns the product of [other] and this matrix, [other] on the left. */
    fun dot(other: Matrix): Matrix

    /** Returns [other] as a row vector multiplied by this matrix. */
    fun dot(other: Point): Point

    /** Returns a copy as an Apache Commons Math [RealMatrix]. */
    fun toRealMatrix(): RealMatrix
}
