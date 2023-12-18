package com.bestvike.collections.generic;

import com.bestvike.CultureInfo;
import com.bestvike.IComparison;
import com.bestvike.linq.exception.ExceptionArgument;
import com.bestvike.linq.exception.ThrowHelper;

import java.text.Collator;
import java.util.Comparator;

/**
 * Created by 许崇雷 on 2017-07-18.
 */
public final class Comparer<T> implements Comparator<T> {
    private static final Comparer<?> DEFAULT = new Comparer<>(null);
    private static final Comparer<?> DEFAULT_INVARIANT = new Comparer<>(CultureInfo.getInvariantCulture());
    private static final Comparator<Float> FLOAT = new FloatComparer();
    private static final Comparator<Double> DOUBLE = new DoubleComparer();

    private final Collator collator;

    private Comparer(Collator collator) {
        this.collator = collator;
    }

    public static <T> Comparator<T> Default() {
        //noinspection unchecked
        return (Comparator<T>) DEFAULT;
    }

    public static <T> Comparator<T> DefaultInvariant() {
        //noinspection unchecked
        return (Comparator<T>) DEFAULT_INVARIANT;
    }

    public static Comparator<Float> Float() {
        return FLOAT;
    }

    public static Comparator<Double> Double() {
        return DOUBLE;
    }

    public static <T> Comparator<T> create(Collator collator) {
        if (collator == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.collator);
        return new Comparer<>(collator);
    }

    public static <T> Comparator<T> create(IComparison<? super T> comparison) {
        if (comparison == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.comparison);
        return new ComparisonComparer<>(comparison);
    }

    @Override
    public int compare(T x, T y) {
        if (x == y)
            return 0;
        if (x == null)
            return -1;
        if (y == null)
            return 1;
        if (x instanceof String && y instanceof String)
            return (this.collator == null ? CultureInfo.getCurrentCulture() : this.collator).compare((String) x, (String) y);
        if (x instanceof Comparable)
            //noinspection unchecked
            return ((Comparable<T>) x).compareTo(y);
        if (y instanceof Comparable)
            //noinspection unchecked
            return -((Comparable<T>) y).compareTo(x);
        ThrowHelper.throwImplementComparableException();
        return 0;
    }


    private static final class FloatComparer implements Comparator<Float> {
        @Override
        public int compare(Float x, Float y) {
            if (x != null) {
                if (y != null)
                    return this.comparePrimitive(x, y);
                return 1;
            }
            if (y != null)
                return -1;
            return 0;
        }

        private int comparePrimitive(float x, float y) {
            if (x < y)
                return -1;
            if (x > y)
                return 1;
            if (x == y)
                return 0;

            // At least one of the values is NaN.
            if (Float.isNaN(x))
                return Float.isNaN(y) ? 0 : -1;
            else // y is NaN.
                return 1;
        }
    }


    private static final class DoubleComparer implements Comparator<Double> {
        @Override
        public int compare(Double x, Double y) {
            if (x != null) {
                if (y != null)
                    return this.comparePrimitive(x, y);
                return 1;
            }
            if (y != null)
                return -1;
            return 0;
        }

        private int comparePrimitive(double x, double y) {
            if (x < y)
                return -1;
            if (x > y)
                return 1;
            if (x == y)
                return 0;

            // At least one of the values is NaN.
            if (Double.isNaN(x))
                return Double.isNaN(y) ? 0 : -1;
            else // y is NaN.
                return 1;
        }
    }
}
