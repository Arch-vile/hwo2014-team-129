package noobbot;

import hwo.kurjatturskat.core.message.carpositions.CarPositionsMsg;
import hwo.kurjatturskat.core.message.gameinit.GameInitMsg;
import hwo.kurjatturskat.core.message.yourcar.YourCarMsg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Main {
    public static void main(String... args) throws IOException {

        String host = args[0];
        int port = Integer.parseInt(args[1]);
        String botName = args[2];
        String botKey = args[3];

        System.out.println("Connecting to " + host + ":" + port + " as "
                + botName + "/" + botKey);

        final Socket socket = new Socket(host, port);
        final PrintWriter writer = new PrintWriter(new OutputStreamWriter(
                socket.getOutputStream(), "utf-8"));

        final BufferedReader reader = new BufferedReader(new InputStreamReader(
                socket.getInputStream(), "utf-8"));

        new Main(reader, writer, new Join(botName, botKey));
        socket.close();
    }

    final Gson gson = new Gson();

    private PrintWriter writer;

    public Main(final BufferedReader reader, final PrintWriter writer,
            final Join join) throws IOException {
        this.writer = writer;
        String line = null;

        Gson prettyGson = new GsonBuilder().setPrettyPrinting().create();

        send(join);
        while ((line = reader.readLine()) != null) {

            final MsgWrapper msgFromServer = gson.fromJson(line,
                    MsgWrapper.class);
            if (msgFromServer.msgType.equals("carPositions")) {
                System.out.println(prettyGson.toJson(line));

                final CarPositionsMsg carPos = gson.fromJson(line,
                        CarPositionsMsg.class);
                if (carPos.data[0].piecePosition.pieceIndex > 37) {
                    send(new Throttle(1.0));
                } else {
                    send(new Throttle(0.65));
                }
            } else if (msgFromServer.msgType.equals("yourCar")) {
                final YourCarMsg myCar = gson.fromJson(line, YourCarMsg.class);
                System.out.println("yourCar name=" + myCar.data.name
                        + " color=" + myCar.data.color);
            } else if (msgFromServer.msgType.equals("join")) {
                System.out.println("Joined");
            } else if (msgFromServer.msgType.equals("gameInit")) {
                System.out.println("Race init");
                // Print json nicely
                // System.out.println(prettyGson.toJson(msgFromServer));

                final GameInitMsg gameInit = gson.fromJson(line,
                        GameInitMsg.class);
                System.out.println("gameInit Laps="
                        + gameInit.data.race.raceSession.laps + " Pieces: "
                        + gameInit.data.race.track.pieces.length);
            } else if (msgFromServer.msgType.equals("gameEnd")) {
                System.out.println("Race end");
            } else if (msgFromServer.msgType.equals("gameStart")) {
                System.out.println("Race start");
            } else {
                send(new Ping());
            }
        }
    }

    private void send(final SendMsg msg) {
        writer.println(msg.toJson());
        writer.flush();
    }
}

abstract class SendMsg {
    public String toJson() {
        return new Gson().toJson(new MsgWrapper(this));
    }

    protected Object msgData() {
        return this;
    }

    protected abstract String msgType();
}

class MsgWrapper {
    public final String msgType;
    public final String foo;
    public final Object data;

    MsgWrapper(final String msgType, final String bar, final Object data) {
        this.msgType = msgType;
        this.data = data;
        this.foo = bar;
    }

    public MsgWrapper(final SendMsg sendMsg) {
        this(sendMsg.msgType(), "null", sendMsg.msgData());
    }

}

class Join extends SendMsg {
    public final String name;
    public final String key;

    Join(final String name, final String key) {
        this.name = name;
        this.key = key;
    }

    @Override
    protected String msgType() {
        return "join";
    }
}

class Ping extends SendMsg {
    @Override
    protected String msgType() {
        return "ping";
    }
}

class Throttle extends SendMsg {
    private double value;

    public Throttle(double value) {
        this.value = value;
    }

    @Override
    protected Object msgData() {
        return value;
    }

    @Override
    protected String msgType() {
        return "throttle";
    }
}
