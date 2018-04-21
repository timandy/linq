package com.bestvike.linq.impl.order;

import com.bestvike.function.Func1;

import java.util.Comparator;

/**
 * Created by 许崇雷 on 2018-04-18.
 */
public class CachingComparerWithChild<TElement, TKey> extends CachingComparer<TElement, TKey> {
    private final AbstractCachingComparer<TElement> child;

    public CachingComparerWithChild(Func1<TElement, TKey> keySelector, Comparator<TKey> comparer, boolean descending, AbstractCachingComparer<TElement> child) {
        super(keySelector, comparer, descending);
        this.child = child;
    }

    protected int compare(TElement element, boolean cacheLower) {
        TKey newKey = this.keySelector.apply(element);
        int cmp = this.descending ? this.comparer.compare(this.lastKey, newKey) : this.comparer.compare(newKey, this.lastKey);
        if (cmp == 0)
            return this.child.compare(element, cacheLower);
        if (cacheLower == cmp < 0) {
            this.lastKey = newKey;
            this.child.setElement(element);
        }
        return cmp;
    }

    protected void setElement(TElement element) {
        super.setElement(element);
        this.child.setElement(element);
    }
}
