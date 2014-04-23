package hwo.kurjatturskat.core.message.gameend;

public class GameEndData {
    public final GameEndResults results[];
    public final GameEndBestLaps bestLaps[];

    public GameEndData(GameEndResults results[], GameEndBestLaps bestLaps[]) {
        this.results = results;
        this.bestLaps = bestLaps;
    }
}
