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
import java.math.MathContext;

/**
 * Created by 许崇雷 on 2018-04-22.
 */
public final class Average {
    private Average() {
    }

    public static double averageInt(IEnumerable<Integer> source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        try (IEnumerator<Integer> e = source.enumerator()) {
            if (!e.moveNext())
                ThrowHelper.throwNoElementsException();

            long sum = e.current();
            long count = 1;
            while (e.moveNext()) {
                sum = Math.addExact(sum, e.current());
                count = Math.addExact(count, 1);
            }
            return (double) sum / count;
        }
    }

    public static Double averageIntNull(IEnumerable<Integer> source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        try (IEnumerator<Integer> e = source.enumerator()) {
            while (e.moveNext()) {
                Integer v = e.current();
                if (v != null) {
                    long sum = v;
                    long count = 1;
                    while (e.moveNext()) {
                        v = e.current();
                        if (v != null) {
                            sum = Math.addExact(sum, v);
                            count = Math.addExact(count, 1);
                        }
                    }
                    return (double) sum / count;
                }
            }
        }
        return null;
    }

    public static double averageLong(IEnumerable<Long> source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        try (IEnumerator<Long> e = source.enumerator()) {
            if (!e.moveNext())
                ThrowHelper.throwNoElementsException();

            long sum = e.current();
            long count = 1;
            while (e.moveNext()) {
                sum = Math.addExact(sum, e.current());
                count = Math.addExact(count, 1);
            }
            return (double) sum / count;
        }
    }

    public static Double averageLongNull(IEnumerable<Long> source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        try (IEnumerator<Long> e = source.enumerator()) {
            while (e.moveNext()) {
                Long v = e.current();
                if (v != null) {
                    long sum = v;
                    long count = 1;
                    while (e.moveNext()) {
                        v = e.current();
                        if (v != null) {
                            sum = Math.addExact(sum, v);
                            count = Math.addExact(count, 1);
                        }
                    }
                    return (double) sum / count;
                }
            }
        }
        return null;
    }

    public static float averageFloat(IEnumerable<Float> source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        try (IEnumerator<Float> e = source.enumerator()) {
            if (!e.moveNext())
                ThrowHelper.throwNoElementsException();

            double sum = e.current();
            long count = 1;
            while (e.moveNext()) {
                sum += e.current();
                count = Math.addExact(count, 1);
            }
            return (float) (sum / count);
        }
    }

    public static Float averageFloatNull(IEnumerable<Float> source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        try (IEnumerator<Float> e = source.enumerator()) {
            while (e.moveNext()) {
                Float v = e.current();
                if (v != null) {
                    double sum = v;
                    long count = 1;
                    while (e.moveNext()) {
                        v = e.current();
                        if (v != null) {
                            sum += v;
                            count = Math.addExact(count, 1);
                        }
                    }
                    return (float) (sum / count);
                }
            }
        }
        return null;
    }

    public static double averageDouble(IEnumerable<Double> source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        try (IEnumerator<Double> e = source.enumerator()) {
            if (!e.moveNext())
                ThrowHelper.throwNoElementsException();

            double sum = e.current();
            long count = 1;
            while (e.moveNext()) {
                sum += e.current();
                count = Math.addExact(count, 1);
            }
            return sum / count;
        }
    }

    public static Double averageDoubleNull(IEnumerable<Double> source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        try (IEnumerator<Double> e = source.enumerator()) {
            while (e.moveNext()) {
                Double v = e.current();
                if (v != null) {
                    double sum = v;
                    long count = 1;
                    while (e.moveNext()) {
                        v = e.current();
                        if (v != null) {
                            sum += v;
                            count = Math.addExact(count, 1);
                        }
                    }
                    return sum / count;
                }
            }
        }
        return null;
    }

    public static BigDecimal averageDecimal(IEnumerable<BigDecimal> source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        try (IEnumerator<BigDecimal> e = source.enumerator()) {
            if (!e.moveNext())
                ThrowHelper.throwNoElementsException();

            BigDecimal sum = e.current();
            long count = 1;
            while (e.moveNext()) {
                sum = sum.add(e.current());
                count = Math.addExact(count, 1);
            }
            return sum.divide(BigDecimal.valueOf(count), MathContext.DECIMAL128);
        }
    }

    public static BigDecimal averageDecimalNull(IEnumerable<BigDecimal> source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        try (IEnumerator<BigDecimal> e = source.enumerator()) {
            while (e.moveNext()) {
                BigDecimal v = e.current();
                if (v != null) {
                    BigDecimal sum = v;
                    long count = 1;
                    while (e.moveNext()) {
                        v = e.current();
                        if (v != null) {
                            sum = sum.add(v);
                            count = Math.addExact(count, 1);
                        }
                    }
                    return sum.divide(BigDecimal.valueOf(count), MathContext.DECIMAL128);
                }
            }
        }
        return null;
    }

    public static <TSource> double averageInt(IEnumerable<TSource> source, IntFunc1<TSource> selector) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (selector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.selector);

        try (IEnumerator<TSource> e = source.enumerator()) {
            if (!e.moveNext())
                ThrowHelper.throwNoElementsException();

            long sum = selector.apply(e.current());
            long count = 1;
            while (e.moveNext()) {
                sum = Math.addExact(sum, selector.apply(e.current()));
                count = Math.addExact(count, 1);
            }
            return (double) sum / count;
        }
    }

    public static <TSource> Double averageIntNull(IEnumerable<TSource> source, NullableIntFunc1<TSource> selector) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (selector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.selector);

        try (IEnumerator<TSource> e = source.enumerator()) {
            while (e.moveNext()) {
                Integer v = selector.apply(e.current());
                if (v != null) {
                    long sum = v;
                    long count = 1;
                    while (e.moveNext()) {
                        v = selector.apply(e.current());
                        if (v != null) {
                            sum = Math.addExact(sum, v);
                            count = Math.addExact(count, 1);
                        }
                    }
                    return (double) sum / count;
                }
            }
        }
        return null;
    }

    public static <TSource> double averageLong(IEnumerable<TSource> source, LongFunc1<TSource> selector) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (selector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.selector);

        try (IEnumerator<TSource> e = source.enumerator()) {
            if (!e.moveNext())
                ThrowHelper.throwNoElementsException();

            long sum = selector.apply(e.current());
            long count = 1;
            while (e.moveNext()) {
                sum = Math.addExact(sum, selector.apply(e.current()));
                count = Math.addExact(count, 1);
            }
            return (double) sum / count;
        }
    }

    public static <TSource> Double averageLongNull(IEnumerable<TSource> source, NullableLongFunc1<TSource> selector) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (selector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.selector);

        try (IEnumerator<TSource> e = source.enumerator()) {
            while (e.moveNext()) {
                Long v = selector.apply(e.current());
                if (v != null) {
                    long sum = v;
                    long count = 1;
                    while (e.moveNext()) {
                        v = selector.apply(e.current());
                        if (v != null) {
                            sum = Math.addExact(sum, v);
                            count = Math.addExact(count, 1);
                        }
                    }
                    return (double) sum / count;
                }
            }
        }
        return null;
    }

    public static <TSource> float averageFloat(IEnumerable<TSource> source, FloatFunc1<TSource> selector) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (selector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.selector);

        try (IEnumerator<TSource> e = source.enumerator()) {
            if (!e.moveNext())
                ThrowHelper.throwNoElementsException();

            double sum = selector.apply(e.current());
            long count = 1;
            while (e.moveNext()) {
                sum += selector.apply(e.current());
                count = Math.addExact(count, 1);
            }
            return (float) (sum / count);
        }
    }

    public static <TSource> Float averageFloatNull(IEnumerable<TSource> source, NullableFloatFunc1<TSource> selector) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (selector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.selector);

        try (IEnumerator<TSource> e = source.enumerator()) {
            while (e.moveNext()) {
                Float v = selector.apply(e.current());
                if (v != null) {
                    double sum = v;
                    long count = 1;
                    while (e.moveNext()) {
                        v = selector.apply(e.current());
                        if (v != null) {
                            sum += v;
                            count = Math.addExact(count, 1);
                        }
                    }
                    return (float) (sum / count);
                }
            }
        }
        return null;
    }

    public static <TSource> double averageDouble(IEnumerable<TSource> source, DoubleFunc1<TSource> selector) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (selector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.selector);

        try (IEnumerator<TSource> e = source.enumerator()) {
            if (!e.moveNext())
                ThrowHelper.throwNoElementsException();

            double sum = selector.apply(e.current());
            long count = 1;
            while (e.moveNext()) {
                sum += selector.apply(e.current());
                count = Math.addExact(count, 1);
            }
            return sum / count;
        }
    }

    public static <TSource> Double averageDoubleNull(IEnumerable<TSource> source, NullableDoubleFunc1<TSource> selector) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (selector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.selector);

        try (IEnumerator<TSource> e = source.enumerator()) {
            while (e.moveNext()) {
                Double v = selector.apply(e.current());
                if (v != null) {
                    double sum = v;
                    long count = 1;
                    while (e.moveNext()) {
                        v = selector.apply(e.current());
                        if (v != null) {
                            sum += v;
                            count = Math.addExact(count, 1);
                        }
                    }
                    return sum / count;
                }
            }
        }
        return null;
    }

    public static <TSource> BigDecimal averageDecimal(IEnumerable<TSource> source, DecimalFunc1<TSource> selector) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (selector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.selector);

        try (IEnumerator<TSource> e = source.enumerator()) {
            if (!e.moveNext())
                ThrowHelper.throwNoElementsException();

            BigDecimal sum = selector.apply(e.current());
            long count = 1;
            while (e.moveNext()) {
                sum = sum.add(selector.apply(e.current()));
                count = Math.addExact(count, 1);
            }
            return sum.divide(BigDecimal.valueOf(count), MathContext.DECIMAL128);
        }
    }

    public static <TSource> BigDecimal averageDecimalNull(IEnumerable<TSource> source, NullableDecimalFunc1<TSource> selector) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (selector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.selector);

        try (IEnumerator<TSource> e = source.enumerator()) {
            while (e.moveNext()) {
                BigDecimal v = selector.apply(e.current());
                if (v != null) {
                    BigDecimal sum = v;
                    long count = 1;
                    while (e.moveNext()) {
                        v = selector.apply(e.current());
                        if (v != null) {
                            sum = sum.add(v);
                            count = Math.addExact(count, 1);
                        }
                    }
                    return sum.divide(BigDecimal.valueOf(count), MathContext.DECIMAL128);
                }
            }
        }
        return null;
    }
}
