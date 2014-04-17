package hwo.kurjatturskat.core.message.gameinit;

public class Track {
    public final String id;
    public final String name;
    public final TrackPieces pieces[];
    public final TrackLanes lanes[];

    public Track(String id, String name, TrackPieces pieces[],
            TrackLanes lanes[]) {
        this.id = id;
        this.name = name;
        this.pieces = pieces;
        this.lanes = lanes;
    }
}
