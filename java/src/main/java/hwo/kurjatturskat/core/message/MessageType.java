package hwo.kurjatturskat.core.message;

public enum MessageType {

    // NOTE: Enums must be in small letter for json to be outputted correctly
    carPositions("carPositions"), yourCar("yourCar"), join("join"), gameInit(
            "gameInit"), gameStart("gameStart"), throttle("throttle"), switchLane(
            "switchLane"), unknown("unknown"), lapFinished("lapFinished"), spawn(
            "spawn"), crash("crash"), finish("finish"), gameEnd("gameEnd");

    private final String type;

    MessageType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public static MessageType get(String type) {
        for (MessageType enumType : MessageType.values()) {
            if (enumType.getType().equals(type)) {
                return enumType;
            }
        }
        return unknown;
    }

}
