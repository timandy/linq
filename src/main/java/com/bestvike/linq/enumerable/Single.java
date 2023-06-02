package com.bestvike.linq.enumerable;

import com.bestvike.collections.generic.IList;
import com.bestvike.function.Predicate1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.exception.ExceptionArgument;
import com.bestvike.linq.exception.ThrowHelper;
import com.bestvike.out;

/**
 * Created by 许崇雷 on 2018-05-04.
 */
public final class Single {
    private Single() {
    }

    public static <TSource> TSource single(IEnumerable<TSource> source) {
        out<Boolean> foundRef = out.init();
        TSource single = tryGetSingle(source, foundRef);
        if (!foundRef.value)
            ThrowHelper.throwNoElementsException();
        return single;
    }

    public static <TSource> TSource single(IEnumerable<TSource> source, Predicate1<TSource> predicate) {
        out<Boolean> foundRef = out.init();
        TSource single = tryGetSingle(source, predicate, foundRef);
        if (!foundRef.value)
            ThrowHelper.throwNoElementsException();
        return single;
    }

    public static <TSource> TSource singleOrDefault(IEnumerable<TSource> source) {
        out<Boolean> foundRef = out.init();
        return tryGetSingle(source, foundRef);
    }

    public static <TSource> TSource singleOrDefault(IEnumerable<TSource> source, TSource defaultValue) {
        out<Boolean> foundRef = out.init();
        TSource single = tryGetSingle(source, foundRef);
        return foundRef.value ? single : defaultValue;
    }

    public static <TSource> TSource singleOrDefault(IEnumerable<TSource> source, Predicate1<TSource> predicate) {
        out<Boolean> foundRef = out.init();
        return tryGetSingle(source, predicate, foundRef);
    }

    public static <TSource> TSource singleOrDefault(IEnumerable<TSource> source, Predicate1<TSource> predicate, TSource defaultValue) {
        out<Boolean> foundRef = out.init();
        TSource single = tryGetSingle(source, predicate, foundRef);
        return foundRef.value ? single : defaultValue;
    }

    private static <TSource> TSource tryGetSingle(IEnumerable<TSource> source, out<Boolean> found) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        if (source instanceof IList) {
            IList<TSource> list = (IList<TSource>) source;
            switch (list._getCount()) {
                case 0:
                    found.value = false;
                    return null;
                case 1:
                    found.value = true;
                    return list.get(0);
            }
        } else {
            try (IEnumerator<TSource> e = source.enumerator()) {
                if (!e.moveNext()) {
                    found.value = false;
                    return null;
                }
                TSource result = e.current();
                if (!e.moveNext()) {
                    found.value = true;
                    return result;
                }
            }
        }

        found.value = false;
        ThrowHelper.throwMoreThanOneElementException();
        return null;
    }

    private static <TSource> TSource tryGetSingle(IEnumerable<TSource> source, Predicate1<TSource> predicate, out<Boolean> found) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (predicate == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.predicate);

        try (IEnumerator<TSource> e = source.enumerator()) {
            while (e.moveNext()) {
                TSource result = e.current();
                if (predicate.apply(result)) {
                    while (e.moveNext()) {
                        if (predicate.apply(e.current()))
                            ThrowHelper.throwMoreThanOneMatchException();
                    }
                    found.value = true;
                    return result;
                }
            }
        }

        found.value = false;
        return null;
    }
}
