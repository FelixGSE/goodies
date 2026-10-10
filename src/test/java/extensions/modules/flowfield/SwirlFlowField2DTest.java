package extensions.modules.flowfield;

import org.junit.jupiter.api.Test;

import extensions.modules.shapes.Circle;
import processing.core.PApplet;
import processing.core.PVector;

class SwirlFlowField2DTest {

    @Test
    void shouldCreateASwirlFlowField(){

        PVector center = new PVector(0,0);
        float radius = 100;
        Circle circle = new Circle(center, radius);
        PVector initialLocation = circle.computeRandomPointIn();
        PVector initialVelocity = new Circle(center, radius * new PApplet().random(0,0.1f)).computeRandomPointOn();
        var particle = FlowFieldParticle.initParticle(initialLocation,initialVelocity);
        FlowField flowField = new SwirlFlowField2D(circle, 10);
        flowField.initOrUpdateFlowFieldVectors();
        var newParticle = flowField.moveParticleThroughFlowField(particle,1);
        System.out.println(particle);
        System.out.println(newParticle);
    }


}
