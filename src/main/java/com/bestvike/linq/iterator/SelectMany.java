package com.bestvike.linq.iterator;

import com.bestvike.function.Func1;
import com.bestvike.function.Func2;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.exception.Errors;

/**
 * Created by 许崇雷 on 2018-05-04.
 */
public final class SelectMany {
    private SelectMany() {
    }

    public static <TSource, TResult> IEnumerable<TResult> selectMany(IEnumerable<TSource> source, Func1<TSource, IEnumerable<TResult>> selector) {
        if (source == null)
            throw Errors.argumentNull("source");
        if (selector == null)
            throw Errors.argumentNull("selector");

        return new SelectManyIterator<>(source, selector);
    }

    public static <TSource, TResult> IEnumerable<TResult> selectMany(IEnumerable<TSource> source, Func2<TSource, Integer, IEnumerable<TResult>> selector) {
        if (source == null)
            throw Errors.argumentNull("source");
        if (selector == null)
            throw Errors.argumentNull("selector");

        return new SelectManyIterator2<>(source, selector);
    }

    public static <TSource, TCollection, TResult> IEnumerable<TResult> selectMany(IEnumerable<TSource> source, Func1<TSource, IEnumerable<TCollection>> collectionSelector, Func2<TSource, TCollection, TResult> resultSelector) {
        if (source == null)
            throw Errors.argumentNull("source");
        if (collectionSelector == null)
            throw Errors.argumentNull("collectionSelector");
        if (resultSelector == null)
            throw Errors.argumentNull("resultSelector");

        return new SelectManyIterator3<>(source, collectionSelector, resultSelector);
    }

    public static <TSource, TCollection, TResult> IEnumerable<TResult> selectMany(IEnumerable<TSource> source, Func2<TSource, Integer, IEnumerable<TCollection>> collectionSelector, Func2<TSource, TCollection, TResult> resultSelector) {
        if (source == null)
            throw Errors.argumentNull("source");
        if (collectionSelector == null)
            throw Errors.argumentNull("collectionSelector");
        if (resultSelector == null)
            throw Errors.argumentNull("resultSelector");

        return new SelectManyIterator4<>(source, collectionSelector, resultSelector);
    }
}
