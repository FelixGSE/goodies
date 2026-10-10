package extensions.modules.sketches;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.junit.jupiter.api.Test;

import goodies.projections.StereographicProjection;
import goodies.algebra.RotationMatrixUtils;
import goodies.utils.Conversions;
import processing.core.PVector;

class SketchTest {

    @Test
    public void shouldRotateFiveDimensionalPoint() {
        double[] xArray = {0.0d, 0.0d, 0.0d, 0.0d, 0.0d};
        RealVector x = MatrixUtils.createRealVector(xArray);
        float angle = 0.01f;

        RealVector rotated = null;
        for (int i = 0; i <= 4; i++) {

            RealMatrix rotation = RotationMatrixUtils.getRotationMatrixForAxis(0, 2, 5, angle);
            rotated = rotation.preMultiply(x);
        }

        x = rotated;

        RealVector rotatedDown = StereographicProjection.projectDownOneDimension(2, rotated);
        rotatedDown= StereographicProjection.projectDownOneDimension(2, rotatedDown);
        System.out.println(rotatedDown);
        PVector pVector = Conversions.realVectorToPVector(rotatedDown);
        System.out.println(pVector);

    }

}
