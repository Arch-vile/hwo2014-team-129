package hwo.kurjatturskat.util;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class LoopingListTest {

    LoopingList<String> list;

    @Before
    public void init() {
        List<String> numbers = Arrays.asList(new String[] { "first", "second",
                "third", "fourth" });
        this.list = new LoopingList<String>(numbers);
    }

    @Test
    public void after_init_should_give_first_element() {
        assertThat(list.getCurrent(), is("first"));
    }

    @Test
    public void next() {
        assertThat(list.getNext(), is("second"));
    }

    @Test
    public void next_loops() {
        list.setCurrent(3);
        assertThat(list.getNext(), is("first"));
    }

    @Test
    public void setcurrentByIndex() {
        assertThat(list.setCurrent(2), is("third"));
        assertThat(list.getCurrent(), is("third"));
    }

    @Test
    public void setcurrent() {
        assertThat(list.setCurrent("second"), is("second"));
        assertThat(list.getCurrent(), is("second"));
    }

    @Test
    public void setcurrent_loops() {
        assertThat(list.setCurrent(5), is("second"));
        assertThat(list.getCurrent(), is("second"));
    }

    @Test
    public void advance_increase_index_by_one() {
        assertThat(list.advance(), is("second"));
        assertThat(list.getCurrent(), is("second"));
    }

}
