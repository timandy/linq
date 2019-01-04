package com.bestvike.linq.bridge.enumerable;

import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.bridge.enumerator.IterableEnumerator;

/**
 * Created by 许崇雷 on 2017-07-17.
 */
public final class IterableEnumerable<TElement> implements IEnumerable<TElement> {
    private final Iterable<TElement> source;

    public IterableEnumerable(Iterable<TElement> source) {
        this.source = source;
    }

    @Override
    public IEnumerator<TElement> enumerator() {
        return new IterableEnumerator<>(this.source);
    }
}
