package com.bestvike.linq.entity;

import java.util.Iterator;

/**
 * @author 许崇雷
 * @date 2017/7/28
 */
public final class IteratorDemo implements Iterator<String> {
    private final int count;
    private int current = 0;

    public IteratorDemo(int count) {
        this.count = count;
    }

    @Override
    public boolean hasNext() {
        return this.current < this.count;
    }

    @Override
    public String next() {
        this.current++;
        return String.valueOf(this.current);
    }
}
