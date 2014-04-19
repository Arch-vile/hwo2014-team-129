package hwo.kurjatturskat.util;

import java.util.List;

public class LoopingList<T> {

    private int index;
    private List<T> items;

    public LoopingList(List<T> items) {
        this.items = items;
        this.index = 0;
    }

    public T getCurrent() {
        return this.items.get(this.index);
    }

    public T setCurrent(int index) {
        this.index = normalize(index);
        return getCurrent();
    }

    public T getNext() {
        return this.items.get(nextIndex());
    }

    public T advance() {
        this.index = nextIndex();
        return getCurrent();
    }

    private int nextIndex() {
        return normalize(index + 1);
    }

    private int normalize(int index) {
        return index % this.items.size();
    }

    public T getByIndex(int index) {
        return this.items.get(this.normalize(index));
    }

    public List<T> getAll() {
        return this.items;
    }

}
