package goodies.containers

class ShapeTreeNode<T>(
    val value: T,
    val left: ShapeTreeNode<T>? = null,
    val right: ShapeTreeNode<T>? = null
)

