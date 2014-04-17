package hwo.kurjatturskat.core.message;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

public class MessageSender {

    private final Socket socket;
    private final PrintWriter writer;

    public MessageSender(final Socket socket)
            throws UnsupportedEncodingException, IOException {
        this.socket = socket;
        this.writer = new PrintWriter(new OutputStreamWriter(
                socket.getOutputStream(), "utf-8"));

    }

    public void sendMessage(Message message) {
        System.out.println("writing: " + message.toJson());
        writer.println(message.toJson());
        writer.flush();
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            this.socket.close();
        } catch (Throwable t) {
            throw t;
        } finally {
            super.finalize();
        }
    }
}
