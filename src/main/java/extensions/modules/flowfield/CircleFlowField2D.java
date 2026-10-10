package extensions.modules.flowfield;

import extensions.modules.shapes.ClipObject;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;

public class CircleFlowField2D extends FlowField {

    public CircleFlowField2D(ClipObject clippingObject, int parts) {
        super(clippingObject, parts);

    }


    @Override
    public void initOrUpdateFlowFieldVectors() {
        List<PVector> vectors = new ArrayList<>();
        for( List<Float> coordinates : getFlowFieldCoordinateGrid().getCoordinates() ){

            float xVector = ( coordinates.get(1) );
            float yVector = ( - coordinates.get(0) );
            vectors.add(new PVector(xVector, yVector));

        }

        setFlowFieldVectors(vectors);
    }
}
