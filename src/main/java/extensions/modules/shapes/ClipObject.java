package extensions.modules.shapes;

import extensions.modules.grid.Grid;
import processing.core.PVector;

public interface ClipObject {

    boolean pointIsInside(PVector point);
    Grid getEqualGridPartition(int parts);
    PVector computeRandomPointIn();

}
