package hwo.kurjatturskat.visualization;

import org.la4j.vector.Vector;

public class Straight extends TrackElement {

    private double length;
    /**
     * Contains the length
     */
    private Vector direction;

    public Straight(double length) {
        super(TrackElement.TYPE_STRAIGHT);
        this.length = length;
    }

    public Vector getDirection() {
        return direction;
    }

    public void setDirection(Vector direction) {
        this.direction = direction;
    }

    public double getLength() {
        return this.length;
    }

    public Vector calculateEndPosition() {
        return this.position.add(this.direction);
    }

    @Override
    public String toString() {
        return String.format("Straight start: %s direction: %s end: %s",
                this.position, this.direction, calculateEndPosition());
    }
}
