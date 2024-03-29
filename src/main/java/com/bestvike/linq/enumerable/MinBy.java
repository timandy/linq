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
 * Created by 许崇雷 on 2018-05-09.
 */
public final class MinBy {
    private MinBy() {
    }

    public static <TSource> TSource minByInt(IEnumerable<TSource> source, IntFunc1<TSource> keySelector) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (keySelector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.keySelector);

        try (IEnumerator<TSource> e = source.enumerator()) {
            if (!e.moveNext())
                ThrowHelper.throwNoElementsException();

            TSource value = e.current();
            int key = keySelector.apply(value);
            while (e.moveNext()) {
                TSource curValue = e.current();
                int curKey = keySelector.apply(curValue);
                if (curKey < key) {
                    value = curValue;
                    key = curKey;
                }
            }
            return value;
        }
    }

    public static <TSource> TSource minByIntNull(IEnumerable<TSource> source, NullableIntFunc1<TSource> keySelector) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (keySelector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.keySelector);

        try (IEnumerator<TSource> e = source.enumerator()) {
            if (!e.moveNext())
                return null;

            TSource value = e.current();
            Integer key = keySelector.apply(value);
            while (key == null) {
                if (!e.moveNext())
                    return value;
                value = e.current();
                key = keySelector.apply(value);
            }

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

    public static <TSource> TSource minByLong(IEnumerable<TSource> source, LongFunc1<TSource> keySelector) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (keySelector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.keySelector);

        try (IEnumerator<TSource> e = source.enumerator()) {
            if (!e.moveNext())
                ThrowHelper.throwNoElementsException();

            TSource value = e.current();
            long key = keySelector.apply(value);
            while (e.moveNext()) {
                TSource curValue = e.current();
                long curKey = keySelector.apply(curValue);
                if (curKey < key) {
                    value = curValue;
                    key = curKey;
                }
            }
            return value;
        }
    }

    public static <TSource> TSource minByLongNull(IEnumerable<TSource> source, NullableLongFunc1<TSource> keySelector) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (keySelector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.keySelector);

        try (IEnumerator<TSource> e = source.enumerator()) {
            if (!e.moveNext())
                return null;

            TSource value = e.current();
            Long key = keySelector.apply(value);
            while (key == null) {
                if (!e.moveNext())
                    return value;
                value = e.current();
                key = keySelector.apply(value);
            }

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

    public static <TSource> TSource minByFloat(IEnumerable<TSource> source, FloatFunc1<TSource> keySelector) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (keySelector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.keySelector);

        try (IEnumerator<TSource> e = source.enumerator()) {
            if (!e.moveNext())
                ThrowHelper.throwNoElementsException();

            TSource value = e.current();
            float key = keySelector.apply(value);
            Comparator<Float> comparer = Comparer.Float();
            while (e.moveNext()) {
                TSource curValue = e.current();
                float curKey = keySelector.apply(curValue);
                if (comparer.compare(curKey, key) < 0) {
                    value = curValue;
                    key = curKey;
                }
            }
            return value;
        }
    }

    public static <TSource> TSource minByFloatNull(IEnumerable<TSource> source, NullableFloatFunc1<TSource> keySelector) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (keySelector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.keySelector);

        try (IEnumerator<TSource> e = source.enumerator()) {
            if (!e.moveNext())
                return null;

            TSource value = e.current();
            Float key = keySelector.apply(value);
            while (key == null) {
                if (!e.moveNext())
                    return value;
                value = e.current();
                key = keySelector.apply(value);
            }

            Comparator<Float> comparer = Comparer.Float();
            while (e.moveNext()) {
                TSource curValue = e.current();
                Float curKey = keySelector.apply(curValue);
                if (curKey != null && comparer.compare(curKey, key) < 0) {
                    value = curValue;
                    key = curKey;
                }
            }
            return value;
        }
    }

    public static <TSource> TSource minByDouble(IEnumerable<TSource> source, DoubleFunc1<TSource> keySelector) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (keySelector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.keySelector);

        try (IEnumerator<TSource> e = source.enumerator()) {
            if (!e.moveNext())
                ThrowHelper.throwNoElementsException();

            TSource value = e.current();
            double key = keySelector.apply(value);
            Comparator<Double> comparer = Comparer.Double();
            while (e.moveNext()) {
                TSource curValue = e.current();
                double curKey = keySelector.apply(curValue);
                if (comparer.compare(curKey, key) < 0) {
                    value = curValue;
                    key = curKey;
                }
            }
            return value;
        }
    }

    public static <TSource> TSource minByDoubleNull(IEnumerable<TSource> source, NullableDoubleFunc1<TSource> keySelector) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (keySelector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.keySelector);

        try (IEnumerator<TSource> e = source.enumerator()) {
            if (!e.moveNext())
                return null;

            TSource value = e.current();
            Double key = keySelector.apply(value);
            while (key == null) {
                if (!e.moveNext())
                    return value;
                value = e.current();
                key = keySelector.apply(value);
            }

            Comparator<Double> comparer = Comparer.Double();
            while (e.moveNext()) {
                TSource curValue = e.current();
                Double curKey = keySelector.apply(curValue);
                if (curKey != null && comparer.compare(curKey, key) < 0) {
                    value = curValue;
                    key = curKey;
                }
            }
            return value;
        }
    }

    public static <TSource> TSource minByDecimal(IEnumerable<TSource> source, DecimalFunc1<TSource> keySelector) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (keySelector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.keySelector);

        try (IEnumerator<TSource> e = source.enumerator()) {
            if (!e.moveNext())
                ThrowHelper.throwNoElementsException();

            TSource value = e.current();
            BigDecimal key = keySelector.apply(value);
            if (key == null)
                ThrowHelper.throwNullPointerException();
            while (e.moveNext()) {
                TSource curValue = e.current();
                BigDecimal curKey = keySelector.apply(curValue);
                if (curKey == null)
                    ThrowHelper.throwNullPointerException();
                if (curKey.compareTo(key) < 0) {
                    value = curValue;
                    key = curKey;
                }
            }
            return value;
        }
    }

    public static <TSource> TSource minByDecimalNull(IEnumerable<TSource> source, NullableDecimalFunc1<TSource> keySelector) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (keySelector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.keySelector);

        try (IEnumerator<TSource> e = source.enumerator()) {
            if (!e.moveNext())
                return null;

            TSource value = e.current();
            BigDecimal key = keySelector.apply(value);
            while (key == null) {
                if (!e.moveNext())
                    return value;
                value = e.current();
                key = keySelector.apply(value);
            }

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

    public static <TSource, TKey> TSource minBy(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector) {
        return minBy(source, keySelector, null);
    }

    public static <TSource, TKey> TSource minBy(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector, Comparator<TKey> comparer) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (keySelector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.keySelector);
        if (comparer == null)
            comparer = Comparer.Default();

        try (IEnumerator<TSource> e = source.enumerator()) {
            if (!e.moveNext())
                ThrowHelper.throwNoElementsException();

            TSource value = e.current();
            TKey key = keySelector.apply(value);
            if (key == null)
                ThrowHelper.throwNullPointerException();
            while (e.moveNext()) {
                TSource curValue = e.current();
                TKey curKey = keySelector.apply(curValue);
                if (curKey == null)
                    ThrowHelper.throwNullPointerException();
                if (comparer.compare(curKey, key) < 0) {
                    value = curValue;
                    key = curKey;
                }
            }
            return value;
        }
    }

    public static <TSource, TKey> TSource minByNull(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector) {
        return minByNull(source, keySelector, null);
    }

    public static <TSource, TKey> TSource minByNull(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector, Comparator<TKey> comparer) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (keySelector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.keySelector);
        if (comparer == null)
            comparer = Comparer.Default();

        try (IEnumerator<TSource> e = source.enumerator()) {
            if (!e.moveNext())
                return null;

            TSource value = e.current();
            TKey key = keySelector.apply(value);
            while (key == null) {
                if (!e.moveNext())
                    return value;
                value = e.current();
                key = keySelector.apply(value);
            }

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
}
