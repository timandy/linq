package com.bestvike.linq.enumerable;

import com.bestvike.collections.generic.EqualityComparer;
import com.bestvike.collections.generic.IEqualityComparer;
import com.bestvike.collections.generic.ILinkedList;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.exception.ExceptionArgument;
import com.bestvike.linq.exception.ThrowHelper;

/**
 * Created by 许崇雷 on 2019-06-14.
 */
public final class IndexOf {
    private IndexOf() {
    }

    public static <TSource> int indexOf(IEnumerable<TSource> source, TSource value) {
        if (source instanceof ILinkedList) {
            ILinkedList<TSource> list = (ILinkedList<TSource>) source;
            return list._indexOf(value);
        }
        return indexOf(source, value, null);
    }

    public static <TSource> int indexOf(IEnumerable<TSource> source, TSource value, IEqualityComparer<TSource> comparer) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        if (comparer == null)
            comparer = EqualityComparer.Default();

        int index = 0;
        try (IEnumerator<TSource> e = source.enumerator()) {
            while (e.moveNext()) {
                if (comparer.equals(e.current(), value))
                    return index;
                index++;
            }
        }

        return -1;
    }

    public static <TSource> int lastIndexOf(IEnumerable<TSource> source, TSource value) {
        if (source instanceof ILinkedList) {
            ILinkedList<TSource> list = (ILinkedList<TSource>) source;
            return list._lastIndexOf(value);
        }
        return lastIndexOf(source, value, null);
    }

    public static <TSource> int lastIndexOf(IEnumerable<TSource> source, TSource value, IEqualityComparer<TSource> comparer) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        if (comparer == null)
            comparer = EqualityComparer.Default();

        // see ReverseIterator.moveNext()
        Buffer<TSource> buffer = new Buffer<>(source);
        Object[] array = buffer.items;
        for (int i = buffer.count - 1; i >= 0; i--) {
            //noinspection unchecked
            if (comparer.equals((TSource) array[i], value))
                return i;
        }

        return -1;
    }
}
