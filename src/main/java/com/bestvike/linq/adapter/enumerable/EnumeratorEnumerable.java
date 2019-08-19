package com.bestvike.linq.adapter.enumerable;

import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.exception.ThrowHelper;

/**
 * Created by 许崇雷 on 2019-08-19.
 */
public final class EnumeratorEnumerable<TSource> implements IEnumerable<TSource> {
    private final IEnumerator<TSource> source;
    private boolean called;

    public EnumeratorEnumerable(IEnumerator<TSource> source) {
        this.source = source;
    }

    @Override
    public IEnumerator<TSource> enumerator() {
        if (this.called)
            ThrowHelper.throwRepeatInvokeException();
        this.called = true;
        return this.source;
    }
}
