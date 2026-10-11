package goodies.algebra

import org.apache.commons.math3.linear.MatrixUtils
import org.apache.commons.math3.linear.RealMatrix

class TranslationMatrix : GMatrix {
    constructor(vararg translationFactors: Number) : super(
        createTranslationMatrix(translationFactors.toList()),
    )

    constructor(translationFactors: List<Number>) : super(
        createTranslationMatrix(translationFactors),
    )

    constructor(dimensions: Int, translationFactor: Number) : super(
        createTranslationMatrix(List(dimensions) { translationFactor }),
    )

    private companion object {
        fun createTranslationMatrix(translationFactors: List<Number>): RealMatrix {
            val dimensions = translationFactors.size

            val translationMatrix = MatrixUtils.createRealIdentityMatrix(dimensions)

            require(dimensions > 0) {
                "translationFactors must contain at least one element."
            }

            for (i in 0 until dimensions) {
                translationMatrix.setEntry(i, dimensions - 1, translationFactors[i].toDouble())
            }

            return translationMatrix
        }
    }
}
