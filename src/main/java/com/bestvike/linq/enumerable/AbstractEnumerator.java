package com.bestvike.linq.enumerable;

import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.exception.ThrowHelper;

/**
 * 迭代器,初始 state 为 0
 * <p>
 * Created by 许崇雷 on 2017-07-12.
 */
public abstract class AbstractEnumerator<TSource> implements IEnumerator<TSource> {
    protected int state;
    protected TSource current;
    private boolean checkedNext;
    private boolean hasNext;

    @Override
    public abstract boolean moveNext();

    @Override
    public TSource current() {
        return this.current;
    }

    @Override
    public boolean hasNext() {
        if (!this.checkedNext) {
            this.hasNext = this.moveNext();
            this.checkedNext = true;
        }
        return this.hasNext;
    }

    @Override
    public TSource next() {
        if (this.hasNext()) {
            this.checkedNext = false;
            return this.current();
        }
        ThrowHelper.throwNoSuchElementException();
        return null;
    }

    @Override
    public void reset() {
        ThrowHelper.throwNotSupportedException();
    }

    @Override
    public void close() {
        this.current = null;
        this.state = -1;
    }
}
