package hwo.kurjatturskat.core.message.yourcar;

import hwo.kurjatturskat.core.message.Message;
import hwo.kurjatturskat.core.message.MessageType;

public class YourCarMsg extends Message<YourCarData> {

    public YourCarMsg(YourCarData data) {
        super(MessageType.yourCar, data);
    }
}
