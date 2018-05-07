package com.bestvike.linq.iterator;

import com.bestvike.collections.generic.Array;
import com.bestvike.function.Func1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.exception.Errors;
import com.bestvike.out;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 许崇雷 on 2018-05-03.
 */
public final class Range {
    private Range() {
    }

    public static IEnumerable<Integer> range(int start, int count) {
        long max = ((long) start) + count - 1;
        if (count < 0 || max > Integer.MAX_VALUE)
            throw Errors.argumentOutOfRange("count");

        if (count == 0)
            return EmptyPartition.instance();

        return new RangeIterator(start, count);
    }
}


final class RangeIterator extends Iterator<Integer> implements IPartition<Integer> {
    private final int start;
    private final int end;

    RangeIterator(int start, int count) {
        assert count > 0;
        this.start = start;
        this.end = start + count;
    }

    @Override
    public AbstractIterator<Integer> clone() {
        return new RangeIterator(this.start, this.end - this.start);
    }

    @Override
    public boolean moveNext() {
        switch (this.state) {
            case 1:
                assert this.start != this.end;
                this.current = this.start;
                this.state = 2;
                return true;
            case 2:
                if (++this.current == this.end) {
                    this.close();
                    return false;
                }
                return true;
            default:
                return false;
        }
    }

    @Override
    public <TResult> IEnumerable<TResult> _select(Func1<Integer, TResult> selector) {
        return new SelectIPartitionIterator<>(this, selector);
    }

    @Override
    public IPartition<Integer> _skip(int count) {
        return count >= this.end - this.start
                ? EmptyPartition.instance()
                : new RangeIterator(this.start + count, this.end - this.start - count);
    }

    @Override
    public IPartition<Integer> _take(int count) {
        return count >= this.end - this.start
                ? this
                : new RangeIterator(this.start, count);
    }

    @Override
    public Integer _tryGetElementAt(int index, out<Boolean> found) {
        if (index < this.end - this.start) {
            found.setValue(true);
            return this.start + index;
        }

        found.setValue(false);
        return null;
    }

    @Override
    public Integer _tryGetFirst(out<Boolean> found) {
        found.setValue(true);
        return this.start;
    }

    @Override
    public Integer _tryGetLast(out<Boolean> found) {
        found.setValue(true);
        return this.end - 1;
    }

    @Override
    public int _getCount(boolean onlyIfCheap) {
        return this.end - this.start;
    }

    @Override
    public Integer[] _toArray(Class<Integer> clazz) {
        Integer[] array = new Integer[this.end - this.start];
        int cur = this.start;
        for (int i = 0; i != array.length; ++i) {
            array[i] = cur;
            ++cur;
        }

        return array;
    }

    @Override
    public Array<Integer> _toArray() {
        Array<Integer> array = Array.create(this.end - this.start);
        int cur = this.start;
        for (int i = 0; i != array.length(); ++i) {
            array.set(i, cur);
            ++cur;
        }

        return array;
    }

    @Override
    public List<Integer> _toList() {
        List<Integer> list = new ArrayList<>(this.end - this.start);
        for (int cur = this.start; cur != this.end; cur++)
            list.add(cur);

        return list;
    }
}
