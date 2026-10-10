package goodies.utils

import org.apache.commons.math3.distribution.*
import kotlin.math.abs
import kotlin.random.Random

/**
 * Generates a random sample with replacement from the given input list.
 *
 * This function returns a list of length [n], where each element is randomly
 * selected from the input list. Sampling is done with replacement, meaning
 * the same element may appear more than once in the resulting sample.
 *
 * @param input The input list from which to sample.
 * @param n The size of the sample to generate.
 * @return A list containing the randomly sampled elements.
 */
fun <T> sampleWithReplacementFromList(input: List<T>, n: Int): List<T> {
    return List(n) { input.random() }
}

/** Probabilities given as `Float` are off by up to ~1e-7 each; the tolerance admits them. */
const val PROBABILITY_SUM_TOLERANCE = 1e-6

/**
 * Draws one of [items], item `i` with probability `weights[i]`.
 *
 * Weights are probabilities summing to one within [PROBABILITY_SUM_TOLERANCE].
 * Pass a seeded [rng] to make the draw reproducible.
 */
fun <T> weightedSample(items: List<T>, weights: List<Number>, rng: Random = Random.Default): T {
    val weightsAsDouble = weights.map { it.toDouble() }
    val sum = weightsAsDouble.sum()
    require(abs(sum - 1.0) <= PROBABILITY_SUM_TOLERANCE) { "Weights must sum to one, was $sum" }
    require(items.size == weights.size) { "The number of items and weights must match" }
    val cumulativeWeights = weightsAsDouble.scan(0.0) { acc, weight -> acc + weight }.drop(1)
    val randomValue = rng.nextDouble() * cumulativeWeights.last()
    val index = cumulativeWeights.indexOfFirst { it > randomValue }
    return items[index]
}

fun <T> weightedSampleWithReplacementFromList(
    items: List<T>,
    weights: List<Double>,
    n: Int,
    rng: Random = Random.Default,
): List<T> {
    return List(n) { weightedSample(items, weights, rng) }
}

fun zeroOrOne(): Int {
    return if (Math.random() > 0.5) {
        0
    } else {
        1
    }
}

fun randomBitList(n: Int): List<Int> {
    return List(n) { zeroOrOne() }
}

fun randomNormal(mean: Number, std: Number): Float {
    return NormalDistribution(
        mean.toDouble(),
        std.toDouble(),
    ).sample().toFloat()
}

fun randomNormal(
    mean: List<Number> = listOf(0, 0),
    covariance: List<List<Number>> = listOf(listOf(1, 0), listOf(0, 1)),
): List<Float> {
    require(mean.size == covariance.size) { "Dimensions of mean and covariance must be equal" }
    require(covariance.all { it.size == covariance.size }) { "Covariance matrix must be square" }

    val meanArray = mean.toTypedArray().map { it.toDouble() }.toDoubleArray()
    val covMatrixArray = covariance.map { it.map { jt -> jt.toDouble() }.toDoubleArray() }.toTypedArray()

    return MultivariateNormalDistribution(meanArray, covMatrixArray).sample().toList().map { it.toFloat() }
}

fun nRandomNormal(mean: Number, std: Number, n: Int): List<Float> {
    return List(n) { randomNormal(mean, std) }
}

fun logNormal(mu: Number, sigma: Number): Float {
    require(sigma.toFloat() > 0) { "Standard deviation (sigma) must be greater than 0" }
    val logNormalDistribution = LogNormalDistribution(mu.toDouble(), sigma.toDouble())
    return logNormalDistribution.sample().toFloat()
}

fun paretoSample(scale: Number, shape: Number): Float {
    val paretoDistribution = ParetoDistribution(scale.toDouble(), shape.toDouble())
    return paretoDistribution.sample().toFloat()
}

fun chiSquaredSample(degreesOfFreedom: Number): Float {
    val dof = degreesOfFreedom.toDouble()
    val chiSquaredDistribution = ChiSquaredDistribution(dof)
    return chiSquaredDistribution.sample().toFloat()
}

fun randomInteger(a: Int, b: Int): Int {
    require(a <= b) { "Invalid range: a must be less than or equal to b" }
    return Random.nextInt(a, b + 1)
}

fun randomInteger(n: Int, a: Int, b: Int): List<Int> {
    return List(n) { randomInteger(a, b) }
}

fun randomFloat(a: Number = 0.0, b: Number = 1.0): Float {
    require(a.toFloat() <= b.toFloat()) { "Invalid range: a must be less than or equal to b" }
    return a.toFloat() + (Random.nextFloat() * (b.toFloat() - a.toFloat()))
}

fun randomFloats(n: Int, a: Number = 0.0, b: Number = 1.0): List<Float> {
    return List(n) { randomFloat(a, b) }
}
