package com.bestvike.linq.impl.ordered;

import com.bestvike.collections.generic.Comparer;
import com.bestvike.function.Func1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.exception.Errors;

import java.util.Comparator;

/**
 * Created by 许崇雷 on 2017/7/17.
 */
public final class OrderedEnumerable<TElement, TKey> extends AbstractOrderedEnumerable<TElement> {
    private final AbstractOrderedEnumerable<TElement> parent;
    private final Func1<TElement, TKey> keySelector;
    private final Comparator<TKey> comparer;
    private final boolean descending;

    OrderedEnumerable(IEnumerable<TElement> source, Func1<TElement, TKey> keySelector, Comparator<TKey> comparer, boolean descending, AbstractOrderedEnumerable<TElement> parent) {
        if (source == null)
            throw Errors.argumentNull("source");
        if (keySelector == null)
            throw Errors.argumentNull("keySelector");

        this.source = source;
        this.parent = parent;
        this.keySelector = keySelector;
        this.comparer = comparer == null ? Comparer.Default() : comparer;
        this.descending = descending;
    }

    protected AbstractEnumerableSorter<TElement> getEnumerableSorter(AbstractEnumerableSorter<TElement> next) {
        AbstractEnumerableSorter<TElement> sorter = new EnumerableSorter<>(this.keySelector, this.comparer, this.descending, next);
        if (this.parent != null)
            sorter = this.parent.getEnumerableSorter(sorter);
        return sorter;
    }

    protected AbstractCachingComparer<TElement> getComparer(AbstractCachingComparer<TElement> childComparer) {
        AbstractCachingComparer<TElement> cmp = childComparer == null
                ? new CachingComparer<>(this.keySelector, this.comparer, this.descending)
                : new CachingComparerWithChild<>(this.keySelector, this.comparer, this.descending, childComparer);
        return this.parent != null ? this.parent.getComparer(cmp) : cmp;
    }
}
