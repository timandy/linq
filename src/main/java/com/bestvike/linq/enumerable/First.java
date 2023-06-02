package com.bestvike.linq.enumerable;

import com.bestvike.collections.generic.IList;
import com.bestvike.function.Predicate1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.exception.ExceptionArgument;
import com.bestvike.linq.exception.ThrowHelper;
import com.bestvike.out;

/**
 * Created by 许崇雷 on 2018-04-28.
 */
public final class First {
    private First() {
    }

    public static <TSource> TSource first(IEnumerable<TSource> source) {
        out<Boolean> foundRef = out.init();
        TSource first = tryGetFirst(source, foundRef);
        if (!foundRef.value)
            ThrowHelper.throwNoElementsException();
        return first;
    }

    public static <TSource> TSource first(IEnumerable<TSource> source, Predicate1<TSource> predicate) {
        out<Boolean> foundRef = out.init();
        TSource first = tryGetFirst(source, predicate, foundRef);
        if (!foundRef.value)
            ThrowHelper.throwNoMatchException();
        return first;
    }

    public static <TSource> TSource firstOrDefault(IEnumerable<TSource> source) {
        out<Boolean> foundRef = out.init();
        return tryGetFirst(source, foundRef);
    }

    public static <TSource> TSource firstOrDefault(IEnumerable<TSource> source, TSource defaultValue) {
        out<Boolean> foundRef = out.init();
        TSource first = tryGetFirst(source, foundRef);
        return foundRef.value ? first : defaultValue;
    }

    public static <TSource> TSource firstOrDefault(IEnumerable<TSource> source, Predicate1<TSource> predicate) {
        out<Boolean> foundRef = out.init();
        return tryGetFirst(source, predicate, foundRef);
    }

    public static <TSource> TSource firstOrDefault(IEnumerable<TSource> source, Predicate1<TSource> predicate, TSource defaultValue) {
        out<Boolean> foundRef = out.init();
        TSource first = tryGetFirst(source, predicate, foundRef);
        return foundRef.value ? first : defaultValue;
    }

    private static <TSource> TSource tryGetFirst(IEnumerable<TSource> source, out<Boolean> found) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        if (source instanceof IPartition) {
            IPartition<TSource> partition = (IPartition<TSource>) source;
            return partition._tryGetFirst(found);
        }

        if (source instanceof IList) {
            IList<TSource> list = (IList<TSource>) source;
            if (list._getCount() > 0) {
                found.value = true;
                return list.get(0);
            }
        } else {
            try (IEnumerator<TSource> e = source.enumerator()) {
                if (e.moveNext()) {
                    found.value = true;
                    return e.current();
                }
            }
        }

        found.value = false;
        return null;
    }

    private static <TSource> TSource tryGetFirst(IEnumerable<TSource> source, Predicate1<TSource> predicate, out<Boolean> found) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (predicate == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.predicate);

        if (source instanceof AbstractOrderedEnumerable) {
            AbstractOrderedEnumerable<TSource> ordered = (AbstractOrderedEnumerable<TSource>) source;
            return ordered._tryGetFirst(predicate, found);
        }

        try (IEnumerator<TSource> e = source.enumerator()) {
            while (e.moveNext()) {
                TSource element = e.current();
                if (predicate.apply(element)) {
                    found.value = true;
                    return element;
                }
            }
        }

        found.value = false;
        return null;
    }
}
