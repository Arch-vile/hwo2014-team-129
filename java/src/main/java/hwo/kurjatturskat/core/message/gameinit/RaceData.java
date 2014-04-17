package hwo.kurjatturskat.core.message.gameinit;


public class RaceData {
    public final RaceSession raceSession;
    public final Track track;
    public final Car cars[];

    public RaceData(RaceSession raceSession, Track track, Car cars[]) {
        this.raceSession = raceSession;
        this.track = track;
        this.cars = cars;
    }

}
