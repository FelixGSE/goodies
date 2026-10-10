package extensions.modules.shapes;

import extensions.modules.grid.Grid;
import lombok.Getter;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;
import processing.core.PVector;

import java.util.Iterator;
import java.util.List;

@Getter
@ToString
public class Square implements Iterable<PVector> {

  private final PVector A = new PVector(-1, -1);
  private final PVector B = new PVector(1, -1);
  private final PVector C = new PVector(1, 1);
  private final PVector D = new PVector(-1, 1);

  List<PVector> vertices = List.of(A, B, C, D);

  @Override
  public Iterator<PVector> iterator() {
    return vertices.iterator();
  }

  public PVector getCenter() {
    return Utils.lineIntersection(new Line(getTop(), getBottom()), new Line(getLeft(), getRight()));
  }

  public PVector getLeft() {
    return getEdgeD().getMidPoint();
  }

  public PVector getRight() {
    return getEdgeB().getMidPoint();
  }

  public PVector getBottom() {
    return getEdgeA().getMidPoint();
  }

  public PVector getTop() {
    return getEdgeC().getMidPoint();
  }

  public Line getEdgeA() {
    return new Line(getA(), getB());
  }

  public Line getEdgeB() {
    return new Line(getB(), getC());
  }

  public Line getEdgeC() {
    return new Line(getC(), getD());
  }

  public Line getEdgeD() {
    return new Line(getD(), getA());
  }

  public void scale(float factor) {
    for (PVector vertex : this) {
      vertex.mult(factor);
    }
  }

  public void translate(PVector translation) {
    for (PVector vertex : this) {
      vertex.add(translation);
    }
  }

  public static @NotNull Square getSquare(PVector translation, float scale) {
    Square square = new Square();
    square.scale(scale);
    square.translate(translation);
    return square;
  }

  public static @NotNull Square getSquare(PVector translation) {
    Square square = new Square();
    square.translate(translation);
    return square;
  }

  public static @NotNull Square getSquare(float scale) {
    Square square = new Square();
    square.scale(scale);
    return square;
  }

  public Grid getEqualGridPartition(int parts) {

    return new Grid(getLeft().x, getRight().x, parts, getBottom().y, getTop().y, parts, 2);
  }
}
