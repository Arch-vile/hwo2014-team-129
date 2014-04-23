package hwo.kurjatturskat.core.message.carpositions;

import hwo.kurjatturskat.core.message.Message;
import hwo.kurjatturskat.core.message.MessageType;

// TODO: is gameId and gameTick in each message. Move to parent class.
public class CarPositionsMsg extends Message<CarPosition[]> {
    public final String gameId;
    public final int gameTick;

    public CarPositionsMsg(CarPosition[] cars, String gameId, int gameTick) {
        super(MessageType.carPositions, cars);
        this.gameId = gameId;
        this.gameTick = gameTick;
    }

}
