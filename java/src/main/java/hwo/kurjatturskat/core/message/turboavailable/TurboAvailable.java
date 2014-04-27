package hwo.kurjatturskat.core.message.turboavailable;

public class TurboAvailable {
    public double turboDurationMilliseconds;
    public long turboDurationTicks;
    public double turboFactor;

    public TurboAvailable(double t, long d, double tf) {
        this.turboDurationMilliseconds = t;
        this.turboDurationTicks = d;
        this.turboFactor = tf;
    }
}
