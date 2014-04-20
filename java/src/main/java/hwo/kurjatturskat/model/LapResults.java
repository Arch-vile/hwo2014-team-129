package hwo.kurjatturskat.model;

import hwo.kurjatturskat.core.message.gameinit.RaceSession;
import hwo.kurjatturskat.core.message.lapfinished.LapFinishedData;

import java.util.ArrayList;
import java.util.List;

public class LapResults {

    private RaceSession raceSession;
    private List<LapFinishedData> laps;

    public LapResults(RaceSession raceSession) {
        this.raceSession = raceSession;
        this.laps = new ArrayList<>();
    }

    public void lapFinished(LapFinishedData lapData) {
        this.laps.add(lapData);
    }

    public List<LapFinishedData> getLaps() {
        return this.laps;
    }

    public RaceSession getRaceSession() {
        return raceSession;
    }

}
