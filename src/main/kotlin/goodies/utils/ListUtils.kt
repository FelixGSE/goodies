package utils

fun <T> itemPercentages(items: List<T>): Map<T, Double> {
    if (items.isEmpty()) {
        throw IllegalAccessException("items must have at least one argument")
    }
    val itemCounts = items.groupingBy { it }.eachCount()
    val total = items.size.toDouble()
    return itemCounts.mapValues { (_, count) -> count / total }
}

fun cartesianProduct(
    first: Iterable<Number>,
    second: Iterable<Number>,
    vararg next: Iterable<Number>,
): List<List<Number>> {
    val a = first.toList()
    val b = second.toList()
    val sets = next.map { it.toList() }
    return listOf(a, b)
        .plus(sets)
        .fold(listOf(listOf<Number>())) { acc, set ->
            acc.flatMap { list -> set.map { element -> list + element } }
        }.toList()
}

fun floatRange(
    from: Number,
    to: Number,
    step: Number,
): List<Float> {
    val output = mutableListOf<Float>()
    var i = from.toFloat()
    while (i <= to.toDouble()) {
        output.add(i)
        i += step.toFloat()
    }
    return output
}

fun linSpace(
    start: Number,
    stop: Number,
    num: Int,
): List<Float> {
    val step = (stop.toDouble() - start.toDouble()) / (num.toDouble() - 1)
    val output = mutableListOf<Float>()
    for (i in 0 until num) {
        output.add(start.toFloat() + step.toFloat() * i)
    }
    return output.toList()
}
