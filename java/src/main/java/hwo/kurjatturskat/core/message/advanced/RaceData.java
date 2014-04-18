package hwo.kurjatturskat.core.message.advanced;

import hwo.kurjatturskat.core.message.JoinData;

public class RaceData {
    public final JoinData botId;
    public final String trackName;
    public final String password;
    public final int carCount;

    public RaceData(String name, String key, String trackName, String password,
            int carCount) {
        this.botId = new JoinData(name, key);
        this.trackName = trackName;
        this.password = password;
        this.carCount = carCount;
    }
}
