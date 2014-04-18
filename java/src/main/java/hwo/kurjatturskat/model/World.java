package hwo.kurjatturskat.model;

import hwo.kurjatturskat.core.message.CarIdentifier;
import hwo.kurjatturskat.core.message.carpositions.CarPosition;
import hwo.kurjatturskat.core.message.carpositions.CarPositionsMsg;
import hwo.kurjatturskat.core.message.carpositions.PiecePosition;
import hwo.kurjatturskat.core.message.gameinit.GameInitMsg;

import java.util.ArrayList;

public class World {

    private TrackModel trackModel;
    private CarIdentifier yourCar;

    private ArrayList<TrackPosition> myCarTravels = new ArrayList<TrackPosition>();

    public void update(CarPositionsMsg msg) {
        PiecePosition myPiecePos = this.getPiecePositionForCar(msg.getData(),
                this.yourCar);
        trackModel.setCurrentPiece(myPiecePos.pieceIndex);
        TrackPosition trackPos = new TrackPosition(msg.gameTick,
                myPiecePos.pieceIndex, myPiecePos.inPieceDistance,
                myPiecePos.lane);
        this.myCarTravels.add(trackPos);

    }

    public void update(GameInitMsg message) {
        this.trackModel = new TrackModel(message.getData().race.track.pieces);
    }

    public void setYourCarId(CarIdentifier carId) {
        this.yourCar = carId;
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
}
