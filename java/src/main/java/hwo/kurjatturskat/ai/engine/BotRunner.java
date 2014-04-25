package hwo.kurjatturskat.ai.engine;

import hwo.kurjatturskat.ai.drivers.Driver;
import hwo.kurjatturskat.ai.drivers.MarkusBot;
import hwo.kurjatturskat.core.message.CrashMsg;
import hwo.kurjatturskat.core.message.JoinMsg;
import hwo.kurjatturskat.core.message.LaunchTurboMsg;
import hwo.kurjatturskat.core.message.Message;
import hwo.kurjatturskat.core.message.MessageReceiver;
import hwo.kurjatturskat.core.message.MessageSender;
import hwo.kurjatturskat.core.message.MessageType;
import hwo.kurjatturskat.core.message.PingMsg;
import hwo.kurjatturskat.core.message.SpawnMsg;
import hwo.kurjatturskat.core.message.SwitchLaneMsg;
import hwo.kurjatturskat.core.message.ThrottleMsg;
import hwo.kurjatturskat.core.message.advanced.CreateRaceMsg;
import hwo.kurjatturskat.core.message.advanced.JoinRaceMsg;
import hwo.kurjatturskat.core.message.carpositions.CarPositionsMsg;
import hwo.kurjatturskat.core.message.gameend.GameEndMsg;
import hwo.kurjatturskat.core.message.gameinit.GameInitMsg;
import hwo.kurjatturskat.core.message.gameinit.TrackPieces;
import hwo.kurjatturskat.core.message.lapfinished.LapFinishedMsg;
import hwo.kurjatturskat.core.message.turbo.TurboEndMsg;
import hwo.kurjatturskat.core.message.turbo.TurboStartMsg;
import hwo.kurjatturskat.core.message.turboavailable.TurboAvailableMsg;
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
    private final String operation;
    private final int cars;
    private final String password;
    private String gameId;

    private World world;

    private boolean draw;

    public BotRunner(MessageReceiver receiver, MessageSender sender,
            Driver driver, String botName, String botKey, String track,
            String operation, int cars, String password, boolean draw) {
        this.receiver = receiver;
        this.sender = sender;
        this.driver = driver;
        this.botName = botName;
        this.botKey = botKey;
        this.track = track;
        this.world = new World(driver, botName);
        this.gameId = "";
        this.operation = operation;
        this.cars = cars;
        this.password = password;
        this.draw = draw;
    }

    public void run() throws Throwable {

        initializeSequence();

        PlotterView plotter = null;
        if (this.draw)
            plotter = new PlotterView(world);

        Message<?> message = null;

        boolean switchSent = false;
        TrackPieces nextLaneSelectionPoint = world.getTrackModel()
                .getNextSwitch();
        while ((message = this.receiver.waitForMessage()) != null
                && !tournamentEnd(message)) {

            updateWorld(message);

            // TODO: move all estimators to the Driver
            if (world.myPhysics.getAccelerationEstimator() != null) {
                if (!this.driver.slipEstimatorSet()) {
                    this.driver.setSlipEstimatingBehaviour(world.myPhysics
                            .getAccelerationEstimator());
                }
            }

            if (this.draw)
                plotter.plot();

            // TODO: clean up
            if (world.isInitialized()) {
                boolean sendPing = true;

                String direction = this.driver.getLane(world);
                if (direction != null) {
                    if (!switchSent) {
                        this.sender.sendMessage(new SwitchLaneMsg(direction));
                        switchSent = true;
                        sendPing = false;
                        continue;
                    } else if (!nextLaneSelectionPoint.equals(world
                            .getTrackModel().getNextSwitch())) {
                        switchSent = false;
                        nextLaneSelectionPoint = world.getTrackModel()
                                .getNextSwitch();
                    }
                }

                Boolean turbo = this.driver.launchTurbo(world);
                if (turbo != null) {
                    this.sender.sendMessage(new LaunchTurboMsg());
                    sendPing = false;
                    world.clearTurbo();
                }

                // TODO: change the behavirous not to flooed with thorttle
                // messages if there is no need to change. Send the ping instead
                // if required
                Double throttle = this.driver.getThrottle(world);
                if (throttle != null) {
                    // Let's update physics with throttle
                    world.myPhysics.setThrottle(throttle);

                    // Let's check what physics think about coefficients
                    // System.out.println("Angle Friction: "
                    // + world.myPhysics.getApproxCarAngleFriction());

                    this.sender.sendMessage(new ThrottleMsg(throttle));
                    sendPing = false;
                }

                // Send ping message to server if nothing else is sent.
                if (sendPing) {
                    this.sender.sendMessage(new PingMsg());
                }

            }
        }

        shutdown();

    }

    private void initializeSequence() throws IOException {
        if (this.operation.equals("create")) {
            System.out.println("Create new race @" + this.track + ", cars: "
                    + this.cars);
            this.sender.sendMessage(new CreateRaceMsg(this.botName,
                    this.botKey, this.track, this.password, this.cars));

        } else if (this.operation.equals("join")) {
            System.out.println("Join race @" + this.track + ", cars: "
                    + this.cars);
            this.sender.sendMessage(new JoinRaceMsg(this.botName, this.botKey,
                    this.track, this.password, this.cars));
        } else if (this.operation.equals("defaultjoin")) {
            System.out.println("Join race!");
            this.sender.sendMessage(new JoinMsg(this.botName, this.botKey));
        }

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
            if (!this.gameId.equals("")) {
                this.gameId = ((CarPositionsMsg) message).gameId;
            }
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
        case gameEnd:
            this.world.update((GameEndMsg) message);
            break;
        case turboAvailable:
            this.world.update((TurboAvailableMsg) message);
            break;
        // Not in techspec!
        case turboStart:
            this.world.update((TurboStartMsg) message);
            break;
        case turboEnd:
            this.world.update((TurboEndMsg) message);
            break;

        default:
            break;
        }
    }

    public static void main(String... args) throws Throwable {
        String track = extractArg(args, "track", "");
        String operation = extractArg(args, "operation", "defaultjoin");
        int cars = Integer.parseInt(extractArg(args, "cars", "1"));
        String password = extractArg(args, "password", "topsecret");
        String draw = extractArg(args, "draw", "no");

        String host = args[0];
        int port = Integer.parseInt(args[1]);
        String botName = args[2];
        String botKey = args[3];

        System.out.println("Draw: " + draw);
        System.out.println("Connecting to " + host + ":" + port + " as "
                + botName + "/" + botKey + " @ " + track);

        // Driver myBot = new ConstantThrottleBot(0.9);
        // Driver myBot = new SlowBot();
        Driver myBot = new MarkusBot();
        final Socket socket = new Socket(host, port);
        BotRunner runner = new BotRunner(new MessageReceiver(socket),
                new MessageSender(socket), myBot, botName, botKey, track,
                operation, cars, password, "yes".equals(draw));

        runner.run();
    }

    private static String extractArg(String[] args, String name, String defaultV) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].contains(name + "=")) {
                return args[i].substring(args[i].indexOf("=") + 1);
            }
        }

        return defaultV;
    }
}
