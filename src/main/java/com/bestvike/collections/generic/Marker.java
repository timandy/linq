package com.bestvike.collections.generic;

/**
 * Created by 许崇雷 on 2017-09-30.
 */
public final class Marker {//struct
    private final int count;
    private final int index;

    public Marker(int count, int index) {
        assert count >= 0;
        assert index >= 0;

        this.count = count;
        this.index = index;
    }

    public int getCount() {
        return this.count;
    }

    public int getIndex() {
        return this.index;
    }

    @Override
    public String toString() {
        return String.format("index: %s, count: %s", this.index, this.count);
    }
}
