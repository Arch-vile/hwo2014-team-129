package hwo.kurjatturskat.core.message;

public class GameStartMsg extends Message<StupidData> {

    public GameStartMsg(StupidData data, String gameId, Long gameTick) {
        super(MessageType.gameStart, data, gameId, gameTick);
    }
}
