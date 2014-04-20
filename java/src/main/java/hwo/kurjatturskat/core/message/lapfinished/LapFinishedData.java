package hwo.kurjatturskat.core.message.lapfinished;

import hwo.kurjatturskat.core.message.CarIdentifier;

public class LapFinishedData {
    public final CarIdentifier car;
    public final LapTime lapTime;
    public final RaceTime raceTime;
    public final Ranking ranking;

    public LapFinishedData(CarIdentifier car, LapTime lapTime,
            RaceTime raceTime, Ranking ranking) {
        this.car = car;
        this.lapTime = lapTime;
        this.raceTime = raceTime;
        this.ranking = ranking;
    }
}
