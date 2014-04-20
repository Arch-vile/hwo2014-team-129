package hwo.kurjatturskat.model;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import hwo.kurjatturskat.core.message.gameinit.TrackPieces;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class TrackModelTest {

    private TrackModel model;
    TrackPieces straight1 = new TrackPieces(110d, 0d, 0d, false);
    TrackPieces curveRight1 = new TrackPieces(0d, 200d, 90d, false);
    TrackPieces curveRight2 = new TrackPieces(0d, 200d, 90d, true);
    TrackPieces curveLeft1 = new TrackPieces(0d, 200d, -90d, false);
    TrackPieces straight2 = new TrackPieces(220d, 0d, 0d, true);

    @Before
    public void setup() {
        List<TrackPieces> pieces = new ArrayList<>();

        pieces.add(straight1);
        pieces.add(curveRight1);
        pieces.add(curveRight2);
        pieces.add(curveLeft1);
        pieces.add(straight2);

        model = new TrackModel(pieces.toArray(new TrackPieces[pieces.size()]),
                null, null, null, null);
    }

    @Test
    public void getNextSwitch_returns_next_when_on_non_switch() {
        model.setMyCurrentTrackPiece(straight1);
        assertThat(model.getNextSwitch(), is(curveRight2));
    }

    @Test
    public void getNextSwitch_returns_next_when_on_switch() {
        model.setMyCurrentTrackPiece(curveRight2);
        assertThat(model.getNextSwitch(), is(straight2));
    }

    @Test
    public void getNextSwitch_works_if_only_one_switch_on_track_in_past() {
        model.setMyCurrentTrackPiece(curveLeft1);
        model.getAll().remove(straight2);
        assertThat(model.getNextSwitch(), is(curveRight2));
    }

    @Test
    public void getNextSwitch_works_on_consequent_switches() {
        TrackPieces newSwitch = new TrackPieces(0d, 200d, 90d, true);
        model.getAll().add(3, newSwitch);
        model.setMyCurrentTrackPiece(curveRight2);
        assertThat(model.getNextSwitch(), is(newSwitch));

    }

}
