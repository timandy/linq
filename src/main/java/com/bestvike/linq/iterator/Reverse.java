package com.bestvike.linq.iterator;

import com.bestvike.collections.generic.Array;
import com.bestvike.collections.generic.ICollection;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.exception.Errors;
import com.bestvike.linq.util.ArrayUtils;

import java.util.Collections;
import java.util.List;

/**
 * Created by 许崇雷 on 2018-05-03.
 */
public final class Reverse {
    private Reverse() {
    }

    public static <TSource> IEnumerable<TSource> reverse(IEnumerable<TSource> source) {
        if (source == null)
            throw Errors.argumentNull("source");

        return new ReverseIterator<>(source);
    }
}


final class ReverseIterator<TSource> extends Iterator<TSource> implements IIListProvider<TSource> {
    private final IEnumerable<TSource> source;
    private Array<TSource> buffer;

    ReverseIterator(IEnumerable<TSource> source) {
        assert source != null;
        this.source = source;
    }

    @Override
    public AbstractIterator<TSource> clone() {
        return new ReverseIterator<>(this.source);
    }

    @Override
    public boolean moveNext() {
        if (this.state - 2 <= -2) {
            // Either someone called a method and cast us to IEnumerable without calling GetEnumerator,
            // or we were already disposed. In either case, iteration has ended, so return false.
            // A comparison is made against -2 instead of _state <= 0 because we want to handle cases where
            // the source is really large and adding the bias causes _state to overflow.
            assert this.state == -1 || this.state == 0;
            this.close();
            return false;
        }

        switch (this.state) {
            case 1:
                // Iteration has just started. Capture the source into an array and set _state to 2 + the count.
                // Having an extra field for the count would be more readable, but we save it into _state with a
                // bias instead to minimize field size of the iterator.
                Buffer<TSource> buffer = new Buffer<>(this.source);
                this.buffer = buffer.getItems();
                this.state = buffer.getCount() + 2;
            default:
                // At this stage, _state starts from 2 + the count. _state - 3 represents the current index into the
                // buffer. It is continuously decremented until it hits 2, which means that we've run out of items to
                // yield and should return false.
                int index = this.state - 3;
                if (index != -1) {
                    this.current = this.buffer.get(index);
                    --this.state;
                    return true;
                }

                break;
        }

        this.close();
        return false;
    }

    @Override
    public void close() {
        this.buffer = null;
        super.close();
    }

    @Override
    public int _getCount(boolean onlyIfCheap) {
        if (onlyIfCheap) {
            if (this.source instanceof IIListProvider) {
                IIListProvider<TSource> listProv = ((IIListProvider<TSource>) this.source);
                return listProv._getCount(true);
            }
            if (this.source instanceof ICollection) {
                ICollection<TSource> col = ((ICollection<TSource>) this.source);
                return col._getCount();
            }
            return -1;
        }

        return this.source.count();
    }

    @Override
    public TSource[] _toArray(Class<TSource> clazz) {
        TSource[] array = this.source.toArray(clazz);
        ArrayUtils.reverse(array);
        return array;
    }

    @Override
    public Array<TSource> _toArray() {
        Array<TSource> array = this.source.toArray();
        Array.reverse(array);
        return array;
    }

    @Override
    public List<TSource> _toList() {
        List<TSource> list = this.source.toList();
        Collections.reverse(list);
        return list;
    }
}
