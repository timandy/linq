package com.bestvike.linq.enumerator;

import java.util.Iterator;

/**
 * @author 许崇雷
 * @date 2017/7/17
 */
public final class IterableEnumerator<TSource> extends AbstractEnumerator<TSource> {
    private final Iterable<TSource> source;
    private Iterator<TSource> iterator;

    public IterableEnumerator(Iterable<TSource> source) {
        this.source = source;
    }

    @Override
    public boolean moveNext() {
        switch (this.state) {
            case 1:
                this.iterator = this.source.iterator();
                this.state = 2;
            case 2:
                if (this.iterator.hasNext()) {
                    this.current = this.iterator.next();
                    return true;
                }
                this.close();
                return false;
            default:
                return false;
        }
    }

    @Override
    public void close() {
        this.iterator = null;
        super.close();
    }
}
