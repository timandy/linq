package com.bestvike.linq.enumerable;

import com.bestvike.function.Predicate1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.exception.ExceptionArgument;
import com.bestvike.linq.exception.ThrowHelper;

/**
 * Created by 许崇雷 on 2017-09-11.
 */
public final class AnyAll {
    private AnyAll() {
    }

    public static <TSource> boolean any(IEnumerable<TSource> source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        try (IEnumerator<TSource> e = source.enumerator()) {
            return e.moveNext();
        }
    }

    public static <TSource> boolean any(IEnumerable<TSource> source, Predicate1<TSource> predicate) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (predicate == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.predicate);

        try (IEnumerator<TSource> e = source.enumerator()) {
            while (e.moveNext()) {
                if (predicate.apply(e.current()))
                    return true;
            }
        }
        return false;
    }

    public static <TSource> boolean all(IEnumerable<TSource> source, Predicate1<TSource> predicate) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (predicate == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.predicate);

        try (IEnumerator<TSource> e = source.enumerator()) {
            while (e.moveNext()) {
                if (!predicate.apply(e.current()))
                    return false;
            }
        }
        return true;
    }
}
