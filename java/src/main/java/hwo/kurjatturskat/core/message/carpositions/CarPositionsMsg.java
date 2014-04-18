package hwo.kurjatturskat.core.message.carpositions;

import hwo.kurjatturskat.core.message.Message;
import hwo.kurjatturskat.core.message.MessageType;

public class CarPositionsMsg extends Message<CarPosition[]> {

    public final int gameTick;

    public CarPositionsMsg(CarPosition[] cars, int gameTick) {
        super(MessageType.carPositions, cars);
        this.gameTick = gameTick;
    }

}
