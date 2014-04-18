package hwo.kurjatturskat.core.message.yourcar;

import hwo.kurjatturskat.core.message.CarIdentifier;
import hwo.kurjatturskat.core.message.Message;
import hwo.kurjatturskat.core.message.MessageType;

public class YourCarMsg extends Message<CarIdentifier> {

    public YourCarMsg(CarIdentifier data) {
        super(MessageType.yourCar, data);
    }
}
