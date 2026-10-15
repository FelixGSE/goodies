package goodies.geometry.interfaces

/** Shape without an inside. */
interface OpenShape : Shape {
    override val isClosed: Boolean get() = false
}
