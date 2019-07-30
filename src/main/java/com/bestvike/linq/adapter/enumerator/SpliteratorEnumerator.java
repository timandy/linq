package com.bestvike.linq.adapter.enumerator;

import com.bestvike.linq.enumerable.AbstractEnumerator;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;

/**
 * Created by 许崇雷 on 2019-07-30.
 */
public final class SpliteratorEnumerator<TSource> extends AbstractEnumerator<TSource> {
    private final Spliterator<TSource> source;
    private Iterator<TSource> iterator;

    public SpliteratorEnumerator(Spliterator<TSource> source) {
        this.source = source;
    }

    @Override
    public boolean moveNext() {
        switch (this.state) {
            case 0:
                this.iterator = Spliterators.iterator(this.source);
                this.state = 1;
            case 1:
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
