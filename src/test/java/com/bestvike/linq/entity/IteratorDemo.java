package com.bestvike.linq.entity;

import java.util.Iterator;

/**
 * Created by 许崇雷 on 2017/7/28.
 */
public final class IteratorDemo implements Iterator<Long> {
    private final long count;
    private long current = 0;

    IteratorDemo(long count) {
        this.count = count;
    }

    @Override
    public boolean hasNext() {
        return this.current < this.count;
    }

    @Override
    public Long next() {
        this.current++;
        return this.current;
    }
}
