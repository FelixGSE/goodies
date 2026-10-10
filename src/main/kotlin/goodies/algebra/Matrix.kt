package goodies.algebra

import goodies.geometry.Point
import org.apache.commons.math3.linear.RealMatrix

interface Matrix {

    val determinant: Float

    fun dot(other: Matrix): Matrix
    fun dot(other: Point): Point
    fun toRealMatrix(): RealMatrix
}
