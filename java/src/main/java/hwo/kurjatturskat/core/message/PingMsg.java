package hwo.kurjatturskat.core.message;

public class PingMsg extends Message<String> {

    public PingMsg() {
        super(MessageType.ping, null);
    }
}
