package hwo.kurjatturskat.core.message;

// TODO: move all the outbound message to separate package
public class LaunchTurboMsg extends Message<String> {

    public LaunchTurboMsg() {
        super(MessageType.turbo, "Bite the dust, succers!");
    }

}
