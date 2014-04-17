package hwo.kurjatturskat.ai.engine;

import hwo.kurjatturskat.ai.drivers.Driver;
import hwo.kurjatturskat.core.message.JoinMsg;
import hwo.kurjatturskat.core.message.Message;
import hwo.kurjatturskat.core.message.MessageReceiver;
import hwo.kurjatturskat.core.message.MessageSender;
import hwo.kurjatturskat.core.message.ThrottleMsg;
import hwo.kurjatturskat.core.message.carpositions.CarPositionsMsg;

import java.io.IOException;
import java.net.Socket;

public class BotRunner {

    private final MessageReceiver receiver;
    private final MessageSender sender;
    private final Driver driver;
    private final String botName;
    private final String botKey;

    public BotRunner(MessageReceiver receiver, MessageSender sender,
            Driver driver, String botName, String botKey) {
        this.receiver = receiver;
        this.sender = sender;
        this.driver = driver;
        this.botName = botName;
        this.botKey = botKey;
    }

    public void run() throws IOException {
        this.sender.sendMessage(new JoinMsg(this.botName, this.botKey));

        Message<?> message = null;
        while ((message = this.receiver.waitForMessage()) != null) {

            switch (message.getType()) {
            case carPositions:
                CarPositionsMsg msg = (CarPositionsMsg) message;
                sender.sendMessage(new ThrottleMsg(1.0d));
            }
        }
    }

    public static void main(String... args) throws IOException {

        String host = args[0];
        int port = Integer.parseInt(args[1]);
        String botName = args[2];
        String botKey = args[3];

        System.out.println("Connecting to " + host + ":" + port + " as "
                + botName + "/" + botKey);

        final Socket socket = new Socket(host, port);
        BotRunner runner = new BotRunner(new MessageReceiver(socket),
                new MessageSender(socket), null, botName, botKey);

        runner.run();
    }

}
