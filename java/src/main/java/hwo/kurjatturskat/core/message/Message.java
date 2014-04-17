package hwo.kurjatturskat.core.message;

import com.google.gson.Gson;

public class Message<T> {

    private MessageType msgType;
    private T data = null;

    public Message(MessageType type) {
        this.msgType = type;
    }

    public Message(MessageType type, T data) {
        this.msgType = type;
        this.data = data;
    }

    public String getMsgType() {
        return msgType.getType();
    }

    public MessageType getType() {
        return this.msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = MessageType.get(msgType);
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }
}
