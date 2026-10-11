package goodies.geometry.interfaces

/** Shape that can be translated and scaled. */
interface Geom {
    fun translate(vararg translation: Number): Shape

    fun translate(translation: Number): Shape

    fun scale(factor: Number): Shape
}
