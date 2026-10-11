package goodies.algebra

class TransformationStack(
    val dimension: Int,
) {
    private val matrices = mutableListOf<GMatrix>(IdentityMatrix(dimension))

    fun withTranslation(translationVector: Number) {
        matrices.add(TranslationMatrix(dimension, translationVector))
    }

//    fun build(): GMatrix {
//        return matrices.reduce { acc, gMatrix -> acc.dot(gMatrix) }
//    }
}
