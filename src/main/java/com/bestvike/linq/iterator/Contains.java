package com.bestvike.linq.iterator;

import com.bestvike.collections.generic.EqualityComparer;
import com.bestvike.collections.generic.ICollection;
import com.bestvike.collections.generic.IEqualityComparer;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.exception.Errors;

/**
 * Created by 许崇雷 on 2018-04-27.
 */
public final class Contains {
    private Contains() {
    }

    public static <TSource> boolean contains(IEnumerable<TSource> source, TSource value) {
        return source instanceof ICollection
                ? ((ICollection<TSource>) source)._contains(value)
                : contains(source, value, null);

    }

    public static <TSource> boolean contains(IEnumerable<TSource> source, TSource value, IEqualityComparer<TSource> comparer) {
        if (source == null)
            throw Errors.argumentNull("source");

        if (comparer == null)
            comparer = EqualityComparer.Default();

        for (TSource element : source) {
            if (comparer.equals(element, value))
                return true;
        }

        return false;
    }
}
