package com.bestvike.linq.iterator;

import com.bestvike.collections.generic.IList;
import com.bestvike.function.Func1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.exception.Errors;

/**
 * Created by 许崇雷 on 2018-05-04.
 */
public final class Single {
    private Single() {
    }

    public static <TSource> TSource single(IEnumerable<TSource> source) {
        if (source == null)
            throw Errors.argumentNull("source");

        if (source instanceof IList) {
            IList<TSource> list = (IList<TSource>) source;
            switch (list._getCount()) {
                case 0:
                    throw Errors.noElements();
                case 1:
                    return list.get(0);
            }
        } else {
            try (IEnumerator<TSource> e = source.enumerator()) {
                if (!e.moveNext())
                    throw Errors.noElements();
                TSource result = e.current();
                if (!e.moveNext())
                    return result;
            }
        }

        throw Errors.moreThanOneElement();
    }

    public static <TSource> TSource single(IEnumerable<TSource> source, Func1<TSource, Boolean> predicate) {
        if (source == null)
            throw Errors.argumentNull("source");
        if (predicate == null)
            throw Errors.argumentNull("predicate");

        try (IEnumerator<TSource> e = source.enumerator()) {
            while (e.moveNext()) {
                TSource result = e.current();
                if (predicate.apply(result)) {
                    while (e.moveNext()) {
                        if (predicate.apply(e.current()))
                            throw Errors.moreThanOneMatch();
                    }
                    return result;
                }
            }
        }

        throw Errors.noMatch();
    }

    public static <TSource> TSource singleOrDefault(IEnumerable<TSource> source) {
        if (source == null)
            throw Errors.argumentNull("source");

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

        throw Errors.moreThanOneElement();
    }

    public static <TSource> TSource singleOrDefault(IEnumerable<TSource> source, Func1<TSource, Boolean> predicate) {
        if (source == null)
            throw Errors.argumentNull("source");
        if (predicate == null)
            throw Errors.argumentNull("predicate");

        try (IEnumerator<TSource> e = source.enumerator()) {
            while (e.moveNext()) {
                TSource result = e.current();
                if (predicate.apply(result)) {
                    while (e.moveNext()) {
                        if (predicate.apply(e.current()))
                            throw Errors.moreThanOneMatch();
                    }
                    return result;
                }
            }
        }

        return null;
    }
}
