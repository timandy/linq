package com.bestvike.collections.generic;

import com.bestvike.IComparison;
import com.bestvike.linq.exception.Errors;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

/**
 * Created by 许崇雷 on 2017/7/18.
 */
public final class Comparer<T> implements Comparator<T> {
    private static final Comparer DEFAULT = new Comparer();
    private static final Comparer DEFAULT_INVARIANT = new Comparer(Collator.getInstance(Locale.ROOT));
    private static final Comparer DEFAULT_CURRENT = new Comparer(Collator.getInstance(Locale.CHINA));

    private final Collator collator;

    private Comparer() {
        this.collator = null;
    }

    public Comparer(Collator collator) {
        if (collator == null)
            throw Errors.argumentNull("collator");
        this.collator = collator;
    }

    @SuppressWarnings("unchecked")
    public static <T> Comparator<T> Default() {
        return DEFAULT;
    }

    @SuppressWarnings("unchecked")
    public static <T> Comparator<T> DefaultInvariant() {
        return DEFAULT_INVARIANT;
    }

    @SuppressWarnings("unchecked")
    public static <T> Comparator<T> DefaultCurrent() {
        return DEFAULT_CURRENT;
    }

    public static <T> Comparator<T> create(IComparison<T> comparison) {
        if (comparison == null)
            throw Errors.argumentNull("comparison");
        return new ComparisonComparer<>(comparison);
    }

    @SuppressWarnings("unchecked")
    @Override
    public int compare(T x, T y) {
        if (x == y)
            return 0;
        if (x == null)
            return -1;
        if (y == null)
            return 1;
        if (this.collator != null) {
            String sx = null;
            String sy = null;
            if (x instanceof String)
                sx = (String) x;
            if (y instanceof String)
                sy = (String) y;
            if (sx != null && sy != null)
                return this.collator.compare(sx, sy);
        }
        if (x instanceof Comparable)
            return ((Comparable) x).compareTo(y);
        if (y instanceof Comparable)
            return -((Comparable) y).compareTo(x);
        throw Errors.implementComparable();
    }
}
