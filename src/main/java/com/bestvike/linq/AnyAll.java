package com.bestvike.linq;

import com.bestvike.linq.exception.Errors;
import com.bestvike.linq.function.Func1;

/**
 * Created by 许崇雷 on 2017-09-11.
 */
final class AnyAll {
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
        for (TSource element : source) {
            if (predicate.apply(element))
                return true;
        }
        return false;
    }

    public static <TSource> boolean all(IEnumerable<TSource> source, Func1<TSource, Boolean> predicate) {
        if (source == null)
            throw Errors.argumentNull("source");
        if (predicate == null)
            throw Errors.argumentNull("predicate");
        for (TSource element : source) {
            if (!predicate.apply(element))
                return false;
        }
        return true;
    }
}
