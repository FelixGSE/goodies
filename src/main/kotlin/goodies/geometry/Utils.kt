package goodies.geometry

import kotlin.math.sqrt

/** Returns true when [number] is the square of an integer. */
fun isPerfectSquare(number: Int): Boolean {
    val sqrt = sqrt(number.toDouble()).toInt()
    return sqrt * sqrt == number
}

/** Returns true when [number] is a Fibonacci number, by Gessel's test: 5n² + 4 or 5n² - 4 is a perfect square. */
fun isFibonacciNumber(number: Int): Boolean =
    isPerfectSquare(GESSEL_FACTOR * number * number + GESSEL_OFFSET) ||
        isPerfectSquare(GESSEL_FACTOR * number * number - GESSEL_OFFSET)

/** Returns the Fibonacci numbers 1, 1, 2, 3, 5, ... that lie in [a, b]; 1 appears twice. */
fun generateFibonacciInRange(
    a: Long,
    b: Long,
): List<Long> {
    val fibonacciNumbers = mutableListOf<Long>()
    var prev = 0L
    var current = 1L

    while (current <= b) {
        if (current >= a) {
            fibonacciNumbers.add(current)
        }

        val next = prev + current
        prev = current
        current = next
    }

    return fibonacciNumbers
}

/** Returns the sum of the squares of [input]. */
fun sumOfSquares(input: Iterable<Number>): Float = input.map { it.toFloat() * it.toFloat() }.sum()

/** Returns true when [input] is prime, by trial division up to half of it, O(n). */
fun isPrime(input: Int): Boolean {
    if (input <= 1) {
        return false
    }
    for (i in 2..input / 2) {
        if (input % i == 0) {
            return false
        }
    }
    return true
}

/** Returns true when [n] is even. */
fun isEven(n: Int): Boolean = n % 2 == 0

/**
 * Returns the width and the height, in that order, of the bounding box of 2D [points].
 *
 * Throws IllegalAccessException for fewer than three points.
 */
fun getBoundingBoxHeightAndWidth(points: List<Point>): List<Float> {
    if (points.size < MIN_BOUNDING_POINTS) {
        throw IllegalAccessException()
    }
    var minX = points[0].x
    var minY = points[0].y
    var maxX = points[0].x
    var maxY = points[0].y

    for (i in 1 until points.size) {
        val point = points[i]

        if (point.x < minX) {
            minX = point.x
        } else if (point.x > maxX) {
            maxX = point.x
        }

        if (point.y < minY) {
            minY = point.y
        } else if (point.y > maxY) {
            maxY = point.y
        }
    }
    return listOf(maxX - minX, maxY - minY)
}

val unitVertices: List<Point> =
    listOf(
        Point(0, 0),
        Point(1, 0),
        Point(1, 1),
        Point(0, 1),
    )

/** Returns the segments between consecutive [points], plus the one from the last back to the first when [closed]. */
fun listOfPointsToListOfLines(
    points: List<Point>,
    closed: Boolean = false,
): List<Line> {
    val lines = mutableListOf<Line>()
    for (i in 0 until points.size - 1) {
        val currentPoint = points[i]
        val nextPoint = points[i + 1]
        lines.add(Line(a = currentPoint, b = nextPoint))
    }
    if (closed) {
        lines.add(Line(a = points.last(), b = points.first()))
    }
    return lines.toList()
}

private const val GESSEL_FACTOR = 5
private const val GESSEL_OFFSET = 4
private const val MIN_BOUNDING_POINTS = 3
