package com.bestvike.linq.iterator;

import com.bestvike.collections.generic.Array;
import com.bestvike.collections.generic.LargeArrayBuilder;
import com.bestvike.function.Func1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.impl.partition.IIListProvider;
import com.bestvike.linq.util.Utilities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 许崇雷 on 2018-05-04.
 */
final class SelectEnumerableIterator<TSource, TResult> extends Iterator<TResult> implements IIListProvider<TResult> {
    private final IEnumerable<TSource> source;
    private final Func1<TSource, TResult> selector;
    private IEnumerator<TSource> enumerator;

    SelectEnumerableIterator(IEnumerable<TSource> source, Func1<TSource, TResult> selector) {
        assert source != null;
        assert selector != null;
        this.source = source;
        this.selector = selector;
    }

    public Iterator<TResult> clone() {
        return new SelectEnumerableIterator<>(this.source, this.selector);
    }

    public void close() {
        if (this.enumerator != null) {
            this.enumerator.close();
            this.enumerator = null;
        }
        super.close();
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

    public <TResult2> IEnumerable<TResult2> _select(Func1<TResult, TResult2> selector) {
        return new SelectEnumerableIterator<>(this.source, Utilities.combineSelectors(this.selector, selector));
    }

    public TResult[] _toArray(Class<TResult> clazz) {
        LargeArrayBuilder<TResult> builder = new LargeArrayBuilder<>();
        for (TSource item : this.source)
            builder.add(this.selector.apply(item));

        return builder.toArray(clazz);
    }

    public Array<TResult> _toArray() {
        LargeArrayBuilder<TResult> builder = new LargeArrayBuilder<>();
        for (TSource item : this.source)
            builder.add(this.selector.apply(item));

        return builder.toArray();
    }

    public List<TResult> _toList() {
        List<TResult> list = new ArrayList<>();
        for (TSource item : this.source)
            list.add(this.selector.apply(item));

        return list;
    }

    public int _getCount(boolean onlyIfCheap) {
        // In case someone uses Count() to force evaluation of
        // the selector, run it provided `onlyIfCheap` is false.
        if (onlyIfCheap)
            return -1;

        int count = 0;
        for (TSource item : this.source) {
            this.selector.apply(item);
            count = Math.addExact(count, 1);
        }

        return count;
    }
}
