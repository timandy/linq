package com.bestvike.linq.impl.partition;

import com.bestvike.collections.generic.Array;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.impl.order.AbstractOrderedEnumerable;
import com.bestvike.out;

import java.util.List;

/**
 * Created by 许崇雷 on 2018-04-18.
 */
public final class OrderedPartition<TElement> implements IPartition<TElement> {
    private final AbstractOrderedEnumerable<TElement> source;
    private final int minIndexInclusive;
    private final int maxIndexInclusive;

    public OrderedPartition(AbstractOrderedEnumerable<TElement> source, int minIdxInclusive, int maxIdxInclusive) {
        this.source = source;
        this.minIndexInclusive = minIdxInclusive;
        this.maxIndexInclusive = maxIdxInclusive;
    }

    public IEnumerator<TElement> enumerator() {
        return this.source.enumerator(this.minIndexInclusive, this.maxIndexInclusive);
    }

    public IPartition<TElement> _skip(int count) {
        int minIndex = this.minIndexInclusive + count;
        return minIndex > this.maxIndexInclusive ? EmptyPartition.instance() : new OrderedPartition<>(this.source, minIndex, this.maxIndexInclusive);
    }

    public IPartition<TElement> _take(int count) {
        int maxIndex = this.minIndexInclusive + count - 1;
        if (maxIndex >= this.maxIndexInclusive)
            return this;

        return new OrderedPartition<>(this.source, this.minIndexInclusive, maxIndex);
    }

    public TElement _tryGetElementAt(int index, out<Boolean> found) {
        if (index <= (this.maxIndexInclusive - this.minIndexInclusive)) {
            return this.source._tryGetElementAt(index + this.minIndexInclusive, found);
        }

        found.setValue(false);
        return null;
    }

    public TElement _tryGetFirst(out<Boolean> found) {
        return this.source._tryGetElementAt(this.minIndexInclusive, found);
    }

    public TElement _tryGetLast(out<Boolean> found) {
        return this.source._tryGetLast(this.minIndexInclusive, this.maxIndexInclusive, found);
    }

    @Override
    public TElement[] _toArray(Class<TElement> clazz) {
        return this.source._toArray(this.minIndexInclusive, this.maxIndexInclusive, clazz);
    }

    public Array<TElement> _toArray() {
        return this.source._toArray(this.minIndexInclusive, this.maxIndexInclusive);
    }

    public List<TElement> _toList() {
        return this.source._toList(this.minIndexInclusive, this.maxIndexInclusive);
    }

    public int _getCount(boolean onlyIfCheap) {
        return this.source._getCount(this.minIndexInclusive, this.maxIndexInclusive, onlyIfCheap);
    }
}
