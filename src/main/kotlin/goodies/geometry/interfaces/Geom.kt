package goodies.geometry.interfaces

/** Shape that can be translated and scaled. */
interface Geom {
    /** Returns this shape moved by [translation], one value per axis. */
    fun translate(vararg translation: Number): Shape

    /** Returns this shape moved by [translation] along every axis. */
    fun translate(translation: Number): Shape

    /** Returns this shape with every vertex multiplied by [factor]. */
    fun scale(factor: Number): Shape
}
