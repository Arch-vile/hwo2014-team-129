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

    // TODO: add isStraight
    public boolean isCurve() {
        return this.angle != null;
    }

    public boolean isCurveRight() {
        if (this.angle > 0)
            return true;
        return false;
    }

    public boolean isCurveLeft() {
        if (this.angle < 0)
            return true;
        return false;
    }

    public String curveString() {
        if (this.isCurve()) {
            if (this.angle < 0)
                return "Left";
            else if (this.angle > 0)
                return "Right";
        }
        return "Straight";
    }

}
