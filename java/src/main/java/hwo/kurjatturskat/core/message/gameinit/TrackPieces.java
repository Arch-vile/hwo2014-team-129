package hwo.kurjatturskat.core.message.gameinit;

import com.google.gson.annotations.SerializedName;

public class TrackPieces {
    public final Double length;
    public final Double radius;
    public final Double angle;

    @SerializedName("switch")
    public final boolean isSwitch;

    public TrackPieces(Double length, Double radius, Double angle,
            boolean isSwitch) {
        this.length = length;
        this.radius = radius;
        this.angle = angle;
        this.isSwitch = isSwitch;
    }

    public boolean isCurve() {
        return this.angle != null;
    }
}
