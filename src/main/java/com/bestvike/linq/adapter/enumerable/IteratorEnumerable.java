package com.bestvike.linq.adapter.enumerable;

import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.adapter.enumerator.IteratorEnumerator;
import com.bestvike.linq.exception.ThrowHelper;

import java.util.Iterator;

/**
 * Created by 许崇雷 on 2019-07-01.
 */
public final class IteratorEnumerable<TSource> implements IEnumerable<TSource> {
    private final Iterator<TSource> source;
    private boolean called;

    public IteratorEnumerable(Iterator<TSource> source) {
        this.source = source;
    }

    @Override
    public IEnumerator<TSource> enumerator() {
        if (this.called)
            ThrowHelper.throwNotSupportedException();
        this.called = true;
        return new IteratorEnumerator<>(this.source);
    }
}
