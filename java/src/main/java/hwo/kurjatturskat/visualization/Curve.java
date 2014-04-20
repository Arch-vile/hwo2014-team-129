package hwo.kurjatturskat.visualization;

import hwo.kurjatturskat.core.message.gameinit.TrackPieces;

import org.la4j.vector.Vector;

public class Curve extends TrackElement {

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
    public Curve(TrackPieces trackPiece) {
        super(TrackElement.TYPE_CURVE, trackPiece);
    }

    public double getAngle() {
        return getTrackPiece().angle;
    }

    public double getRadius() {
        return getTrackPiece().radius;
    }

    public Vector getRelativeStartPoint() {
        return relativeStartPos;
    }

    public void setRelativeStartPos(Vector startPos) {
        this.relativeStartPos = startPos;

    }

    public Vector calculateRelativeEndPoint() {
        return VectorMath.rotate(relativeStartPos, getAngle());
    }

    public Vector calculateEndPoint() {
        return getPosition().add(calculateRelativeEndPoint());
    }

    public Vector calculateStartPoint() {
        return getPosition().add(getRelativeStartPoint());
    }

    @Override
    public String toString() {
        return String
                .format("Curve center: %s startPoint: %s endPoint: %s angle: %s radius: %s",
                        getPosition(), this.calculateStartPoint(),
                        this.calculateEndPoint(), getAngle(), getRadius());
    }

}
