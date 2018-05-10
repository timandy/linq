package com.bestvike.linq.iterator;

import com.bestvike.collections.generic.Comparer;
import com.bestvike.function.Func1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.exception.Errors;

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
            throw Errors.noElements();
        return value;
    }

    public static Integer maxIntNull(IEnumerable<Integer> source) {
        if (source == null)
            throw Errors.argumentNull("source");

        Integer value;
        try (IEnumerator<Integer> e = source.enumerator()) {
            while (e.moveNext()) {
                value = e.current();
                if (value == null)
                    continue;
                while (e.moveNext()) {
                    Integer x = e.current();
                    if (x != null && x > value)
                        value = x;
                }
                return value;
            }
        }
        return null;
    }

    public static long maxLong(IEnumerable<Long> source) {
        Long value = maxLongNull(source);
        if (value == null)
            throw Errors.noElements();
        return value;
    }

    public static Long maxLongNull(IEnumerable<Long> source) {
        if (source == null)
            throw Errors.argumentNull("source");

        Long value;
        try (IEnumerator<Long> e = source.enumerator()) {
            while (e.moveNext()) {
                value = e.current();
                if (value == null)
                    continue;
                while (e.moveNext()) {
                    Long x = e.current();
                    if (x != null && x > value)
                        value = x;
                }
                return value;
            }
        }
        return null;
    }

    public static float maxFloat(IEnumerable<Float> source) {
        Float value = maxFloatNull(source);
        if (value == null)
            throw Errors.noElements();
        return value;
    }

    public static Float maxFloatNull(IEnumerable<Float> source) {
        if (source == null)
            throw Errors.argumentNull("source");

        Float value;
        try (IEnumerator<Float> e = source.enumerator()) {
            while (e.moveNext()) {
                value = e.current();
                if (value == null)
                    continue;
                while (e.moveNext()) {
                    Float x = e.current();
                    if (x != null && (x > value || Float.isNaN(value)))
                        value = x;
                }
                return value;
            }
        }
        return null;
    }

    public static double maxDouble(IEnumerable<Double> source) {
        Double value = maxDoubleNull(source);
        if (value == null)
            throw Errors.noElements();
        return value;
    }

    public static Double maxDoubleNull(IEnumerable<Double> source) {
        if (source == null)
            throw Errors.argumentNull("source");

        Double value;
        try (IEnumerator<Double> e = source.enumerator()) {
            while (e.moveNext()) {
                value = e.current();
                if (value == null)
                    continue;
                while (e.moveNext()) {
                    Double x = e.current();
                    if (x != null && (x > value || Double.isNaN(value)))
                        value = x;
                }
                return value;
            }
        }
        return null;
    }

    public static BigDecimal maxDecimal(IEnumerable<BigDecimal> source) {
        BigDecimal value = maxDecimalNull(source);
        if (value == null)
            throw Errors.noElements();
        return value;
    }

    public static BigDecimal maxDecimalNull(IEnumerable<BigDecimal> source) {
        if (source == null)
            throw Errors.argumentNull("source");

        BigDecimal value;
        try (IEnumerator<BigDecimal> e = source.enumerator()) {
            while (e.moveNext()) {
                value = e.current();
                if (value == null)
                    continue;
                while (e.moveNext()) {
                    BigDecimal x = e.current();
                    if (x != null && x.compareTo(value) > 0)
                        value = x;
                }
                return value;
            }
        }
        return null;
    }

    public static <TSource> TSource max(IEnumerable<TSource> source) {
        TSource value = maxNull(source);
        if (value == null)
            throw Errors.noElements();
        return value;
    }

    public static <TSource> TSource maxNull(IEnumerable<TSource> source) {
        if (source == null)
            throw Errors.argumentNull("source");

        Comparator<TSource> comparer = Comparer.Default();
        TSource value;
        try (IEnumerator<TSource> e = source.enumerator()) {
            while (e.moveNext()) {
                value = e.current();
                if (value == null)
                    continue;
                while (e.moveNext()) {
                    TSource x = e.current();
                    if (x != null && comparer.compare(x, value) > 0)
                        value = x;
                }
                return value;
            }
        }
        return null;
    }

    public static <TSource> int maxInt(IEnumerable<TSource> source, Func1<TSource, Integer> selector) {
        Integer value = maxIntNull(source, selector);
        if (value == null)
            throw Errors.noElements();
        return value;
    }

    public static <TSource> Integer maxIntNull(IEnumerable<TSource> source, Func1<TSource, Integer> selector) {
        if (source == null)
            throw Errors.argumentNull("source");
        if (selector == null)
            throw Errors.argumentNull("selector");

        Integer value;
        try (IEnumerator<TSource> e = source.enumerator()) {
            while (e.moveNext()) {
                value = selector.apply(e.current());
                if (value == null)
                    continue;
                while (e.moveNext()) {
                    Integer x = selector.apply(e.current());
                    if (x != null && x > value)
                        value = x;
                }
                return value;
            }
        }
        return null;
    }

    public static <TSource> long maxLong(IEnumerable<TSource> source, Func1<TSource, Long> selector) {
        Long value = maxLongNull(source, selector);
        if (value == null)
            throw Errors.noElements();
        return value;
    }

    public static <TSource> Long maxLongNull(IEnumerable<TSource> source, Func1<TSource, Long> selector) {
        if (source == null)
            throw Errors.argumentNull("source");
        if (selector == null)
            throw Errors.argumentNull("selector");

        Long value;
        try (IEnumerator<TSource> e = source.enumerator()) {
            while (e.moveNext()) {
                value = selector.apply(e.current());
                if (value == null)
                    continue;
                while (e.moveNext()) {
                    Long x = selector.apply(e.current());
                    if (x != null && x > value)
                        value = x;
                }
                return value;
            }
        }
        return null;
    }

    public static <TSource> float maxFloat(IEnumerable<TSource> source, Func1<TSource, Float> selector) {
        Float value = maxFloatNull(source, selector);
        if (value == null)
            throw Errors.noElements();
        return value;
    }

    public static <TSource> Float maxFloatNull(IEnumerable<TSource> source, Func1<TSource, Float> selector) {
        if (source == null)
            throw Errors.argumentNull("source");
        if (selector == null)
            throw Errors.argumentNull("selector");

        Float value;
        try (IEnumerator<TSource> e = source.enumerator()) {
            while (e.moveNext()) {
                value = selector.apply(e.current());
                if (value == null)
                    continue;
                while (e.moveNext()) {
                    Float x = selector.apply(e.current());
                    if (x != null && (x > value || Float.isNaN(value)))
                        value = x;
                }
                return value;
            }
        }
        return null;
    }

    public static <TSource> double maxDouble(IEnumerable<TSource> source, Func1<TSource, Double> selector) {
        Double value = maxDoubleNull(source, selector);
        if (value == null)
            throw Errors.noElements();
        return value;
    }

    public static <TSource> Double maxDoubleNull(IEnumerable<TSource> source, Func1<TSource, Double> selector) {
        if (source == null)
            throw Errors.argumentNull("source");
        if (selector == null)
            throw Errors.argumentNull("selector");

        Double value;
        try (IEnumerator<TSource> e = source.enumerator()) {
            while (e.moveNext()) {
                value = selector.apply(e.current());
                if (value == null)
                    continue;
                while (e.moveNext()) {
                    Double x = selector.apply(e.current());
                    if (x != null && (x > value || Double.isNaN(value)))
                        value = x;
                }
                return value;
            }
        }
        return null;
    }

    public static <TSource> BigDecimal maxDecimal(IEnumerable<TSource> source, Func1<TSource, BigDecimal> selector) {
        BigDecimal value = maxDecimalNull(source, selector);
        if (value == null)
            throw Errors.noElements();
        return value;
    }

    public static <TSource> BigDecimal maxDecimalNull(IEnumerable<TSource> source, Func1<TSource, BigDecimal> selector) {
        if (source == null)
            throw Errors.argumentNull("source");
        if (selector == null)
            throw Errors.argumentNull("selector");

        BigDecimal value;
        try (IEnumerator<TSource> e = source.enumerator()) {
            while (e.moveNext()) {
                value = selector.apply(e.current());
                if (value == null)
                    continue;
                while (e.moveNext()) {
                    BigDecimal x = selector.apply(e.current());
                    if (x != null && x.compareTo(value) > 0)
                        value = x;
                }
                return value;
            }
        }
        return null;
    }

    public static <TSource, TResult> TResult max(IEnumerable<TSource> source, Func1<TSource, TResult> selector) {
        TResult value = maxNull(source, selector);
        if (value == null)
            throw Errors.noElements();
        return value;
    }

    public static <TSource, TResult> TResult maxNull(IEnumerable<TSource> source, Func1<TSource, TResult> selector) {
        if (source == null)
            throw Errors.argumentNull("source");
        if (selector == null)
            throw Errors.argumentNull("selector");

        Comparator<TResult> comparer = Comparer.Default();
        TResult value;
        try (IEnumerator<TSource> e = source.enumerator()) {
            while (e.moveNext()) {
                value = selector.apply(e.current());
                if (value == null)
                    continue;
                while (e.moveNext()) {
                    TResult x = selector.apply(e.current());
                    if (x != null && comparer.compare(x, value) > 0)
                        value = x;
                }
                return value;
            }
        }
        return null;
    }
}
