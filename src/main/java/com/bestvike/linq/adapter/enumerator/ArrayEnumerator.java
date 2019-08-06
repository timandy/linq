package com.bestvike.linq.adapter.enumerator;

import com.bestvike.linq.enumerable.AbstractEnumerator;

/**
 * Created by 许崇雷 on 2017-07-13.
 */
public final class ArrayEnumerator<TSource> extends AbstractEnumerator<TSource> {
    private final Object[] source;
    private final int startIndex;
    private final int endIndex;

    public ArrayEnumerator(Object[] source) {
        this(source, 0, source.length);
    }

    public ArrayEnumerator(Object[] source, int startIndex, int count) {
        this.source = source;
        this.startIndex = startIndex;
        this.endIndex = startIndex + count;
    }

    @Override
    public boolean moveNext() {
        if (this.state == -1)
            return false;
        int index = this.state == 0 ? this.startIndex : this.state;
        if (index < this.endIndex) {
            //noinspection unchecked
            this.current = (TSource) this.source[index];
            this.state = index + 1;
            return true;
        }
        this.close();
        return false;
    }
}
