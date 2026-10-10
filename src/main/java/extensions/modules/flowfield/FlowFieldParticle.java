package extensions.modules.flowfield;

import processing.core.PApplet;
import processing.core.PVector;

public record FlowFieldParticle(PVector location, PVector velocity, PVector acceleration, boolean hitEdge) {

    public static FlowFieldParticle initParticle(PVector initialLocation, PVector initialVelocity) {
        return new FlowFieldParticle(initialLocation, initialVelocity, new PVector(0, 0), false);
    }

    public FlowFieldParticle updateLocation(PVector force, float topSpeed) {

        PVector newAcceleration = acceleration().copy().add(force);

        PVector newVelocity = velocity().copy().add(newAcceleration).limit(topSpeed);

        PVector newLocation = location().copy().add(newVelocity);

        acceleration.mult(0);

        return new FlowFieldParticle(newLocation, newVelocity, new PVector(0, 0), false);

    }

    public void draw(PApplet sketch) {

        sketch.point(location().x, location().y, location().z);

    }

}