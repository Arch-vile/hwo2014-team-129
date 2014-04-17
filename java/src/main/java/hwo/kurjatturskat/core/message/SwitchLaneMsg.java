package hwo.kurjatturskat.core.message;

public class SwitchLaneMsg extends Message<String> {

    public SwitchLaneMsg(String direction) {
        super(MessageType.switchLane, direction);
    }

}
