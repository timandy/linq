package com.bestvike.linq.entity;

import java.util.Iterator;

/**
 * Created by 许崇雷 on 2017/7/28.
 */
public final class IterableDemo implements Iterable<Long> {
    private final long count;

    public IterableDemo(long count) {
        this.count = count;
    }

    @Override
    public Iterator<Long> iterator() {
        return new IteratorDemo(this.count);
    }
}
