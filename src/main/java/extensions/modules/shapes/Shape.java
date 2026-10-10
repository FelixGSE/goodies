package extensions.modules.shapes;

import processing.core.PVector;
import processing.core.PApplet;

public interface Shape {

      public void draw(PApplet sketch);
      public double a();
      public double b();
      public double c();
      public PVector getA();
      public PVector getB();
      public PVector getC();
      public PVector computeRandomPointOnSide(String side);
      public PVector computeRandomGaussianPointOnSide(String side,float sigma);
      public PVector computeMidPointOnSide(String side);
      public String getShortestSide();
      public String getLongestSide();

//
//
//    public ArrayList<PVector> vertexList;
    // public ArrayList<PVector> vertexList;
//    public Object getShifted(PVector mover);
//    public void shiftMe(gPVector mover);
//    public Object getScaled(PVector mover);
//    public void scaleMe(PVector mover);

//    public void drawRandomPointInside(PApplet myParent);
//    public void drawRandomPointInside(PApplet myParent, float offSet);
//    public void rotate(PVector mover);
//    public void rotateMe(PVector mover);
//    public void rotateObjectOnCircle(PVector mover);
//    public void rotateMeOnCircle(PVector mover);
//    public void rotateOnEllipse(PVector mover);
//    public void rotateMeOnEllipse(PVector mover);
//    public void randomFill(PVector mover);
//    public void drawRandomFille(PVector mover);
//    private void resetPointList() {
//        ArrayList<PVector> vertexList = new ArrayList<>();
//    }
}
