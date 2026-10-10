package goodies.algebra

import org.apache.commons.math3.linear.MatrixUtils
import org.apache.commons.math3.linear.RealMatrix
import kotlin.math.cos
import kotlin.math.sin

object RotationMatrixUtils {
    @JvmStatic
    fun getRotationMatrixForAxis(
        i: Int,
        j: Int,
        dimensions: Int,
        angle: Float,
    ): RealMatrix {
        require(!(i > dimensions || j > dimensions))
        require(i < j) { "Dimension j must be greater than i" }

        val angleAsRadians = Math.toRadians(angle.toDouble())

        val rotationMatrix = MatrixUtils.createRealIdentityMatrix(dimensions)

        val rowI = rotationMatrix.getRow(i)
        rowI[i] = cos(angleAsRadians)
        rowI[j] = -sin(angleAsRadians)
        rotationMatrix.setRow(i, rowI)

        val rowJ = rotationMatrix.getRow(j)
        rowJ[i] = sin(angleAsRadians)
        rowJ[j] = cos(angleAsRadians)
        rotationMatrix.setRow(j, rowJ)

        return rotationMatrix
    }
}
