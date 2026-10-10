package extensions.modules.shapes;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Polygon {

    public final ArrayList<PVector> vertices;
    private final int N;
    private boolean closed;

    public Polygon(ArrayList<PVector> vertices) {
        this.vertices = vertices;
        this.N = vertices.size();
        this.closed = false;
    }

    public Polygon(ArrayList<PVector> vertices, boolean closed) {
        this.vertices = vertices;
        this.N = vertices.size();
        this.closed = closed;
    }



    public static Polygon fromListOfLines(List<Line> edges){
        PVector first = Utils.roundPVector(edges.get(0).getStart(),4);
        PVector last = Utils.roundPVector(edges.get(edges.size() - 1).getEnd(),4);
        boolean closedPolygon = first == last;
        return new Polygon(Utils.lineCollectionToPointCollection(edges),closedPolygon);
    }

    public boolean getClosed(){
        return closed;
    }


    public void setClosed(boolean closed){
        this.closed = closed;
    }

    public ArrayList<PVector> getVertices() {
        return this.vertices;
    }

    public List<Line> getEdges(){
        if (closed){
            var listOfLines = Utils.pointCollectionToLineCollection(vertices);
            listOfLines.add(new Line(vertices.get(vertices.size() - 1), vertices.get(0)));
            return listOfLines;
        }
        return Utils.pointCollectionToLineCollection(vertices);

    }

    public int getN() {
        return this.N;
    }

    public PVector getVertex(int i){
        return vertices.get(i);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Polygon polygon = (Polygon) o;
        return N == polygon.N && Objects.equals(vertices, polygon.vertices);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vertices, N);
    }

    public Polygon deform(int depth){
        ArrayList<PVector> set = new ArrayList<>();
        for(Line line : getEdges()) {
            ArrayList<PVector> deformedEdge = line.subdivide(depth);
            set.addAll(deformedEdge);
        }
        return new Polygon(set);
    }

    public void draw(PApplet sketch) {
        sketch.beginShape();
        for (PVector vertex : vertices) {
            sketch.vertex(vertex.x, vertex.y);
        }
        if (closed) {
            sketch.endShape(PGraphics.CLOSE);
        } else {
            sketch.endShape();
        }
    }



}
