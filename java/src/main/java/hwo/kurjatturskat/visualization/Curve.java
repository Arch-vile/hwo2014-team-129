package hwo.kurjatturskat.visualization;

import org.la4j.vector.Vector;

public class Curve extends TrackElement {

    private double angle;
    private double radius;

    /**
     * Track start pos relative to circle center
     */
    private Vector relativeStartPos;

    /**
     * 
     * @param angle
     *            in degrees
     * @param radius
     */
    public Curve(double angle, double radius) {
        super(TrackElement.TYPE_CURVE);
        this.angle = angle;
        this.radius = radius;
    }

    public double getAngle() {
        return this.angle;
    }

    public double getRadius() {
        return this.radius;
    }

    public Vector getRelativeStartPoint() {
        return relativeStartPos;
    }

    public void setRelativeStartPos(Vector startPos) {
        this.relativeStartPos = startPos;

    }

    public Vector calculateRelativeEndPoint() {
        return VectorMath.rotate(relativeStartPos, angle);
    }

    public Vector calculateEndPoint() {
        return this.position.add(calculateRelativeEndPoint());
    }

    public Vector calculateStartPoint() {
        return this.position.add(getRelativeStartPoint());
    }

    @Override
    public String toString() {
        return String
                .format("Curve center: %s startPoint: %s endPoint: %s angle: %s radius: %s",
                        this.position, this.calculateStartPoint(),
                        this.calculateEndPoint(), angle, radius);
    }

}
