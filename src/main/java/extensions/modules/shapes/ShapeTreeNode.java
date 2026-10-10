package extensions.modules.shapes;

public class ShapeTreeNode {

    private ShapeTreeNode left;
    private ShapeTreeNode right;
    private Shape shape;
    private Line split;

    public ShapeTreeNode(Shape shape) {
        this.shape = shape;
    }

    public void setLeft(ShapeTreeNode node) {
        this.left = node;
    }

    public ShapeTreeNode getLeft() {
        return this.left;
    }

    ;

    public void setRight(ShapeTreeNode node) {
        this.right = node;
    }

    public ShapeTreeNode getRight() {
        return this.right;
    }

    ;

    public void setShape(Shape shape) {
        this.shape = shape;
    }

    public Shape getShape() {
        return this.shape;
    }

    ;

    public void setSplit(Line splitLine) {
        this.split = splitLine;
    }

    ;

    public Line getSplit() {
        return this.split;
    }

    ;


}
