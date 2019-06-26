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
        for (Integer v : source)
            sum = Math.addExact(sum, v);
        return sum;
    }

    public static int sumIntNull(IEnumerable<Integer> source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        int sum = 0;
        for (Integer v : source)
            if (v != null)
                sum = Math.addExact(sum, v);
        return sum;
    }

    public static long sumLong(IEnumerable<Long> source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        long sum = 0;
        for (Long v : source)
            sum = Math.addExact(sum, v);
        return sum;
    }

    public static long sumLongNull(IEnumerable<Long> source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        long sum = 0;
        for (Long v : source)
            if (v != null)
                sum = Math.addExact(sum, v);
        return sum;
    }

    public static float sumFloat(IEnumerable<Float> source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        double sum = 0;
        for (Float v : source)
            sum += v;
        return (float) sum;
    }

    public static float sumFloatNull(IEnumerable<Float> source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        double sum = 0;
        for (Float v : source)
            if (v != null)
                sum += v;
        return (float) sum;
    }

    public static double sumDouble(IEnumerable<Double> source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        double sum = 0;
        for (Double v : source)
            sum += v;
        return sum;
    }

    public static double sumDoubleNull(IEnumerable<Double> source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        double sum = 0;
        for (Double v : source)
            if (v != null)
                sum += v;
        return sum;
    }

    public static BigDecimal sumDecimal(IEnumerable<BigDecimal> source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        BigDecimal sum = BigDecimal.ZERO;
        for (BigDecimal v : source)
            sum = sum.add(v);
        return sum;
    }

    public static BigDecimal sumDecimalNull(IEnumerable<BigDecimal> source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        BigDecimal sum = BigDecimal.ZERO;
        for (BigDecimal v : source)
            if (v != null)
                sum = sum.add(v);
        return sum;
    }

    public static <TSource> int sumInt(IEnumerable<TSource> source, IntFunc1<TSource> selector) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (selector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.selector);

        int sum = 0;
        for (TSource item : source)
            sum = Math.addExact(sum, selector.apply(item));
        return sum;
    }

    public static <TSource> int sumIntNull(IEnumerable<TSource> source, NullableIntFunc1<TSource> selector) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (selector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.selector);

        int sum = 0;
        for (TSource item : source) {
            Integer v = selector.apply(item);
            if (v != null)
                sum = Math.addExact(sum, v);
        }
        return sum;
    }

    public static <TSource> long sumLong(IEnumerable<TSource> source, LongFunc1<TSource> selector) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (selector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.selector);

        long sum = 0;
        for (TSource item : source)
            sum = Math.addExact(sum, selector.apply(item));
        return sum;
    }

    public static <TSource> long sumLongNull(IEnumerable<TSource> source, NullableLongFunc1<TSource> selector) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (selector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.selector);

        long sum = 0;
        for (TSource item : source) {
            Long v = selector.apply(item);
            if (v != null)
                sum = Math.addExact(sum, v);
        }
        return sum;
    }

    public static <TSource> float sumFloat(IEnumerable<TSource> source, FloatFunc1<TSource> selector) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (selector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.selector);

        double sum = 0;
        for (TSource item : source)
            sum += selector.apply(item);
        return (float) sum;
    }

    public static <TSource> float sumFloatNull(IEnumerable<TSource> source, NullableFloatFunc1<TSource> selector) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (selector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.selector);

        double sum = 0;
        for (TSource item : source) {
            Float v = selector.apply(item);
            if (v != null)
                sum += v;
        }
        return (float) sum;
    }

    public static <TSource> double sumDouble(IEnumerable<TSource> source, DoubleFunc1<TSource> selector) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (selector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.selector);

        double sum = 0;
        for (TSource item : source)
            sum += selector.apply(item);
        return sum;
    }

    public static <TSource> double sumDoubleNull(IEnumerable<TSource> source, NullableDoubleFunc1<TSource> selector) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (selector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.selector);

        double sum = 0;
        for (TSource item : source) {
            Double v = selector.apply(item);
            if (v != null)
                sum += v;
        }
        return sum;
    }

    public static <TSource> BigDecimal sumDecimal(IEnumerable<TSource> source, DecimalFunc1<TSource> selector) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (selector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.selector);

        BigDecimal sum = BigDecimal.ZERO;
        for (TSource item : source)
            sum = sum.add(selector.apply(item));
        return sum;
    }

    public static <TSource> BigDecimal sumDecimalNull(IEnumerable<TSource> source, NullableDecimalFunc1<TSource> selector) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (selector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.selector);

        BigDecimal sum = BigDecimal.ZERO;
        for (TSource item : source) {
            BigDecimal v = selector.apply(item);
            if (v != null)
                sum = sum.add(v);
        }
        return sum;
    }
}
