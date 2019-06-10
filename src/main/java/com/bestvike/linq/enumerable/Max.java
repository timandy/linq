package com.bestvike.linq.enumerable;

import com.bestvike.collections.generic.Comparer;
import com.bestvike.function.Func1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.exception.ExceptionArgument;
import com.bestvike.linq.exception.ThrowHelper;

import java.math.BigDecimal;
import java.util.Comparator;

/**
 * Created by 许崇雷 on 2018-05-02.
 */
public final class Max {
    private Max() {
    }

    public static int maxInt(IEnumerable<Integer> source) {
        Integer value = maxIntNull(source);
        if (value == null)
            ThrowHelper.throwNoElementsException();
        return value;
    }

    public static Integer maxIntNull(IEnumerable<Integer> source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        Integer value = null;
        try (IEnumerator<Integer> e = source.enumerator()) {
            do {
                if (!e.moveNext())
                    return value;
                value = e.current();
            }
            while (value == null);

            while (e.moveNext()) {
                Integer cur = e.current();
                if (cur != null && cur > value)
                    value = cur;
            }
        }

        return value;
    }

    public static long maxLong(IEnumerable<Long> source) {
        Long value = maxLongNull(source);
        if (value == null)
            ThrowHelper.throwNoElementsException();
        return value;
    }

    public static Long maxLongNull(IEnumerable<Long> source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        Long value = null;
        try (IEnumerator<Long> e = source.enumerator()) {
            do {
                if (!e.moveNext())
                    return value;
                value = e.current();
            }
            while (value == null);

            while (e.moveNext()) {
                Long cur = e.current();
                if (cur != null && cur > value)
                    value = cur;
            }
        }

        return value;
    }

    public static float maxFloat(IEnumerable<Float> source) {
        Float value = maxFloatNull(source);
        if (value == null)
            ThrowHelper.throwNoElementsException();
        return value;
    }

    public static Float maxFloatNull(IEnumerable<Float> source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        Float value = null;
        try (IEnumerator<Float> e = source.enumerator()) {
            do {
                if (!e.moveNext())
                    return value;
                value = e.current();
            }
            while (value == null);

            while (Float.isNaN(value)) {
                if (!e.moveNext())
                    return value;
                Float cur = e.current();
                if (cur != null)
                    value = cur;
            }

            while (e.moveNext()) {
                Float cur = e.current();
                if (cur != null && cur > value)
                    value = cur;
            }
        }

        return value;
    }

    public static double maxDouble(IEnumerable<Double> source) {
        Double value = maxDoubleNull(source);
        if (value == null)
            ThrowHelper.throwNoElementsException();
        return value;
    }

    public static Double maxDoubleNull(IEnumerable<Double> source) {
        if (source == null) {
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        }

        Double value = null;
        try (IEnumerator<Double> e = source.enumerator()) {
            do {
                if (!e.moveNext())
                    return value;
                value = e.current();
            }
            while (value == null);

            while (Double.isNaN(value)) {
                if (!e.moveNext())
                    return value;
                Double cur = e.current();
                if (cur != null)
                    value = cur;
            }

            while (e.moveNext()) {
                Double cur = e.current();
                if (cur != null && cur > value)
                    value = cur;
            }
        }

        return value;
    }

    public static BigDecimal maxDecimal(IEnumerable<BigDecimal> source) {
        BigDecimal value = maxDecimalNull(source);
        if (value == null)
            ThrowHelper.throwNoElementsException();
        return value;
    }

    public static BigDecimal maxDecimalNull(IEnumerable<BigDecimal> source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        BigDecimal value = null;
        try (IEnumerator<BigDecimal> e = source.enumerator()) {
            do {
                if (!e.moveNext())
                    return value;
                value = e.current();
            }
            while (value == null);

            while (e.moveNext()) {
                BigDecimal cur = e.current();
                if (cur != null && cur.compareTo(value) > 0)
                    value = cur;
            }
        }

        return value;
    }

    public static <TSource> TSource max(IEnumerable<TSource> source) {
        TSource value = maxNull(source);
        if (value == null)
            ThrowHelper.throwNoElementsException();
        return value;
    }

    public static <TSource> TSource maxNull(IEnumerable<TSource> source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        Comparator<TSource> comparer = Comparer.Default();
        TSource value = null;
        try (IEnumerator<TSource> e = source.enumerator()) {
            do {
                if (!e.moveNext())
                    return value;
                value = e.current();
            }
            while (value == null);

            while (e.moveNext()) {
                TSource cur = e.current();
                if (cur != null && comparer.compare(cur, value) > 0)
                    value = cur;
            }
        }

        return value;
    }

    public static <TSource> int maxInt(IEnumerable<TSource> source, Func1<TSource, Integer> selector) {
        Integer value = maxIntNull(source, selector);
        if (value == null)
            ThrowHelper.throwNoElementsException();
        return value;
    }

    public static <TSource> Integer maxIntNull(IEnumerable<TSource> source, Func1<TSource, Integer> selector) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (selector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.selector);

        Integer value = null;
        try (IEnumerator<TSource> e = source.enumerator()) {
            do {
                if (!e.moveNext())
                    return value;
                value = selector.apply(e.current());
            }
            while (value == null);

            while (e.moveNext()) {
                Integer cur = selector.apply(e.current());
                if (cur != null && cur > value)
                    value = cur;
            }
        }

        return value;
    }

    public static <TSource> long maxLong(IEnumerable<TSource> source, Func1<TSource, Long> selector) {
        Long value = maxLongNull(source, selector);
        if (value == null)
            ThrowHelper.throwNoElementsException();
        return value;
    }

    public static <TSource> Long maxLongNull(IEnumerable<TSource> source, Func1<TSource, Long> selector) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (selector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.selector);

        Long value = null;
        try (IEnumerator<TSource> e = source.enumerator()) {
            do {
                if (!e.moveNext())
                    return value;
                value = selector.apply(e.current());
            }
            while (value == null);

            while (e.moveNext()) {
                Long cur = selector.apply(e.current());
                if (cur != null && cur > value)
                    value = cur;
            }
        }

        return value;
    }

    public static <TSource> float maxFloat(IEnumerable<TSource> source, Func1<TSource, Float> selector) {
        Float value = maxFloatNull(source, selector);
        if (value == null)
            ThrowHelper.throwNoElementsException();
        return value;
    }

    public static <TSource> Float maxFloatNull(IEnumerable<TSource> source, Func1<TSource, Float> selector) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (selector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.selector);

        Float value = null;
        try (IEnumerator<TSource> e = source.enumerator()) {
            do {
                if (!e.moveNext())
                    return value;
                value = selector.apply(e.current());
            }
            while (value == null);

            while (Float.isNaN(value)) {
                if (!e.moveNext())
                    return value;
                Float cur = selector.apply(e.current());
                if (cur != null)
                    value = cur;
            }

            while (e.moveNext()) {
                Float cur = selector.apply(e.current());
                if (cur != null && cur > value)
                    value = cur;
            }
        }

        return value;
    }

    public static <TSource> double maxDouble(IEnumerable<TSource> source, Func1<TSource, Double> selector) {
        Double value = maxDoubleNull(source, selector);
        if (value == null)
            ThrowHelper.throwNoElementsException();
        return value;
    }

    public static <TSource> Double maxDoubleNull(IEnumerable<TSource> source, Func1<TSource, Double> selector) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (selector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.selector);

        Double value = null;
        try (IEnumerator<TSource> e = source.enumerator()) {
            do {
                if (!e.moveNext())
                    return value;
                value = selector.apply(e.current());
            }
            while (value == null);

            while (Double.isNaN(value)) {
                if (!e.moveNext())
                    return value;
                Double cur = selector.apply(e.current());
                if (cur != null)
                    value = cur;
            }

            while (e.moveNext()) {
                Double cur = selector.apply(e.current());
                if (cur != null && cur > value)
                    value = cur;
            }
        }

        return value;
    }

    public static <TSource> BigDecimal maxDecimal(IEnumerable<TSource> source, Func1<TSource, BigDecimal> selector) {
        BigDecimal value = maxDecimalNull(source, selector);
        if (value == null)
            ThrowHelper.throwNoElementsException();
        return value;
    }

    public static <TSource> BigDecimal maxDecimalNull(IEnumerable<TSource> source, Func1<TSource, BigDecimal> selector) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (selector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.selector);

        BigDecimal value = null;
        try (IEnumerator<TSource> e = source.enumerator()) {
            do {
                if (!e.moveNext())
                    return value;
                value = selector.apply(e.current());
            }
            while (value == null);

            while (e.moveNext()) {
                BigDecimal cur = selector.apply(e.current());
                if (cur != null && cur.compareTo(value) > 0)
                    value = cur;
            }
        }

        return value;
    }

    public static <TSource, TResult> TResult max(IEnumerable<TSource> source, Func1<TSource, TResult> selector) {
        TResult value = maxNull(source, selector);
        if (value == null)
            ThrowHelper.throwNoElementsException();
        return value;
    }

    public static <TSource, TResult> TResult maxNull(IEnumerable<TSource> source, Func1<TSource, TResult> selector) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (selector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.selector);

        Comparator<TResult> comparer = Comparer.Default();
        TResult value = null;
        try (IEnumerator<TSource> e = source.enumerator()) {
            do {
                if (!e.moveNext())
                    return value;
                value = selector.apply(e.current());
            }
            while (value == null);

            while (e.moveNext()) {
                TResult cur = selector.apply(e.current());
                if (cur != null && comparer.compare(cur, value) > 0)
                    value = cur;
            }
        }

        return value;
    }
}
