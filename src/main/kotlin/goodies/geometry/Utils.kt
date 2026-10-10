package goodies.geometry

fun isPerfectSquare(number: Int): Boolean {
    val sqrt = kotlin.math.sqrt(number.toDouble()).toInt()
    return sqrt * sqrt == number
}

fun isFibonacciNumber(number: Int): Boolean =
    isPerfectSquare(5 * number * number + 4) ||
        isPerfectSquare(5 * number * number - 4)

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

fun sumOfSquares(input: Iterable<Number>): Float = input.map { it.toFloat() * it.toFloat() }.sum()

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

fun isEven(n: Int): Boolean = n % 2 == 0

fun getBoundingBoxHeightAndWidth(points: List<Point>): List<Float> {
    if (points.size < 3) {
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

val unitVertices =
    listOf(
        Point(0, 0),
        Point(1, 0),
        Point(1, 1),
        Point(0, 1),
    )

fun listOfPointsToListOfLines(
    points: List<Point>,
    closed: Boolean = false,
): List<Line> {
    val lines = mutableListOf<Line>()
    for (i in 0 until points.size - 1) {
        val currentPoint = points[i]
        val nextPoint = points[i + 1]
        lines.add(Line(currentPoint, nextPoint))
    }
    if (closed) {
        lines.add(Line(points.last(), points.first()))
    }
    return lines.toList()
}
