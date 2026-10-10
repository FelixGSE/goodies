package extensions.modules.grid;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import extensions.modules.shapes.Quadrilateral;
import lombok.Getter;
import lombok.ToString;
import processing.core.PApplet;

@Getter
@ToString
public class Grid implements Iterable<List<Float>> {

    private List<List<Integer>> coordinateIndices;
    private final List<List<Float>> coordinates;

    private final List<Quadrilateral> cells = null;

    private List<Integer> size;
    private int dimensions;


    public Grid(float xStart, float xEnd, int xParts, float yStart, float yEnd, int yParts, int dimensions) {

        this.dimensions = dimensions;
        this.coordinates = new ArrayList<>();
        this.coordinateIndices = new ArrayList<>();

        float xStepSize = (xEnd - xStart) / xParts;
        float yStepSize = (yEnd - yStart) / yParts;

        int xIndex = 0;
        for (float x = xStart; x <= xEnd; x += xStepSize) {
            xIndex += 1;
            int yIndex = 0;
            for (float y = yStart; y <= yEnd; y += yStepSize) {
                yIndex += 1;
                float xVal = PApplet.ceil(x);
                float yVal = PApplet.ceil(y);
                coordinates.add(List.of(xVal, yVal));
                coordinateIndices.add(List.of(xIndex,yIndex));

            }
            this.size = List.of(xIndex,yIndex);

        }



    }




    public List<Float> computeCentroid(){
        float centroidX = 0;
        float centroidY = 0;
        for(List<Float> coords : getCoordinates()){
            centroidX += coords.get(0);
            centroidY += coords.get(1);
        }
        return List.of(centroidX/getCoordinates().size(),centroidY/getCoordinates().size());


    }

    public Grid(float xStart, float xEnd, int xParts, float yStart, float yEnd, int yParts,
            float zStart, float zEnd, int zParts, int dimensions) {
        this.dimensions = dimensions;
        this.coordinates = new ArrayList<>();
        float xStepSize = (xEnd - xStart) / xParts;
        float yStepSize = (yEnd - yStart) / yParts;
        float zStepSize = (zEnd - zStart) / zParts;


        for (float x = xStart; x <= xEnd; x += xStepSize) {

            for (float y = yStart; y <= yEnd; y += yStepSize) {

                for (float z = zStart; z <= zEnd; z += zStepSize) {
                    float xVal = PApplet.ceil(x);
                    float yVal = PApplet.ceil(y);
                    float zVal = PApplet.ceil(z);
                    coordinates.add(List.of(xVal, yVal, zVal));
                }


            }

        }
    }





//        float xValue;
//        float yValue;
//        float zValue;
//
//        for (int x = 0; x <= xParts; x++) {
//
//            if(x == xParts){
//                xValue = PApplet.ceil(x * xStepSize);
//            } else {
//                xValue = x * xStepSize;
//            }
//
//            for (int y = 0; y <= yParts; y++) {
//
//                if(y == yParts){
//                    yValue = PApplet.ceil(y * yStepSize);
//                } else {
//                    yValue = y * yStepSize;
//                }
//
//                for (int z = 0; z <= xParts; z++) {
//
//                    if(z == zParts){
//                        zValue = PApplet.ceil(z * zStepSize);
//                    } else {
//                        zValue = z * zStepSize;
//                    }
//
//
//                    coordinates.add(List.of(xValue, yValue, zValue));
//
//                }
//
//            }
//
//        }


    public Grid(List<List<Float>> coordinates){
        this.coordinates = coordinates;
    }

    public Grid(List<List<Integer>> coordinateIndices, List<List<Float>> coordinates){
        this.dimensions = coordinateIndices.get(0).size();
        this.coordinateIndices = coordinateIndices;
        this.coordinates = coordinates;
    }

    @Override
    public Iterator<List<Float>> iterator() {
        return coordinates.iterator();
    }

    public void draw(PApplet sketch){

        for(List<Float> gridPoint : this){
            if(gridPoint.size() == 2){
                sketch.point(gridPoint.get(0), gridPoint.get(1));
            } else if (gridPoint.size() == 3){
                sketch.point(gridPoint.get(0), gridPoint.get(1), gridPoint.get(2));
            }

        }


    }


}
