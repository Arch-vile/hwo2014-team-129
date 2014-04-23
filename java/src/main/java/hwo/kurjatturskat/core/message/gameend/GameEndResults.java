package hwo.kurjatturskat.core.message.gameend;

import hwo.kurjatturskat.core.message.CarIdentifier;

public class GameEndResults {
    public final CarIdentifier car;
    public final GameEndResult result;

    public GameEndResults(CarIdentifier car, GameEndResult result) {
        this.car = car;
        this.result = result;
    }
}
