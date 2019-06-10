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
 * Created by 许崇雷 on 2018-05-03.
 */
public final class Min {
    private Min() {
    }

    public static int minInt(IEnumerable<Integer> source) {
        Integer value = minIntNull(source);
        if (value == null)
            ThrowHelper.throwNoElementsException();
        return value;
    }

    public static Integer minIntNull(IEnumerable<Integer> source) {
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
                if (cur != null && cur < value)
                    value = cur;
            }
        }

        return value;
    }

    public static long minLong(IEnumerable<Long> source) {
        Long value = minLongNull(source);
        if (value == null)
            ThrowHelper.throwNoElementsException();
        return value;
    }

    public static Long minLongNull(IEnumerable<Long> source) {
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
                if (cur != null && cur < value)
                    value = cur;
            }
        }

        return value;
    }

    public static float minFloat(IEnumerable<Float> source) {
        Float value = minFloatNull(source);
        if (value == null)
            ThrowHelper.throwNoElementsException();
        return value;
    }

    public static Float minFloatNull(IEnumerable<Float> source) {
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

            while (e.moveNext()) {
                Float cur = e.current();
                if (cur != null) {
                    if (cur < value)
                        value = cur;
                    else if (Float.isNaN(cur))
                        return cur;
                }
            }
        }

        return value;
    }

    public static double minDouble(IEnumerable<Double> source) {
        Double value = minDoubleNull(source);
        if (value == null)
            ThrowHelper.throwNoElementsException();
        return value;
    }

    public static Double minDoubleNull(IEnumerable<Double> source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        Double value = null;
        try (IEnumerator<Double> e = source.enumerator()) {
            do {
                if (!e.moveNext())
                    return value;
                value = e.current();
            }
            while (value == null);

            while (e.moveNext()) {
                Double cur = e.current();
                if (cur != null) {
                    if (cur < value)
                        value = cur;
                    else if (Double.isNaN(cur))
                        return cur;
                }
            }
        }

        return value;
    }

    public static BigDecimal minDecimal(IEnumerable<BigDecimal> source) {
        BigDecimal value = minDecimalNull(source);
        if (value == null)
            ThrowHelper.throwNoElementsException();
        return value;
    }

    public static BigDecimal minDecimalNull(IEnumerable<BigDecimal> source) {
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
                if (cur != null && cur.compareTo(value) < 0)
                    value = cur;
            }
        }

        return value;
    }

    public static <TSource> TSource min(IEnumerable<TSource> source) {
        TSource value = minNull(source);
        if (value == null)
            ThrowHelper.throwNoElementsException();
        return value;
    }

    public static <TSource> TSource minNull(IEnumerable<TSource> source) {
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
                if (cur != null && comparer.compare(cur, value) < 0)
                    value = cur;
            }
        }

        return value;
    }

    public static <TSource> int minInt(IEnumerable<TSource> source, Func1<TSource, Integer> selector) {
        Integer value = minIntNull(source, selector);
        if (value == null)
            ThrowHelper.throwNoElementsException();
        return value;
    }

    public static <TSource> Integer minIntNull(IEnumerable<TSource> source, Func1<TSource, Integer> selector) {
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
                if (cur != null && cur < value)
                    value = cur;
            }
        }

        return value;
    }

    public static <TSource> long minLong(IEnumerable<TSource> source, Func1<TSource, Long> selector) {
        Long value = minLongNull(source, selector);
        if (value == null)
            ThrowHelper.throwNoElementsException();
        return value;
    }

    public static <TSource> Long minLongNull(IEnumerable<TSource> source, Func1<TSource, Long> selector) {
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
                if (cur != null && cur < value)
                    value = cur;
            }
        }

        return value;
    }

    public static <TSource> float minFloat(IEnumerable<TSource> source, Func1<TSource, Float> selector) {
        Float value = minFloatNull(source, selector);
        if (value == null)
            ThrowHelper.throwNoElementsException();
        return value;
    }

    public static <TSource> Float minFloatNull(IEnumerable<TSource> source, Func1<TSource, Float> selector) {
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

            while (e.moveNext()) {
                Float cur = selector.apply(e.current());
                if (cur != null) {
                    if (cur < value)
                        value = cur;
                    else if (Float.isNaN(cur))
                        return cur;
                }
            }
        }

        return value;
    }

    public static <TSource> double minDouble(IEnumerable<TSource> source, Func1<TSource, Double> selector) {
        Double value = minDoubleNull(source, selector);
        if (value == null)
            ThrowHelper.throwNoElementsException();
        return value;
    }

    public static <TSource> Double minDoubleNull(IEnumerable<TSource> source, Func1<TSource, Double> selector) {
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

            while (e.moveNext()) {
                Double cur = selector.apply(e.current());
                if (cur != null) {
                    if (cur < value)
                        value = cur;
                    else if (Double.isNaN(cur))
                        return cur;
                }
            }
        }

        return value;
    }

    public static <TSource> BigDecimal minDecimal(IEnumerable<TSource> source, Func1<TSource, BigDecimal> selector) {
        BigDecimal value = minDecimalNull(source, selector);
        if (value == null)
            ThrowHelper.throwNoElementsException();
        return value;
    }

    public static <TSource> BigDecimal minDecimalNull(IEnumerable<TSource> source, Func1<TSource, BigDecimal> selector) {
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
                if (cur != null && cur.compareTo(value) < 0)
                    value = cur;
            }
        }

        return value;
    }

    public static <TSource, TResult> TResult min(IEnumerable<TSource> source, Func1<TSource, TResult> selector) {
        TResult value = minNull(source, selector);
        if (value == null)
            ThrowHelper.throwNoElementsException();
        return value;
    }

    public static <TSource, TResult> TResult minNull(IEnumerable<TSource> source, Func1<TSource, TResult> selector) {
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
                if (cur != null && comparer.compare(cur, value) < 0)
                    value = cur;
            }
        }

        return value;
    }
}
