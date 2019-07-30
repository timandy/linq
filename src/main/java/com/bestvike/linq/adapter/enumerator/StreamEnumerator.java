package com.bestvike.linq.adapter.enumerator;

import com.bestvike.linq.enumerable.AbstractEnumerator;

import java.util.Iterator;
import java.util.stream.Stream;

/**
 * Created by 许崇雷 on 2019-07-30.
 */
public final class StreamEnumerator<TSource> extends AbstractEnumerator<TSource> {
    private final Stream<TSource> source;
    private Iterator<TSource> iterator;

    public StreamEnumerator(Stream<TSource> source) {
        this.source = source;
    }

    @Override
    public boolean moveNext() {
        switch (this.state) {
            case 0:
                this.iterator = this.source.iterator();
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
