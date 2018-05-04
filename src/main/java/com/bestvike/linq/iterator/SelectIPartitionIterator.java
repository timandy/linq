package com.bestvike.linq.iterator;

import com.bestvike.collections.generic.Array;
import com.bestvike.collections.generic.LargeArrayBuilder;
import com.bestvike.function.Func1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.impl.partition.IPartition;
import com.bestvike.linq.util.ArrayUtils;
import com.bestvike.linq.util.ListUtils;
import com.bestvike.linq.util.Utilities;
import com.bestvike.out;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 许崇雷 on 2018-05-04.
 */
final class SelectIPartitionIterator<TSource, TResult> extends Iterator<TResult> implements IPartition<TResult> {
    private final IPartition<TSource> source;
    private final Func1<TSource, TResult> selector;
    private IEnumerator<TSource> enumerator;

    SelectIPartitionIterator(IPartition<TSource> source, Func1<TSource, TResult> selector) {
        assert source != null;
        assert selector != null;
        this.source = source;
        this.selector = selector;
    }

    public Iterator<TResult> clone() {
        return new SelectIPartitionIterator<>(this.source, this.selector);
    }

    public boolean moveNext() {
        switch (this.state) {
            case 1:
                this.enumerator = this.source.enumerator();
                this.state = 2;
            case 2:
                if (this.enumerator.moveNext()) {
                    this.current = this.selector.apply(this.enumerator.current());
                    return true;
                }
                this.close();
                return false;
            default:
                return false;
        }
    }

    public void close() {
        if (this.enumerator != null) {
            this.enumerator.close();
            this.enumerator = null;
        }
        super.close();
    }

    public <TResult2> IEnumerable<TResult2> _select(Func1<TResult, TResult2> selector) {
        return new SelectIPartitionIterator<>(this.source, Utilities.combineSelectors(this.selector, selector));
    }

    public IPartition<TResult> _skip(int count) {
        assert count > 0;
        return new SelectIPartitionIterator<>(this.source._skip(count), this.selector);
    }

    public IPartition<TResult> _take(int count) {
        return new SelectIPartitionIterator<>(this.source._take(count), this.selector);
    }

    public TResult _tryGetElementAt(int index, out<Boolean> found) {
        TSource input = this.source._tryGetElementAt(index, found);
        return found.getValue() ? this.selector.apply(input) : null;
    }

    public TResult _tryGetFirst(out<Boolean> found) {
        TSource input = this.source._tryGetFirst(found);
        return found.getValue() ? this.selector.apply(input) : null;
    }

    public TResult _tryGetLast(out<Boolean> found) {
        TSource input = this.source._tryGetLast(found);
        return found.getValue() ? this.selector.apply(input) : null;
    }

    private TResult[] lazyToArray(Class<TResult> clazz) {
        assert this.source._getCount(true) == -1;

        LargeArrayBuilder<TResult> builder = new LargeArrayBuilder<>();
        for (TSource input : this.source)
            builder.add(this.selector.apply(input));

        return builder.toArray(clazz);
    }

    private Array<TResult> lazyToArray() {
        assert this.source._getCount(true) == -1;

        LargeArrayBuilder<TResult> builder = new LargeArrayBuilder<>();
        for (TSource input : this.source)
            builder.add(this.selector.apply(input));

        return builder.toArray();
    }

    private TResult[] preallocatingToArray(Class<TResult> clazz, int count) {
        assert count > 0;
        assert count == this.source._getCount(true);

        TResult[] array = ArrayUtils.newInstance(clazz, count);
        int index = 0;
        for (TSource input : this.source) {
            array[index] = this.selector.apply(input);
            ++index;
        }

        return array;
    }

    private Array<TResult> preallocatingToArray(int count) {
        assert count > 0;
        assert count == this.source._getCount(true);

        Array<TResult> array = Array.create(count);
        int index = 0;
        for (TSource input : this.source) {
            array.set(index, this.selector.apply(input));
            ++index;
        }

        return array;
    }

    public TResult[] _toArray(Class<TResult> clazz) {
        int count = this.source._getCount(true);
        switch (count) {
            case -1:
                return this.lazyToArray(clazz);
            case 0:
                return ArrayUtils.empty(clazz);
            default:
                return this.preallocatingToArray(clazz, count);
        }
    }

    @Override
    public Array<TResult> _toArray() {
        int count = this.source._getCount(true);
        switch (count) {
            case -1:
                return this.lazyToArray();
            case 0:
                return Array.empty();
            default:
                return this.preallocatingToArray(count);
        }
    }

    public List<TResult> _toList() {
        int count = this.source._getCount(true);
        List<TResult> list;
        switch (count) {
            case -1:
                list = ListUtils.empty();
                break;
            case 0:
                return ListUtils.empty();
            default:
                list = new ArrayList<>(count);
                break;
        }

        for (TSource input : this.source)
            list.add(this.selector.apply(input));

        return list;
    }

    public int _getCount(boolean onlyIfCheap) {
        // In case someone uses Count() to force evaluation of
        // the selector, run it provided `onlyIfCheap` is false.
        if (!onlyIfCheap) {
            for (TSource item : this.source)
                this.selector.apply(item);
        }

        return this.source._getCount(onlyIfCheap);
    }
}
