package com.bestvike.linq.enumerable;

import com.bestvike.collections.generic.IList;
import com.bestvike.function.Predicate1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.exception.ExceptionArgument;
import com.bestvike.linq.exception.ThrowHelper;

/**
 * Created by 许崇雷 on 2019-06-17.
 */
public final class FindIndex {
    private FindIndex() {
    }

    public static <TSource> int findIndex(IEnumerable<TSource> source, Predicate1<TSource> predicate) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (predicate == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.predicate);

        if (source instanceof IList) {
            IList<TSource> list = (IList<TSource>) source;
            return list._findIndex(predicate);
        }

        int index = 0;
        try (IEnumerator<TSource> e = source.enumerator()) {
            while (e.moveNext()) {
                if (predicate.apply(e.current()))
                    return index;
                index++;
            }
        }

        return -1;
    }

    public static <TSource> int findLastIndex(IEnumerable<TSource> source, Predicate1<TSource> predicate) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (predicate == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.predicate);

        if (source instanceof IList) {
            IList<TSource> list = (IList<TSource>) source;
            return list._findLastIndex(predicate);
        }

        // see ReverseIterator.moveNext()
        Buffer<TSource> buffer = new Buffer<>(source);
        Object[] array = buffer.items;
        for (int i = buffer.count - 1; i >= 0; i--) {
            //noinspection unchecked
            if (predicate.apply((TSource) array[i]))
                return i;
        }

        return -1;
    }
}
