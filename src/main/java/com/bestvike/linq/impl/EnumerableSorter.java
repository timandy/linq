package com.bestvike.linq.impl;

import com.bestvike.linq.function.Func1;
import com.bestvike.linq.util.Array;

import java.util.Comparator;

/**
 * @author 许崇雷
 * @date 2017/7/11
 */
public class EnumerableSorter<TElement, TKey> extends AbstractEnumerableSorter<TElement> {
    private final Func1<TElement, TKey> keySelector;
    private final Comparator<TKey> comparer;
    private final boolean descending;
    private final AbstractEnumerableSorter<TElement> next;
    private Array<TKey> keys;

    public EnumerableSorter(Func1<TElement, TKey> keySelector, Comparator<TKey> comparer, boolean descending, AbstractEnumerableSorter<TElement> next) {
        this.keySelector = keySelector;
        this.comparer = comparer;
        this.descending = descending;
        this.next = next;
    }

    @Override
    protected void computeKeys(Array<TElement> elements, int count) {
        this.keys = Array.create(count);
        for (int i = 0; i < count; i++)
            this.keys.set(i, this.keySelector.apply(elements.get(i)));
        if (this.next != null)
            this.next.computeKeys(elements, count);
    }

    @Override
    protected int compareKeys(int index1, int index2) {
        int c = this.comparer.compare(this.keys.get(index1), this.keys.get(index2));
        if (c == 0) {
            if (this.next == null)
                return index1 - index2;
            return this.next.compareKeys(index1, index2);
        }
        return this.descending ? -c : c;
    }
}