package goodies.geometry.interfaces

interface OpenShape : Shape {
    override val isClosed: Boolean get() = false
}
