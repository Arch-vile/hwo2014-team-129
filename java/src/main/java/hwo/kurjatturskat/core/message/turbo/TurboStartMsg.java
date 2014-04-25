package hwo.kurjatturskat.core.message.turbo;

import hwo.kurjatturskat.core.message.Message;
import hwo.kurjatturskat.core.message.MessageCarData;
import hwo.kurjatturskat.core.message.MessageType;

public class TurboStartMsg extends Message<MessageCarData> {

    public TurboStartMsg(MessageCarData data) {
        super(MessageType.turboStart, data);
    }
}
