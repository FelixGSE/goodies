package goodies.projections

import org.apache.commons.math3.linear.MatrixUtils
import org.apache.commons.math3.linear.RealVector
import processing.core.PVector

object StereographicProjection {
    @JvmStatic
    fun project3Dto2D(
        distance: Float,
        pointToProject: PVector,
    ): PVector {
        val scale = 1.0 / (distance - pointToProject.z)
        val matrixData =
            arrayOf(
                doubleArrayOf(scale, 0.0, 0.0),
                doubleArrayOf(0.0, scale, 0.0),
                doubleArrayOf(0.0, 0.0, 0.0),
            )
        val m = MatrixUtils.createRealMatrix(matrixData)
        val v =
            MatrixUtils.createRealVector(
                doubleArrayOf(pointToProject.x.toDouble(), pointToProject.y.toDouble(), pointToProject.z.toDouble()),
            )
        val projection = m.preMultiply(v)
        return PVector(projection.getEntry(0).toFloat(), projection.getEntry(1).toFloat())
    }

    @JvmStatic
    fun projectDownOneDimension(
        distance: Float,
        pointToProject: RealVector,
    ): RealVector {
        val arr = pointToProject.toArray()
        val lastElement = arr.last()
        val scale = 1.0 / (distance - lastElement)
        val projected = arr.dropLast(1).map { it * scale }.toDoubleArray()
        return MatrixUtils.createRealVector(projected)
    }

    @JvmStatic
    fun projectDownOneDimension(
        distance: Float,
        pointToProject: List<Float>,
    ): List<Float> {
        val lastElement = pointToProject.last()
        val scale = 1.0f / (distance - lastElement)
        return pointToProject.dropLast(1).map { it * scale }
    }
}
