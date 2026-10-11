package goodies.algebra

import goodies.geometry.Point
import org.apache.commons.math3.linear.LUDecomposition

fun allGVectorsHaveEqualDimensions(vectors: List<Point>): Boolean {
    val dimensions = vectors[0].dimensions
    return vectors.all { it.dimensions == dimensions }
}

fun areLinearlyIndependent(vectors: List<Point>): Boolean {
    if (!allGVectorsHaveEqualDimensions(vectors)) {
        throw IllegalArgumentException("All vectors must have the same dimension")
    }

    return GMatrix(vectors, orientation = "column").isSingular
}

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
