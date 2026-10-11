package goodies.algebra

import goodies.geometry.Point
import org.apache.commons.math3.linear.DefaultRealMatrixChangingVisitor
import org.apache.commons.math3.linear.EigenDecomposition
import org.apache.commons.math3.linear.LUDecomposition
import org.apache.commons.math3.linear.MatrixUtils
import org.apache.commons.math3.linear.RealMatrix
import org.apache.commons.math3.linear.SingularValueDecomposition

/**
 * Dense matrix backed by an Apache Commons Math [RealMatrix] of `Double` entries.
 *
 * Entries and derived values are read back as `Float`; iterating yields the rows as [Point]s.
 */
open class GMatrix(
    private val matrix: RealMatrix,
) : Iterable<Point>,
    Graphics,
    Matrix {
    val size: Pair<Int, Int>
        get() = Pair(first = matrix.rowDimension, second = matrix.columnDimension)

    val isSquare: Boolean
        get() = matrix.isSquare

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

    val copy: GMatrix
        get() = GMatrix(matrix.copy())

    val transpose: GMatrix
        get() = GMatrix(matrix.transpose())

    val inverse: GMatrix
        get() {
            require(isSquare) { NOT_SQUARE }
            return GMatrix(LUDecomposition(matrix).solver.inverse)
        }

    val isSymmetric: Boolean
        get() = MatrixUtils.isSymmetric(matrix, 0.000_001)

    val isSingular: Boolean
        get() = LUDecomposition(matrix).solver.isNonSingular

    val isOrthogonal: Boolean
        get() = transpose == inverse

    val entrySum: Float
        get() {
            var sumVal = 0.0
            matrix.walkInOptimizedOrder(
                object : DefaultRealMatrixChangingVisitor() {
                    override fun visit(
                        row: Int,
                        column: Int,
                        value: Double,
                    ): Double {
                        sumVal += value
                        return value
                    }
                },
            )
            return sumVal.toFloat()
        }

    val trace: Float
        get() {
            require(isSquare) { NOT_SQUARE }
            return matrix.trace.toFloat()
        }

    override val determinant: Float
        get() {
            require(isSquare) { NOT_SQUARE }
            return LUDecomposition(matrix).determinant.toFloat()
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

    val eigenValues: List<Float>
        get() {
            require(isSquare) { NOT_SQUARE }
            return EigenDecomposition(matrix).realEigenvalues.map { it.toFloat() }
        }

    val eigenVectors: List<Point>
        get() {
            require(isSquare) { NOT_SQUARE }
            return GMatrix(EigenDecomposition(matrix).v).columns
        }

    val svd: Triple<GMatrix, GMatrix, GMatrix>
        get() {
            val svd = SingularValueDecomposition(matrix)
            return Triple(first = GMatrix(svd.u), second = GMatrix(svd.s), third = GMatrix(svd.v))
        }

    val isPositiveDefinite: Boolean
        get() = eigenValues.all { it > 0.0 }

    val isPositiveSemiDefinite: Boolean
        get() = eigenValues.all { it >= 0.0 }

    val isNegativeDefinite: Boolean
        get() = eigenValues.all { it < 0.0 }

    val isNegativeSemiDefinite: Boolean
        get() = eigenValues.all { it <= 0.0 }

    val isIndefinite: Boolean
        get() {
            val hasPositive = eigenValues.any { it > 0 }
            val hasNegative = eigenValues.any { it < 0 }

            return hasPositive && hasNegative
        }

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

    constructor(values: List<Point>, orientation: String = ROW) : this(
        when (orientation) {
            COLUMN -> MatrixUtils.createRealMatrix(values.map { it.toDoubleArray() }.toTypedArray())
            ROW -> MatrixUtils.createRealMatrix(values.map { it.toDoubleArray() }.toTypedArray())
            else -> throw IllegalArgumentException("Orientation must be either 'column' or 'row'")
        },
    )

    override fun toString(): String = "GMatrix(${matrix.rowDimension}x${matrix.columnDimension})"

    override fun hashCode(): Int {
        var result = matrix.hashCode()
        result = 31 * result + size.hashCode()
        return result
    }

    override fun iterator(): Iterator<Point> = rows.iterator()

    operator fun get(
        i: Int,
        j: Int,
    ): Float = matrix.getEntry(i, j).toFloat()

    operator fun get(index: Int): Point = Point(matrix.getRow(index).toList())

    operator fun plus(other: GMatrix): GMatrix = GMatrix(matrix.add(other.toRealMatrix()))

    operator fun plus(scalar: Number): GMatrix = GMatrix(matrix.scalarAdd(scalar.toDouble()))

    operator fun minus(other: GMatrix): GMatrix = GMatrix(matrix.subtract(other.toRealMatrix()))

    operator fun minus(scalar: Number): GMatrix = GMatrix(matrix.scalarAdd(-1.0 * scalar.toDouble()))

    operator fun times(other: GMatrix): GMatrix = this.elementWiseTimes(other)

    operator fun times(point: Point): Point = this.dot(point)

    operator fun times(scalar: Number): GMatrix = GMatrix(matrix.scalarMultiply(scalar.toDouble()))

    operator fun div(scalar: Number): GMatrix = GMatrix(matrix.scalarMultiply(1.0 / scalar.toDouble()))

    override fun makeAffine(): GMatrix =
        GMatrix(columns.map { it.extend(0) } + listOf(Point(List(size.first) { 0 } + 1)))

    fun center(): GMatrix = GMatrix(columns.map { it - it.average() }, orientation = COLUMN)

    fun normalize(): GMatrix = GMatrix(columns.map { it.normalize() }, orientation = COLUMN)

    fun rowMean(): Point = Point(rows.map { it.average() })

    fun colMean(): Point = Point(columns.map { it.average() })

    fun rowSum(): Point = Point(rows.map { it.sum() })

    fun colSum(): Point = Point(rows.map { it.sum() })

    fun rowMax(): Point = Point(rows.map { it.max() })

    fun colMax(): Point = Point(columns.map { it.max() })

    fun rowBind(matrix: GMatrix): GMatrix {
        require(this.size.first == matrix.size.first) { "Matrices must have the same number of columns" }
        return GMatrix(this.rows + matrix.rows)
    }

    fun colBind(matrix: GMatrix): GMatrix {
        require(this.size.second == matrix.size.second) { "Matrices must have the same number of columns" }
        return GMatrix(this.columns + matrix.columns)
    }

    fun pow(exponent: Int): GMatrix = GMatrix(matrix.power(exponent))

    fun elementWiseTimes(otherMatrix: GMatrix): GMatrix {
        require(size == otherMatrix.size) { "Matrices must have equal size" }
        return GMatrix(matrix.multiply(otherMatrix.toRealMatrix()))
    }

    override fun dot(other: Matrix): Matrix = GMatrix(matrix.preMultiply(other.toRealMatrix()))

    override fun dot(other: Point): Point = Point(matrix.preMultiply(other.toRealVector()))

    fun getRow(index: Int): Point = Point(matrix.getRow(index).toList())

    fun getColumn(index: Int): Point = Point(matrix.getColumn(index).toList())

    fun luDecomposition(): Triple<GMatrix, GMatrix, GMatrix> {
        val luDecomposition = LUDecomposition(matrix)
        return Triple(
            first = GMatrix(luDecomposition.l),
            second = GMatrix(luDecomposition.u),
            third = GMatrix(luDecomposition.p),
        )
    }

    override fun toRealMatrix(): RealMatrix = matrix.copy()

    fun toArrayOfDoubleArray(): Array<DoubleArray> = rows.map { it.toDoubleArray() }.toTypedArray()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is GMatrix) return false

        if (matrix != other.matrix) return false
        return rows == other.rows
    }
}

operator fun Number.plus(gMatrix: GMatrix): GMatrix = gMatrix + this

operator fun Number.minus(gMatrix: GMatrix): GMatrix = gMatrix + -1.0 * this.toDouble()

operator fun Number.times(gMatrix: GMatrix): GMatrix = gMatrix * this

operator fun Number.div(gMatrix: GMatrix): GMatrix = gMatrix / this

private const val NOT_SQUARE = "Matrix must be square"
private const val ROW = "row"
private const val COLUMN = "column"
