package com.bestvike.linq.bridge.enumerable;

import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.exception.Errors;

/**
 * Created by 许崇雷 on 2018-05-11.
 */
public final class RunOnceEnumerable<TElement> implements IEnumerable<TElement> {
    private final IEnumerable<TElement> source;
    private boolean called;

    public RunOnceEnumerable(IEnumerable<TElement> source) {
        this.source = source;
    }

    @Override
    public IEnumerator<TElement> enumerator() {
        if (this.called)
            throw Errors.notSupported();
        this.called = true;
        return this.source.enumerator();
    }
}
