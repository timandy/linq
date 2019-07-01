package com.bestvike.linq.adapter.enumerable;

import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.adapter.enumerator.EnumerationEnumerator;
import com.bestvike.linq.exception.ThrowHelper;

import java.util.Enumeration;

/**
 * Created by 许崇雷 on 2019-07-01.
 */
public final class EnumerationEnumerable<TSource> implements IEnumerable<TSource> {
    private final Enumeration<TSource> source;
    private boolean called;

    public EnumerationEnumerable(Enumeration<TSource> source) {
        this.source = source;
    }

    @Override
    public IEnumerator<TSource> enumerator() {
        if (this.called)
            ThrowHelper.throwNotSupportedException();
        this.called = true;
        return new EnumerationEnumerator<>(this.source);
    }
}
