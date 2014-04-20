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

    @Before
    public void setup() {
        List<TrackPieces> pieces = new ArrayList<>();
        pieces.add(new TrackPieces(110d, 0d, 0d, false)); // First straight 110
        pieces.add(new TrackPieces(0d, 200d, 90d, false)); // curve
                                                           // right
                                                           // angle=90
        pieces.add(new TrackPieces(0d, 200d, 90d, true)); // curve back
                                                          // right angle=90
                                                          // with switch
        pieces.add(new TrackPieces(0d, 200d, -90d, false)); // curve left angle
                                                            // 90
        pieces.add(new TrackPieces(220d, 0d, 0d, true)); // straight 220 with
                                                         // switch

        model = new TrackModel(pieces.toArray(new TrackPieces[pieces.size()]),
                null, null, null, null);
    }

    @Test
    public void getNextSwitch_returns_next_when_on_non_switch() {
        model.setMyCurrentTrackPiece(0);
        assertThat(model.getNextSwitch(), is(2));
    }

    @Test
    public void getNextSwitch_returns_next_when_on_switch() {
        model.setMyCurrentTrackPiece(2);
        assertThat(model.getNextSwitch(), is(4));
    }

    @Test
    public void getNextSwitch_works_if_only_one_switch_on_track() {
        model.setMyCurrentTrackPiece(0);
        model.getAll().remove(4);
        assertThat(model.getNextSwitch(), is(2));
    }

    @Test
    public void getNextSwitch_works_on_consequent_switches() {
        model.getAll().add(3, new TrackPieces(0d, 200d, 90d, true));
        model.setMyCurrentTrackPiece(2);
        assertThat(model.getNextSwitch(), is(3));

    }

}
