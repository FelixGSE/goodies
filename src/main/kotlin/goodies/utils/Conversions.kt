package goodies.utils

import org.apache.commons.math3.linear.MatrixUtils
import org.apache.commons.math3.linear.RealVector
import processing.core.PApplet
import processing.core.PVector

object Conversions {
    @JvmStatic
    fun realVectorToListOfFloats(realVector: RealVector): List<Float> = realVector.toArray().map { it.toFloat() }

    @JvmStatic
    fun listOfFloatsToRealArray(listOfFloats: List<Float>): RealVector =
        MatrixUtils.createRealVector(listOfFloats.map { it.toDouble() }.toDoubleArray())

    @JvmStatic
    fun doubleToFloat(doubleValue: Double): Float = doubleValue.toFloat()

    @JvmStatic
    fun floatToDouble(floatValue: Float): Double = floatValue.toDouble()

    // DESIGN: NUM-5 renamed to a toX() extension when moved to draw/Conversions.kt
    @Suppress("ktlint:standard:function-naming")
    @JvmStatic
    fun PVectorToDoubleArray(
        pvector: PVector,
        dimensions: Int,
    ): DoubleArray =
        when (dimensions) {
            2 -> doubleArrayOf(pvector.x.toDouble(), pvector.y.toDouble())
            3 -> doubleArrayOf(pvector.x.toDouble(), pvector.y.toDouble(), pvector.z.toDouble())
            else -> throw IllegalArgumentException("Invalid number of dimensions")
        }

    @JvmStatic
    fun realVectorToPVector(realVector: RealVector): PVector {
        val arr = realVector.toArray()
        return when (arr.size) {
            2 -> PVector(arr[0].toFloat(), arr[1].toFloat())
            3 -> PVector(arr[0].toFloat(), arr[1].toFloat(), arr[2].toFloat())
            else -> throw IllegalArgumentException("Can only convert 2 and 3 dimensional values to PVector")
        }
    }

    // DESIGN: NUM-5 renamed to a toX() extension when moved to draw/Conversions.kt
    @Suppress("ktlint:standard:function-naming")
    @JvmStatic
    fun PVector2DToRealVector(pVector: PVector): RealVector =
        MatrixUtils.createRealVector(doubleArrayOf(pVector.x.toDouble(), pVector.y.toDouble()))

    // DESIGN: NUM-5 renamed to a toX() extension when moved to draw/Conversions.kt
    @Suppress("ktlint:standard:function-naming")
    @JvmStatic
    fun PVector3DToRealVector(pVector: PVector): RealVector =
        MatrixUtils.createRealVector(doubleArrayOf(pVector.x.toDouble(), pVector.y.toDouble(), pVector.z.toDouble()))

    @JvmStatic
    fun hexColorToInteger(hexString: String): Int = PApplet.unhex(hexString)

    @JvmStatic
    fun listOfFloatsToPVector(listOfFloats: List<Float>): PVector =
        when (listOfFloats.size) {
            2 -> PVector(listOfFloats[0], listOfFloats[1])
            3 -> PVector(listOfFloats[0], listOfFloats[1], listOfFloats[2])
            else -> throw IllegalArgumentException("Can only convert 2 and 3 dimensional values to PVector")
        }

    @JvmStatic
    fun listOfFloatsToDoubleArray(listOfFloats: List<Float>): DoubleArray =
        listOfFloats.map { it.toDouble() }.toDoubleArray()

    @JvmStatic
    fun listOfListOfFloatsToDoubleArrays(listOfFloats: List<List<Float>>): Array<DoubleArray> =
        listOfFloats.map { listOfFloatsToDoubleArray(it) }.toTypedArray()

    @JvmStatic
    fun doubleArrayToPVector(value: DoubleArray): PVector =
        when (value.size) {
            2 -> PVector(value[0].toFloat(), value[1].toFloat())
            3 -> PVector(value[0].toFloat(), value[1].toFloat(), value[2].toFloat())
            else -> throw IllegalArgumentException("Can only convert 2 and three dimensional Arrays to PVector")
        }

    @JvmStatic
    fun doubleArrayToListOfFloats(listOfDoubles: DoubleArray): List<Float> = listOfDoubles.map { it.toFloat() }
}
