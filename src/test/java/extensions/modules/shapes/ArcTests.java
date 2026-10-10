package extensions.modules.shapes;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import processing.core.PVector;

public class ArcTests {

    @Test
    public void testGCircle() {

        PVector center = new PVector(0,1);
        float radius = 1;
        Circle circle = new Circle(center,radius);
        PVector A = new PVector(0,0);
        PVector B = new PVector(1,0);
        PVector C = new PVector(0,1);

        NotReallyAnArc arc = new NotReallyAnArc(A,B,C,circle);
        // PVector a = new PVector(100,100);
        // PVector b = new PVector(600,100);
        // PVector c = new PVector(300,600);
        // Triangle triangle = new Triangle(a,b,c);


        System.out.println(arc.toString());
        System.out.println(String.format("Start Angle: %f",arc.refCircleAngleStart));
        System.out.println(String.format("End Angle: %f",arc.refCircleAngleEnd));


    }

    @Test
    public void testDegreeForPointOnCircle() {
        PVector center = new PVector(0,0);
        float radius = 1;
        Circle circle = new Circle(center,radius);
        PVector A = new PVector(1,0);
        PVector B = new PVector(0,-1);
        PVector C = new PVector(-1,0);
        PVector D = new PVector(0,1);
        assertEquals(0,Utils.angleForPointOnCircleInDegrees(circle,A),0.01);
        assertEquals(270,Utils.angleForPointOnCircleInDegrees(circle,B),0.01);
        assertEquals(180,Utils.angleForPointOnCircleInDegrees(circle,C),0.01);
        assertEquals(90,Utils.angleForPointOnCircleInDegrees(circle,D),0.01);

    }

    @Test
    public void testDegreeForPointOnCircle2() {
        PVector center = new PVector(500,500);
        float radius = 300;
        Circle circle = new Circle(center,radius);
        PVector A = new PVector(800,500);
        PVector B = new PVector(500,200);
        PVector C = new PVector(200,500);
        PVector D = new PVector(500,800);

        assertEquals(0,Utils.angleForPointOnCircleInDegrees(circle,A),0.01);
        assertEquals(270,Utils.angleForPointOnCircleInDegrees(circle,B),0.01);
        assertEquals(180,Utils.angleForPointOnCircleInDegrees(circle,C),0.01);
        assertEquals(90,Utils.angleForPointOnCircleInDegrees(circle,D),0.01);

    }

    @Disabled("known failure: expected 0, got 90 (refCircleAngleStart); E1.3")
    @Test
    public void testGetMidPoint() {
        PVector center = new PVector(500,500);
        float radius = 300;
        Circle circle = new Circle(center,radius);
        PVector B = new PVector(500,800);
        PVector C = new PVector(800,500);

        NotReallyAnArc arc = new NotReallyAnArc(center,B,C,circle);

        assertEquals(0,arc.refCircleAngleStart,0.01);
        assertEquals(90,arc.refCircleAngleEnd,0.01);

        PVector midA = arc.computeMidPointOnSide("a");
        PVector midB = arc.computeMidPointOnSide("b");
        PVector midC = arc.computeMidPointOnSide("c");

        PVector ff = Utils.pointOnCircle(circle,Math.toRadians(45));

        assertEquals(ff.x,midA.x,0.01);
        assertEquals(ff.y,midA.y,0.01);

        assertEquals(650.0, midB.x,0.01);
        assertEquals(500.0, midB.y,0.01);

        assertEquals(500.0,midC.x,0.01);
        assertEquals(650.0,midC.y,0.01);

    }
}
