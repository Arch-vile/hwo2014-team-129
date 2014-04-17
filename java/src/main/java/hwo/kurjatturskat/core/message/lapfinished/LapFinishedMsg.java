package hwo.kurjatturskat.core.message.lapfinished;

import hwo.kurjatturskat.core.message.Message;
import hwo.kurjatturskat.core.message.MessageType;

public class LapFinishedMsg extends Message<LapFinishedData> {

    public LapFinishedMsg(LapFinishedData data) {
        super(MessageType.lapFinished, data);
    }
}
