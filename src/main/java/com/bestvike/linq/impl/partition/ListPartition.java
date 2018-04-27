package com.bestvike.linq.impl.partition;

import com.bestvike.collections.generic.Array;
import com.bestvike.function.Func1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.iterator.Iterator;
import com.bestvike.linq.util.ArrayUtils;
import com.bestvike.linq.util.ListUtils;
import com.bestvike.out;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 许崇雷 on 2018-04-21.
 */
public final class ListPartition<TSource> extends Iterator<TSource> implements IPartition<TSource> {
    private final List<TSource> source;
    private final int minIndexInclusive;
    private final int maxIndexInclusive;

    public ListPartition(List<TSource> source, int minIndexInclusive, int maxIndexInclusive) {
        assert source != null;
        assert minIndexInclusive >= 0;
        assert minIndexInclusive <= maxIndexInclusive;

        this.source = source;
        this.minIndexInclusive = minIndexInclusive;
        this.maxIndexInclusive = maxIndexInclusive;
    }

    public Iterator<TSource> clone() {
        return new ListPartition<>(this.source, this.minIndexInclusive, this.maxIndexInclusive);
    }

    public boolean moveNext() {
        // _state - 1 represents the zero-based index into the list.
        // Having a separate field for the index would be more readable. However, we save it
        // into _state with a bias to minimize field size of the iterator.
        if (this.state == -1)
            return false;
        int index = this.state - 1;
        if (index <= (this.maxIndexInclusive - this.minIndexInclusive) && index < this.source.size() - this.minIndexInclusive) {
            this.current = this.source.get(this.minIndexInclusive + index);
            ++this.state;
            return true;
        }

        this.close();
        return false;
    }

    public <TResult> IEnumerable<TResult> _select(Func1<TSource, TResult> selector) {
        return new SelectListPartitionIterator<TSource, TResult>(this.source, selector, this.minIndexInclusive, this.maxIndexInclusive);
    }

    public IPartition<TSource> _skip(int count) {
        int minIndex = this.minIndexInclusive + count;
        return minIndex > this.maxIndexInclusive ? EmptyPartition.instance() : new ListPartition<>(this.source, minIndex, this.maxIndexInclusive);
    }

    public IPartition<TSource> _take(int count) {
        int maxIndex = this.minIndexInclusive + count - 1;
        return maxIndex >= this.maxIndexInclusive ? this : new ListPartition<>(this.source, this.minIndexInclusive, maxIndex);
    }

    public TSource _tryGetElementAt(int index, out<Boolean> found) {
        if (index <= (this.maxIndexInclusive - this.minIndexInclusive) && index < this.source.size() - this.minIndexInclusive) {
            found.setValue(true);
            return this.source.get(this.minIndexInclusive + index);
        }

        found.setValue(false);
        return null;
    }

    public TSource _tryGetFirst(out<Boolean> found) {
        if (this.source.size() > this.minIndexInclusive) {
            found.setValue(true);
            return this.source.get(this.minIndexInclusive);
        }

        found.setValue(false);
        return null;
    }

    public TSource _tryGetLast(out<Boolean> found) {
        int lastIndex = this.source.size() - 1;
        if (lastIndex >= this.minIndexInclusive) {
            found.setValue(true);
            return this.source.get(Math.min(lastIndex, this.maxIndexInclusive));
        }

        found.setValue(false);
        return null;
    }

    private int getCount() {
        int count = this.source.size();
        if (count <= this.minIndexInclusive)
            return 0;

        return Math.min(count - 1, this.maxIndexInclusive) - this.minIndexInclusive + 1;
    }

    public TSource[] _toArray(Class<TSource> clazz) {
        int count = this.getCount();
        if (count == 0)
            return ArrayUtils.empty(clazz);

        TSource[] array = ArrayUtils.newInstance(clazz, count);
        for (int i = 0, curIdx = this.minIndexInclusive; i != array.length; ++i, ++curIdx)
            array[i] = this.source.get(curIdx);

        return array;
    }

    @Override
    public Array<TSource> _toArray() {
        int count = this.getCount();
        if (count == 0)
            return Array.empty();

        Array<TSource> array = Array.create(count);
        for (int i = 0, curIdx = this.minIndexInclusive; i != array.length(); ++i, ++curIdx)
            array.set(i, this.source.get(curIdx));

        return array;
    }

    public List<TSource> _toList() {
        int count = this.getCount();
        if (count == 0) {
            return ListUtils.empty();
        }

        List<TSource> list = new ArrayList<>(count);
        int end = this.minIndexInclusive + count;
        for (int i = this.minIndexInclusive; i != end; ++i)
            list.add(this.source.get(i));

        return list;
    }

    public int _getCount(boolean onlyIfCheap) {
        return this.getCount();
    }
}
