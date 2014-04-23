package hwo.kurjatturskat.core.message;

public class CrashMsg extends Message<MessageCarData> {

    public CrashMsg(MessageCarData data) {
        super(MessageType.crash, data);
    }
}
