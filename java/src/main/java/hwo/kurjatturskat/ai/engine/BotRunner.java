package hwo.kurjatturskat.ai.engine;

import hwo.kurjatturskat.ai.drivers.Driver;
import hwo.kurjatturskat.ai.drivers.MarkusBot;
import hwo.kurjatturskat.core.message.CrashMsg;
import hwo.kurjatturskat.core.message.Message;
import hwo.kurjatturskat.core.message.MessageReceiver;
import hwo.kurjatturskat.core.message.MessageSender;
import hwo.kurjatturskat.core.message.MessageType;
import hwo.kurjatturskat.core.message.SpawnMsg;
import hwo.kurjatturskat.core.message.SwitchLaneMsg;
import hwo.kurjatturskat.core.message.ThrottleMsg;
import hwo.kurjatturskat.core.message.advanced.CreateRaceMsg;
import hwo.kurjatturskat.core.message.carpositions.CarPositionsMsg;
import hwo.kurjatturskat.core.message.gameinit.GameInitMsg;
import hwo.kurjatturskat.core.message.lapfinished.LapFinishedMsg;
import hwo.kurjatturskat.core.message.yourcar.YourCarMsg;
import hwo.kurjatturskat.model.World;
import hwo.kurjatturskat.visualization.PlotterView;

import java.io.IOException;
import java.net.Socket;

public class BotRunner {

    private final MessageReceiver receiver;
    private final MessageSender sender;
    private final Driver driver;
    private final String botName;
    private final String botKey;
    private final String track;
    private String gameId;

    private World world;

    public BotRunner(MessageReceiver receiver, MessageSender sender,
            Driver driver, String botName, String botKey, String track) {
        this.receiver = receiver;
        this.sender = sender;
        this.driver = driver;
        this.botName = botName;
        this.botKey = botKey;
        this.track = track;
        this.world = new World();
        this.gameId = "";
    }

    public void run() throws Throwable {

        initializeSequence();
        PlotterView plotter = new PlotterView(world);

        // this.sender.sendMessage(new JoinRaceMsg(this.botName, this.botKey,
        // this.track, "topsecret", 1));

        // this.sender.sendMessage(new JoinRaceMsg("driver2", this.botKey,
        // this.track, "topsecret", 1));

        // this.sender.sendMessage(new JoinMsg(this.botName, this.botKey));

        Message<?> message = null;

        boolean switchSent = false;
        int sswitch = world.getTrackModel().getNextSwitch();
        while ((message = this.receiver.waitForMessage()) != null
                && !tournamentEnd(message)) {

            updateWorld(message);
            plotter.plot();

            if (world.isInitialized()) {
                String direction = this.driver.getLane(world);
                if (direction != null) {
                    if (!switchSent) {
                        this.sender.sendMessage(new SwitchLaneMsg(direction));
                        switchSent = true;
                    } else if (sswitch != world.getTrackModel().getNextSwitch()) {
                        switchSent = false;
                        sswitch = world.getTrackModel().getNextSwitch();
                    }
                }
                // } else {
                Double throttle = this.driver.getThrottle(world);
                this.sender.sendMessage(new ThrottleMsg(throttle));
                // }
            }
        }

        shutdown();

    }

    private void initializeSequence() throws IOException {
        this.sender.sendMessage(new CreateRaceMsg(this.botName, this.botKey,
                this.track, "topsecret", 1));

        YourCarMsg yourCarMsg = (YourCarMsg) waitForMsg(MessageType.yourCar);
        updateWorld(yourCarMsg);

        GameInitMsg gameInitMsg = (GameInitMsg) waitForMsg(MessageType.gameInit);
        updateWorld(gameInitMsg);

    }

    private Message waitForMsg(MessageType type) throws IOException {
        Message<?> message = null;
        while ((message = this.receiver.waitForMessage()) != null) {
            if (message.getType() != type) {
                System.err.println(String.format(
                        "Waited for %s message but got: %s", type,
                        message.getType()));
            } else {
                System.out.println(String.format(
                        "Successfully received the %s ", type));
                return message;
            }
        }

        return null;
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
        case gameStart:
            break;
        case carPositions:
            this.world.update((CarPositionsMsg) message);
            this.gameId = ((CarPositionsMsg) message).gameId;
            break;
        case gameInit:
            this.world.update((GameInitMsg) message);
            break;
        case yourCar:
            this.world.setMyCarId(((YourCarMsg) message).getData());
            break;
        case crash:
            this.world.setOffTrack((CrashMsg) message);
            break;
        case spawn:
            this.world.setOnTrack((SpawnMsg) message);
            break;
        case lapFinished:
            this.world.update((LapFinishedMsg) message);
            break;
        default:
            break;
        }
    }

    public static void main(String... args) throws Throwable {

        String host = args[0];
        int port = Integer.parseInt(args[1]);
        String botName = args[2];
        String botKey = args[3];
        String track = "germany";
        if (args.length == 5) {
            track = args[4];
        }

        System.out.println("Connecting to " + host + ":" + port + " as "
                + botName + "/" + botKey + " @ " + track);

        // Driver myBot = new ConstantThrottleBot(0.3);
        // Driver myBot = new SlowBot();
        Driver myBot = new MarkusBot();
        final Socket socket = new Socket(host, port);
        BotRunner runner = new BotRunner(new MessageReceiver(socket),
                new MessageSender(socket), myBot, botName, botKey, track);

        runner.run();
    }
}
