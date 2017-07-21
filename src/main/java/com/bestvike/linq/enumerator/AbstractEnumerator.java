package com.bestvike.linq.enumerator;

import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.exception.Errors;

/**
 * 迭代器,初始 state 为 0
 *
 * @author 许崇雷
 * @date 2017/7/12
 */
public abstract class AbstractEnumerator<TSource> implements IEnumerator<TSource> {
    private boolean checkedNext;
    private boolean hasNext;
    protected int state;
    protected TSource current;

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
        throw Errors.noSuchElement();
    }

    @Override
    public void reset() {
        throw Errors.notSupported();
    }

    @Override
    public void close() {
        this.current = null;
        this.state = -1;
    }
}
