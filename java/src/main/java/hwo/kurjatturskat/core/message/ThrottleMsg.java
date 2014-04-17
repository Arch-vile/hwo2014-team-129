package hwo.kurjatturskat.core.message;

public class ThrottleMsg extends Message<Double> {

    public ThrottleMsg(Double throttle) {
        super(MessageType.throttle, throttle);
    }

}
