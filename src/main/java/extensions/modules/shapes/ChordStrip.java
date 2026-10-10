package extensions.modules.shapes;

import goodies.utils.Conversions;
import processing.core.PApplet;
import processing.core.PVector;

public class ChordStrip {

    public PVector A;
    public PVector B;
    public PVector C;
    public PVector D;
    public Circle refCircle;
    public float refAngle;
    public float chordAngleClose;
    public float chordAngleFar;

    public ChordStrip(final Circle refCircle, float refAngle, float chordAngleClose, float chordAngleFar) {
        this.refCircle = refCircle;
        this.refAngle = refAngle;
        this.chordAngleClose = chordAngleClose;
        this.chordAngleFar = chordAngleFar;

        PVector refPoint = refCircle.getPointOnCircleForAngle(refAngle);
        this.A = Utils.rotatePointClockWiseAroundAReferencePoint(refCircle.center, refPoint, chordAngleClose);
        this.B = Utils.rotatePointClockWiseAroundAReferencePoint(refCircle.center, refPoint, chordAngleFar);
        this.C = Utils.rotatePointCounterClockWiseAroundAReferencePoint(refCircle.center, refPoint, chordAngleClose);
        this.D = Utils.rotatePointCounterClockWiseAroundAReferencePoint(refCircle.center, refPoint, chordAngleFar);
    }

    public ChordStrip(Circle refCircle, float refAngle, float chordAngleClose) {
        this.refCircle = refCircle;
        this.refAngle = refAngle;
        this.chordAngleClose = chordAngleClose;
        this.chordAngleFar = 0;

        PVector refPoint = refCircle.getPointOnCircleForAngle(refAngle);
        this.A = Utils.rotatePointClockWiseAroundAReferencePoint(refCircle.center, refPoint, chordAngleClose);
        this.C = Utils.rotatePointCounterClockWiseAroundAReferencePoint(refCircle.center, refPoint, chordAngleClose);
    }


    public void draw(PApplet sketch) {
        if(chordAngleFar != 0){
            sketch.line(A.x, A.y, C.x, C.y);
            sketch.line(B.x, B.y, D.x, D.y);
            float lower =  Conversions.doubleToFloat(Math.toRadians(refAngle + chordAngleClose));
            float upper =  Conversions.doubleToFloat(Math.toRadians(refAngle + chordAngleFar));

            sketch.arc(refCircle.center.x, refCircle.center.y, refCircle.radius * 2, refCircle.radius * 2,
                    lower, upper);

            float lower2 =  Conversions.doubleToFloat(Math.toRadians(refAngle - chordAngleClose));
            float upper2 =  Conversions.doubleToFloat(Math.toRadians(refAngle - chordAngleFar));

            sketch.arc(refCircle.center.x, refCircle.center.y, refCircle.radius * 2, refCircle.radius * 2,
                    upper2, lower2 );


        } else {

            sketch.line(A.x, A.y, C.x, C.y);

            float lower =  Conversions.doubleToFloat(Math.toRadians(refAngle - chordAngleClose));
            float upper =  Conversions.doubleToFloat(Math.toRadians(refAngle + chordAngleClose));

            sketch.arc(refCircle.center.x, refCircle.center.y, refCircle.radius * 2, refCircle.radius * 2,
                    lower, upper );


        }


    }
}
