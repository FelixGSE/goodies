package goodies.algebra

import goodies.geometry.Point
import org.apache.commons.math3.linear.*

open class GMatrix(private val matrix: RealMatrix) : Iterable<Point>, Graphics, Matrix {

    constructor(values: List<List<Number>>) : this(
        MatrixUtils.createRealMatrix(
            values.map { it.map { inner -> inner.toDouble() }.toDoubleArray() }.toTypedArray(),
        ),
    )

    constructor(values: Array<Array<Number>>) : this(
        MatrixUtils.createRealMatrix(
            values.map { it.map { inner -> inner.toDouble() }.toDoubleArray() }.toTypedArray(),
        ),
    )

    constructor(values: List<Point>, orientation: String = "row") : this(
        when (orientation) {
            "column" -> MatrixUtils.createRealMatrix(values.map { it.toDoubleArray() }.toTypedArray())
            "row" -> MatrixUtils.createRealMatrix(values.map { it.toDoubleArray() }.toTypedArray())
            else -> throw IllegalArgumentException("Orientation must be either 'column' or 'row'")
        },
    )

    // toString with size
    override fun toString(): String {
        return "GMatrix(${matrix.rowDimension}x${matrix.columnDimension})"
    }

    override fun hashCode(): Int {
        var result = matrix.hashCode()
        result = 31 * result + size.hashCode()
        return result
    }

    override fun iterator(): Iterator<Point> {
        return rows.iterator()
    }

    operator fun get(i: Int, j: Int): Float {
        return matrix.getEntry(i, j).toFloat()
    }

    operator fun get(index: Int): Point {
        return Point(matrix.getRow(index).toList())
    }

    operator fun plus(other: GMatrix): GMatrix {
        return GMatrix(matrix.add(other.toRealMatrix()))
    }

    operator fun plus(scalar: Number): GMatrix {
        return GMatrix(matrix.scalarAdd(scalar.toDouble()))
    }

    operator fun minus(other: GMatrix): GMatrix {
        return GMatrix(matrix.subtract(other.toRealMatrix()))
    }

    operator fun minus(scalar: Number): GMatrix {
        return GMatrix(matrix.scalarAdd(-1.0 * scalar.toDouble()))
    }

    operator fun times(other: GMatrix): GMatrix {
        return this.elementWiseTimes(other)
    }

    operator fun times(point: Point): Point {
        return this.dot(point)
    }

    operator fun times(scalar: Number): GMatrix {
        return GMatrix(matrix.scalarMultiply(scalar.toDouble()))
    }

    operator fun div(scalar: Number): GMatrix {
        return GMatrix(matrix.scalarMultiply(1.0 / scalar.toDouble()))
    }

    val copy: GMatrix
        get() {
            return GMatrix(matrix.copy())
        }

    override fun makeAffine(): GMatrix {
        return GMatrix(columns.map { it.extend(0) } + listOf(Point(List(size.first) { 0 } + 1)))
    }

    fun center(): GMatrix {
        return GMatrix(columns.map { it - it.average() }, orientation = "column")
    }

    fun normalize(): GMatrix {
        return GMatrix(columns.map { it.normalize() }, orientation = "column")
    }

    fun rowMean(): Point {
        return Point(rows.map { it.average() })
    }

    fun colMean(): Point {
        return Point(columns.map { it.average() })
    }

    fun rowSum(): Point {
        return Point(rows.map { it.sum() })
    }

    fun colSum(): Point {
        return Point(rows.map { it.sum() })
    }

    fun rowMax(): Point {
        return Point(rows.map { it.max() })
    }

    fun colMax(): Point {
        return Point(columns.map { it.max() })
    }

    fun rowBind(matrix: GMatrix): GMatrix {
        require(this.size.first == matrix.size.first) { "Matrices must have the same number of columns" }
        return GMatrix(this.rows + matrix.rows)
    }

    fun colBind(matrix: GMatrix): GMatrix {
        require(this.size.second == matrix.size.second) { "Matrices must have the same number of columns" }
        return GMatrix(this.columns + matrix.columns)
    }

    val size: Pair<Int, Int>
        get() {
            return Pair(matrix.rowDimension, matrix.columnDimension)
        }

    val isSquare: Boolean
        get() {
            return matrix.isSquare
        }

    val isSymmetric: Boolean
        get() {
            return MatrixUtils.isSymmetric(matrix, 0.000_001)
        }

    val isSingular: Boolean
        get() {
            return LUDecomposition(matrix).solver.isNonSingular
        }

    val isOrthogonal: Boolean
        get() {
            return transpose == inverse
        }

    val entrySum: Float
        get() {

            var sumVal = 0.0
            matrix.walkInOptimizedOrder(object : org.apache.commons.math3.linear.DefaultRealMatrixChangingVisitor() {
                override fun visit(row: Int, column: Int, value: Double): Double {
                    sumVal += value
                    return value
                }
            })

            return sumVal.toFloat()
        }

    val trace: Float
        get() {
            if (!isSquare) {
                throw IllegalArgumentException("Matrix must be square")
            } else {
                return matrix.trace.toFloat()
            }
        }

    override val determinant: Float
        get() {
            if (!isSquare) {
                throw IllegalArgumentException("Matrix must be square")
            } else {
                return LUDecomposition(matrix).determinant.toFloat()
            }
        }

    val rank: Int
        get() {
            val luDecomposition = LUDecomposition(matrix)
            val rowEchelonForm = luDecomposition.u

            var rank = 0
            for (row in 0 until rowEchelonForm.rowDimension) {
                val rowVector = rowEchelonForm.getRowVector(row)
                if (rowVector.norm > 0.0) {
                    rank++
                }
            }

            return rank
        }

    val inverse: GMatrix
        get() {
            if (!isSquare) {
                throw IllegalArgumentException("Matrix must be square")
            } else {
                return GMatrix(LUDecomposition(matrix).solver.inverse)
            }
        }

    val transpose: GMatrix
        get() {
            return GMatrix(matrix.transpose())
        }

    fun pow(exponent: Int): GMatrix {
        return GMatrix(matrix.power(exponent))
    }

    fun elementWiseTimes(otherMatrix: GMatrix): GMatrix {
        if (size != otherMatrix.size) {
            throw IllegalArgumentException("Matrices must have equal size")
        }
        return GMatrix(matrix.multiply(otherMatrix.toRealMatrix()))
    }

    override fun dot(other: Matrix): Matrix {
        return GMatrix(matrix.preMultiply(other.toRealMatrix()))
    }

    override fun dot(other: Point): Point {
        return Point(matrix.preMultiply(other.toRealVector()))
    }

    fun getRow(index: Int): Point {
        return Point(matrix.getRow(index).toList())
    }

    fun getColumn(index: Int): Point {
        return Point(matrix.getColumn(index).toList())
    }

    val rows: List<Point>
        get() {

            val rowList = mutableListOf<Point>()
            for (i in 0 until size.first) {
                rowList.add(Point(matrix.getRowVector(i)))
            }
            return rowList.toList()
        }

    val columns: List<Point>
        get() {

            val columnList = mutableListOf<Point>()
            for (i in 0 until size.first) {
                columnList.add(Point(matrix.getColumnVector(i)))
            }
            return columnList.toList()
        }

    fun luDecomposition(): Triple<GMatrix, GMatrix, GMatrix> {
        val luDecomposition = LUDecomposition(matrix)
        return Triple(GMatrix(luDecomposition.l), GMatrix(luDecomposition.u), GMatrix(luDecomposition.p))
    }

    val eigenValues: List<Float>
        get() {
            if (!isSquare) {
                throw IllegalArgumentException("Matrix must be square")
            } else {
                return EigenDecomposition(matrix).realEigenvalues.map { it.toFloat() }
            }
        }

    val eigenVectors: List<Point>
        get() {
            if (!isSquare) {
                throw IllegalArgumentException("Matrix must be square")
            } else {
                return GMatrix(EigenDecomposition(matrix).v).columns
            }
        }

    val svd: Triple<GMatrix, GMatrix, GMatrix>
        get() {
            val svd = SingularValueDecomposition(matrix)
            return Triple(GMatrix(svd.u), GMatrix(svd.s), GMatrix(svd.v))
        }

    val isPositiveDefinite: Boolean
        get() {
            return eigenValues.all { it > 0.0 }
        }

    val isPositiveSemiDefinite: Boolean
        get() {
            return eigenValues.all { it >= 0.0 }
        }
    val isNegativeDefinite: Boolean
        get() {
            return eigenValues.all { it < 0.0 }
        }

    val isNegativeSemiDefinite: Boolean
        get() {
            return eigenValues.all { it <= 0.0 }
        }

    val isIndefinite: Boolean
        get() {

            val containsPositive = eigenValues.any { it > 0 }
            val containsNegative = eigenValues.any { it < 0 }

            return containsPositive && containsNegative
        }

//    val nullSpace: Point
//        get() {
//            val svd = SingularValueDecomposition(matrix)
//            val rank = svd.rank
//            val vt = svd.vt
//
//            val nullSpaceVectors = mutableListOf<RealVector>()
//
//            for (i in rank until vt.rowDimension) {
//                val nullSpaceVector = vt.getRowVector(i)
//                nullSpaceVectors.add(nullSpaceVector)
//            }
//
//            return Point(nullSpaceVectors)
//        }

    override fun toRealMatrix(): RealMatrix {
        return matrix.copy()
    }

    fun toArrayOfDoubleArray(): Array<DoubleArray> {
        return rows.map { it.toDoubleArray() }.toTypedArray()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is GMatrix) return false

        if (matrix != other.matrix) return false
        return rows == other.rows
    }
}

operator fun Number.plus(gMatrix: GMatrix): GMatrix {
    return gMatrix + this
}

operator fun Number.minus(gMatrix: GMatrix): GMatrix {
    return gMatrix + (-1.0 * this.toDouble())
}

operator fun Number.times(gMatrix: GMatrix): GMatrix {
    return gMatrix * this
}

operator fun Number.div(gMatrix: GMatrix): GMatrix {
    return gMatrix / this
}
