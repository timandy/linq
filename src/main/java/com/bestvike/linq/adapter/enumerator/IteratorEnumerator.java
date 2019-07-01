package com.bestvike.linq.adapter.enumerator;

import com.bestvike.linq.enumerable.AbstractEnumerator;

import java.util.Iterator;

/**
 * Created by 许崇雷 on 2019-07-01.
 */
public final class IteratorEnumerator<TSource> extends AbstractEnumerator<TSource> {
    private final Iterator<TSource> source;

    public IteratorEnumerator(Iterator<TSource> source) {
        this.source = source;
    }

    @Override
    public boolean moveNext() {
        switch (this.state) {
            case 0:
                if (this.source.hasNext()) {
                    this.current = this.source.next();
                    return true;
                }
                this.close();
                return false;
            default:
                return false;
        }
    }
}
