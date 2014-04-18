package hwo.kurjatturskat.model;

import hwo.kurjatturskat.core.message.CarIdentifier;
import hwo.kurjatturskat.core.message.CrashMsg;
import hwo.kurjatturskat.core.message.SpawnMsg;
import hwo.kurjatturskat.core.message.carpositions.CarPosition;
import hwo.kurjatturskat.core.message.carpositions.CarPositionsMsg;
import hwo.kurjatturskat.core.message.gameinit.GameInitMsg;

public class World {

    private TrackModel trackModel;
    private CarIdentifier yourCar;
    private boolean onTrack;

    public void update(CarPositionsMsg msg) {
        int yourPieceIndex = getYourPieceIndex(msg.getData());
        trackModel.setCurrentPiece(yourPieceIndex);
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
}
