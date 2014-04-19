package hwo.kurjatturskat.visualization;

import org.la4j.matrix.Matrix;
import org.la4j.matrix.dense.Basic2DMatrix;
import org.la4j.vector.Vector;
import org.la4j.vector.dense.BasicVector;

public class VectorMath {

    /**
     * Rotates counter clockwise
     * 
     * @param vector
     * @param degrees
     * @return
     */
    public static Vector rotate(Vector vector, double degrees) {
        double radians = Math.toRadians(degrees);
        Matrix b = new Basic2DMatrix(new double[][] {
                { Math.cos(radians), Math.sin(radians) },
                { Math.sin(radians) * -1.0d, Math.cos(radians) } });

        return vector.multiply(b);

    }

    public static Vector zeroVector() {
        return new BasicVector(new double[] { 0, 0 });
    }

    public static Vector normalizedZeroDegrees() {
        return new BasicVector(new double[] { 1, 0 });
    }

    public static Vector zeroDegrees(double length) {
        return normalizedZeroDegrees().multiply(length);
    }

    public static double length(Vector first, Vector second) {
        return Math.sqrt((second.get(0) - first.get(0))
                * (second.get(0) - first.get(0))
                + (second.get(1) - first.get(1))
                * (second.get(1) - first.get(1)));
    }
}
