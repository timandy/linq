package com.bestvike.linq.enumerable;

import com.bestvike.collections.generic.ICollection;
import com.bestvike.function.Predicate1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.exception.ExceptionArgument;
import com.bestvike.linq.exception.ThrowHelper;
import com.bestvike.out;

/**
 * Created by 许崇雷 on 2018-04-27.
 */
public final class Count {
    private Count() {
    }

    public static <TSource> int count(IEnumerable<TSource> source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

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

    public static <TSource> int count(IEnumerable<TSource> source, Predicate1<TSource> predicate) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (predicate == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.predicate);

        int count = 0;
        try (IEnumerator<TSource> e = source.enumerator()) {
            while (e.moveNext()) {
                if (predicate.apply(e.current()))
                    count = Math.addExact(count, 1);
            }
        }

        return count;
    }

    public static <TSource> long longCount(IEnumerable<TSource> source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        long count = 0;
        try (IEnumerator<TSource> e = source.enumerator()) {
            while (e.moveNext())
                count = Math.addExact(count, 1);
        }

        return count;
    }

    public static <TSource> long longCount(IEnumerable<TSource> source, Predicate1<TSource> predicate) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (predicate == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.predicate);

        long count = 0;
        try (IEnumerator<TSource> e = source.enumerator()) {
            while (e.moveNext()) {
                if (predicate.apply(e.current()))
                    count = Math.addExact(count, 1);
            }
        }

        return count;
    }

    public static <TSource> boolean tryGetNonEnumeratedCount(IEnumerable<TSource> source, out<Integer> count) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        if (source instanceof ICollection) {
            ICollection<TSource> collection = (ICollection<TSource>) source;
            count.value = collection._getCount();
            return true;
        }

        if (source instanceof IIListProvider) {
            IIListProvider<TSource> provider = (IIListProvider<TSource>) source;
            int c = provider._getCount(true);
            if (c >= 0) {
                count.value = c;
                return true;
            }
        }

        count.value = 0;
        return false;
    }
}
