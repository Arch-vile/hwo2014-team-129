package hwo.kurjatturskat.core.message.carpositions;

import hwo.kurjatturskat.core.message.Message;
import hwo.kurjatturskat.core.message.MessageType;

public class CarPositionsMsg extends Message<Car[]> {

    public final int gameTick;

    public CarPositionsMsg(Car[] cars, int gameTick) {
        super(MessageType.carPositions, cars);
        this.gameTick = gameTick;
    }

}
