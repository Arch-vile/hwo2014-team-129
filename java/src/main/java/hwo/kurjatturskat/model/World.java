package hwo.kurjatturskat.model;

import hwo.kurjatturskat.core.message.CarIdentifier;
import hwo.kurjatturskat.core.message.carpositions.CarPosition;
import hwo.kurjatturskat.core.message.carpositions.CarPositionsMsg;
import hwo.kurjatturskat.core.message.gameinit.GameInitMsg;

public class World {

    private TrackModel trackModel;
    private CarIdentifier yourCar;

    public void update(CarPositionsMsg msg) {
        int yourPieceIndex = getYourPieceIndex(msg.getData());
        trackModel.setCurrentPiece(yourPieceIndex);
    }

    public void update(GameInitMsg message) {
        this.trackModel = new TrackModel(message.getData().race.track.pieces);
    }

    public void setYourCarId(CarIdentifier carId) {
        this.yourCar = carId;
    }

    private Integer getYourPieceIndex(CarPosition[] data) {
        for (CarPosition pos : data) {
            CarIdentifier id = pos.id;
            if (this.yourCar.color.equals(id.color)) {
                return pos.piecePosition.pieceIndex;
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
