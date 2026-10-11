package goodies.algebra

import org.apache.commons.math3.linear.MatrixUtils
import org.apache.commons.math3.linear.RealMatrix

class OrthogonalProjectionMatrix : GMatrix {
    constructor(dimensions: Int, targetDimensions: Int) : super(getOrthogonalProjectionMatrix(dimensions, dimensions))

    private companion object {
        fun getOrthogonalProjectionMatrix(
            dimensions: Int,
            targetDimensions: Int,
        ): RealMatrix {
            val identityMatrix = MatrixUtils.createRealIdentityMatrix(dimensions)

            return identityMatrix.getSubMatrix(
                0,
                targetDimensions - 1,
                0,
                identityMatrix.columnDimension - 1,
            )
        }
    }
}
