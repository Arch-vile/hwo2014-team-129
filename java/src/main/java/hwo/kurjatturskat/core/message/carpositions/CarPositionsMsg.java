package hwo.kurjatturskat.core.message.carpositions;

import hwo.kurjatturskat.core.message.Message;
import hwo.kurjatturskat.core.message.MessageType;

public class CarPositionsMsg extends Message<CarPosition[]> {

    public CarPositionsMsg(CarPosition[] cars, String gameId, Long gameTick) {
        super(MessageType.carPositions, cars, gameId, gameTick);
    }

}
