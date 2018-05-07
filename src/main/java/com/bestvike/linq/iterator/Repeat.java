package com.bestvike.linq.iterator;

import com.bestvike.collections.generic.Array;
import com.bestvike.function.Func1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.exception.Errors;
import com.bestvike.linq.util.ArrayUtils;
import com.bestvike.out;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 许崇雷 on 2018-05-03.
 */
public final class Repeat {
    private Repeat() {
    }

    public static <TResult> IEnumerable<TResult> repeat(TResult element, int count) {
        if (count < 0)
            throw Errors.argumentOutOfRange("count");

        if (count == 0)
            return EmptyPartition.instance();

        return new RepeatIterator<>(element, count);
    }
}


final class RepeatIterator<TResult> extends Iterator<TResult> implements IPartition<TResult> {
    private final int count;

    RepeatIterator(TResult element, int count) {
        assert count > 0;
        this.current = element;
        this.count = count;
    }

    @Override
    public AbstractIterator<TResult> clone() {
        return new RepeatIterator<>(this.current, this.count);
    }

    @Override
    public boolean moveNext() {
        // Having a separate field for the number of sent items would be more readable.
        // However, we save it into _state with a bias to minimize field size of the iterator.
        int sent = this.state - 1;

        // We can't have sent a negative number of items, obviously. However, if this iterator
        // was illegally casted to IEnumerator without GetEnumerator being called, or if we've
        // already been disposed, then `sent` will be negative.
        if (sent >= 0 && sent != this.count) {
            ++this.state;
            return true;
        }

        this.close();
        return false;
    }

    @Override
    public void close() {
        // Don't let super.close wipe current.
        this.state = -1;
    }

    @Override
    public <TResult2> IEnumerable<TResult2> _select(Func1<TResult, TResult2> selector) {
        return new SelectIPartitionIterator<>(this, selector);
    }

    @Override
    public IPartition<TResult> _skip(int count) {
        return count >= this.count
                ? EmptyPartition.instance()
                : new RepeatIterator<>(this.current, this.count - count);
    }

    @Override
    public IPartition<TResult> _take(int count) {
        return count >= this.count
                ? this
                : new RepeatIterator<>(this.current, count);
    }

    @Override
    public TResult _tryGetElementAt(int index, out<Boolean> found) {
        if (index < this.count) {
            found.setValue(true);
            return this.current;
        }

        found.setValue(false);
        return null;
    }

    @Override
    public TResult _tryGetFirst(out<Boolean> found) {
        found.setValue(true);
        return this.current;
    }

    @Override
    public TResult _tryGetLast(out<Boolean> found) {
        found.setValue(true);
        return this.current;
    }

    @Override
    public int _getCount(boolean onlyIfCheap) {
        return this.count;
    }

    @Override
    public TResult[] _toArray(Class<TResult> clazz) {
        TResult[] array = ArrayUtils.newInstance(clazz, this.count);
        if (this.current != null)
            ArrayUtils.fill(array, this.current);

        return array;
    }

    @Override
    public Array<TResult> _toArray() {
        Array<TResult> array = Array.create(this.count);
        if (this.current != null)
            Array.fill(array, this.current);

        return array;
    }

    @Override
    public List<TResult> _toList() {
        List<TResult> list = new ArrayList<>(this.count);
        for (int i = 0; i != this.count; ++i)
            list.add(this.current);

        return list;
    }
}
