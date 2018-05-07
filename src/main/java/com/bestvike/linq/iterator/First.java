package com.bestvike.linq.iterator;

import com.bestvike.collections.generic.IList;
import com.bestvike.function.Func1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.exception.Errors;
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
        if (!foundRef.getValue())
            throw Errors.noElements();
        return first;
    }

    public static <TSource> TSource first(IEnumerable<TSource> source, Func1<TSource, Boolean> predicate) {
        out<Boolean> foundRef = out.init();
        TSource first = tryGetFirst(source, predicate, foundRef);
        if (!foundRef.getValue())
            throw Errors.noMatch();
        return first;
    }

    public static <TSource> TSource firstOrDefault(IEnumerable<TSource> source) {
        out<Boolean> foundRef = out.init();
        return tryGetFirst(source, foundRef);
    }

    public static <TSource> TSource firstOrDefault(IEnumerable<TSource> source, Func1<TSource, Boolean> predicate) {
        out<Boolean> foundRef = out.init();
        return tryGetFirst(source, predicate, foundRef);
    }

    private static <TSource> TSource tryGetFirst(IEnumerable<TSource> source, out<Boolean> found) {
        if (source == null)
            throw Errors.argumentNull("source");

        if (source instanceof IPartition) {
            IPartition<TSource> partition = (IPartition<TSource>) source;
            out<Boolean> foundRef = out.init();
            return partition._tryGetFirst(foundRef);
        }

        if (source instanceof IList) {
            IList<TSource> list = (IList<TSource>) source;
            if (list._getCount() > 0) {
                found.setValue(true);
                return list.get(0);
            }
        } else {
            try (IEnumerator<TSource> e = source.enumerator()) {
                if (e.moveNext()) {
                    found.setValue(true);
                    return e.current();
                }
            }
        }

        found.setValue(false);
        return null;
    }

    private static <TSource> TSource tryGetFirst(IEnumerable<TSource> source, Func1<TSource, Boolean> predicate, out<Boolean> found) {
        if (source == null)
            throw Errors.argumentNull("source");
        if (predicate == null)
            throw Errors.argumentNull("predicate");

        if (source instanceof AbstractOrderedEnumerable) {
            AbstractOrderedEnumerable<TSource> ordered = (AbstractOrderedEnumerable<TSource>) source;
            out<Boolean> foundRef = out.init();
            return ordered._tryGetFirst(predicate, foundRef);
        }

        for (TSource element : source) {
            if (predicate.apply(element)) {
                found.setValue(true);
                return element;
            }
        }

        found.setValue(false);
        return null;
    }
}
