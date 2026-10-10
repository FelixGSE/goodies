package goodies.geometry.interfaces

interface Geom {
    fun translate(vararg translation: Number): Shape
    fun translate(translation: Number): Shape
    fun scale(factor: Number): Shape
}
