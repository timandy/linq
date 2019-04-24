package com.bestvike.linq.enumerable;

import com.bestvike.collections.generic.IEqualityComparer;
import com.bestvike.function.Func1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.ILookup;
import com.bestvike.linq.exception.ExceptionArgument;
import com.bestvike.linq.exception.ThrowHelper;

/**
 * Created by 许崇雷 on 2018-05-10.
 */
public final class ToLookup {
    private ToLookup() {
    }

    public static <TSource, TKey> ILookup<TKey, TSource> toLookup(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector) {
        return toLookup(source, keySelector, (IEqualityComparer<TKey>) null);
    }

    public static <TSource, TKey> ILookup<TKey, TSource> toLookup(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector, IEqualityComparer<TKey> comparer) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (keySelector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.keySelector);

        return Lookup.create(source, keySelector, comparer);
    }

    public static <TSource, TKey, TElement> ILookup<TKey, TElement> toLookup(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector, Func1<TSource, TElement> elementSelector) {
        return toLookup(source, keySelector, elementSelector, null);
    }

    public static <TSource, TKey, TElement> ILookup<TKey, TElement> toLookup(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector, Func1<TSource, TElement> elementSelector, IEqualityComparer<TKey> comparer) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (keySelector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.keySelector);
        if (elementSelector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.elementSelector);

        return Lookup.create(source, keySelector, elementSelector, comparer);
    }
}
