package hwo.kurjatturskat.core.message.turbo;

import hwo.kurjatturskat.core.message.Message;
import hwo.kurjatturskat.core.message.MessageCarData;
import hwo.kurjatturskat.core.message.MessageType;

public class TurboEndMsg extends Message<MessageCarData> {

    public TurboEndMsg(MessageCarData data) {
        super(MessageType.turboEnd, data);
    }

}
