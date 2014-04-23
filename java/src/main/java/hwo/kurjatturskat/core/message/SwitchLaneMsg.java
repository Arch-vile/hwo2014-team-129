package hwo.kurjatturskat.core.message;

// TODO: move all the outbound message to separate package
public class SwitchLaneMsg extends Message<String> {

    public SwitchLaneMsg(String direction) {
        super(MessageType.switchLane, direction);
    }

}
