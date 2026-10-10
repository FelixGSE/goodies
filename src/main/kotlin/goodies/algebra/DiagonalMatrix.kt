package goodies.algebra

import org.apache.commons.math3.linear.MatrixUtils

class DiagonalMatrix(
    data: Collection<Number>,
) : GMatrix(MatrixUtils.createRealDiagonalMatrix(data.map { it.toDouble() }.toDoubleArray()))
