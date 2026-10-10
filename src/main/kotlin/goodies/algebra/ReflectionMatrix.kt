package goodies.algebra

import org.apache.commons.math3.linear.MatrixUtils
import org.apache.commons.math3.linear.RealMatrix

class ReflectionMatrix : GMatrix {

    constructor(dimensions: Int, reflectionAxis: Int) : super(
        createMirrorMatrix(dimensions, reflectionAxis)
    )

    private companion object {
        fun createMirrorMatrix(dimensions: Int, reflectionAxis: Int): RealMatrix {
            require(reflectionAxis in 0 until dimensions) {
                "reflectionAxis must be a valid axis index between 0 and ${dimensions - 1}."
            }

            val mirrorMatrix = MatrixUtils.createRealIdentityMatrix(dimensions)
            mirrorMatrix.setEntry(reflectionAxis, reflectionAxis, -1.0)

            return mirrorMatrix
        }

    }

}