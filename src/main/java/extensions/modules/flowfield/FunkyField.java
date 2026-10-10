package extensions.modules.flowfield;

import extensions.modules.shapes.ClipObject;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;

public class FunkyField extends FlowField {

    public FunkyField(ClipObject clippingObject, int parts) {
        super(clippingObject, parts);

    }


    @Override
    public void initOrUpdateFlowFieldVectors() {
        List<PVector> vectors = new ArrayList<>();
        for( List<Float> coordinates : getFlowFieldCoordinateGrid().getCoordinates() ){
            float x = coordinates.get(0);
            float y = coordinates.get(1);


            float xVector = x - y - x * (x*x + y*y);
            float yVector = x + y - y * (x*x + y*y);
            vectors.add(new PVector(xVector, yVector));

        }

        setFlowFieldVectors(vectors);
    }
}
