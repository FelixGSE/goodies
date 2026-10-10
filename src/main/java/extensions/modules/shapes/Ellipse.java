package extensions.modules.shapes;

import processing.core.PApplet;
import processing.core.PVector;

public class Ellipse {

    public PVector center;
    public float height;
    public float width;
    public float rotation;

    public Ellipse(PVector center, float width, float height, float rotation) {
        this.center = center;
        this.width = width;
        this.height = height;
        this.rotation = rotation;
    }

    public Ellipse shift(PVector mover){
        PVector newCenter = center.copy().add(mover);
        return new Ellipse(newCenter,width,height,rotation);

    }

    public void scaleMe(float scalingFactor) {
        this.width = this.width * scalingFactor;
        this.height = this.height * scalingFactor;
    }

    public Ellipse getScaledEllipse(float scalingFactor) {
        return new Ellipse(center, width * scalingFactor,height * scalingFactor,rotation);
    }



    public void draw(PApplet sketch){

        sketch.ellipse(center.x,center.y,2*width,2*height);

    }

}
