package goodies.algebra

import goodies.geometry.Point
import org.apache.commons.math3.linear.RealMatrix

/** Linear map that multiplies matrices and points. */
interface Matrix {
    val determinant: Float

    fun dot(other: Matrix): Matrix

    fun dot(other: Point): Point

    fun toRealMatrix(): RealMatrix
}
