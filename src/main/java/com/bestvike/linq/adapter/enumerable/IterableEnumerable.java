package com.bestvike.linq.adapter.enumerable;

import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.adapter.enumerator.IterableEnumerator;

/**
 * Created by 许崇雷 on 2017-07-17.
 */
public final class IterableEnumerable<TSource> implements IEnumerable<TSource> {
    private final Iterable<TSource> source;

    public IterableEnumerable(Iterable<TSource> source) {
        this.source = source;
    }

    @Override
    public IEnumerator<TSource> enumerator() {
        return new IterableEnumerator<>(this.source);
    }
}
