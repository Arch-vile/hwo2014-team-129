package hwo.kurjatturskat.core.message.lapfinished;

public class RaceTime {
    public int laps;
    public int ticks;
    public int millis;

    public RaceTime() {
        this.laps = 0;
        this.ticks = 0;
        this.millis = 0;
    }

    public RaceTime(int laps, int ticks, int millis) {
        this.laps = laps;
        this.ticks = ticks;
        this.millis = millis;
    }

    public int getCurrentLap() {
        return this.laps + 1;
    }

    public void update(RaceTime data) {
        this.laps = data.laps;
        this.ticks = data.ticks;
        this.millis = data.millis;

    }
}
