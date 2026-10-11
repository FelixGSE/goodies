package goodies.algebra

import org.apache.commons.math3.linear.MatrixUtils
import org.apache.commons.math3.linear.RealMatrix

class ScalingMatrix : GMatrix {
    constructor(vararg scalingFactors: Number) : super(
        createScalingMatrix(scalingFactors.toList()),
    )

    constructor(scalingFactors: List<Number>) : super(
        createScalingMatrix(scalingFactors),
    )

    constructor(dimensions: Int, scalingValue: Number) : super(
        createScalingMatrix(List(dimensions) { scalingValue }),
    )

    private companion object {
        fun createScalingMatrix(scalingFactors: List<Number>): RealMatrix {
            val dimensions = scalingFactors.size

            val scalingMatrix =
                MatrixUtils.createRealDiagonalMatrix(
                    scalingFactors.map { it.toDouble() }.toDoubleArray(),
                )

            require(dimensions > 0) {
                "scalingFactors must contain at least one element."
            }

            return scalingMatrix
        }
    }
}
