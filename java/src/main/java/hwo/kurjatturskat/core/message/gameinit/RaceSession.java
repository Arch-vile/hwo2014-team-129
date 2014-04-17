package hwo.kurjatturskat.core.message.gameinit;

public class RaceSession {
    public final int laps;
    public final int maxLapTimeMs;
    public final boolean quickRace;

    public RaceSession(int laps, int maxLapTimeMs, boolean quickRace) {
        this.laps = laps;
        this.maxLapTimeMs = maxLapTimeMs;
        this.quickRace = quickRace;
    }
}
