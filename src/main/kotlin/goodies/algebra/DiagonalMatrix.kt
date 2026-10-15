package goodies.algebra

import org.apache.commons.math3.linear.MatrixUtils

/** Square [GMatrix] with [data] on the diagonal and zeros elsewhere. */
class DiagonalMatrix(
    data: Collection<Number>,
) : GMatrix(MatrixUtils.createRealDiagonalMatrix(data.map { it.toDouble() }.toDoubleArray()))
