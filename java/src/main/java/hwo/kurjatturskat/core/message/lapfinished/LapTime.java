package hwo.kurjatturskat.core.message.lapfinished;

import java.util.concurrent.TimeUnit;

public class LapTime {
    public int lap;
    public int ticks;
    public int millis;

    public LapTime(int lap, int ticks, int millis) {
        this.lap = lap;
        this.ticks = ticks;
        this.millis = millis;
    }

    public String getLapTime() {
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis
                - TimeUnit.MINUTES.toMillis(minutes));
        long millisLeft = millis - TimeUnit.MINUTES.toMillis(minutes)
                - TimeUnit.SECONDS.toMillis(seconds);

        String time = String.format("%02d:%02d:%03d", minutes, seconds,
                millisLeft);
        return time;
    }

}
