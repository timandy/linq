package com.bestvike.linq.iterator;

import com.bestvike.collections.generic.Comparer;
import com.bestvike.function.Func1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.exception.Errors;

import java.math.BigDecimal;
import java.util.Comparator;

/**
 * Created by 许崇雷 on 2018-05-09.
 */
public final class MinBy {
    private MinBy() {
    }

    public static <TSource> TSource minByInt(IEnumerable<TSource> source, Func1<TSource, Integer> keySelector) {
        if (source == null)
            throw Errors.argumentNull("source");
        if (keySelector == null)
            throw Errors.argumentNull("keySelector");

        TSource value;
        Integer key;
        try (IEnumerator<TSource> e = source.enumerator()) {
            while (e.moveNext()) {
                value = e.current();
                key = keySelector.apply(value);
                if (key == null)
                    continue;
                while (e.moveNext()) {
                    TSource curValue = e.current();
                    Integer curKey = keySelector.apply(curValue);
                    if (curKey != null && curKey < key) {
                        value = curValue;
                        key = curKey;
                    }
                }
                return value;
            }
        }
        throw Errors.noElements();
    }

    public static <TSource> TSource minByIntNull(IEnumerable<TSource> source, Func1<TSource, Integer> keySelector) {
        if (source == null)
            throw Errors.argumentNull("source");
        if (keySelector == null)
            throw Errors.argumentNull("keySelector");

        TSource value;
        Integer key;
        try (IEnumerator<TSource> e = source.enumerator()) {
            while (e.moveNext()) {
                value = e.current();
                key = keySelector.apply(value);
                if (key == null)
                    continue;
                while (e.moveNext()) {
                    TSource curValue = e.current();
                    Integer curKey = keySelector.apply(curValue);
                    if (curKey != null && curKey < key) {
                        value = curValue;
                        key = curKey;
                    }
                }
                return value;
            }
        }
        return null;
    }

    public static <TSource> TSource minByLong(IEnumerable<TSource> source, Func1<TSource, Long> keySelector) {
        if (source == null)
            throw Errors.argumentNull("source");
        if (keySelector == null)
            throw Errors.argumentNull("keySelector");

        TSource value;
        Long key;
        try (IEnumerator<TSource> e = source.enumerator()) {
            while (e.moveNext()) {
                value = e.current();
                key = keySelector.apply(value);
                if (key == null)
                    continue;
                while (e.moveNext()) {
                    TSource curValue = e.current();
                    Long curKey = keySelector.apply(curValue);
                    if (curKey != null && curKey < key) {
                        value = curValue;
                        key = curKey;
                    }
                }
                return value;
            }
        }
        throw Errors.noElements();
    }

    public static <TSource> TSource minByLongNull(IEnumerable<TSource> source, Func1<TSource, Long> keySelector) {
        if (source == null)
            throw Errors.argumentNull("source");
        if (keySelector == null)
            throw Errors.argumentNull("keySelector");

        TSource value;
        Long key;
        try (IEnumerator<TSource> e = source.enumerator()) {
            while (e.moveNext()) {
                value = e.current();
                key = keySelector.apply(value);
                if (key == null)
                    continue;
                while (e.moveNext()) {
                    TSource curValue = e.current();
                    Long curKey = keySelector.apply(curValue);
                    if (curKey != null && curKey < key) {
                        value = curValue;
                        key = curKey;
                    }
                }
                return value;
            }
        }
        return null;
    }

    public static <TSource> TSource minByFloat(IEnumerable<TSource> source, Func1<TSource, Float> keySelector) {
        if (source == null)
            throw Errors.argumentNull("source");
        if (keySelector == null)
            throw Errors.argumentNull("keySelector");

        TSource value;
        Float key;
        try (IEnumerator<TSource> e = source.enumerator()) {
            while (e.moveNext()) {
                value = e.current();
                key = keySelector.apply(value);
                if (key == null)
                    continue;
                while (e.moveNext()) {
                    TSource curValue = e.current();
                    Float curKey = keySelector.apply(curValue);
                    if (curKey != null && (curKey < key || Float.isNaN(curKey))) {
                        value = curValue;
                        key = curKey;
                    }
                }
                return value;
            }
        }
        throw Errors.noElements();
    }

    public static <TSource> TSource minByFloatNull(IEnumerable<TSource> source, Func1<TSource, Float> keySelector) {
        if (source == null)
            throw Errors.argumentNull("source");
        if (keySelector == null)
            throw Errors.argumentNull("keySelector");

        TSource value;
        Float key;
        try (IEnumerator<TSource> e = source.enumerator()) {
            while (e.moveNext()) {
                value = e.current();
                key = keySelector.apply(value);
                if (key == null)
                    continue;
                while (e.moveNext()) {
                    TSource curValue = e.current();
                    Float curKey = keySelector.apply(curValue);
                    if (curKey != null && (curKey < key || Float.isNaN(curKey))) {
                        value = curValue;
                        key = curKey;
                    }
                }
                return value;
            }
        }
        return null;
    }

    public static <TSource> TSource minByDouble(IEnumerable<TSource> source, Func1<TSource, Double> keySelector) {
        if (source == null)
            throw Errors.argumentNull("source");
        if (keySelector == null)
            throw Errors.argumentNull("keySelector");

        TSource value;
        Double key;
        try (IEnumerator<TSource> e = source.enumerator()) {
            while (e.moveNext()) {
                value = e.current();
                key = keySelector.apply(value);
                if (key == null)
                    continue;
                while (e.moveNext()) {
                    TSource curValue = e.current();
                    Double curKey = keySelector.apply(curValue);
                    if (curKey != null && (curKey < key || Double.isNaN(curKey))) {
                        value = curValue;
                        key = curKey;
                    }
                }
                return value;
            }
        }
        throw Errors.noElements();
    }

    public static <TSource> TSource minByDoubleNull(IEnumerable<TSource> source, Func1<TSource, Double> keySelector) {
        if (source == null)
            throw Errors.argumentNull("source");
        if (keySelector == null)
            throw Errors.argumentNull("keySelector");

        TSource value;
        Double key;
        try (IEnumerator<TSource> e = source.enumerator()) {
            while (e.moveNext()) {
                value = e.current();
                key = keySelector.apply(value);
                if (key == null)
                    continue;
                while (e.moveNext()) {
                    TSource curValue = e.current();
                    Double curKey = keySelector.apply(curValue);
                    if (curKey != null && (curKey < key || Double.isNaN(curKey))) {
                        value = curValue;
                        key = curKey;
                    }
                }
                return value;
            }
        }
        return null;
    }

    public static <TSource> TSource minByDecimal(IEnumerable<TSource> source, Func1<TSource, BigDecimal> keySelector) {
        if (source == null)
            throw Errors.argumentNull("source");
        if (keySelector == null)
            throw Errors.argumentNull("keySelector");

        TSource value;
        BigDecimal key;
        try (IEnumerator<TSource> e = source.enumerator()) {
            while (e.moveNext()) {
                value = e.current();
                key = keySelector.apply(value);
                if (key == null)
                    continue;
                while (e.moveNext()) {
                    TSource curValue = e.current();
                    BigDecimal curKey = keySelector.apply(curValue);
                    if (curKey != null && curKey.compareTo(key) < 0) {
                        value = curValue;
                        key = curKey;
                    }
                }
                return value;
            }
        }
        throw Errors.noElements();
    }

    public static <TSource> TSource minByDecimalNull(IEnumerable<TSource> source, Func1<TSource, BigDecimal> keySelector) {
        if (source == null)
            throw Errors.argumentNull("source");
        if (keySelector == null)
            throw Errors.argumentNull("keySelector");

        TSource value;
        BigDecimal key;
        try (IEnumerator<TSource> e = source.enumerator()) {
            while (e.moveNext()) {
                value = e.current();
                key = keySelector.apply(value);
                if (key == null)
                    continue;
                while (e.moveNext()) {
                    TSource curValue = e.current();
                    BigDecimal curKey = keySelector.apply(curValue);
                    if (curKey != null && curKey.compareTo(key) < 0) {
                        value = curValue;
                        key = curKey;
                    }
                }
                return value;
            }
        }
        return null;
    }

    public static <TSource, TKey> TSource minBy(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector) {
        if (source == null)
            throw Errors.argumentNull("source");
        if (keySelector == null)
            throw Errors.argumentNull("keySelector");

        Comparator<TKey> comparer = Comparer.Default();
        TSource value;
        TKey key;
        try (IEnumerator<TSource> e = source.enumerator()) {
            while (e.moveNext()) {
                value = e.current();
                key = keySelector.apply(value);
                if (key == null)
                    continue;
                while (e.moveNext()) {
                    TSource curValue = e.current();
                    TKey curKey = keySelector.apply(curValue);
                    if (curKey != null && comparer.compare(curKey, key) < 0) {
                        value = curValue;
                        key = curKey;
                    }
                }
                return value;
            }
        }
        throw Errors.noElements();
    }

    public static <TSource, TKey> TSource minByNull(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector) {
        if (source == null)
            throw Errors.argumentNull("source");
        if (keySelector == null)
            throw Errors.argumentNull("keySelector");

        Comparator<TKey> comparer = Comparer.Default();
        TSource value;
        TKey key;
        try (IEnumerator<TSource> e = source.enumerator()) {
            while (e.moveNext()) {
                value = e.current();
                key = keySelector.apply(value);
                if (key == null)
                    continue;
                while (e.moveNext()) {
                    TSource curValue = e.current();
                    TKey curKey = keySelector.apply(curValue);
                    if (curKey != null && comparer.compare(curKey, key) < 0) {
                        value = curValue;
                        key = curKey;
                    }
                }
                return value;
            }
        }
        return null;
    }
}
