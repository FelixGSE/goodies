package extensions.modules.shapes;

import java.util.Iterator;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import extensions.modules.grid.Grid;
import lombok.Getter;
import lombok.ToString;
import processing.core.PApplet;
import processing.core.PVector;

@Getter
@ToString
public class Cube implements Iterable<PVector> {

    private final PVector A = new PVector(-1, -1, -1);
    private final PVector B = new PVector(1, -1, -1);
    private final PVector C = new PVector(1, 1, -1);
    private final PVector D = new PVector(-1, 1, -1);

    private final PVector E = new PVector(-1, -1, 1);
    private final PVector F = new PVector(1, -1, 1);
    private final PVector G = new PVector(1, 1, 1);
    private final PVector H = new PVector(-1, 1, 1);

    List<PVector> vertices = List.of(A, B, C, D, E, F, G, H);

    private final PVector center = computeCenter();

    private final PVector bottom = new PVector(0, -1, 0);
    private final PVector top = new PVector(0, 1, 0);
    private final PVector near = new PVector(0, 0, 1);
    private final PVector far = new PVector(0, 0, -1);
    private final PVector left = new PVector(-1, 0, 0);
    private final PVector right = new PVector(1, 0, 0);

    private final List<PVector> faceCenter = List.of(bottom, top, near, far, left, right);

    public void scale(float factor) {
        for(PVector vertex : this){
            vertex.mult(factor);
        }

        for(PVector vertex : getFaceCenter()){
            vertex.mult(factor);
        }

    }


    public void translate(PVector translation){
        for(PVector vertex : this){
            vertex.add(translation);
        }
        center.add(translation);

        for(PVector vertex : getFaceCenter()){
            vertex.add(translation);
        }

    }

    public static @NotNull Cube getCube(PVector translation, float scale) {
        Cube cube = new Cube();
        cube.scale(scale);
        cube.translate(translation);
        return cube;
    }

    public static @NotNull Cube getCube(PVector translation) {
        Cube cube = new Cube();
        cube.translate(translation);
        return cube;
    }

    public static @NotNull Cube getCube(float scale) {
        Cube cube = new Cube();
        cube.scale(scale);
        return cube;
    }

    public Grid getEqualGridPartition(int parts){

        return new Grid(getLeft().x, getRight().x, parts, getBottom().y, getTop().y, parts, getFar().z, getNear().z, parts, 3);

    }

    private PVector computeCenter(){
        float x = 0;
        float y = 0;
        float z = 0;

        for(PVector vertex : this){
            x = x + vertex.x;
            y = y + vertex.y;
            z = z + vertex.z;
        }

        return new PVector(x/8, y/8, z/8);
    }

    public float getLength (){
        return PApplet.abs(getRight().x - getLeft().x);
    }

    public float getWidth (){
        return PApplet.abs(getNear().z - getFar().z);
    }

    public float getHeight(){
        return PApplet.abs(getTop().y - getBottom().y);
    }

    public float getVolume(){
        return getLength() * getWidth() * getHeight();
    }

    @Override
    public Iterator<PVector> iterator() {
        return vertices.iterator();
    }


    public void draw(@NotNull PApplet sketch) {
        sketch.point(getA().x, getA().y, getA().z);
        sketch.point(getB().x, getB().y, getB().z);
        sketch.point(getC().x, getC().y, getC().z);
        sketch.point(getD().x, getD().y, getD().z);
        sketch.point(getE().x, getE().y, getE().z);
        sketch.point(getF().x, getF().y, getF().z);
        sketch.point(getG().x, getG().y, getG().z);
        sketch.point(getH().x, getH().y, getH().z);
    }


}
