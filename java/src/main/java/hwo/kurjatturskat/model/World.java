package hwo.kurjatturskat.model;

import hwo.kurjatturskat.core.message.CarIdentifier;
import hwo.kurjatturskat.core.message.CrashMsg;
import hwo.kurjatturskat.core.message.SpawnMsg;
import hwo.kurjatturskat.core.message.carpositions.CarPosition;
import hwo.kurjatturskat.core.message.carpositions.CarPositionsMsg;
import hwo.kurjatturskat.core.message.carpositions.PiecePosition;
import hwo.kurjatturskat.core.message.gameinit.GameInitMsg;
import hwo.kurjatturskat.core.message.gameinit.TrackPieces;

import java.util.ArrayList;
import java.util.List;

public class World {

    private TrackModel trackModel;
    private CarIdentifier yourCar;
    private boolean onTrack;

    private ArrayList<TrackPosition> myCarTravels = new ArrayList<TrackPosition>();

    public void update(CarPositionsMsg msg) {
        PiecePosition myPiecePos = this.getPiecePositionForCar(msg.getData(),
                this.yourCar);
        trackModel.setCurrentPiece(myPiecePos.pieceIndex);
        TrackPosition trackPos = new TrackPosition(msg.gameTick,
                myPiecePos.pieceIndex, myPiecePos.inPieceDistance,
                myPiecePos.lane);

        this.myCarTravels.add(trackPos);

        int posSize = this.myCarTravels.size();
        List<TrackPosition> trackPoses = this.myCarTravels.subList(0, 0);
        if (posSize > 1) {
            trackPoses = this.myCarTravels.subList(posSize - 2, posSize - 1);
        }

        double distanceMoved = this.getDistanceMoved(trackPoses);
        System.out.println("Distance moved (" + myPiecePos.pieceIndex + "): "
                + distanceMoved);

    }

    public void update(GameInitMsg message) {
        this.trackModel = new TrackModel(message.getData().race.track.pieces);
    }

    public void setYourCarId(CarIdentifier carId) {
        this.yourCar = carId;
        this.onTrack = true;
    }

    public boolean onTrack() {
        return this.onTrack;
    }

    /**
     * Retrieves the piecePosition for given car identifier from car position
     * data.
     * 
     * @param data
     * @param carId
     * @return Null is returned if we have no position data for given car id.
     */
    private PiecePosition getPiecePositionForCar(CarPosition[] data,
            CarIdentifier carId) {
        for (CarPosition posData : data) {
            CarIdentifier id = posData.id;
            if (carId.color.equals(id.color)) {
                return posData.piecePosition;

            }
        }
        System.err.println("Could not determine yourself from car positions!");
        return null;
    }

    public TrackModel getTrackModel() {
        return trackModel;
    }

    public boolean isInitialized() {
        return this.trackModel != null && this.yourCar != null;
    }

    public void setOffTrack(CrashMsg message) {
        if (this.yourCar.name.equals(message.getData().name)
                && this.yourCar.color.equals(message.getData().color)) {
            System.out.println("We crashed!");
            this.onTrack = false;
        }
    }

    public void setOnTrack(SpawnMsg message) {
        if (this.yourCar.name.equals(message.getData().name)
                && this.yourCar.color.equals(message.getData().color)) {
            System.out.println("We are back on track!");
            this.onTrack = true;
        }
    }

    /**
     * We return the distance moved we can gather from given track positions.
     * 
     * @param positions
     * @return
     */
    public double getDistanceMoved(List<TrackPosition> positions) {
        double distance = 0.0;
        TrackPieces currPiece = null;
        TrackPieces prevPiece = null;
        TrackPosition prevPosition = null;
        System.out.println("Positions size: " + positions.size());
        for (TrackPosition position : positions) {
            double tempDistance = 0.0; // distance from one absolute position in
                                       // piece
            currPiece = this.trackModel.getPieceForIndex(position.pieceIndex);
            tempDistance += position.inPieceDistance;
            if (prevPosition != null) {
                if (!prevPiece.equals(currPiece)) {
                    // add the end of the previous piece to distance traveled
                    if (!prevPiece.isCurve()) {
                        tempDistance += prevPiece.length
                                - prevPosition.inPieceDistance;
                    } else {
                        // this should consider the lane
                        tempDistance += (prevPiece.angle / 360)
                                * prevPiece.radius
                                - prevPosition.inPieceDistance;
                    }
                } else {
                    tempDistance -= prevPosition.inPieceDistance;
                }
            }
            prevPiece = currPiece;
            prevPosition = position;
            distance += tempDistance;
        }

        return distance;
    }
}
