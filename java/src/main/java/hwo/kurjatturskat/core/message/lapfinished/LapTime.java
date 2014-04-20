package hwo.kurjatturskat.core.message.lapfinished;

public class LapTime {
    public int lap;
    public int ticks;
    public int millis;

    public LapTime() {
        this.lap = -1;
        this.ticks = -1;
        this.millis = -1;
    }

    public LapTime(int lap, int ticks, int millis) {
        this.lap = lap;
        this.ticks = ticks;
        this.millis = millis;
    }

    public void update(LapTime lapTime) {
        this.lap = lapTime.lap;
        this.ticks = lapTime.ticks;
        this.millis = lapTime.millis;
    }
}
