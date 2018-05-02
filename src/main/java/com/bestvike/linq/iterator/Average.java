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
        if (source == null)
            throw Errors.argumentNull("source");

        try (IEnumerator<Integer> e = source.enumerator()) {
            if (!e.moveNext())
                throw Errors.noElements();

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
        if (source == null)
            throw Errors.argumentNull("source");

        try (IEnumerator<Long> e = source.enumerator()) {
            if (!e.moveNext())
                throw Errors.noElements();

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
        if (source == null)
            throw Errors.argumentNull("source");

        try (IEnumerator<Float> e = source.enumerator()) {
            if (!e.moveNext())
                throw Errors.noElements();

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
        if (source == null)
            throw Errors.argumentNull("source");

        try (IEnumerator<Double> e = source.enumerator()) {
            if (!e.moveNext())
                throw Errors.noElements();

            double sum = e.current();
            long count = 1;
            while (e.moveNext()) {
                // There is an opportunity to short-circuit here, in that if e.current() is
                // ever NaN then the result will always be NaN. Assuming that this case is
                // rare enough that not checking is the better approach generally.
                sum += e.current();
                count = Math.addExact(count, 1);
            }

            return sum / count;
        }
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
        if (source == null)
            throw Errors.argumentNull("source");

        try (IEnumerator<BigDecimal> e = source.enumerator()) {
            if (!e.moveNext())
                throw Errors.noElements();

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
        if (source == null)
            throw Errors.argumentNull("source");
        if (selector == null)
            throw Errors.argumentNull("selector");

        try (IEnumerator<TSource> e = source.enumerator()) {
            if (!e.moveNext())
                throw Errors.noElements();

            long sum = selector.apply(e.current());
            long count = 1;
            while (e.moveNext()) {
                sum = Math.addExact(sum, selector.apply(e.current()));
                count = Math.addExact(count, 1);
            }

            return (double) sum / count;
        }
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
        if (source == null)
            throw Errors.argumentNull("source");
        if (selector == null)
            throw Errors.argumentNull("selector");

        try (IEnumerator<TSource> e = source.enumerator()) {
            if (!e.moveNext())
                throw Errors.noElements();

            long sum = selector.apply(e.current());
            long count = 1;
            while (e.moveNext()) {
                sum = Math.addExact(sum, selector.apply(e.current()));
                count = Math.addExact(count, 1);
            }

            return (double) sum / count;
        }
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
        if (source == null)
            throw Errors.argumentNull("source");
        if (selector == null)
            throw Errors.argumentNull("selector");

        try (IEnumerator<TSource> e = source.enumerator()) {
            if (!e.moveNext())
                throw Errors.noElements();

            double sum = selector.apply(e.current());
            long count = 1;
            while (e.moveNext()) {
                sum += selector.apply(e.current());
                count = Math.addExact(count, 1);
            }

            return (float) (sum / count);
        }
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
        if (source == null)
            throw Errors.argumentNull("source");
        if (selector == null)
            throw Errors.argumentNull("selector");

        try (IEnumerator<TSource> e = source.enumerator()) {
            if (!e.moveNext())
                throw Errors.noElements();

            double sum = selector.apply(e.current());
            long count = 1;
            while (e.moveNext()) {
                // There is an opportunity to short-circuit here, in that if e.current() is
                // ever NaN then the result will always be NaN. Assuming that this case is
                // rare enough that not checking is the better approach generally.
                sum += selector.apply(e.current());
                count = Math.addExact(count, 1);
            }

            return sum / count;
        }
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
        if (source == null)
            throw Errors.argumentNull("source");
        if (selector == null)
            throw Errors.argumentNull("selector");

        try (IEnumerator<TSource> e = source.enumerator()) {
            if (!e.moveNext())
                throw Errors.noElements();

            BigDecimal sum = selector.apply(e.current());
            long count = 1;
            while (e.moveNext()) {
                sum = sum.add(selector.apply(e.current()));
                count = Math.addExact(count, 1);
            }

            return sum.divide(BigDecimal.valueOf(count), MathContext.DECIMAL128);
        }
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
