package extensions.modules.shapes;

import org.junit.jupiter.api.Test;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class QuadrilateralTest {

    @Test
    void containsShouldReturnTrueIfPointIsInsideQuadrilateral() {
        // Given
        PVector A = new PVector(-10, -10);
        PVector B = new PVector(-10, 10);
        PVector C = new PVector(10, 10);
        PVector D = new PVector(10, -10);
        Quadrilateral quadrilateral = new Quadrilateral(A, B, C, D);
        PVector testPoint = new PVector(1, 1);

        // When
        boolean result = quadrilateral.contains(testPoint);

        // Then
        assertThat(result).isEqualTo(true);

    }

    @Test
    void containsShouldReturnTrueIfPointIsOutsideQuadrilateral() {
        // Given
        PVector A = new PVector(-10, -10);
        PVector B = new PVector(-10, 10);
        PVector C = new PVector(10, 10);
        PVector D = new PVector(10, -10);
        Quadrilateral quadrilateral = new Quadrilateral(A, B, C, D);
        PVector testPoint = new PVector(99, 99);

        // When
        boolean result = quadrilateral.contains(testPoint);

        // Then
        assertThat(result).isEqualTo(false);

    }

    @Test
    void containsAllShouldReturnTrueWhenAllPointsInAListAreInsideAQuadrilateral() {
        // Given
        PVector A = new PVector(-10, 10);
        PVector B = new PVector(10, 10);
        PVector C = new PVector(10, -10);
        PVector D = new PVector(-10, -10);
        Quadrilateral quadrilateral = new Quadrilateral(A, B, C, D);
        List<PVector> testPoints = List.of(new PVector(1, 2), new PVector(-1, 3));

        // When
        boolean result = quadrilateral.containsAll(testPoints);

        // Then
        assertThat(result).isEqualTo(true);


    }

    @Test
    void containsAllShouldReturnFalseWhenAtLeastOnePointInAListAreOutsideAQuadrilateral() {
        // Given
        PVector A = new PVector(-10, 10);
        PVector B = new PVector(10, 10);
        PVector C = new PVector(10, -10);
        PVector D = new PVector(-10, -10);
        Quadrilateral quadrilateral = new Quadrilateral(A, B, C, D);
        List<PVector> testPoints = List.of(new PVector(99, 99), new PVector(-1, 3));

        // When
        boolean result = quadrilateral.containsAll(testPoints);

        // Then
        assertThat(result).isEqualTo(false);

    }

    @Test
    void containsAllShouldReturnFalseWhenAllPointInAListAreOutsideAQuadrilateral() {
        // Given
        PVector A = new PVector(-10, 10);
        PVector B = new PVector(10, 10);
        PVector C = new PVector(10, -10);
        PVector D = new PVector(-10, -10);
        Quadrilateral quadrilateral = new Quadrilateral(A, B, C, D);
        List<PVector> testPoints = List.of(new PVector(99, 99), new PVector(-99, -99));

        // When
        boolean result = quadrilateral.containsAll(testPoints);

        // Then
        assertThat(result).isEqualTo(false);

    }

    @Test
    void shouldCreateQuadrilateralFromAPAppletClassWithAppropiateSize() {
        // Given
        PApplet sketch = new PApplet();
        sketch.setSize(10, 10);

        PVector A = new PVector(0, 0);
        PVector B = new PVector(10, 0);
        PVector C = new PVector(0, 10);
        PVector D = new PVector(10, 10);
        Quadrilateral expected = new Quadrilateral(A, B, C, D);

        // When
        Quadrilateral result = Quadrilateral.fromSketch(sketch);

        // Then
        assertThat(result).isEqualTo(expected);

    }

}
