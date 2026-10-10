package goodies.geometry.interfaces

import goodies.geometry.Point

interface Shape {

    fun center(): Shape
    fun centerAt(point: Point): Shape
    fun normalize(): Shape

    val isClosed: Boolean

//    fun shear
//    fun rotate(angle : Float) : Geom

//    fun randomPointIn(): Point
//    fun randomPointOn(): Point
//    fun contains(): Boolean
//    fun area(): Float
//    fun circumference(): Float
//    fun deform(): Shape
//    fun translateCenter(): Shape
//    fun overlaps(): Boolean
//    fun intersects(): Boolean
//
//
}
