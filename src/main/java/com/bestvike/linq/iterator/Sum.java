package com.bestvike.linq.iterator;

import com.bestvike.function.Func1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.exception.Errors;

import java.math.BigDecimal;

/**
 * Created by 许崇雷 on 2018-05-08.
 */
public final class Sum {
    private Sum() {
    }

    public static int sumInt(IEnumerable<Integer> source) {
        if (source == null)
            throw Errors.argumentNull("source");

        int sum = 0;
        for (Integer v : source)
            if (v != null)
                sum = Math.addExact(sum, v);
        return sum;
    }

    public static long sumLong(IEnumerable<Long> source) {
        if (source == null)
            throw Errors.argumentNull("source");

        long sum = 0;
        for (Long v : source)
            if (v != null)
                sum = Math.addExact(sum, v);
        return sum;
    }

    public static float sumFloat(IEnumerable<Float> source) {
        if (source == null)
            throw Errors.argumentNull("source");

        double sum = 0;
        for (Float v : source)
            if (v != null)
                sum += v;
        return (float) sum;
    }

    public static double sumDouble(IEnumerable<Double> source) {
        if (source == null)
            throw Errors.argumentNull("source");

        double sum = 0;
        for (Double v : source)
            if (v != null)
                sum += v;
        return sum;
    }

    public static BigDecimal sumDecimal(IEnumerable<BigDecimal> source) {
        if (source == null)
            throw Errors.argumentNull("source");

        BigDecimal sum = BigDecimal.ZERO;
        for (BigDecimal v : source)
            if (v != null)
                sum = sum.add(v);
        return sum;
    }

    public static <TSource> int sumInt(IEnumerable<TSource> source, Func1<TSource, Integer> selector) {
        if (source == null)
            throw Errors.argumentNull("source");

        int sum = 0;
        for (TSource item : source) {
            Integer v = selector.apply(item);
            if (v != null)
                sum = Math.addExact(sum, v);
        }
        return sum;
    }

    public static <TSource> long sumLong(IEnumerable<TSource> source, Func1<TSource, Long> selector) {
        if (source == null)
            throw Errors.argumentNull("source");

        long sum = 0;
        for (TSource item : source) {
            Long v = selector.apply(item);
            if (v != null)
                sum = Math.addExact(sum, v);
        }
        return sum;
    }

    public static <TSource> float sumFloat(IEnumerable<TSource> source, Func1<TSource, Float> selector) {
        if (source == null)
            throw Errors.argumentNull("source");

        double sum = 0;
        for (TSource item : source) {
            Float v = selector.apply(item);
            if (v != null)
                sum += v;
        }
        return (float) sum;
    }

    public static <TSource> double sumDouble(IEnumerable<TSource> source, Func1<TSource, Double> selector) {
        if (source == null)
            throw Errors.argumentNull("source");

        double sum = 0;
        for (TSource item : source) {
            Double v = selector.apply(item);
            if (v != null)
                sum += v;
        }
        return sum;
    }

    public static <TSource> BigDecimal sumDecimal(IEnumerable<TSource> source, Func1<TSource, BigDecimal> selector) {
        if (source == null)
            throw Errors.argumentNull("source");

        BigDecimal sum = BigDecimal.ZERO;
        for (TSource item : source) {
            BigDecimal v = selector.apply(item);
            if (v != null)
                sum = sum.add(v);
        }
        return sum;
    }
}
