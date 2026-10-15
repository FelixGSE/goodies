package goodies.algebra

/** Collects transformation matrices for [dimension] axes, starting with the identity. */
class TransformationStack(
    val dimension: Int,
) {
    private val matrices = mutableListOf<GMatrix>(IdentityMatrix(dimension))

    /** Adds a translation by [translationVector] along every axis. */
    fun withTranslation(translationVector: Number) {
        matrices.add(TranslationMatrix(dimensions = dimension, translationFactor = translationVector))
    }
}
