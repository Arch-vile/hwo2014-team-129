package hwo.kurjatturskat.core.message;

import com.google.gson.Gson;

public class Message<T> {

    private MessageType msgType;
    private T data;

    public Message(MessageType type) {
        this.msgType = type;
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
