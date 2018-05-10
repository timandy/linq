package com.bestvike.linq.iterator;

import com.bestvike.function.Func1;
import com.bestvike.function.Func2;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.exception.Errors;

/**
 * Created by 许崇雷 on 2017-09-11.
 */
public final class Aggregate {
    private Aggregate() {
    }

    public static <TSource> TSource aggregate(IEnumerable<TSource> source, Func2<TSource, TSource, TSource> func) {
        if (source == null)
            throw Errors.argumentNull("source");
        if (func == null)
            throw Errors.argumentNull("func");

        try (IEnumerator<TSource> e = source.enumerator()) {
            if (!e.moveNext())
                throw Errors.noElements();

            TSource result = e.current();
            while (e.moveNext())
                result = func.apply(result, e.current());
            return result;
        }
    }

    public static <TSource, TAccumulate> TAccumulate aggregate(IEnumerable<TSource> source, TAccumulate seed, Func2<TAccumulate, TSource, TAccumulate> func) {
        if (source == null)
            throw Errors.argumentNull("source");
        if (func == null)
            throw Errors.argumentNull("func");

        TAccumulate result = seed;
        try (IEnumerator<TSource> e = source.enumerator()) {
            while (e.moveNext())
                result = func.apply(result, e.current());
        }
        return result;
    }

    public static <TSource, TAccumulate, TResult> TResult aggregate(IEnumerable<TSource> source, TAccumulate seed, Func2<TAccumulate, TSource, TAccumulate> func, Func1<TAccumulate, TResult> resultSelector) {
        if (source == null)
            throw Errors.argumentNull("source");
        if (func == null)
            throw Errors.argumentNull("func");
        if (resultSelector == null)
            throw Errors.argumentNull("resultSelector");

        TAccumulate result = seed;
        try (IEnumerator<TSource> e = source.enumerator()) {
            while (e.moveNext())
                result = func.apply(result, e.current());
        }
        return resultSelector.apply(result);
    }
}
