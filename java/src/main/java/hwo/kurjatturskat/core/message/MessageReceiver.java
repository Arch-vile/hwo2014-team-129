package hwo.kurjatturskat.core.message;

import hwo.kurjatturskat.core.message.carpositions.CarPositionsMsg;
import hwo.kurjatturskat.core.message.gameend.GameEndMsg;
import hwo.kurjatturskat.core.message.gameinit.GameInitMsg;
import hwo.kurjatturskat.core.message.lapfinished.LapFinishedMsg;
import hwo.kurjatturskat.core.message.turboavailable.TurboAvailableMsg;
import hwo.kurjatturskat.core.message.yourcar.YourCarMsg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

import com.google.gson.Gson;

public class MessageReceiver {

    private final Socket socket;
    final BufferedReader reader;
    final Gson gson = new Gson();

    public MessageReceiver(final Socket socket)
            throws UnsupportedEncodingException, IOException {
        this.socket = socket;
        this.reader = new BufferedReader(new InputStreamReader(
                socket.getInputStream(), "utf-8"));

    }

    public Message<?> waitForMessage() throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {

            System.out.println(line);

            Message<?> msg = gson.fromJson(line, Message.class);

            MessageType type = msg.getType() != null ? msg.getType()
                    : MessageType.unknown;

            switch (type) {

            case carPositions:
                return gson.fromJson(line, CarPositionsMsg.class);

            case yourCar:
                return gson.fromJson(line, YourCarMsg.class);

            case join:
                return gson.fromJson(line, JoinMsg.class);

            case gameInit:
                return gson.fromJson(line, GameInitMsg.class);

            case crash:
                return gson.fromJson(line, CrashMsg.class);

            case spawn:
                return gson.fromJson(line, SpawnMsg.class);

            case gameStart:
                return gson.fromJson(line, GameStartMsg.class);

            case gameEnd:
                return gson.fromJson(line, GameEndMsg.class);

            case lapFinished:
                return gson.fromJson(line, LapFinishedMsg.class);

            case turboAvailable:
                return gson.fromJson(line, TurboAvailableMsg.class);

            case tournamentEnd:
                return msg;

            default:
                System.err.println("Unhandled message: " + line);
                return new Message(MessageType.unknown);
            }

        }

        return new Message(MessageType.unknown);
    }

    @Override
    protected void finalize() throws Throwable {
        shutdown();
    }

    public void shutdown() throws Throwable {
        try {
            System.out.println("Closing input socket");
            this.socket.close();
        } catch (Throwable t) {
            throw t;
        } finally {
            super.finalize();
        }

    }

}
