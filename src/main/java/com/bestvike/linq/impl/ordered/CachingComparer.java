package com.bestvike.linq.impl.ordered;

import com.bestvike.function.Func1;

import java.util.Comparator;

/**
 * Created by 许崇雷 on 2018-04-18.
 */
public class CachingComparer<TElement, TKey> extends AbstractCachingComparer<TElement> {
    protected final Func1<TElement, TKey> keySelector;
    protected final Comparator<TKey> comparer;
    protected final boolean descending;
    protected TKey lastKey;

    public CachingComparer(Func1<TElement, TKey> keySelector, Comparator<TKey> comparer, boolean descending) {
        this.keySelector = keySelector;
        this.comparer = comparer;
        this.descending = descending;
    }

    int compare(TElement element, boolean cacheLower) {
        TKey newKey = this.keySelector.apply(element);
        int cmp = this.descending ? this.comparer.compare(this.lastKey, newKey) : this.comparer.compare(newKey, this.lastKey);
        if (cacheLower == cmp < 0)
            this.lastKey = newKey;
        return cmp;
    }

    void setElement(TElement element) {
        this.lastKey = this.keySelector.apply(element);
    }
}
