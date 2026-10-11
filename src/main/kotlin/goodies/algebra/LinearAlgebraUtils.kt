package goodies.algebra

import goodies.geometry.Point
import org.apache.commons.math3.linear.LUDecomposition

/** Returns true when every point in [vectors] has the dimension of the first one. */
fun allGVectorsHaveEqualDimensions(vectors: List<Point>): Boolean {
    val dimensions = vectors[0].dimensions
    return vectors.all { it.dimensions == dimensions }
}

/**
 * Returns true when the square matrix with [vectors] as rows is non-singular.
 *
 * Throws IllegalArgumentException when the dimensions differ.
 */
fun areLinearlyIndependent(vectors: List<Point>): Boolean {
    require(allGVectorsHaveEqualDimensions(vectors)) { "All vectors must have the same dimension" }

    return GMatrix(vectors, orientation = "column").isSingular
}

/** Solves `coefficients · x = constants` for x by LU decomposition. */
fun solveLinearSystem(
    coefficients: GMatrix,
    constants: Point,
): Point {
    val mat = coefficients.toRealMatrix()
    val vec = constants.toRealVector()
    val luDecomposition = LUDecomposition(mat)
    val solver = luDecomposition.solver

    return Point(solver.solve(vec))
}
