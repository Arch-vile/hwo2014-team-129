package hwo.kurjatturskat.core.message.gameinit;

public class RaceSession {
    public final int laps;
    public final int maxLapTimeMs;
    public final boolean quickRace;
    public final int durationMs;

    /*
     * Race
     */
    public RaceSession(int laps, int maxLapTimeMs, boolean quickRace,
            int durationMs) {
        this.laps = laps;
        this.maxLapTimeMs = maxLapTimeMs;
        this.quickRace = quickRace;
        this.durationMs = durationMs;
    }

    /*
     * Qualifying
     */
    public RaceSession(int durationMs) {
        this.laps = 0;
        this.maxLapTimeMs = 0;
        this.quickRace = false;
        this.durationMs = durationMs;
    }

    public boolean isQualifying() {
        if (this.durationMs > 0)
            return true;
        return false;
    }
}
