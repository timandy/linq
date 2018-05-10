package com.bestvike.linq.iterator;

import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.enumerator.AbstractEnumerator;

/**
 * 迭代对象,初始 state 为 1
 * <p>
 * Created by 许崇雷 on 2017/7/13.
 */
public abstract class AbstractIterator<TSource> extends AbstractEnumerator<TSource> implements IEnumerable<TSource> {
    private final long threadId;

    public AbstractIterator() {
        this.threadId = Thread.currentThread().getId();
    }

    @Override
    public abstract AbstractIterator<TSource> clone();

    @Override
    public IEnumerator<TSource> enumerator() {
        AbstractIterator<TSource> enumerator = (this.state == 0 && this.threadId == Thread.currentThread().getId()) ? this : this.clone();
        enumerator.state = 1;
        return enumerator;
    }
}
