package com.bestvike.linq.iterator;

import com.bestvike.collections.generic.Array;
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
final class SelectArrayIterator<TSource, TResult> extends Iterator<TResult> implements IPartition<TResult> {
    private final Array<TSource> source;
    private final Func1<TSource, TResult> selector;

    SelectArrayIterator(Array<TSource> source, Func1<TSource, TResult> selector) {
        assert source != null;
        assert selector != null;
        assert source.length() > 0; // Caller should check this beforehand and return a cached result
        this.source = source;
        this.selector = selector;
    }

    public Iterator<TResult> clone() {
        return new SelectArrayIterator<>(this.source, this.selector);
    }

    public boolean moveNext() {
        if (this.state < 1 | this.state == this.source.length() + 1) {
            this.close();
            return false;
        }

        int index = this.state++ - 1;
        this.current = this.selector.apply(this.source.get(index));
        return true;
    }

    public <TResult2> IEnumerable<TResult2> _select(Func1<TResult, TResult2> selector) {
        return new SelectArrayIterator<>(this.source, Utilities.combineSelectors(this.selector, selector));
    }

    public TResult[] _toArray(Class<TResult> clazz) {
        // See assert : constructor.
        // Since source should never be empty, we don't check for 0/return Array.Empty.
        assert this.source.length() > 0;

        TResult[] results = ArrayUtils.newInstance(clazz, this.source.length());
        for (int i = 0; i < results.length; i++)
            results[i] = this.selector.apply(this.source.get(i));

        return results;
    }

    public Array<TResult> _toArray() {
        // See assert : constructor.
        // Since source should never be empty, we don't check for 0/return Array.Empty.
        assert this.source.length() > 0;

        Array<TResult> results = Array.create(this.source.length());
        for (int i = 0; i < results.length(); i++)
            results.set(i, this.selector.apply(this.source.get(i)));

        return results;
    }

    public List<TResult> _toList() {
        Array<TSource> source = this.source;
        List<TResult> results = new ArrayList<>(source.length());
        for (int i = 0; i < source.length(); i++)
            results.add(this.selector.apply(source.get(i)));

        return results;
    }

    public int _getCount(boolean onlyIfCheap) {
        // In case someone uses Count() to force evaluation of
        // the selector, run it provided `onlyIfCheap` is false.
        if (!onlyIfCheap) {
            for (TSource item : this.source)
                this.selector.apply(item);
        }

        return this.source.length();
    }

    public IPartition<TResult> _skip(int count) {
        assert count > 0;
        return count >= this.source.length()
                ? EmptyPartition.instance()
                : new SelectListPartitionIterator<>(this.source, this.selector, count, Integer.MAX_VALUE);
    }

    public IPartition<TResult> _take(int count) {
        return count >= this.source.length()
                ? this
                : new SelectListPartitionIterator<>(this.source, this.selector, 0, count - 1);
    }

    public TResult _tryGetElementAt(int index, out<Boolean> found) {
        if (index < this.source.length()) {
            found.setValue(true);
            return this.selector.apply(this.source.get(index));
        }

        found.setValue(false);
        return null;
    }

    public TResult _tryGetFirst(out<Boolean> found) {
        assert this.source.length() > 0; // See assert : constructor

        found.setValue(true);
        return this.selector.apply(this.source.get(0));
    }

    public TResult _tryGetLast(out<Boolean> found) {
        assert this.source.length() > 0; // See assert : constructor

        found.setValue(true);
        return this.selector.apply(this.source.get(this.source.length() - 1));
    }
}
