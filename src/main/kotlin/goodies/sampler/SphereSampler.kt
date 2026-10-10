package goodies.sampler

import org.apache.commons.math3.distribution.NormalDistribution
import org.apache.commons.math3.linear.ArrayRealVector
import org.apache.commons.math3.linear.RealVector
import kotlin.math.pow
import kotlin.math.sqrt

object SphereSampler {

    @JvmStatic
    fun drawPointOnUnitSphere(): RealVector {
        val unitNormal = NormalDistribution(0.0, 1.0)
        val x1 = unitNormal.sample()
        val x2 = unitNormal.sample()
        val x3 = unitNormal.sample()
        val lambda = sqrt(x1.pow(2) + x2.pow(2) + x3.pow(2))
        return ArrayRealVector(doubleArrayOf(x1 / lambda, x2 / lambda, x3 / lambda), false)
    }

    @JvmStatic
    fun drawPointOnUnitNSphere(dimensions: Int): RealVector {
        val unitNormal = NormalDistribution(0.0, 1.0)
        val points = (0 until dimensions).map { unitNormal.sample() }
        val lambda = sqrt(points.sumOf { it.pow(2) })
        val normalized = points.map { it / lambda }.toDoubleArray()
        return ArrayRealVector(normalized, true)
    }

    @JvmStatic
    fun lengthOfVector(inputVector: RealVector): Double {
        return sqrt(
            inputVector.getEntry(0).pow(2) +
                inputVector.getEntry(1).pow(2) +
                inputVector.getEntry(2).pow(2)
        )
    }
}
