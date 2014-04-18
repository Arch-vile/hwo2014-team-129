package hwo.kurjatturskat.core.message;

public class MessageCarData extends CarIdentifier {

    public final String gameId;
    public final int gameTick;

    public MessageCarData(String name, String color, String gameId, int gameTick) {
        super(name, color);
        this.gameId = gameId;
        this.gameTick = gameTick;
    }
}
