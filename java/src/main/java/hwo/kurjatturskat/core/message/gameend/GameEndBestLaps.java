package hwo.kurjatturskat.core.message.gameend;

import hwo.kurjatturskat.core.message.CarIdentifier;
import hwo.kurjatturskat.core.message.lapfinished.LapTime;

public class GameEndBestLaps {
    public final CarIdentifier car;
    public final LapTime result;

    public GameEndBestLaps(CarIdentifier car, LapTime result) {
        this.car = car;
        this.result = result;
    }
}
