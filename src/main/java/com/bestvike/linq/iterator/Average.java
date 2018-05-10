package com.bestvike.linq.iterator;

import com.bestvike.function.Func1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.exception.Errors;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * Created by 许崇雷 on 2018-04-22.
 */
public final class Average {
    private Average() {
    }

    public static double averageInt(IEnumerable<Integer> source) {
        Double value = averageIntNull(source);
        if (value == null)
            throw Errors.noElements();
        return value;
    }

    public static Double averageIntNull(IEnumerable<Integer> source) {
        if (source == null)
            throw Errors.argumentNull("source");

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
        Double value = averageLongNull(source);
        if (value == null)
            throw Errors.noElements();
        return value;
    }

    public static Double averageLongNull(IEnumerable<Long> source) {
        if (source == null)
            throw Errors.argumentNull("source");

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
        Float value = averageFloatNull(source);
        if (value == null)
            throw Errors.noElements();
        return value;
    }

    public static Float averageFloatNull(IEnumerable<Float> source) {
        if (source == null)
            throw Errors.argumentNull("source");

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
        Double value = averageDoubleNull(source);
        if (value == null)
            throw Errors.noElements();
        return value;
    }

    public static Double averageDoubleNull(IEnumerable<Double> source) {
        if (source == null)
            throw Errors.argumentNull("source");

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
        BigDecimal value = averageDecimalNull(source);
        if (value == null)
            throw Errors.noElements();
        return value;
    }

    public static BigDecimal averageDecimalNull(IEnumerable<BigDecimal> source) {
        if (source == null)
            throw Errors.argumentNull("source");

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

    public static <TSource> double averageInt(IEnumerable<TSource> source, Func1<TSource, Integer> selector) {
        Double value = averageIntNull(source, selector);
        if (value == null)
            throw Errors.noElements();
        return value;
    }

    public static <TSource> Double averageIntNull(IEnumerable<TSource> source, Func1<TSource, Integer> selector) {
        if (source == null)
            throw Errors.argumentNull("source");
        if (selector == null)
            throw Errors.argumentNull("selector");

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

    public static <TSource> double averageLong(IEnumerable<TSource> source, Func1<TSource, Long> selector) {
        Double value = averageLongNull(source, selector);
        if (value == null)
            throw Errors.noElements();
        return value;
    }

    public static <TSource> Double averageLongNull(IEnumerable<TSource> source, Func1<TSource, Long> selector) {
        if (source == null)
            throw Errors.argumentNull("source");
        if (selector == null)
            throw Errors.argumentNull("selector");

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

    public static <TSource> float averageFloat(IEnumerable<TSource> source, Func1<TSource, Float> selector) {
        Float value = averageFloatNull(source, selector);
        if (value == null)
            throw Errors.noElements();
        return value;
    }

    public static <TSource> Float averageFloatNull(IEnumerable<TSource> source, Func1<TSource, Float> selector) {
        if (source == null)
            throw Errors.argumentNull("source");
        if (selector == null)
            throw Errors.argumentNull("selector");

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

    public static <TSource> double averageDouble(IEnumerable<TSource> source, Func1<TSource, Double> selector) {
        Double value = averageDoubleNull(source, selector);
        if (value == null)
            throw Errors.noElements();
        return value;
    }

    public static <TSource> Double averageDoubleNull(IEnumerable<TSource> source, Func1<TSource, Double> selector) {
        if (source == null)
            throw Errors.argumentNull("source");
        if (selector == null)
            throw Errors.argumentNull("selector");

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

    public static <TSource> BigDecimal averageDecimal(IEnumerable<TSource> source, Func1<TSource, BigDecimal> selector) {
        BigDecimal value = averageDecimalNull(source, selector);
        if (value == null)
            throw Errors.noElements();
        return value;
    }

    public static <TSource> BigDecimal averageDecimalNull(IEnumerable<TSource> source, Func1<TSource, BigDecimal> selector) {
        if (source == null)
            throw Errors.argumentNull("source");
        if (selector == null)
            throw Errors.argumentNull("selector");

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
