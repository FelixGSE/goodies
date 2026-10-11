package goodies.brush

import goodies.geometry.Point
import org.apache.commons.math3.distribution.MultivariateNormalDistribution
import org.apache.commons.math3.distribution.NormalDistribution
import kotlin.math.abs

internal fun samplePins(
    mean: DoubleArray,
    covariance: Array<DoubleArray>,
    count: Int,
): List<Point> = List(count) { Point(MultivariateNormalDistribution(mean, covariance).sample().toList()) }

internal fun absoluteNormalSamples(
    mean: Number,
    std: Number,
    count: Int,
): List<Float> = List(count) { abs(NormalDistribution(mean.toDouble(), std.toDouble()).sample()).toFloat() }
