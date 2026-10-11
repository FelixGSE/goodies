package goodies.support

import goodies.geometry.Point
import org.assertj.core.api.AbstractAssert
import org.assertj.core.api.Assertions
import org.assertj.core.data.Offset

class PointAssert(
    actual: Point,
) : AbstractAssert<PointAssert, Point>(actual, PointAssert::class.java) {
    fun isCloseTo(
        expected: Point,
        precision: Offset<Double>,
    ): PointAssert {
        isNotNull
        Assertions
            .assertThat(actual.toDoubleArray())
            .describedAs("coordinates of %s", actual)
            .containsExactly(expected.toDoubleArray(), precision)
        return this
    }
}

fun assertThat(actual: Point): PointAssert = PointAssert(actual)
