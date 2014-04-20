package hwo.kurjatturskat.visualization;

import hwo.kurjatturskat.core.message.gameinit.TrackPieces;

import java.util.ArrayList;
import java.util.List;

import org.la4j.vector.Vector;

public class TrackElement {

    public static final String TYPE_STRAIGHT = "straight";
    public static final String TYPE_CURVE = "curve";

    private final TrackPieces trackPiece;
    private final String type;
    private Vector position;

    public TrackElement(String type, TrackPieces trackPiece) {
        this.type = type;
        this.trackPiece = trackPiece;
    }

    public String getType() {
        return this.type;
    }

    public Vector getPosition() {
        return this.position;
    }

    public boolean isCurve() {
        return TYPE_CURVE.equals(type);
    }

    public void setPosition(Vector position) {
        this.position = position;
    }

    public boolean isStraight() {
        return TYPE_STRAIGHT.equals(type);
    }

    public static List<TrackElement> convert(List<TrackPieces> pieces) {

        List<TrackElement> elements = new ArrayList<>();
        for (TrackPieces piece : pieces) {
            elements.add(convert(piece));
        }

        return elements;
    }

    private static TrackElement convert(TrackPieces piece) {
        if (piece.isCurve()) {
            return new Curve(piece);
        } else {
            return new Straight(piece);
        }

    }

    public TrackPieces getTrackPiece() {
        return trackPiece;
    }

}
