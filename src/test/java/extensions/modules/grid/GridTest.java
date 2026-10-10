package extensions.modules.grid;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.google.common.collect.Table;
import com.google.common.collect.TreeBasedTable;

public class GridTest {
    @Test
    void shouldCreateA2DGrid() {
        
        // Given
        Grid grid = new Grid(0, 100, 10, 0, 100, 10, 2);

        int result = grid.getCoordinates().size();

        assertThat(result).isEqualTo(121);

    }

    @Test
    void shouldCreateA3DGrid() {
        
        // Given
        Grid grid = new Grid(0, 100, 10, 0, 100, 10,0, 100, 10, 3);

        int result = grid.getCoordinates().size();

        assertThat(result).isEqualTo(1331);

    }


    @Test
    void shouldCreateGridAsTable() {



        Table<Integer, Integer, List<Float>> grid = TreeBasedTable.create();
        var chords =  List.of(0.1f,0.2f);
        grid.put(0,0, chords);

        System.out.println(grid.get(0,0));



    }

}

