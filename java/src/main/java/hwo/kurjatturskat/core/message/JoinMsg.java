package hwo.kurjatturskat.core.message;

public class JoinMsg extends Message<JoinData> {

    public JoinMsg(String name, String key) {
        super(MessageType.join, new JoinData(name, key));
    }

}
