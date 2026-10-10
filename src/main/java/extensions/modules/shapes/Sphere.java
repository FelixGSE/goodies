package extensions.modules.shapes;

import extensions.modules.grid.Grid;
import goodies.utils.Conversions;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.List;

import static java.util.stream.Collectors.toList;

@EqualsAndHashCode
@ToString
public class Sphere implements ClipObject {

    @Getter
    private final PVector center;
    @Getter
    private final float radius;

    public Sphere(PVector center, float radius){

        this.center = center;
        this.radius = radius;

    }


    /**
     * Returns a point on a Sphere for given angles theta and phi.
     *
     * @param theta Runs from 0 to PI
     * @param phi   Runs from 0 to 2 * PI
     * @return      PVector with the point on the Sphere
     */
    public PVector getPointOnSphere(float theta, float phi){

        float x = getCenter().x + getRadius() * PApplet.sin(theta) * PApplet.cos(phi);
        float y = getCenter().y + getRadius() * PApplet.sin(theta) * PApplet.sin(phi);
        float z = getCenter().z + getRadius() * PApplet.cos(theta);

        return new PVector(x,y,z);

    }
    public float getVolume(){
        return 4.0f/3.0f * PApplet.PI * PApplet.pow(getRadius(),3);
    }

    public PVector computeRandomPointIn(){
        return new PVector(0,0,0);
    }


    public PVector cleliaPointOnSphere(float theta, float c){

        float x = getCenter().x + getRadius() * PApplet.sin(theta) * PApplet.cos(c * theta);
        float y = getCenter().y + getRadius() * PApplet.sin(theta) * PApplet.sin(c * theta);
        float z = getCenter().z + getRadius() * PApplet.cos(theta);

        return new PVector(x,y,z);

    }

    public boolean pointIsInside(PVector point){

        return getCenter().dist(point) < getRadius();

    }

    public Cube getBoundingBox(){
        return Cube.getCube(getCenter().copy(), radius);
    }


    public Grid getEqualGridPartition(int parts) {

        // FIXME: This should probably part of the Grid class - Implement clipping for different shapes
        List<List<Float>> gridCoordinates = this.getBoundingBox().getEqualGridPartition(parts).getCoordinates();

        List<List<Float>> filteredCoordinates = gridCoordinates.stream().filter(x -> !this.pointIsInside(Conversions.listOfFloatsToPVector(x))).collect(toList());
//        for (Iterator<List<Float>> iter = gridCoordinates.iterator(); iter.hasNext(); ) {
//          List<Float> coordinates = iter.next();
//          ;
//
//          if (! this.pointIsInside(Conversions.listOfFLoatsToPVector(coordinates))) {
//            iter.remove();
//          }


        return new Grid(filteredCoordinates);
    }

    }



