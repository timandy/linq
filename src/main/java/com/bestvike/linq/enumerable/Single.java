package com.bestvike.linq.enumerable;

import com.bestvike.collections.generic.IList;
import com.bestvike.function.Predicate1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.exception.ExceptionArgument;
import com.bestvike.linq.exception.ThrowHelper;

/**
 * Created by 许崇雷 on 2018-05-04.
 */
public final class Single {
    private Single() {
    }

    public static <TSource> TSource single(IEnumerable<TSource> source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        if (source instanceof IList) {
            IList<TSource> list = (IList<TSource>) source;
            switch (list._getCount()) {
                case 0:
                    ThrowHelper.throwNoElementsException();
                    return null;
                case 1:
                    return list.get(0);
            }
        } else {
            try (IEnumerator<TSource> e = source.enumerator()) {
                if (!e.moveNext())
                    ThrowHelper.throwNoElementsException();
                TSource result = e.current();
                if (!e.moveNext())
                    return result;
            }
        }

        ThrowHelper.throwMoreThanOneElementException();
        return null;
    }

    public static <TSource> TSource single(IEnumerable<TSource> source, Predicate1<TSource> predicate) {
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
                    return result;
                }
            }
        }

        ThrowHelper.throwNoMatchException();
        return null;
    }

    public static <TSource> TSource singleOrDefault(IEnumerable<TSource> source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        if (source instanceof IList) {
            IList<TSource> list = (IList<TSource>) source;
            switch (list._getCount()) {
                case 0:
                    return null;
                case 1:
                    return list.get(0);
            }
        } else {
            try (IEnumerator<TSource> e = source.enumerator()) {
                if (!e.moveNext())
                    return null;
                TSource result = e.current();
                if (!e.moveNext())
                    return result;
            }
        }

        ThrowHelper.throwMoreThanOneElementException();
        return null;
    }

    public static <TSource> TSource singleOrDefault(IEnumerable<TSource> source, Predicate1<TSource> predicate) {
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
                    return result;
                }
            }
        }

        return null;
    }
}
