package hwo.kurjatturskat.core.message.lapfinished;

public class LapTime {
    public final int lap;
    public final int ticks;
    public final int millis;

    public LapTime(int lap, int ticks, int millis) {
        this.lap = lap;
        this.ticks = ticks;
        this.millis = millis;
    }
}
