package com.bestvike.linq.enumerator;

import com.bestvike.collections.generic.Array;

/**
 * Created by 许崇雷 on 2017/7/13.
 */
public final class ArrayEnumerator<TSource> extends AbstractEnumerator<TSource> {
    private final Array<TSource> source;
    private final int endIndex;
    private int index;

    public ArrayEnumerator(Array<TSource> source) {
        this(source, 0, source.length());
    }

    public ArrayEnumerator(Array<TSource> source, int index, int count) {
        this.source = source;
        this.index = index;
        this.endIndex = Math.addExact(index, count);
    }

    @Override
    public boolean moveNext() {
        switch (this.state) {
            case 0:
                if (this.index < this.endIndex) {
                    this.current = this.source.get(this.index++);
                    return true;
                }
                this.close();
                return false;
            default:
                return false;
        }
    }
}
