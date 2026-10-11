package goodies.algebra

/** Value that can be lifted into homogeneous coordinates. */
interface Graphics {
    fun makeAffine(): Graphics
}
