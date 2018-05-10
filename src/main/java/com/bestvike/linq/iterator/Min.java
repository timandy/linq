package com.bestvike.linq.iterator;

import com.bestvike.collections.generic.Comparer;
import com.bestvike.function.Func1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.exception.Errors;

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
            throw Errors.noElements();
        return value;
    }

    public static Integer minIntNull(IEnumerable<Integer> source) {
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
                    if (x != null && x < value)
                        value = x;
                }
                return value;
            }
        }
        return null;
    }

    public static long minLong(IEnumerable<Long> source) {
        Long value = minLongNull(source);
        if (value == null)
            throw Errors.noElements();
        return value;
    }

    public static Long minLongNull(IEnumerable<Long> source) {
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
                    if (x != null && x < value)
                        value = x;
                }
                return value;
            }
        }
        return null;
    }

    public static float minFloat(IEnumerable<Float> source) {
        Float value = minFloatNull(source);
        if (value == null)
            throw Errors.noElements();
        return value;
    }

    public static Float minFloatNull(IEnumerable<Float> source) {
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
                    if (x != null && (x < value || Float.isNaN(x)))
                        value = x;
                }
                return value;
            }
        }
        return null;
    }

    public static double minDouble(IEnumerable<Double> source) {
        Double value = minDoubleNull(source);
        if (value == null)
            throw Errors.noElements();
        return value;
    }

    public static Double minDoubleNull(IEnumerable<Double> source) {
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
                    if (x != null && (x < value || Double.isNaN(x)))
                        value = x;
                }
                return value;
            }
        }
        return null;
    }

    public static BigDecimal minDecimal(IEnumerable<BigDecimal> source) {
        BigDecimal value = minDecimalNull(source);
        if (value == null)
            throw Errors.noElements();
        return value;
    }

    public static BigDecimal minDecimalNull(IEnumerable<BigDecimal> source) {
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
                    if (x != null && x.compareTo(value) < 0)
                        value = x;
                }
                return value;
            }
        }
        return null;
    }

    public static <TSource> TSource min(IEnumerable<TSource> source) {
        TSource value = minNull(source);
        if (value == null)
            throw Errors.noElements();
        return value;
    }

    public static <TSource> TSource minNull(IEnumerable<TSource> source) {
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
                    if (x != null && comparer.compare(x, value) < 0)
                        value = x;
                }
                return value;
            }
        }
        return null;
    }

    public static <TSource> int minInt(IEnumerable<TSource> source, Func1<TSource, Integer> selector) {
        Integer value = minIntNull(source, selector);
        if (value == null)
            throw Errors.noElements();
        return value;
    }

    public static <TSource> Integer minIntNull(IEnumerable<TSource> source, Func1<TSource, Integer> selector) {
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
                    if (x != null && x < value)
                        value = x;
                }
                return value;
            }
        }
        return null;
    }

    public static <TSource> long minLong(IEnumerable<TSource> source, Func1<TSource, Long> selector) {
        Long value = minLongNull(source, selector);
        if (value == null)
            throw Errors.noElements();
        return value;
    }

    public static <TSource> Long minLongNull(IEnumerable<TSource> source, Func1<TSource, Long> selector) {
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
                    if (x != null && x < value)
                        value = x;
                }
                return value;
            }
        }
        return null;
    }

    public static <TSource> float minFloat(IEnumerable<TSource> source, Func1<TSource, Float> selector) {
        Float value = minFloatNull(source, selector);
        if (value == null)
            throw Errors.noElements();
        return value;
    }

    public static <TSource> Float minFloatNull(IEnumerable<TSource> source, Func1<TSource, Float> selector) {
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
                    if (x != null && (x < value || Float.isNaN(x)))
                        value = x;
                }
                return value;
            }
        }
        return null;
    }

    public static <TSource> double minDouble(IEnumerable<TSource> source, Func1<TSource, Double> selector) {
        Double value = minDoubleNull(source, selector);
        if (value == null)
            throw Errors.noElements();
        return value;
    }

    public static <TSource> Double minDoubleNull(IEnumerable<TSource> source, Func1<TSource, Double> selector) {
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
                    if (x != null && (x < value || Double.isNaN(x)))
                        value = x;
                }
                return value;
            }
        }
        return null;
    }

    public static <TSource> BigDecimal minDecimal(IEnumerable<TSource> source, Func1<TSource, BigDecimal> selector) {
        BigDecimal value = minDecimalNull(source, selector);
        if (value == null)
            throw Errors.noElements();
        return value;
    }

    public static <TSource> BigDecimal minDecimalNull(IEnumerable<TSource> source, Func1<TSource, BigDecimal> selector) {
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
                    if (x != null && x.compareTo(value) < 0)
                        value = x;
                }
                return value;
            }
        }
        return null;
    }

    public static <TSource, TResult> TResult min(IEnumerable<TSource> source, Func1<TSource, TResult> selector) {
        TResult value = minNull(source, selector);
        if (value == null)
            throw Errors.noElements();
        return value;
    }

    public static <TSource, TResult> TResult minNull(IEnumerable<TSource> source, Func1<TSource, TResult> selector) {
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
                    if (x != null && comparer.compare(x, value) < 0)
                        value = x;
                }
                return value;
            }
        }
        return null;
    }
}
