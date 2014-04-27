package hwo.kurjatturskat.util;

import hwo.kurjatturskat.core.message.gameinit.TrackLanes;
import hwo.kurjatturskat.core.message.gameinit.TrackPieces;
import hwo.kurjatturskat.model.TrackPosition;

public class TrackUtils {

    /**
     * Tells distance between two following track positions.
     * 
     * @param start
     * @param end
     * @return
     */
    public static double getDistance(TrackPosition start, TrackPosition end,
            TrackLanes lane) {
        if (start == null) {
            return end.inPieceDistance;
        }
        if (start.pieceIndex == end.pieceIndex) {
            return end.inPieceDistance - start.inPieceDistance;
        }
        double distance = 0.0;

        TrackPieces startPiece = start.getTrackPiece();

        if (startPiece.isCurve()) {
            double ourLaneOffset = 0.0;

            ourLaneOffset = lane.distanceFromCenter;
            if (startPiece.angle < 0) {
                ourLaneOffset *= -1;
            }

            distance += ((Math.abs(startPiece.angle) / 360) * 2 * Math.PI)
                    * (startPiece.radius - ourLaneOffset)
                    - start.inPieceDistance;
        } else {
            distance += startPiece.length - start.inPieceDistance;
        }
        distance += end.inPieceDistance;

        return distance;
    }

    public static double getPieceLenght(TrackPieces current, TrackLanes lane) {
        if (current.isCurve()) {
            double ourLaneOffset = lane.distanceFromCenter;
            if (current.angle < 0) {
                ourLaneOffset *= -1;
            }

            return ((Math.abs(current.angle) / 360) * 2 * Math.PI)
                    * (current.radius - ourLaneOffset);
        } else {
            return current.length;
        }
    }
}
