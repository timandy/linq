package com.bestvike.linq.iterator;

import com.bestvike.collections.generic.Array;
import com.bestvike.collections.generic.IList;
import com.bestvike.function.Func1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.impl.partition.EmptyPartition;
import com.bestvike.linq.impl.partition.IPartition;
import com.bestvike.linq.util.ArrayUtils;
import com.bestvike.linq.util.Utilities;
import com.bestvike.out;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 许崇雷 on 2018-05-04.
 */
final class SelectListPartitionIterator<TSource, TResult> extends Iterator<TResult> implements IPartition<TResult> {
    private final IList<TSource> source;
    private final Func1<TSource, TResult> selector;
    private final int minIndexInclusive;
    private final int maxIndexInclusive;

    SelectListPartitionIterator(IList<TSource> source, Func1<TSource, TResult> selector, int minIndexInclusive, int maxIndexInclusive) {
        assert source != null;
        assert selector != null;
        assert minIndexInclusive >= 0;
        assert minIndexInclusive <= maxIndexInclusive;
        this.source = source;
        this.selector = selector;
        this.minIndexInclusive = minIndexInclusive;
        this.maxIndexInclusive = maxIndexInclusive;
    }

    public Iterator<TResult> clone() {
        return new SelectListPartitionIterator<>(this.source, this.selector, this.minIndexInclusive, this.maxIndexInclusive);
    }

    public boolean moveNext() {
        // this.state - 1 represents the zero-based index into the list.
        // Having a separate field for the index would be more readable. However, we save it
        // into this.state with a bias to minimize field size of the iterator.
        int index = this.state - 1;
        if (index <= (this.maxIndexInclusive - this.minIndexInclusive) && index < this.source._getCount() - this.minIndexInclusive) {
            this.current = this.selector.apply(this.source.get(this.minIndexInclusive + index));
            ++this.state;
            return true;
        }

        this.close();
        return false;
    }

    public <TResult2> IEnumerable<TResult2> _select(Func1<TResult, TResult2> selector) {
        return new SelectListPartitionIterator<>(this.source, Utilities.combineSelectors(this.selector, selector), this.minIndexInclusive, this.maxIndexInclusive);
    }

    public IPartition<TResult> _skip(int count) {
        assert count > 0;
        int minIndex = this.minIndexInclusive + count;
        return minIndex > this.maxIndexInclusive
                ? EmptyPartition.instance()
                : new SelectListPartitionIterator<>(this.source, this.selector, minIndex, this.maxIndexInclusive);
    }

    public IPartition<TResult> _take(int count) {
        int maxIndex = this.minIndexInclusive + count - 1;
        return maxIndex >= this.maxIndexInclusive
                ? this
                : new SelectListPartitionIterator<>(this.source, this.selector, this.minIndexInclusive, maxIndex);
    }

    public TResult _tryGetElementAt(int index, out<Boolean> found) {
        if (index <= (this.maxIndexInclusive - this.minIndexInclusive) && index < this.source._getCount() - this.minIndexInclusive) {
            found.setValue(true);
            return this.selector.apply(this.source.get(this.minIndexInclusive + index));
        }

        found.setValue(false);
        return null;
    }

    public TResult _tryGetFirst(out<Boolean> found) {
        if (this.source._getCount() > this.minIndexInclusive) {
            found.setValue(true);
            return this.selector.apply(this.source.get(this.minIndexInclusive));
        }

        found.setValue(false);
        return null;
    }

    public TResult _tryGetLast(out<Boolean> found) {
        int lastIndex = this.source._getCount() - 1;
        if (lastIndex >= this.minIndexInclusive) {
            found.setValue(true);
            return this.selector.apply(this.source.get(Math.min(lastIndex, this.maxIndexInclusive)));
        }

        found.setValue(false);
        return null;
    }

    private int _getCount() {
        int count = this.source._getCount();
        if (count <= this.minIndexInclusive)
            return 0;

        return Math.min(count - 1, this.maxIndexInclusive) - this.minIndexInclusive + 1;
    }

    public TResult[] _toArray(Class<TResult> clazz) {
        int count = this._getCount();
        if (count == 0)
            return ArrayUtils.empty(clazz);

        TResult[] array = ArrayUtils.newInstance(clazz, count);
        for (int i = 0, curIdx = this.minIndexInclusive; i != array.length; ++i, ++curIdx)
            array[i] = this.selector.apply(this.source.get(curIdx));

        return array;
    }

    @Override
    public Array<TResult> _toArray() {
        int count = this._getCount();
        if (count == 0)
            return Array.empty();

        Array<TResult> array = Array.create(count);
        for (int i = 0, curIdx = this.minIndexInclusive; i != array.length(); ++i, ++curIdx)
            array.set(i, this.selector.apply(this.source.get(curIdx)));

        return array;
    }

    public List<TResult> _toList() {
        int count = this._getCount();
        if (count == 0)
            return new ArrayList<>();

        List<TResult> list = new ArrayList<>(count);
        int end = this.minIndexInclusive + count;
        for (int i = this.minIndexInclusive; i != end; ++i)
            list.add(this.selector.apply(this.source.get(i)));

        return list;
    }

    public int _getCount(boolean onlyIfCheap) {
        // In case someone uses Count() to force evaluation of
        // the selector, run it provided `onlyIfCheap` is false.
        int count = this._getCount();
        if (!onlyIfCheap) {
            int end = this.minIndexInclusive + count;
            for (int i = this.minIndexInclusive; i != end; ++i)
                this.selector.apply(this.source.get(i));
        }

        return count;
    }
}
