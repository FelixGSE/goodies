package extensions.modules.shapes;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.Test;

import extensions.modules.grid.Grid;
import processing.core.PVector;


public class CircleFlowFieldInABoxSphereTest {

    @Test
    public void shouldClipSphere(){

        Sphere sphere = new Sphere(new PVector(100,100,100), 10);
        System.out.println(sphere);
        System.out.println(sphere.getBoundingBox());
        System.out.println(sphere.getBoundingBox().getEqualGridPartition(10));
        Grid grid = sphere.getEqualGridPartition(10);

        int result = grid.getCoordinates().size();
        //
        assertThat(result).isBetween(1129,1331);

    }

    @Test
    public void shouldContainMostOfClippedVectors(){

        Sphere sphere = new Sphere(new PVector(0,0,0), 1);
        float sphereVolume = sphere.getVolume();

        Cube boundingBox = sphere.getBoundingBox();
        float boundingBoxVolume = boundingBox.getVolume();

        float volumeRatio = sphereVolume / boundingBoxVolume;

        System.out.println(volumeRatio);
        System.out.println(sphereVolume);
        System.out.println(boundingBox.getLength());
        System.out.println(boundingBox.getWidth());
        System.out.println(boundingBox.getHeight());

//        boundingBoxVolume

    }
    
}
