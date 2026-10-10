package extensions.modules.flowfield;

import java.util.ArrayList;
import java.util.List;

import extensions.modules.grid.Grid;
import extensions.modules.shapes.ClipObject;
import goodies.utils.Conversions;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import processing.core.PApplet;
import processing.core.PVector;
import smile.math.distance.EuclideanDistance;
import smile.neighbor.CoverTree;
import smile.neighbor.Neighbor;

@Getter
@ToString
@EqualsAndHashCode
public abstract class FlowField {
    @Setter
    private List<PVector> flowFieldVectors;
    private final Grid flowFieldCoordinateGrid;
    private final CoverTree<double[]> knn;
    private final ClipObject clippingObject;


    public FlowField(ClipObject clippingObject, int parts) {
        this.clippingObject = clippingObject;
        this.flowFieldCoordinateGrid = clippingObject.getEqualGridPartition(parts);
        this.knn = buildKnn(this.flowFieldCoordinateGrid.getCoordinates());
        initOrUpdateFlowFieldVectors();

        if (flowFieldCoordinateGrid.getCoordinates().size() != getFlowFieldVectors().size()) {
            throw new IllegalArgumentException("Coordinates and FlowFieldVectors must have the same length");
        }
    }

    private static CoverTree<double[]> buildKnn(List<List<Float>> coordinatesPoints) {

        double[][] arrayCords = Conversions.listOfListOfFloatsToDoubleArrays(coordinatesPoints);

        return new CoverTree<>(arrayCords, new EuclideanDistance());

    }

    public abstract void initOrUpdateFlowFieldVectors();


    public PVector getClosestPointInFlowField(PVector v) {

        double[] pointArray = Conversions.PVectorToDoubleArray(v, getFlowFieldCoordinateGrid().getDimensions());

        Neighbor<double[], double[]>[] neighbor = knn.knn(pointArray, 1);

        List<Float> closesCoordinates = Conversions.doubleArrayToListOfFloats(neighbor[0].value);
        int closestCoordinateIndex = getFlowFieldCoordinateGrid().getCoordinates().indexOf(closesCoordinates);

        return flowFieldVectors.get(closestCoordinateIndex);

    }

    private boolean isInFlowField(FlowFieldParticle particle) {

        return clippingObject.pointIsInside(particle.location());

    }

    public FlowFieldParticle moveParticleThroughFlowField(FlowFieldParticle particle, float topSpeed) {
        PVector closestPoint = getClosestPointInFlowField(particle.location());
        FlowFieldParticle upDatedParticle = particle.updateLocation(closestPoint, topSpeed);
        if (isInFlowField(particle)) {
            return upDatedParticle;
        } else {
            return new FlowFieldParticle(upDatedParticle.location(), upDatedParticle.velocity(), upDatedParticle.acceleration(), true);
        }
    }

    public FlowFieldParticle randomParticleInField() {
        PVector initialLocation = clippingObject.computeRandomPointIn();
        PVector initialVelocity = getClosestPointInFlowField(initialLocation);
        return FlowFieldParticle.initParticle(initialLocation, initialVelocity);
    }

    //
    public List<FlowFieldParticle> nRandomParticleInField(int n) {
        var list = new ArrayList<FlowFieldParticle>();
        for (int i = 0; i <= n; i++) {
            PVector initialLocation = clippingObject.computeRandomPointIn();
            PVector initialVelocity = getClosestPointInFlowField(initialLocation);
            list.add(FlowFieldParticle.initParticle(initialLocation, initialVelocity));
        }
        return list;
    }

    public List<PVector> genPathThroughField(FlowFieldParticle particle, int steps, float topSpeed) {
        var particlePath = new ArrayList<>(List.of(particle.location()));
        var j = 0;
        while (j <= steps) {
            particle = moveParticleThroughFlowField(particle, topSpeed);
            particlePath.add(particle.location());
            j += 1;
        }
        return particlePath;
    }


    public void draw(PApplet sketch) {

        if (getFlowFieldCoordinateGrid().getDimensions() != 2) {
            throw new IllegalArgumentException("Can only plot 2D FlowFields");
        }
        int vectorLength = 10;

        for (int i = 0; i < getFlowFieldVectors().size(); i++) {

            PVector v = getFlowFieldVectors().get(i);
            List<Float> coordinates = getFlowFieldCoordinateGrid().getCoordinates().get(i);
            sketch.pushMatrix();

            sketch.translate(coordinates.get(1), coordinates.get(0));
            sketch.rotate(v.heading());

            // Plot vectors
            sketch.line(0, 0, 5, 0);
            sketch.line(vectorLength, 0, vectorLength - 4, -2);
            sketch.line(vectorLength, 0, vectorLength - 4, 2);

            sketch.popMatrix();

        }
    }

}