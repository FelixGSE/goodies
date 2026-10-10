package extensions.modules.shapes;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

import goodies.utils.Conversions;
import processing.core.PVector;
class SquareTest {

    @Test
    void getCenter() {
        var square = new Square();
        assertThat(square.getCenter()).isEqualTo(new PVector(0,0));
    }

    @Test
    void equalGridPartition() {
        var square = new Square();
        assertThat(square.getCenter()).isEqualTo(new PVector(0,0));
    }
    @Test
    public void shouldCreateGridPartition() {
        // given
        var square = new Square();
        List<Float> result = square.getEqualGridPartition(10).computeCentroid();

        assertThat(square.getCenter()).isEqualTo(Conversions.listOfFloatsToPVector(result));


    }
}
