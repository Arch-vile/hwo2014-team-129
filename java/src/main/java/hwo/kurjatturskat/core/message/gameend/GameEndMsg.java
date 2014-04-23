package hwo.kurjatturskat.core.message.gameend;

import hwo.kurjatturskat.core.message.Message;
import hwo.kurjatturskat.core.message.MessageType;

public class GameEndMsg extends Message<GameEndData> {

    public GameEndMsg(GameEndData data) {
        super(MessageType.gameEnd, data);
    }
}
