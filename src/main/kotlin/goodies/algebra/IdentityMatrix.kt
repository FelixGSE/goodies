package goodies.algebra

import org.apache.commons.math3.linear.MatrixUtils

/** The [dimensions] by [dimensions] identity matrix. */
open class IdentityMatrix(
    dimensions: Int,
) : GMatrix(MatrixUtils.createRealIdentityMatrix(dimensions))
