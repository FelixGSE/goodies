package goodies.geometry

import goodies.geometry.interfaces.Native2D

class Circle(
    center: Point,
    radius: Number,
) : NSphere(center, radius),
    Native2D {
    init {
        require(dimensions == 2) { "A circle can only have two dimensions" }
    }

    override fun toString(): String = "Circle{center=${center}radius=$radius}"

    fun copy(): Circle = Circle(center.copy(), radius)

    companion object {
        fun unitCircle(): NEllipsoid = unitNSphere(2)
    }
}
