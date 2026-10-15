package goodies.algebra

/** Value that can be lifted into homogeneous coordinates. */
interface Graphics {
    /** Returns this value in homogeneous coordinates, one dimension larger. */
    fun makeAffine(): Graphics
}
