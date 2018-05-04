package com.bestvike.linq.iterator;

import com.bestvike.collections.generic.Array;
import com.bestvike.collections.generic.IList;
import com.bestvike.function.Func1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.impl.partition.IPartition;
import com.bestvike.linq.util.ArrayUtils;
import com.bestvike.linq.util.Utilities;
import com.bestvike.out;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 许崇雷 on 2018-05-04.
 */
final class SelectIListIterator<TSource, TResult> extends Iterator<TResult> implements IPartition<TResult> {
    private final IList<TSource> source;
    private final Func1<TSource, TResult> selector;
    private IEnumerator<TSource> enumerator;

    SelectIListIterator(IList<TSource> source, Func1<TSource, TResult> selector) {
        assert source != null;
        assert selector != null;
        this.source = source;
        this.selector = selector;
    }

    public Iterator<TResult> clone() {
        return new SelectIListIterator<>(this.source, this.selector);
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
        return new SelectIListIterator<>(this.source, Utilities.combineSelectors(this.selector, selector));
    }

    public TResult[] _toArray(Class<TResult> clazz) {
        int count = this.source._getCount();
        if (count == 0)
            return ArrayUtils.empty(clazz);

        TResult[] results = ArrayUtils.newInstance(clazz, count);
        for (int i = 0; i < results.length; i++)
            results[i] = this.selector.apply(this.source.get(i));

        return results;
    }

    public Array<TResult> _toArray() {
        int count = this.source._getCount();
        if (count == 0)
            return Array.empty();

        Array<TResult> results = Array.create(count);
        for (int i = 0; i < results.length(); i++)
            results.set(i, this.selector.apply(this.source.get(i)));

        return results;
    }

    public List<TResult> _toList() {
        int count = this.source._getCount();
        ArrayList<TResult> results = new ArrayList<>(count);
        for (int i = 0; i < count; i++)
            results.add(this.selector.apply(this.source.get(i)));

        return results;
    }

    public int _getCount(boolean onlyIfCheap) {
        // In case someone uses Count() to force evaluation of
        // the selector, run it provided `onlyIfCheap` is false.
        int count = this.source._getCount();

        if (!onlyIfCheap) {
            for (int i = 0; i < count; i++)
                this.selector.apply(this.source.get(i));
        }

        return count;
    }

    public IPartition<TResult> _skip(int count) {
        assert count > 0;
        return new SelectListPartitionIterator<>(this.source, this.selector, count, Integer.MAX_VALUE);
    }

    public IPartition<TResult> _take(int count) {
        return new SelectListPartitionIterator<>(this.source, this.selector, 0, count - 1);
    }

    public TResult _tryGetElementAt(int index, out<Boolean> found) {
        if (index < this.source._getCount()) {
            found.setValue(true);
            return this.selector.apply(this.source.get(index));
        }

        found.setValue(false);
        return null;
    }

    public TResult _tryGetFirst(out<Boolean> found) {
        if (this.source._getCount() != 0) {
            found.setValue(true);
            return this.selector.apply(this.source.get(0));
        }

        found.setValue(false);
        return null;
    }

    public TResult _tryGetLast(out<Boolean> found) {
        int len = this.source._getCount();
        if (len != 0) {
            found.setValue(true);
            return this.selector.apply(this.source.get(len - 1));
        }

        found.setValue(false);
        return null;
    }
}
