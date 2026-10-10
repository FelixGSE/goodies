package extensions.modules.shapes;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

import extensions.modules.grid.Grid;
import processing.core.PVector;

class CubeTest {

    @Test
    public void shouldScaleCube(){

        Cube cube = new Cube();

        cube.scale(10);
        cube.translate(new PVector(10,10,10));


        System.out.println(cube.getCenter());
        // assertThat(result).isEqualTo(-10f);


    }

    @Test
    public void shouldReturnScaledAndTranslatedCube(){

        Cube cube = Cube.getCube(new PVector(300,300),100);


        System.out.println(cube);
        // assertThat(result).isEqualTo(-10f);


    }

    @Test
    public void shouldTranslateAndScaleFaceCenters(){

        // Given
        float scale = 100;
        PVector translation = new PVector(300,300, 300);

        PVector bottom = new PVector(0, -1, 0).mult(scale).add(translation);
        PVector top = new PVector(0, 1, 0).mult(scale).add(translation);
        PVector near = new PVector(0, 0, 1).mult(scale).add(translation);
        PVector far = new PVector(0, 0, -1).mult(scale).add(translation);
        PVector left = new PVector(-1, 0, 0).mult(scale).add(translation);
        PVector right = new PVector(1, 0, 0).mult(scale).add(translation);

        List<PVector> expectedCenterOfFaces = List.of(bottom, top, near,far,left, right);

        // When
        Cube cube = Cube.getCube(translation,scale);

        // Then
        assertThat(expectedCenterOfFaces.size()).isEqualTo(cube.getFaceCenter().size());

        for(int i = 0; i <= expectedCenterOfFaces.size() - 1; i++ ){
            assertThat(expectedCenterOfFaces.get(i)).isEqualTo(cube.getFaceCenter().get(i));
        }

    }

    @Test
    public void shouldGridPartition(){

        // Given
        float scale = 10;
        PVector translation = new PVector(100,100, 100);

        // When
        Cube cube = Cube.getCube(translation,scale);
        Grid grid = cube.getEqualGridPartition(10);

        // Then
        System.out.println(grid);


    }

}
