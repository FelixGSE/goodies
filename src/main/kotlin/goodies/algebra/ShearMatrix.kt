package goodies.algebra

import org.apache.commons.math3.linear.MatrixUtils
import org.apache.commons.math3.linear.RealMatrix

class ShearMatrix : GMatrix {
    constructor(dimensions: Int, shearAxis: Int, shearFactor: Number) : super(
        computeShearMatrix(dimensions, shearAxis, shearFactor),
    )

    private companion object {
        fun computeShearMatrix(
            dimensions: Int,
            shearAxis: Int,
            shearFactor: Number,
        ): RealMatrix {
            require(shearAxis in 0 until dimensions) {
                "shearAxis must be a valid axis index between 0 and ${dimensions - 1}."
            }

            val identityMatrix = MatrixUtils.createRealIdentityMatrix(dimensions)

            for (i in 0 until dimensions) {
                if (i == shearAxis) {
                    identityMatrix.setEntry(i, i, 1.0)
                } else {
                    identityMatrix.setEntry(shearAxis, i, shearFactor.toDouble())
                }
            }

            return identityMatrix
        }
    }
}
