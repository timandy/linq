package com.bestvike.linq.enumerable;

import com.bestvike.collections.generic.EqualityComparer;
import com.bestvike.collections.generic.ICollection;
import com.bestvike.collections.generic.IEqualityComparer;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.exception.ExceptionArgument;
import com.bestvike.linq.exception.ThrowHelper;

/**
 * Created by 许崇雷 on 2018-04-27.
 */
public final class Contains {
    private Contains() {
    }

    public static <TSource> boolean contains(IEnumerable<TSource> source, TSource value) {
        if (source instanceof ICollection) {
            ICollection<TSource> collection = (ICollection<TSource>) source;
            return collection._contains(value);
        }
        return contains(source, value, null);
    }

    public static <TSource> boolean contains(IEnumerable<TSource> source, TSource value, IEqualityComparer<TSource> comparer) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        if (comparer == null)
            comparer = EqualityComparer.Default();

        for (TSource element : source) {
            if (comparer.equals(element, value))
                return true;
        }

        return false;
    }
}
