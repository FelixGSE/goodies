package extensions.modules.shapes;

import java.util.ArrayList;
import java.util.List;

import processing.core.PVector;



public class Band {
    public List<PVector> vertexList;
    public List<PVector> shiftedVertexList;
    public Band(ArrayList<PVector> vertexList, PVector shift) {
        this.vertexList = vertexList;

        this.shiftedVertexList = Utils.shiftVertexList(vertexList,shift);
    }

//    public void drawInLines(PApplet sktech){
//
//        for(i=0;)
//
//
//
//    }
//


}
