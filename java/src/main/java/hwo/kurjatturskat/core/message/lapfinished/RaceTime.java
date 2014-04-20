package hwo.kurjatturskat.core.message.lapfinished;

public class RaceTime {
    public final int laps;
    public final int ticks;
    public final int millis;

    public RaceTime(int laps, int ticks, int millis) {
        this.laps = laps;
        this.ticks = ticks;
        this.millis = millis;
    }

}
