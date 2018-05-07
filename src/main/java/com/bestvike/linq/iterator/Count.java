package com.bestvike.linq.iterator;

import com.bestvike.collections.generic.ICollection;
import com.bestvike.function.Func1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.exception.Errors;

/**
 * Created by 许崇雷 on 2018-04-27.
 */
public final class Count {
    private Count() {
    }

    public static <TSource> int count(IEnumerable<TSource> source) {
        if (source == null)
            throw Errors.argumentNull("source");

        if (source instanceof ICollection) {
            ICollection<TSource> collection = (ICollection<TSource>) source;
            return collection._getCount();
        }

        if (source instanceof IIListProvider) {
            IIListProvider<TSource> listProv = (IIListProvider<TSource>) source;
            return listProv._getCount(false);
        }

        int count = 0;
        try (IEnumerator<TSource> e = source.enumerator()) {
            while (e.moveNext())
                count = Math.addExact(count, 1);
        }

        return count;
    }

    public static <TSource> int count(IEnumerable<TSource> source, Func1<TSource, Boolean> predicate) {
        if (source == null)
            throw Errors.argumentNull("source");

        if (predicate == null)
            throw Errors.argumentNull("predicate");

        int count = 0;
        for (TSource element : source) {
            if (predicate.apply(element))
                count = Math.addExact(count, 1);
        }

        return count;
    }

    public static <TSource> long longCount(IEnumerable<TSource> source) {
        if (source == null)
            throw Errors.argumentNull("source");

        long count = 0;
        try (IEnumerator<TSource> e = source.enumerator()) {
            while (e.moveNext())
                count = Math.addExact(count, 1);
        }

        return count;
    }

    public static <TSource> long longCount(IEnumerable<TSource> source, Func1<TSource, Boolean> predicate) {
        if (source == null)
            throw Errors.argumentNull("source");

        if (predicate == null)
            throw Errors.argumentNull("predicate");

        long count = 0;
        for (TSource element : source) {
            if (predicate.apply(element))
                count = Math.addExact(count, 1);
        }

        return count;
    }
}
