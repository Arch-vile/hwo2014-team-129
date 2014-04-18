package hwo.kurjatturskat.core.message.gameinit;

public class TrackPieces {
    public final Double length;
    public final Double radius;
    public final Double angle;

    public TrackPieces(Double length, Double radius, Double angle) {
        this.length = length;
        this.radius = radius;
        this.angle = angle;
    }

    public boolean isCurve() {
        return this.angle != null;
    }
}
