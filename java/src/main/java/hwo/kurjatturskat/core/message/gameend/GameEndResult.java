package hwo.kurjatturskat.core.message.gameend;

import java.util.concurrent.TimeUnit;

public class GameEndResult {
    public final int laps;
    public final long ticks;
    public final long millis;

    public GameEndResult(int laps, long ticks, long millis) {
        this.laps = laps;
        this.ticks = ticks;
        this.millis = millis;
    }

    public String getRaceTime() {
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
