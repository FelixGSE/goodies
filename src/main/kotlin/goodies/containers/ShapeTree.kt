import goodies.containers.ShapeTreeNode

class ShapeTree<T>(
    val root: ShapeTreeNode<T>,
) {
    fun height(node: ShapeTreeNode<T>? = root): Int {
        if (node == null) return 0
        return maxOf(a = height(node.left), b = height(node.right)) + 1
    }

    fun preorderTraversal(node: ShapeTreeNode<T>? = root): List<T> {
        val result = mutableListOf<T>()
        if (node != null) {
            result.add(node.value)
            result.addAll(preorderTraversal(node.left))
            result.addAll(preorderTraversal(node.right))
        }
        return result
    }

    fun inorderTraversal(node: ShapeTreeNode<T>? = root): List<T> {
        val result = mutableListOf<T>()
        if (node != null) {
            result.addAll(inorderTraversal(node.left))
            result.add(node.value)
            result.addAll(inorderTraversal(node.right))
        }
        return result
    }

    fun postorderTraversal(node: ShapeTreeNode<T>? = root): List<T> {
        val result = mutableListOf<T>()
        if (node != null) {
            result.addAll(postorderTraversal(node.left))
            result.addAll(postorderTraversal(node.right))
            result.add(node.value)
        }
        return result
    }

    fun traverseLeafNodes(node: ShapeTreeNode<T>? = root): List<T> {
        val result = mutableListOf<T>()

        if (node != null) {
            if (node.left == null && node.right == null) {
                result.add(node.value)
            } else {
                result.addAll(traverseLeafNodes(node.left))
                result.addAll(traverseLeafNodes(node.right))
            }
        }

        return result
    }
}
