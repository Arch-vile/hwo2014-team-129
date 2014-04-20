package hwo.kurjatturskat.visualization;

import hwo.kurjatturskat.core.message.gameinit.TrackPieces;

import org.la4j.vector.Vector;

public class Straight extends TrackElement {

    /**
     * Contains the length
     */
    private Vector direction;

    public Straight(TrackPieces trackPiece) {
        super(TrackElement.TYPE_STRAIGHT, trackPiece);
    }

    public Vector getDirection() {
        return direction;
    }

    public void setDirection(Vector direction) {
        this.direction = direction;
    }

    public double getLength() {
        return getTrackPiece().length;
    }

    public Vector calculateEndPosition() {
        return getPosition().add(this.direction);
    }

    @Override
    public String toString() {
        return String.format("Straight start: %s direction: %s end: %s",
                getPosition(), this.direction, calculateEndPosition());
    }
}
