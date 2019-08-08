package com.bestvike.linq.adapter.enumerable;

import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.adapter.enumerator.SpliteratorEnumerator;
import com.bestvike.linq.exception.ThrowHelper;

import java.util.Spliterator;

/**
 * Created by 许崇雷 on 2019-07-30.
 */
public final class SpliteratorEnumerable<TSource> implements IEnumerable<TSource> {
    private final Spliterator<TSource> source;
    private boolean called;

    public SpliteratorEnumerable(Spliterator<TSource> source) {
        this.source = source;
    }

    @Override
    public IEnumerator<TSource> enumerator() {
        if (this.called)
            ThrowHelper.throwRepeatInvokeException();
        this.called = true;
        return new SpliteratorEnumerator<>(this.source);
    }
}
