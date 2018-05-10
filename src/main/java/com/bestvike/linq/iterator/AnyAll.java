package com.bestvike.linq.iterator;

import com.bestvike.function.Func1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.exception.Errors;

/**
 * Created by 许崇雷 on 2017-09-11.
 */
public final class AnyAll {
    private AnyAll() {
    }

    public static <TSource> boolean any(IEnumerable<TSource> source) {
        if (source == null)
            throw Errors.argumentNull("source");

        try (IEnumerator<TSource> e = source.enumerator()) {
            return e.moveNext();
        }
    }

    public static <TSource> boolean any(IEnumerable<TSource> source, Func1<TSource, Boolean> predicate) {
        if (source == null)
            throw Errors.argumentNull("source");
        if (predicate == null)
            throw Errors.argumentNull("predicate");

        try (IEnumerator<TSource> e = source.enumerator()) {
            while (e.moveNext()) {
                if (predicate.apply(e.current()))
                    return true;
            }
        }
        return false;
    }

    public static <TSource> boolean all(IEnumerable<TSource> source, Func1<TSource, Boolean> predicate) {
        if (source == null)
            throw Errors.argumentNull("source");
        if (predicate == null)
            throw Errors.argumentNull("predicate");

        try (IEnumerator<TSource> e = source.enumerator()) {
            while (e.moveNext()) {
                if (!predicate.apply(e.current()))
                    return false;
            }
        }
        return true;
    }
}
