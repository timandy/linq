package com.bestvike.linq.adapter.enumerable;

import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.adapter.enumerator.StreamEnumerator;
import com.bestvike.linq.exception.ThrowHelper;

import java.util.stream.Stream;

/**
 * Created by 许崇雷 on 2019-07-30.
 */
public final class StreamEnumerable<TSource> implements IEnumerable<TSource> {
    private final Stream<TSource> source;
    private boolean called;

    public StreamEnumerable(Stream<TSource> source) {
        this.source = source;
    }

    @Override
    public IEnumerator<TSource> enumerator() {
        if (this.called)
            ThrowHelper.throwNotSupportedException();
        this.called = true;
        return new StreamEnumerator<>(this.source);
    }
}
