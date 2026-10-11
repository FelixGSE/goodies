package goodies.brush

import goodies.geometry.Point
import org.apache.commons.math3.distribution.MultivariateNormalDistribution
import org.apache.commons.math3.distribution.NormalDistribution
import kotlin.math.abs

internal fun samplePins(
    mean: DoubleArray,
    covariance: Array<DoubleArray>,
    count: Int,
): List<Point> {
    val distribution = MultivariateNormalDistribution(mean, covariance)
    return List(count) { Point(distribution.sample().toList()) }
}

internal fun absoluteNormalSamples(
    mean: Number,
    std: Number,
    count: Int,
): List<Float> {
    val distribution = NormalDistribution(mean.toDouble(), std.toDouble())
    return List(count) { abs(distribution.sample()).toFloat() }
}
