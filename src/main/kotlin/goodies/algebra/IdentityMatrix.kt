package goodies.algebra

import org.apache.commons.math3.linear.MatrixUtils

open class IdentityMatrix(
    dimensions: Int,
) : GMatrix(MatrixUtils.createRealIdentityMatrix(dimensions))
