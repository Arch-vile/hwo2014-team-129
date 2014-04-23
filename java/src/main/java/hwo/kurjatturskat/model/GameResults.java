package hwo.kurjatturskat.model;

import hwo.kurjatturskat.core.message.gameend.GameEndBestLaps;
import hwo.kurjatturskat.core.message.gameend.GameEndData;
import hwo.kurjatturskat.core.message.gameend.GameEndResults;

public class GameResults {
    private String phase;
    private GameEndData gameResults;

    public GameResults(String phase) {
        this.phase = phase;
        this.gameResults = null;
    }

    public GameResults(GameEndData results) {
        this.gameResults = results;
    }

    public GameEndResults[] getGameEndResults() {
        return this.gameResults.results;
    }

    public GameEndBestLaps[] getGameEndBestLaps() {
        return this.gameResults.bestLaps;
    }

}
