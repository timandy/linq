package com.bestvike.linq.impl;

import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.exception.Errors;
import com.bestvike.linq.function.Func1;
import com.bestvike.linq.util.Comparer;

import java.util.Comparator;

/**
 * Created by 许崇雷 on 2017/7/17.
 */
public final class OrderedEnumerable<TElement, TKey> extends AbstractOrderedEnumerable<TElement> {
    private final Func1<TElement, TKey> keySelector;
    private final Comparator<TKey> comparer;
    private final boolean descending;
    AbstractOrderedEnumerable<TElement> parent;

    public OrderedEnumerable(IEnumerable<TElement> source, Func1<TElement, TKey> keySelector, Comparator<TKey> comparer, boolean descending) {
        if (source == null) throw Errors.argumentNull("source");
        if (keySelector == null) throw Errors.argumentNull("keySelector");
        this.source = source;
        this.parent = null;
        this.keySelector = keySelector;
        this.comparer = comparer != null ? comparer : Comparer.Default();
        this.descending = descending;
    }

    @Override
    protected AbstractEnumerableSorter<TElement> getEnumerableSorter(AbstractEnumerableSorter<TElement> next) {
        AbstractEnumerableSorter<TElement> sorter = new EnumerableSorter<>(this.keySelector, this.comparer, this.descending, next);
        if (this.parent != null)
            sorter = this.parent.getEnumerableSorter(sorter);
        return sorter;
    }
}
