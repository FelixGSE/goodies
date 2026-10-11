package goodies.algebra

import org.apache.commons.math3.linear.MatrixUtils
import org.apache.commons.math3.linear.RealMatrix
import kotlin.math.cos
import kotlin.math.sin

class RotationMatrix(
    i: Int,
    j: Int,
    dimensions: Int,
    angle: Number,
) : GMatrix(
        getRotationMatrixForAxis(i = i, j = j, dimensions = dimensions, angle = angle),
    ) {
    constructor(index: Int, dimensions: Int, angle: Number) : this(
        getCoordinates(dimensions, index).first,
        getCoordinates(dimensions, index).second,
        dimensions,
        angle,
    )

    companion object {
        fun getRotationMatrixForAxis(
            i: Int,
            j: Int,
            dimensions: Int,
            angle: Number,
        ): RealMatrix {
            require(!((i > dimensions) or (j > dimensions)))
            require(i < j) { "Dimension j must be greater than i" }
            val rotationMatrix = MatrixUtils.createRealIdentityMatrix(dimensions)
            val rowI = rotationMatrix.getRow(i)
            rowI[i] = cos(angle.toDouble())
            rowI[j] = -sin(angle.toDouble())
            rotationMatrix.setRow(i, rowI)
            val rowJ = rotationMatrix.getRow(j)
            rowJ[i] = sin(angle.toDouble())
            rowJ[j] = cos(angle.toDouble())
            rotationMatrix.setRow(j, rowJ)
            return rotationMatrix
        }

        fun getCoordinates(
            dimensions: Int,
            index: Int,
        ): Pair<Int, Int> {
            // TODO: -1 correct?
            require(index > 0 || index < getNumberOfRotations(dimensions) - 1) { "Invalid got $dimensions and $index" }
            var currentIndex = 0
            for (i in 0 until dimensions - 1) {
                for (j in i + 1 until dimensions) {
                    if (currentIndex == index) {
                        return Pair(first = i, second = j)
                    }
                    currentIndex++
                }
            }
            error("no rotation plane $index in $dimensions dimensions")
        }

        fun getNumberOfRotations(dimensions: Int): Int = (0 until dimensions).sum()
    }

    fun toGMatrix(): GMatrix = GMatrix(toRealMatrix().copy())
}
