package extensions.modules.flowfield;

import extensions.modules.shapes.ClipObject;
import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;

public class PolyFlowField2D extends FlowField{


    public static void foo() {
        var a = new ArrayList<>();
        for(double i=0.0; i<=10.0;i=i+2.0){
            a.add(i);

        }

    }


        public PolyFlowField2D(ClipObject clippingObject, int parts) {
            super(clippingObject, parts);

        }


        @Override
        public void initOrUpdateFlowFieldVectors() {
            List<PVector> vectors = new ArrayList<>();
            for( List<Float> coordinates : getFlowFieldCoordinateGrid().getCoordinates() ){

                float xVector = (PApplet.pow(coordinates.get(1),3) - 9 * coordinates.get(1) );
                float yVector = ( PApplet.pow(coordinates.get(0),3) - 9 *coordinates.get(0) );
//                float xVector = (PApplet.cos(coordinates.get(1) + coordinates.get(0)) );
//                float yVector = ( PApplet.sin(coordinates.get(0) * coordinates.get(1) ));
                PVector v = new PVector(xVector, yVector);
                v.setMag(1);
                vectors.add(v);

            }

            setFlowFieldVectors(vectors);
        }
    }



