package goodies.algebra

import org.apache.commons.math3.linear.MatrixUtils
import org.apache.commons.math3.linear.RealMatrix

/**
 * Meant to project [dimensions] down to targetDimensions.
 *
 * Today the target is ignored and the result is the [dimensions] by [dimensions] identity matrix.
 */
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
