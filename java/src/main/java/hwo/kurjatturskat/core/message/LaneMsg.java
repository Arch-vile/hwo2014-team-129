package hwo.kurjatturskat.core.message;

public class LaneMsg {
    public final int startLaneIndex;
    public final int endLaneIndex;

    public LaneMsg(int startLaneIndex, int endLaneIndex) {
        this.startLaneIndex = startLaneIndex;
        this.endLaneIndex = endLaneIndex;
    }
}
