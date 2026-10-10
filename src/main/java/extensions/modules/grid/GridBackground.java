package extensions.modules.grid;

import processing.core.PApplet;

public class GridBackground {

  public static void draw(PApplet sketch) {
    drawGridIml(sketch, 255, 5);
  }

  public static void draw(PApplet sketch, int rgb) {
    drawGridIml(sketch, rgb, 5);
  }

  public static void draw(PApplet sketch, int rgb, int spacing) {
    drawGridIml(sketch, rgb, spacing);
  }

  private static void drawGridIml(PApplet sketch, int rgb, int spacing) {
    for (int i = -sketch.width; i < sketch.height + sketch.width; i += spacing) {
      sketch.stroke(rgb, sketch.random(20, 50));
      drawGridLine(sketch, i, 0, i + sketch.height, sketch.height);
    }
    for (int i = sketch.height + sketch.width; i >= -sketch.width; i -= spacing) {
      sketch.stroke(rgb, sketch.random(20, 50));
      drawGridLine(sketch, i, 0, i - sketch.height, sketch.height);
    }
  }
  private static void drawGridLine(PApplet sketch, float x1, float y1, float x2, float y2) {
    float tmp;
    if (x1 > x2) {
      tmp = x1;
      x1 = x2;
      x2 = tmp;
      tmp = y1;
      y1 = y2;
      y2 = tmp;
    }
    // if (y1 > y2) { tmp = y1; y1 = y2; y2 = tmp; }

    float dx = x2 - x1;
    float dy = y2 - y1;
    float step = 1;

    if (x2 < x1) step = -step;

    float sx = x1;
    float sy = y1;
    for (float x = x1 + step; x <= x2; x += step) {
      float y = y1 + step * dy * (x - x1) / dx;
      sketch.strokeWeight(1 + PApplet.map(sketch.noise(sx, sy), 0, 1, -0.5f, 0.5f));
      sketch.line(
              sx,
              sy,
              x + PApplet.map(sketch.noise(x, y), 0, 1, -1, 1),
              y + PApplet.map(sketch.noise(x, y), 0, 1, -1, 1));
      sx = x;
      sy = y;
    }
  }

}
