package hwo.kurjatturskat.model;

import hwo.kurjatturskat.core.message.gameinit.RaceSession;
import hwo.kurjatturskat.core.message.gameinit.TrackLanes;
import hwo.kurjatturskat.core.message.gameinit.TrackPieces;
import hwo.kurjatturskat.util.LoopingList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TrackModel {

    private LoopingList<TrackPieces> pieces;
    private TrackLanes[] lanes;
    private RaceSession raceSession;
    private String trackId = "";
    private String trackName = "";
    private TrackPieces myCurrentTrackPiece;

    public TrackModel(TrackPieces pieces[], TrackLanes lanes[],
            RaceSession raceSession, String trackId, String trackName) {

        ArrayList<TrackPieces> pieceList = new ArrayList<>();
        pieceList.addAll(Arrays.asList(pieces));

        this.pieces = new LoopingList<>(pieceList);
        this.lanes = lanes;
        this.raceSession = raceSession;
        this.trackId = trackId;
        this.trackName = trackName;
        this.myCurrentTrackPiece = pieces[0];
    }

    public TrackPieces getCurrent() {
        return myCurrentTrackPiece;
    }

    public int getCurrentIndex() {
        return pieces.getIndex(getCurrent());
    }

    public TrackPieces getNext() {
        return pieces.getNext();
    }

    public void setMyCurrentTrackPiece(TrackPieces piece) {
        this.myCurrentTrackPiece = piece;
    }

    public void setMyCurrentTrackPiece(int pieceIndex) {
        this.myCurrentTrackPiece = getPieceForIndex(pieceIndex);
    }

    public TrackPieces getPieceForIndex(int index) {
        return this.pieces.getByIndex(index);
    }

    public List<TrackPieces> getAll() {
        return pieces.getAll();
    }

    public String getTrackName() {
        return this.trackName;
    }

    public String getTrackId() {
        return this.trackId;
    }

    // TODO: remove?
    public int getLaps() {
        return this.raceSession.laps;
    }

    /*
     * Get the next switch piece index starting from current index.
     */

    public TrackPieces getNextSwitch() {
        return getNextSwitchByIndex(this.myCurrentTrackPiece);
    }

    /*
     * Get number of switch pieces on track.
     */
    public int getSwitchPieceCount() {
        List<TrackPieces> pieces = this.getAll();
        int switchCount = 0;

        for (TrackPieces piece : pieces) {
            if (piece.isSwitch) {
                switchCount++;
            }
        }

        return switchCount;
    }

    /*
     * Get the next switch piece index starting from given piece (exclusive).
     */
    public TrackPieces getNextSwitchByIndex(TrackPieces from) {
        this.pieces.setCurrent(from);
        this.pieces.advance();
        while (!this.pieces.getCurrent().isSwitch) {
            this.pieces.advance();
        }
        return this.pieces.getCurrent();
    }

    /*
     * Get lanes count on track.
     */
    public int getLanesCount() {
        return lanes.length;
    }

    /*
     * Get number of possible routes on track.
     */
    public int getRoutesCount() {
        return (int) Math.pow(this.getLanesCount(), this.getSwitchPieceCount());
    }

    public TrackLanes getLane(int index) {
        for (TrackLanes lane : this.lanes) {
            if (index == lane.index) {
                return lane;
            }
        }
        return null;
    }

    public TrackLanes getLaneForIndex(int laneIndex) {
        for (int i = 0; i < this.lanes.length; i++) {
            if (this.lanes[i].index == laneIndex) {
                return this.lanes[i];
            }
        }
        return null;
    }

    public double getLaneLengthOnPiece(TrackPieces piece, TrackLanes lane) {
        if (piece.isCurve()) {
            double ourLaneOffset = 0.0;
            ourLaneOffset = lane.distanceFromCenter;
            if (piece.angle < 0) {
                ourLaneOffset *= -1;
            }

            return ((Math.abs(piece.angle) / 360) * 2 * Math.PI)
                    * (piece.radius - ourLaneOffset);
        } else {
            return piece.length;
        }
    }

    /*
     * Get distance between two pieces, excluding end pieces.
     */
    public double getLaneDistanceBetweenPieces(TrackPieces startPiece,
            TrackPieces endPiece, TrackLanes lane) {

        double distance = 0d;
        this.pieces.setCurrent(startPiece);
        while (this.pieces.advance() != endPiece) {
            distance += getLaneLengthOnPiece(this.pieces.getCurrent(), lane);
        }

        return distance;
    }

    public TrackPieces getNextCurveStart() {
        return this.getNextCurveStartByIndex(this.myCurrentTrackPiece);
    }

    public TrackPieces getNextCurveStartByIndex(TrackPieces from) {
        this.pieces.setCurrent(from);
        while (this.pieces.getNext().curveString().equals(from.curveString())) {
            this.pieces.advance();
        }
        this.pieces.advance();
        while (!this.pieces.getCurrent().isCurve()) {
            this.pieces.advance();
        }
        return this.pieces.getCurrent();
    }

    public TrackPieces getNextCurveEnd() {
        return this.getNextCurveEndByIndex(this.getNextCurveStart());
    }

    public TrackPieces getNextCurveEndByIndex(TrackPieces from) {
        TrackPieces start = this.getNextCurveStartByIndex(from);
        this.pieces.setCurrent(from);

        while (this.pieces.getNext().curveString().equals(start.curveString())) {
            this.pieces.advance();
        }

        return this.pieces.getCurrent();
    }

    public TrackPieces getBiggestAngle(TrackPieces start, TrackPieces end) {
        this.pieces.setCurrent(start);
        TrackPieces biggestAngle = start;
        while (this.pieces.getCurrent() != end) {
            if (Math.abs(this.pieces.getCurrent().angle) > Math
                    .abs(biggestAngle.angle)) {
                biggestAngle = this.pieces.getCurrent();
            }
            this.pieces.advance();
        }
        return biggestAngle;
    }
}
