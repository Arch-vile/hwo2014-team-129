package hwo.kurjatturskat.util;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.la4j.vector.Vector;
import org.la4j.vector.dense.BasicVector;

public class PhysicsTest {

    Physics physics = new Physics(null, null, null);

    @Before
    public void setup() {

    }

    @Test
    public void dragAndFriction_tracks_avarage_values() {

        Vector first = new BasicVector(new double[] { 1, 2 });
        Vector second = new BasicVector(new double[] { 7, 11 });
        Vector third = new BasicVector(new double[] { 5, 1 });

        physics.addNewestSolutionToApproximation(first);
        assertThat(physics.getApproximateDrag(), is(1d));
        assertThat(physics.getApproximateRollingFriction(), is(2d));

        physics.addNewestSolutionToApproximation(second);
        assertThat(physics.getApproximateDrag(), is(4d));
        assertThat(physics.getApproximateRollingFriction(), is(6.5d));

        physics.addNewestSolutionToApproximation(third);
        assertThat(physics.getApproximateDrag(), is(13d / 3d));
        assertThat(physics.getApproximateRollingFriction(), is(14d / 3d));
    }
}
