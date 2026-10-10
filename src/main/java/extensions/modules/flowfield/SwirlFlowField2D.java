package extensions.modules.flowfield;

import java.util.ArrayList;
import java.util.List;

import extensions.modules.shapes.ClipObject;
import processing.core.PVector;

public class SwirlFlowField2D extends FlowField {

    public SwirlFlowField2D(ClipObject clippingObject, int parts) {
        super(clippingObject, parts);

    }


    @Override
    public void initOrUpdateFlowFieldVectors() {
        List<PVector> vectors = new ArrayList<>();
        for( List<Float> coordinates : getFlowFieldCoordinateGrid().getCoordinates() ){

            float xVector = ( coordinates.get(1) - coordinates.get(0) );
            float yVector = ( - coordinates.get(0) - coordinates.get(1) );
            vectors.add(new PVector(xVector, yVector));

        }

        setFlowFieldVectors(vectors);
    }
}
