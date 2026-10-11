package goodies.algebra

/** Collects transformation matrices for [dimension] axes, starting with the identity. */
class TransformationStack(
    val dimension: Int,
) {
    private val matrices = mutableListOf<GMatrix>(IdentityMatrix(dimension))

    fun withTranslation(translationVector: Number) {
        matrices.add(TranslationMatrix(dimensions = dimension, translationFactor = translationVector))
    }
}
