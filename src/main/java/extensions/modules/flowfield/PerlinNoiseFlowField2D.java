package extensions.modules.flowfield;

import java.util.ArrayList;
import java.util.List;

import extensions.modules.shapes.ClipObject;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import processing.core.PApplet;
import processing.core.PVector;

@ToString
@Getter
@Setter
public class PerlinNoiseFlowField2D extends FlowField {

    private float xScale;
    private float yScale;
    private float zScale;
    private float zIncrement;
    private float zOffset;
    private long seed;

//    public PerlinNoiseFlowField2D(ClipObject clippingObject, int parts) {
//        super(clippingObject, parts);
//        setSeed(System.currentTimeMillis());
//    }

    public PerlinNoiseFlowField2D(ClipObject clippingObject, int parts, float xScale, float yScale, float zScale) {

        super(clippingObject, parts);
        setXScale(xScale);
        setYScale(yScale);
        setZScale(zScale);
        setSeed(System.currentTimeMillis());

        setZOffset(0);
        initOrUpdateFlowFieldVectors();

    }

    public PerlinNoiseFlowField2D(ClipObject clippingObject, int parts) {

        super(clippingObject, parts);
        setSeed(System.currentTimeMillis());
        setXScale(0.005F);
        setYScale(0.005F);
        setZScale(0.005F);
        initOrUpdateFlowFieldVectors();

    }


    @Override
    public void initOrUpdateFlowFieldVectors(){
        var p = new PApplet();
        var indices = this.getFlowFieldCoordinateGrid().getCoordinateIndices();
        List<PVector> vectors = new ArrayList<PVector>();
        for(List<Integer> index : indices){
            var scaled_x = index.get(0) * xScale;
            var scaled_y = index.get(1) * yScale;
            var noise_val = p.noise(scaled_x, scaled_y, zScale);
            var angle = PApplet.map(noise_val, 0.0F, 1.0F, 0.0F, PApplet.PI * 2.0F);
            vectors.add(PVector.fromAngle(angle));
        }
        setFlowFieldVectors(vectors);
        }


//    @Override
//    public void initOrUpdateFlowFieldVectors() {
//        var p = new PApplet();
//        p.noiseSeed(getSeed());
//
//        List<Integer> xIndices = this.getFlowFieldCoordinateGrid().getCoordinateIndices().stream().map(l -> l.get(0)).collect(Collectors.toSet()).stream().sorted().toList();
//        List<Integer> yIndices = this.getFlowFieldCoordinateGrid().getCoordinateIndices().stream().map(l -> l.get(1)).collect(Collectors.toSet()).stream().sorted().toList();
//        PVector[] vectorArray = new PVector[getFlowFieldCoordinateGrid().getCoordinateIndices().size()];
//        float yOffset = 0;
//        for (int y : yIndices) {
//
//            float xOffset = 0;
//            for (int x : xIndices) {
//
//                float angle = p.noise(xOffset, yOffset, getZOffset()) * PApplet.TWO_PI;
//                PVector v = PVector.fromAngle(angle);
//                v.setMag(1);
//                xOffset += getXIncrement();
//
//                int index = this.getFlowFieldCoordinateGrid().getCoordinateIndices().indexOf(List.of(x, y));
//                if (index >= 0) {
//                    vectorArray[index] = v;
//                }
//
//            }
//            yOffset += getYIncrement();
//
//        }
//
//        setZOffset(getZOffset() + getZIncrement());
//
//
//        setFlowFieldVectors(Arrays.asList(vectorArray));
//    }
//

//}


// LOKKS GOOD with PerlinNoiseFlowField2D(circle, 30,20,201,21);
//    @Override
//    public void initOrUpdateFlowFieldVectors() {
//        var p = new PApplet();
//        p.noiseSeed(getSeed());
//
//        List<Integer> xIndices = this.getFlowFieldCoordinateGrid().getCoordinateIndices().stream().map(l -> l.get(0)).collect(Collectors.toSet()).stream().sorted().toList();
//        List<Integer> yIndices = this.getFlowFieldCoordinateGrid().getCoordinateIndices().stream().map(l -> l.get(0)).collect(Collectors.toSet()).stream().sorted().toList();
//        PVector[] vectorArray = new PVector[getFlowFieldCoordinateGrid().getCoordinateIndices().size()];
//        float yOffset = 0;
//        for (int y : yIndices) {
//
//            float xOffset = 0;
//            for (int x : xIndices) {
//
//                float angle = p.noise(xOffset, yOffset, getZOffset()) * PApplet.TWO_PI;
//                PVector v = PVector.fromAngle(angle);
//                v.setMag(1);
//                xOffset += getXIncrement();
//
//
//
//                int index = this.getFlowFieldCoordinateGrid().getCoordinateIndices().indexOf(List.of(x, y));
//                if(index >= 0) {
//                    vectorArray[index] = v;
//                }
//
//            }
//            yOffset += getYIncrement();
//
//        }
//
//        setZOffset(getZOffset() + getZIncrement());
//
//
//        setFlowFieldVectors(Arrays.asList(vectorArray));
//    }


}


//    float yoff = 0;
//    for (int y = 0; y < rows; y++) {
//        float xoff = 0;
//        for (int x = 0; x < cols; x++) {
//            float angle = noise(xoff, yoff, zoff) * TWO_PI;
//
//            PVector v = PVector.fromAngle(angle);
//            v.setMag(1);
//            int index = x + y * cols;
//            vectors[index] = v;
//
//            xoff += inc;
//        }
//        yoff += inc;
//    }
//    zoff += 0.004;


//    @Override
//    public void initOrUpdateFlowFieldVectors() {
//        var p = new PApplet();
//        p.noiseSeed(getSeed());
//        List<PVector> vectors = new ArrayList<>();
//        for (List<Integer> cords : this.getFlowFieldCoordinateGrid().getCoordinateIndices()) {
//            float yOffset = getYIncrement() * cords.get(1);
//            float xOffset = getXIncrement() * cords.get(0);
//            float angle = p.noise(xOffset, yOffset, getZOffset()) * PApplet.TWO_PI;
//            PVector v = PVector.fromAngle(angle);
//            v.setMag(1);
//            vectors.add(v);
//            setZOffset(getZOffset() + getZIncrement());
//        }
//        setFlowFieldVectors(vectors);
//    }


//    @Override
//    public void initOrUpdateFlowFieldVectors( ) {
//
//        float yOffset = 0;
//        float xOffset = 0;
//        List<PVector> vectors = new ArrayList<>();
//        for(int i = 0; i < this.getFlowFieldCoordinateGrid().getCoordinateIndices().size() - 1; i++){
//            List<Integer> thisIndex = this.getFlowFieldCoordinateGrid().getCoordinateIndices().get(i);
//            List<Integer> nextIndex = this.getFlowFieldCoordinateGrid().getCoordinateIndices().get(i+1);
//            if(nextIndex.get(0) - thisIndex.get(0) != 0 ){
//                xOffset += getXIncrement();
//            }
//            if (nextIndex.get(1) - thisIndex.get(1) != 0 ){
//                yOffset += getYIncrement();
//            }
//            float angle = new PApplet().noise(xOffset, yOffset, getZOffset()) * PApplet.TWO_PI;
//
//            PVector v = PVector.fromAngle(angle);
//            v.setMag(1);
//
//            vectors.add(v);
//        }
//        setZOffset(getZOffset() + getZIncrement());
//        setFlowFieldVectors(vectors);
//    }
//
//    @Override
//    public void initOrUpdateFlowFieldVectors( ) {
//
//        float yOffset = 0;
//        float xOffset = 0;
//        List<Integer> xIndices = this.getFlowFieldCoordinateGrid().getCoordinateIndices().stream().map(l -> l.get(0)).collect(Collectors.toSet()).stream().sorted().toList();
//        List<Integer> yIndices = this.getFlowFieldCoordinateGrid().getCoordinateIndices().stream().map(l -> l.get(0)).collect(Collectors.toSet()).stream().sorted().toList();
//        List<Integer> indexList = new ArrayList<>();
//        List<PVector> listOfVectors = new ArrayList<>();
//        for (int y : yIndices) {
//
//            for (int x : xIndices) {
//                var l2 = getFlowFieldCoordinateGrid().getCoordinateIndices().stream().toList();
//                int index = this.getFlowFieldCoordinateGrid().getCoordinateIndices().indexOf(List.of(x, y));
//                indexList.add(index);
//
//                float angle = new PApplet().noise(xOffset, yOffset, getZOffset()) * PApplet.TWO_PI;
//                PVector v = PVector.fromAngle(angle);
//                v.setMag(1);
//                xOffset += getXIncrement();
//                listOfVectors.add(v);
//            }
//            yOffset += getYIncrement();
//
//        }
//
//        setZOffset(getZOffset() + getZIncrement());
//
//
//        setFlowFieldVectors(indexList.stream().map(listOfVectors::get).collect(Collectors.toList()));
//    }


