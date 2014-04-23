package hwo.kurjatturskat.core.message.turboavailable;

import hwo.kurjatturskat.core.message.Message;
import hwo.kurjatturskat.core.message.MessageType;

public class TurboAvailableMsg extends Message<TurboAvailable> {

    public TurboAvailableMsg() {
        super(MessageType.turboAvailable);
    }

}
