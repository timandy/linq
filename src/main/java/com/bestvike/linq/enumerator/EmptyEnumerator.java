package com.bestvike.linq.enumerator;

import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.exception.Errors;

/**
 * @author 许崇雷
 * @date 2017/7/17
 */
public final class EmptyEnumerator<TSource> implements IEnumerator<TSource> {
    public static <TSource> IEnumerator<TSource> Instance() {
        return new EmptyEnumerator<>();
    }

    private EmptyEnumerator() {
    }

    @Override
    public boolean moveNext() {
        return false;
    }

    @Override
    public TSource current() {
        return null;
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public TSource next() {
        throw Errors.noSuchElement();
    }

    @Override
    public void reset() {
        throw Errors.notSupported();
    }

    @Override
    public void close() {
    }
}
