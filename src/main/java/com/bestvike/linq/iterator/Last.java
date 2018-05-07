package com.bestvike.linq.iterator;

import com.bestvike.collections.generic.IList;
import com.bestvike.function.Func1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.exception.Errors;
import com.bestvike.out;

/**
 * Created by 许崇雷 on 2018-05-02.
 */
public final class Last {
    private Last() {
    }

    public static <TSource> TSource last(IEnumerable<TSource> source) {
        out<Boolean> foundRef = out.init();
        TSource last = tryGetLast(source, foundRef);
        if (!foundRef.getValue())
            throw Errors.noElements();

        return last;
    }

    public static <TSource> TSource last(IEnumerable<TSource> source, Func1<TSource, Boolean> predicate) {
        out<Boolean> foundRef = out.init();
        TSource last = tryGetLast(source, predicate, foundRef);
        if (!foundRef.getValue())
            throw Errors.noMatch();

        return last;
    }

    public static <TSource> TSource lastOrDefault(IEnumerable<TSource> source) {
        out<Boolean> foundRef = out.init();
        return tryGetLast(source, foundRef);
    }

    public static <TSource> TSource lastOrDefault(IEnumerable<TSource> source, Func1<TSource, Boolean> predicate) {
        out<Boolean> foundRef = out.init();
        return tryGetLast(source, predicate, foundRef);
    }

    private static <TSource> TSource tryGetLast(IEnumerable<TSource> source, out<Boolean> found) {
        if (source == null)
            throw Errors.argumentNull("source");

        if (source instanceof IPartition) {
            IPartition<TSource> partition = (IPartition<TSource>) source;
            return partition._tryGetLast(found);
        }

        if (source instanceof IList) {
            IList<TSource> list = (IList<TSource>) source;
            int count = list._getCount();
            if (count > 0) {
                found.setValue(true);
                return list.get(count - 1);
            }
        } else {
            try (IEnumerator<TSource> e = source.enumerator()) {
                if (e.moveNext()) {
                    TSource result;
                    do {
                        result = e.current();
                    }
                    while (e.moveNext());

                    found.setValue(true);
                    return result;
                }
            }
        }

        found.setValue(false);
        return null;
    }

    private static <TSource> TSource tryGetLast(IEnumerable<TSource> source, Func1<TSource, Boolean> predicate, out<Boolean> found) {
        if (source == null)
            throw Errors.argumentNull("source");

        if (predicate == null)
            throw Errors.argumentNull("predicate");

        if (source instanceof AbstractOrderedEnumerable) {
            AbstractOrderedEnumerable<TSource> ordered = (AbstractOrderedEnumerable<TSource>) source;
            return ordered._tryGetLast(predicate, found);
        }

        if (source instanceof IList) {
            IList<TSource> list = (IList<TSource>) source;
            for (int i = list._getCount() - 1; i >= 0; --i) {
                TSource result = list.get(i);
                if (predicate.apply(result)) {
                    found.setValue(true);
                    return result;
                }
            }
        } else {
            try (IEnumerator<TSource> e = source.enumerator()) {
                while (e.moveNext()) {
                    TSource result = e.current();
                    if (predicate.apply(result)) {
                        while (e.moveNext()) {
                            TSource element = e.current();
                            if (predicate.apply(element))
                                result = element;
                        }

                        found.setValue(true);
                        return result;
                    }
                }
            }
        }

        found.setValue(false);
        return null;
    }
}
