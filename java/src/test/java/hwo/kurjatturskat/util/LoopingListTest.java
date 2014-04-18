package hwo.kurjatturskat.util;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class LoopingListTest {

    LoopingList<Integer> list;

    @Before
    public void init() {
        List<Integer> ints = Arrays.asList(new Integer[] { 1, 2, 3, 4 });
        this.list = new LoopingList<Integer>(ints);
    }

    @Test
    public void after_init_should_give_first_element() {
        assertThat(list.getCurrent(), is(1));
    }

    @Test
    public void next() {
        assertThat(list.getNext(), is(2));
    }

    @Test
    public void next_loops() {
        list.setCurrent(3);
        assertThat(list.getNext(), is(1));
    }

    @Test
    public void setcurrent() {
        assertThat(list.setCurrent(2), is(3));
        assertThat(list.getCurrent(), is(3));
    }

    @Test
    public void setcurrent_loops() {
        assertThat(list.setCurrent(5), is(2));
        assertThat(list.getCurrent(), is(2));
    }

    @Test
    public void advance_increase_index_by_one() {
        assertThat(list.advance(), is(2));
        assertThat(list.getCurrent(), is(2));
    }

}
