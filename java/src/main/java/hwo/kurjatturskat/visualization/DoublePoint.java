package hwo.kurjatturskat.visualization;

public class DoublePoint {

    double x;
    double y;

    public DoublePoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return String.format("x: %f,  y: %f", x, y);
    }
}
