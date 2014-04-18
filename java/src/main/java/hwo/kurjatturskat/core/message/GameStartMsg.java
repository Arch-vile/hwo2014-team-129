package hwo.kurjatturskat.core.message;

public class GameStartMsg extends Message<StupidData> {
    public final String gameId;
    public final long gameTick;

    public GameStartMsg(StupidData data, String gameId, long gameTick) {
        super(MessageType.gameStart, data);
        this.gameId = gameId;
        this.gameTick = gameTick;
    }
}
