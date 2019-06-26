package com.bestvike.linq.enumerable;

import com.bestvike.collections.generic.Comparer;
import com.bestvike.function.DecimalFunc1;
import com.bestvike.function.DoubleFunc1;
import com.bestvike.function.FloatFunc1;
import com.bestvike.function.Func1;
import com.bestvike.function.IntFunc1;
import com.bestvike.function.LongFunc1;
import com.bestvike.function.NullableDecimalFunc1;
import com.bestvike.function.NullableDoubleFunc1;
import com.bestvike.function.NullableFloatFunc1;
import com.bestvike.function.NullableIntFunc1;
import com.bestvike.function.NullableLongFunc1;
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
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        int value;
        try (IEnumerator<Integer> e = source.enumerator()) {
            if (!e.moveNext())
                ThrowHelper.throwNoElementsException();

            value = e.current();
            while (e.moveNext()) {
                int x = e.current();
                if (x < value)
                    value = x;
            }
        }

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
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        long value;
        try (IEnumerator<Long> e = source.enumerator()) {
            if (!e.moveNext())
                ThrowHelper.throwNoElementsException();

            value = e.current();
            while (e.moveNext()) {
                long x = e.current();
                if (x < value)
                    value = x;
            }
        }

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
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        float value;
        try (IEnumerator<Float> e = source.enumerator()) {
            if (!e.moveNext())
                ThrowHelper.throwNoElementsException();

            value = e.current();
            if (Float.isNaN(value))
                return value;

            while (e.moveNext()) {
                float x = e.current();
                if (x < value)
                    value = x;
                else if (Float.isNaN(x))
                    return x;
            }
        }

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

            if (Float.isNaN(value))
                return value;

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
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        double value;
        try (IEnumerator<Double> e = source.enumerator()) {
            if (!e.moveNext())
                ThrowHelper.throwNoElementsException();

            value = e.current();
            if (Double.isNaN(value))
                return value;

            while (e.moveNext()) {
                double x = e.current();
                if (x < value)
                    value = x;
                else if (Double.isNaN(x))
                    return x;
            }
        }

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

            if (Double.isNaN(value))
                return value;

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
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        BigDecimal value;
        try (IEnumerator<BigDecimal> e = source.enumerator()) {
            if (!e.moveNext())
                ThrowHelper.throwNoElementsException();

            value = e.current();
            if (value == null)
                ThrowHelper.throwNullPointerException();
            while (e.moveNext()) {
                BigDecimal x = e.current();
                if (x.compareTo(value) < 0)
                    value = x;
            }
        }

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
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        Comparator<TSource> comparer = Comparer.Default();
        TSource value;
        try (IEnumerator<TSource> e = source.enumerator()) {
            if (!e.moveNext())
                ThrowHelper.throwNoElementsException();

            value = e.current();
            if (value == null)
                ThrowHelper.throwNullPointerException();
            while (e.moveNext()) {
                TSource x = e.current();
                if (x == null)
                    ThrowHelper.throwNullPointerException();
                if (comparer.compare(x, value) < 0)
                    value = x;
            }
        }

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

    public static <TSource> int minInt(IEnumerable<TSource> source, IntFunc1<TSource> selector) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (selector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.selector);

        int value;
        try (IEnumerator<TSource> e = source.enumerator()) {
            if (!e.moveNext())
                ThrowHelper.throwNoElementsException();

            value = selector.apply(e.current());
            while (e.moveNext()) {
                int x = selector.apply(e.current());
                if (x < value)
                    value = x;
            }
        }

        return value;
    }

    public static <TSource> Integer minIntNull(IEnumerable<TSource> source, NullableIntFunc1<TSource> selector) {
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

    public static <TSource> long minLong(IEnumerable<TSource> source, LongFunc1<TSource> selector) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (selector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.selector);

        long value;
        try (IEnumerator<TSource> e = source.enumerator()) {
            if (!e.moveNext())
                ThrowHelper.throwNoElementsException();

            value = selector.apply(e.current());
            while (e.moveNext()) {
                long x = selector.apply(e.current());
                if (x < value)
                    value = x;
            }
        }

        return value;
    }

    public static <TSource> Long minLongNull(IEnumerable<TSource> source, NullableLongFunc1<TSource> selector) {
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

    public static <TSource> float minFloat(IEnumerable<TSource> source, FloatFunc1<TSource> selector) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (selector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.selector);

        float value;
        try (IEnumerator<TSource> e = source.enumerator()) {
            if (!e.moveNext())
                ThrowHelper.throwNoElementsException();

            value = selector.apply(e.current());
            if (Float.isNaN(value))
                return value;

            while (e.moveNext()) {
                float x = selector.apply(e.current());
                if (x < value)
                    value = x;
                else if (Float.isNaN(x))
                    return x;
            }
        }

        return value;
    }

    public static <TSource> Float minFloatNull(IEnumerable<TSource> source, NullableFloatFunc1<TSource> selector) {
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

            if (Float.isNaN(value))
                return value;

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

    public static <TSource> double minDouble(IEnumerable<TSource> source, DoubleFunc1<TSource> selector) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (selector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.selector);

        double value;
        try (IEnumerator<TSource> e = source.enumerator()) {
            if (!e.moveNext())
                ThrowHelper.throwNoElementsException();

            value = selector.apply(e.current());
            if (Double.isNaN(value))
                return value;

            while (e.moveNext()) {
                double x = selector.apply(e.current());
                if (x < value)
                    value = x;
                else if (Double.isNaN(x))
                    return x;
            }
        }

        return value;
    }

    public static <TSource> Double minDoubleNull(IEnumerable<TSource> source, NullableDoubleFunc1<TSource> selector) {
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

            if (Double.isNaN(value))
                return value;

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

    public static <TSource> BigDecimal minDecimal(IEnumerable<TSource> source, DecimalFunc1<TSource> selector) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (selector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.selector);

        BigDecimal value;
        try (IEnumerator<TSource> e = source.enumerator()) {
            if (!e.moveNext())
                ThrowHelper.throwNoElementsException();

            value = selector.apply(e.current());
            if (value == null)
                ThrowHelper.throwNullPointerException();
            while (e.moveNext()) {
                BigDecimal x = selector.apply(e.current());
                if (x.compareTo(value) < 0)
                    value = x;
            }
        }

        return value;
    }

    public static <TSource> BigDecimal minDecimalNull(IEnumerable<TSource> source, NullableDecimalFunc1<TSource> selector) {
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
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (selector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.selector);

        Comparator<TResult> comparer = Comparer.Default();
        TResult value;
        try (IEnumerator<TSource> e = source.enumerator()) {
            if (!e.moveNext())
                ThrowHelper.throwNoElementsException();

            value = selector.apply(e.current());
            if (value == null)
                ThrowHelper.throwNullPointerException();
            while (e.moveNext()) {
                TResult x = selector.apply(e.current());
                if (x == null)
                    ThrowHelper.throwNullPointerException();
                if (comparer.compare(x, value) < 0)
                    value = x;
            }
        }
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
