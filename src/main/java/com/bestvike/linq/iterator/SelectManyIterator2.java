package com.bestvike.linq.iterator;

import com.bestvike.collections.generic.Array;
import com.bestvike.collections.generic.ArrayBuilder;
import com.bestvike.collections.generic.EnumerableHelpers;
import com.bestvike.collections.generic.Marker;
import com.bestvike.collections.generic.SparseArrayBuilder;
import com.bestvike.function.Func2;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.impl.partition.IIListProvider;
import com.bestvike.linq.util.ListUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 许崇雷 on 2017/7/16.
 */
final class SelectManyIterator2<TSource, TResult> extends Iterator<TResult> implements IIListProvider<TResult> {
    private final IEnumerable<TSource> source;
    private final Func2<TSource, Integer, IEnumerable<TResult>> selector;
    private IEnumerator<TSource> enumerator;
    private IEnumerator<TResult> subEnumerator;
    private int index;

    SelectManyIterator2(IEnumerable<TSource> source, Func2<TSource, Integer, IEnumerable<TResult>> selector) {
        assert source != null;
        assert selector != null;

        this.source = source;
        this.selector = selector;
    }

    @Override
    public AbstractIterator<TResult> clone() {
        return new SelectManyIterator2<>(this.source, this.selector);
    }

    @Override
    public boolean moveNext() {
        do {
            switch (this.state) {
                case 1:
                    this.index = -1;
                    this.enumerator = this.source.enumerator();
                    this.state = 2;
                case 2:
                    if (this.enumerator.moveNext()) {
                        TSource item = this.enumerator.current();
                        this.index = Math.addExact(this.index, 1);
                        this.subEnumerator = this.selector.apply(item, this.index).enumerator();
                        this.state = 3;
                        break;
                    }
                    this.close();
                    return false;
                case 3:
                    if (this.subEnumerator.moveNext()) {
                        this.current = this.subEnumerator.current();
                        return true;
                    }
                    this.subEnumerator.close();
                    this.state = 2;
                    break;
                default:
                    return false;
            }
        } while (true);
    }

    @Override
    public void close() {
        if (this.enumerator != null) {
            this.enumerator.close();
            this.enumerator = null;
        }
        if (this.subEnumerator != null) {
            this.subEnumerator.close();
            this.subEnumerator = null;
        }
        super.close();
    }

    @Override
    public int _getCount(boolean onlyIfCheap) {
        if (onlyIfCheap)
            return -1;

        int count = 0;
        int index = 0;
        for (TSource element : this.source) {
            count = Math.addExact(count, this.selector.apply(element, index).count());
            index = Math.addExact(index, 1);
        }

        return count;
    }

    @Override
    public TResult[] _toArray(Class<TResult> clazz) {
        SparseArrayBuilder<TResult> builder = new SparseArrayBuilder<>();
        ArrayBuilder<IEnumerable<TResult>> deferredCopies = new ArrayBuilder<>();
        int index = 0;
        for (TSource element : this.source) {
            IEnumerable<TResult> enumerable = this.selector.apply(element, index);
            index = Math.addExact(index, 1);
            if (builder.reserveOrAdd(enumerable))
                deferredCopies.add(enumerable);
        }

        TResult[] array = builder.toArray(clazz);
        ArrayBuilder<Marker> markers = builder.getMarkers();
        for (int i = 0; i < markers.getCount(); i++) {
            Marker marker = markers.get(i);
            IEnumerable<TResult> enumerable = deferredCopies.get(i);
            EnumerableHelpers.copy(enumerable, array, marker.getIndex(), marker.getCount());
        }

        return array;
    }

    @Override
    public Array<TResult> _toArray() {
        SparseArrayBuilder<TResult> builder = new SparseArrayBuilder<>();
        ArrayBuilder<IEnumerable<TResult>> deferredCopies = new ArrayBuilder<>();
        int index = 0;
        for (TSource element : this.source) {
            IEnumerable<TResult> enumerable = this.selector.apply(element, index);
            index = Math.addExact(index, 1);
            if (builder.reserveOrAdd(enumerable))
                deferredCopies.add(enumerable);
        }

        Array<TResult> array = builder.toArray();
        ArrayBuilder<Marker> markers = builder.getMarkers();
        for (int i = 0; i < markers.getCount(); i++) {
            Marker marker = markers.get(i);
            IEnumerable<TResult> enumerable = deferredCopies.get(i);
            EnumerableHelpers.copy(enumerable, array, marker.getIndex(), marker.getCount());
        }

        return array;
    }

    @Override
    public List<TResult> _toList() {
        List<TResult> list = new ArrayList<>();
        int index = 0;
        for (TSource element : this.source) {
            ListUtils.addRange(list, this.selector.apply(element, index));
            index = Math.addExact(index, 1);
        }

        return list;
    }
}
