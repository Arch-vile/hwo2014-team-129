package hwo.kurjatturskat.core.message.gameinit;

import hwo.kurjatturskat.core.message.Message;
import hwo.kurjatturskat.core.message.MessageType;

public class GameInitMsg extends Message<GameInitData> {

    public GameInitMsg(GameInitData data) {
        super(MessageType.gameInit, data);
    }
}
