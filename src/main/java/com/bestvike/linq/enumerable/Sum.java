package com.bestvike.linq.enumerable;

import com.bestvike.function.DecimalFunc1;
import com.bestvike.function.DoubleFunc1;
import com.bestvike.function.FloatFunc1;
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

/**
 * Created by 许崇雷 on 2018-05-08.
 */
public final class Sum {
    private Sum() {
    }

    public static int sumInt(IEnumerable<Integer> source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        int sum = 0;
        try (IEnumerator<Integer> e = source.enumerator()) {
            while (e.moveNext())
                sum = Math.addExact(sum, e.current());
        }
        return sum;
    }

    public static int sumIntNull(IEnumerable<Integer> source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        int sum = 0;
        try (IEnumerator<Integer> e = source.enumerator()) {
            while (e.moveNext()) {
                Integer v = e.current();
                if (v != null)
                    sum = Math.addExact(sum, v);
            }
        }
        return sum;
    }

    public static long sumLong(IEnumerable<Long> source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        long sum = 0;
        try (IEnumerator<Long> e = source.enumerator()) {
            while (e.moveNext())
                sum = Math.addExact(sum, e.current());
        }
        return sum;
    }

    public static long sumLongNull(IEnumerable<Long> source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        long sum = 0;
        try (IEnumerator<Long> e = source.enumerator()) {
            while (e.moveNext()) {
                Long v = e.current();
                if (v != null)
                    sum = Math.addExact(sum, v);
            }
        }
        return sum;
    }

    public static float sumFloat(IEnumerable<Float> source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        double sum = 0;
        try (IEnumerator<Float> e = source.enumerator()) {
            while (e.moveNext())
                sum += e.current();
        }
        return (float) sum;
    }

    public static float sumFloatNull(IEnumerable<Float> source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        double sum = 0;
        try (IEnumerator<Float> e = source.enumerator()) {
            while (e.moveNext()) {
                Float v = e.current();
                if (v != null)
                    sum += v;
            }
        }
        return (float) sum;
    }

    public static double sumDouble(IEnumerable<Double> source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        double sum = 0;
        try (IEnumerator<Double> e = source.enumerator()) {
            while (e.moveNext())
                sum += e.current();
        }
        return sum;
    }

    public static double sumDoubleNull(IEnumerable<Double> source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        double sum = 0;
        try (IEnumerator<Double> e = source.enumerator()) {
            while (e.moveNext()) {
                Double v = e.current();
                if (v != null)
                    sum += v;
            }
        }
        return sum;
    }

    public static BigDecimal sumDecimal(IEnumerable<BigDecimal> source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        BigDecimal sum = BigDecimal.ZERO;
        try (IEnumerator<BigDecimal> e = source.enumerator()) {
            while (e.moveNext())
                sum = sum.add(e.current());
        }
        return sum;
    }

    public static BigDecimal sumDecimalNull(IEnumerable<BigDecimal> source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        BigDecimal sum = BigDecimal.ZERO;
        try (IEnumerator<BigDecimal> e = source.enumerator()) {
            while (e.moveNext()) {
                BigDecimal v = e.current();
                if (v != null)
                    sum = sum.add(v);
            }
        }
        return sum;
    }

    public static <TSource> int sumInt(IEnumerable<TSource> source, IntFunc1<TSource> selector) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (selector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.selector);

        int sum = 0;
        try (IEnumerator<TSource> e = source.enumerator()) {
            while (e.moveNext())
                sum = Math.addExact(sum, selector.apply(e.current()));
        }
        return sum;
    }

    public static <TSource> int sumIntNull(IEnumerable<TSource> source, NullableIntFunc1<TSource> selector) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (selector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.selector);

        int sum = 0;
        try (IEnumerator<TSource> e = source.enumerator()) {
            while (e.moveNext()) {
                Integer v = selector.apply(e.current());
                if (v != null)
                    sum = Math.addExact(sum, v);
            }
        }
        return sum;
    }

    public static <TSource> long sumLong(IEnumerable<TSource> source, LongFunc1<TSource> selector) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (selector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.selector);

        long sum = 0;
        try (IEnumerator<TSource> e = source.enumerator()) {
            while (e.moveNext())
                sum = Math.addExact(sum, selector.apply(e.current()));
        }
        return sum;
    }

    public static <TSource> long sumLongNull(IEnumerable<TSource> source, NullableLongFunc1<TSource> selector) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (selector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.selector);

        long sum = 0;
        try (IEnumerator<TSource> e = source.enumerator()) {
            while (e.moveNext()) {
                Long v = selector.apply(e.current());
                if (v != null)
                    sum = Math.addExact(sum, v);
            }
        }
        return sum;
    }

    public static <TSource> float sumFloat(IEnumerable<TSource> source, FloatFunc1<TSource> selector) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (selector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.selector);

        double sum = 0;
        try (IEnumerator<TSource> e = source.enumerator()) {
            while (e.moveNext())
                sum += selector.apply(e.current());
        }
        return (float) sum;
    }

    public static <TSource> float sumFloatNull(IEnumerable<TSource> source, NullableFloatFunc1<TSource> selector) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (selector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.selector);

        double sum = 0;
        try (IEnumerator<TSource> e = source.enumerator()) {
            while (e.moveNext()) {
                Float v = selector.apply(e.current());
                if (v != null)
                    sum += v;
            }
        }
        return (float) sum;
    }

    public static <TSource> double sumDouble(IEnumerable<TSource> source, DoubleFunc1<TSource> selector) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (selector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.selector);

        double sum = 0;
        try (IEnumerator<TSource> e = source.enumerator()) {
            while (e.moveNext())
                sum += selector.apply(e.current());
        }
        return sum;
    }

    public static <TSource> double sumDoubleNull(IEnumerable<TSource> source, NullableDoubleFunc1<TSource> selector) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (selector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.selector);

        double sum = 0;
        try (IEnumerator<TSource> e = source.enumerator()) {
            while (e.moveNext()) {
                Double v = selector.apply(e.current());
                if (v != null)
                    sum += v;
            }
        }
        return sum;
    }

    public static <TSource> BigDecimal sumDecimal(IEnumerable<TSource> source, DecimalFunc1<TSource> selector) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (selector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.selector);

        BigDecimal sum = BigDecimal.ZERO;
        try (IEnumerator<TSource> e = source.enumerator()) {
            while (e.moveNext())
                sum = sum.add(selector.apply(e.current()));
        }
        return sum;
    }

    public static <TSource> BigDecimal sumDecimalNull(IEnumerable<TSource> source, NullableDecimalFunc1<TSource> selector) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (selector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.selector);

        BigDecimal sum = BigDecimal.ZERO;
        try (IEnumerator<TSource> e = source.enumerator()) {
            while (e.moveNext()) {
                BigDecimal v = selector.apply(e.current());
                if (v != null)
                    sum = sum.add(v);
            }
        }
        return sum;
    }
}
