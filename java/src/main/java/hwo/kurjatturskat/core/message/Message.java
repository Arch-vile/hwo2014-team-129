package hwo.kurjatturskat.core.message;

import com.google.gson.Gson;

public class Message<T> {

    public String gameId;
    public Long gameTick;
    private MessageType msgType;
    private T data;

    public Message(MessageType type) {
        this.msgType = type;
    }

    public Message(MessageType type, T data, String gameId, Long gameTick) {
        this.msgType = type;
        this.data = data;
        this.gameId = gameId;
        this.gameTick = gameTick;
    }

    public Message(MessageType type, T data) {
        this.msgType = type;
        this.data = data;
    }

    public MessageType getType() {
        return this.msgType;
    }

    public T getData() {
        return data;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }
}
