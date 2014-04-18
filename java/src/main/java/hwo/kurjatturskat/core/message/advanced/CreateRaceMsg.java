package hwo.kurjatturskat.core.message.advanced;

import hwo.kurjatturskat.core.message.Message;
import hwo.kurjatturskat.core.message.MessageType;

public class CreateRaceMsg extends Message<RaceData> {

    public CreateRaceMsg(String name, String key, String trackName,
            String password, int carCount) {
        super(MessageType.createRace, new RaceData(name, key, trackName,
                password, carCount));
    }
}
