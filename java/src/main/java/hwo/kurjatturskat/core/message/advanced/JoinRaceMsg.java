package hwo.kurjatturskat.core.message.advanced;

import hwo.kurjatturskat.core.message.Message;
import hwo.kurjatturskat.core.message.MessageType;

public class JoinRaceMsg extends Message<RaceData> {

    public JoinRaceMsg(String name, String key, String trackName,
            String password, int carCount) {
        super(MessageType.joinRace, new RaceData(name, key, trackName,
                password, carCount));
    }

}
