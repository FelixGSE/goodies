package goodies.geometry

import goodies.geometry.interfaces.Native2D

class Circle(center: Point, radius: Number) : NSphere(center, radius), Native2D {

    init {
        if (dimensions != 2) {
            throw IllegalArgumentException("A circle can only have two dimensions")
        }
    }

    override fun toString(): String {
        return "Circle{center=${center}radius=$radius}"
    }

    fun copy(): Circle {
        return Circle(center.copy(), radius)
    }

    companion object {
        fun unitCircle(): NEllipsoid {
            return unitNSphere(2)
        }
    }
}
