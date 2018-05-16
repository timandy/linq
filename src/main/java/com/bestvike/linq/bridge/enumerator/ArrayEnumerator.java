package com.bestvike.linq.bridge.enumerator;

import com.bestvike.linq.enumerable.AbstractEnumerator;

/**
 * Created by 许崇雷 on 2017/7/13.
 */
public final class ArrayEnumerator<TSource> extends AbstractEnumerator<TSource> {
    private final Object[] source;
    private final int endIndex;
    private int index;

    public ArrayEnumerator(Object[] source) {
        this(source, 0, source.length);
    }

    public ArrayEnumerator(Object[] source, int index, int count) {
        this.source = source;
        this.index = index;
        this.endIndex = Math.addExact(index, count);
    }

    @Override
    public boolean moveNext() {
        switch (this.state) {
            case 0:
                if (this.index < this.endIndex) {
                    //noinspection unchecked
                    this.current = (TSource) this.source[this.index++];
                    return true;
                }
                this.close();
                return false;
            default:
                return false;
        }
    }
}
