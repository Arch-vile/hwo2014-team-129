package hwo.kurjatturskat.ai.engine;

import hwo.kurjatturskat.ai.drivers.Driver;
import hwo.kurjatturskat.ai.drivers.SlowBot;
import hwo.kurjatturskat.core.message.JoinMsg;
import hwo.kurjatturskat.core.message.Message;
import hwo.kurjatturskat.core.message.MessageReceiver;
import hwo.kurjatturskat.core.message.MessageSender;
import hwo.kurjatturskat.core.message.MessageType;
import hwo.kurjatturskat.core.message.SwitchLaneMsg;
import hwo.kurjatturskat.core.message.ThrottleMsg;
import hwo.kurjatturskat.core.message.carpositions.CarPositionsMsg;
import hwo.kurjatturskat.core.message.gameinit.GameInitMsg;
import hwo.kurjatturskat.core.message.yourcar.YourCarMsg;
import hwo.kurjatturskat.model.World;

import java.net.Socket;

public class BotRunner {

    private final MessageReceiver receiver;
    private final MessageSender sender;
    private final Driver driver;
    private final String botName;
    private final String botKey;

    private World world;

    public BotRunner(MessageReceiver receiver, MessageSender sender,
            Driver driver, String botName, String botKey) {
        this.receiver = receiver;
        this.sender = sender;
        this.driver = driver;
        this.botName = botName;
        this.botKey = botKey;
        this.world = new World();
    }

    public void run() throws Throwable {
        this.sender.sendMessage(new JoinMsg(this.botName, this.botKey));

        Message<?> message = null;
        while ((message = this.receiver.waitForMessage()) != null
                && !tournamentEnd(message)) {

            updateWorld(message);

            if (world.isInitialized()) {
                String direction = this.driver.getLane(world);
                if (direction != null) {
                    this.sender.sendMessage(new SwitchLaneMsg(direction));
                } else {
                    Double throttle = this.driver.getThrottle(world);
                    this.sender.sendMessage(new ThrottleMsg(throttle));
                }
            }
        }

        shutdown();

    }

    private void shutdown() throws Throwable {
        System.out.println("Tournament end. Shutting down...");
        receiver.shutdown();
        sender.shutdown();

    }

    private boolean tournamentEnd(Message<?> message) {
        return MessageType.tournamentEnd == message.getType();
    }

    private void updateWorld(Message<?> message) {
        switch (message.getType()) {
        case carPositions:
            this.world.update((CarPositionsMsg) message);
            break;
        case gameInit:
            this.world.update((GameInitMsg) message);
            break;
        case yourCar:
            this.world.setYourCarId(((YourCarMsg) message).getData());
        }
    }

    public static void main(String... args) throws Throwable {

        String host = args[0];
        int port = Integer.parseInt(args[1]);
        String botName = args[2];
        String botKey = args[3];

        System.out.println("Connecting to " + host + ":" + port + " as "
                + botName + "/" + botKey);

        final Socket socket = new Socket(host, port);
        BotRunner runner = new BotRunner(new MessageReceiver(socket),
                new MessageSender(socket), new SlowBot(), botName, botKey);

        runner.run();
    }

}
