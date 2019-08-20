package com.bestvike.linq.enumerable;

import com.bestvike.collections.generic.ICollection;
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

        if (source instanceof ICollection) {
            ICollection<TSource> collectionoft = (ICollection<TSource>) source;
            return collectionoft._getCount() != 0;
        }

        if (source instanceof IIListProvider) {
            // Note that this check differs from the corresponding check in
            // Count (whereas otherwise this method parallels it).  If the count
            // can't be retrieved cheaply, that likely means we'd need to iterate
            // through the entire sequence in order to get the count, and in that
            // case, we'll generally be better off falling through to the logic
            // below that only enumerates at most a single element.
            IIListProvider<TSource> listProv = (IIListProvider<TSource>) source;
            int count = listProv._getCount(true);
            if (count >= 0)
                return count != 0;
        }

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
