package com.bestvike.linq.enumerable;

import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.exception.ExceptionArgument;
import com.bestvike.linq.exception.ThrowHelper;

/**
 * Created by 许崇雷 on 2018-05-11.
 */
public final class RunOnce {
    private RunOnce() {
    }

    public static <TSource> IEnumerable<TSource> runOnce(IEnumerable<TSource> source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        return new RunOnceEnumerable<>(source);
    }
}


final class RunOnceEnumerable<TSource> implements IEnumerable<TSource> {
    private final IEnumerable<TSource> source;
    private boolean called;

    RunOnceEnumerable(IEnumerable<TSource> source) {
        this.source = source;
    }

    @Override
    public IEnumerator<TSource> enumerator() {
        if (this.called)
            ThrowHelper.throwNotSupportedException();
        this.called = true;
        return this.source.enumerator();
    }
}
