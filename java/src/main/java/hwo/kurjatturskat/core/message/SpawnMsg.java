package hwo.kurjatturskat.core.message;


public class SpawnMsg extends Message<MessageCarData> {

    public SpawnMsg(MessageCarData data) {
        super(MessageType.spawn, data);
    }
}
