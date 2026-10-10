package extensions.modules.utils;

import java.util.List;

import extensions.modules.shapes.Line;
import extensions.modules.shapes.Quadrilateral;
import processing.core.PApplet;
import processing.core.PVector;

public class Draw {

  public static void drawHLine(PApplet sketch, float y) {
    sketch.line(0, y, sketch.width, y);
  }

  public static void drawVLine(PApplet sketch, float x) {
    sketch.line(x, 0, 0, sketch.height);
  }

  public static void drawPoint(PApplet sketch, PVector point, float size) {

    sketch.circle(point.x, point.y, size);
  }

  public static void drawRandomPointsOnBackground(PApplet sketch, int n) {
    Quadrilateral quad = Quadrilateral.fromSketch(sketch);
    for (int i = 0; i < n; i++) {
      PVector p = quad.getRandomPointIn();
      sketch.point(p.x, p.y);
    }
  }

  public static void drawOrthogonalBackgroundGrid(PApplet sketch, int rgb, int nRows, int nColumns) {

    List<PVector> rows = Line.makePositiveRealLine(sketch.height).divideLineInRandomPartsAsPointCollection(nRows);
    List<PVector> columns = Line.makePositiveRealLine(sketch.width).divideLineInRandomPartsAsPointCollection(nColumns);

    for(PVector r : rows){

        List<Line> subLine = Line.getHorizontalLine(sketch, r.x).divideLineInRandomPartsAsLineCollection(sketch.width/50);
        for(Line line : subLine){
          sketch.stroke(rgb, sketch.random(20, 50));
          sketch.strokeWeight(
              1 + PApplet.map(sketch.noise(line.start.x, line.start.y), 0, 1, -0.5f, 0.5f));
          line.draw(sketch);
        }

    }

    for(PVector c : columns){

      List<Line> subLine = Line.getverticalLine(sketch, c.x).divideLineInRandomPartsAsLineCollection(sketch.height/50);
      for(Line line : subLine){
        sketch.stroke(rgb, sketch.random(20, 50));
        sketch.strokeWeight(
                1 + PApplet.map(sketch.noise(line.start.x, line.start.y), 0, 1, -0.5f, 0.5f));
        line.draw(sketch);
      }

    }

  }

//   private static void drawBackGroundGridLines(PApplet sketch, List<Line> lines, int rgb){
//     for (Line line : lines) {
//       sketch.stroke(rgb, sketch.random(20, 50));
// //      sketch.strokeWeight(
// //              1 + PApplet.map(sketch.noise(line.start.x, line.start.y), 0, 1, -0.5f, 0.5f));
//       line.draw(sketch);
//     }

//   }

  public static void hello_world() {
    System.out.println("HELLO WORLD");

  }
}
