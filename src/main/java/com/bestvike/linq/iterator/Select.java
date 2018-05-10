package com.bestvike.linq.iterator;

import com.bestvike.collections.generic.Array;
import com.bestvike.collections.generic.IList;
import com.bestvike.function.Func1;
import com.bestvike.function.Func2;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.enumerable.ListEnumerable;
import com.bestvike.linq.exception.Errors;
import com.bestvike.linq.util.ArrayUtils;
import com.bestvike.linq.util.ListUtils;
import com.bestvike.linq.util.Utilities;
import com.bestvike.out;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 许崇雷 on 2018-05-03.
 */
public final class Select {
    private Select() {
    }

    public static <TSource, TResult> IEnumerable<TResult> select(IEnumerable<TSource> source, Func1<TSource, TResult> selector) {
        if (source == null)
            throw Errors.argumentNull("source");
        if (selector == null)
            throw Errors.argumentNull("selector");

        if (source instanceof Iterator) {
            Iterator<TSource> iterator = (Iterator<TSource>) source;
            return iterator._select(selector);
        }

        if (source instanceof IList) {
            if (source instanceof Array) {
                Array<TSource> array = (Array<TSource>) source;
                return array.length() == 0
                        ? EmptyPartition.instance()
                        : new SelectArrayIterator<>(array, selector);
            }
            ListEnumerable<TSource> list = (ListEnumerable<TSource>) source;
            return list._getCount() == 0
                    ? EmptyPartition.instance()
                    : new SelectIListIterator<>(list, selector);
        }

        if (source instanceof IPartition) {
            IPartition<TSource> partition = (IPartition<TSource>) source;
            return new SelectIPartitionIterator<>(partition, selector);
        }

        return new SelectEnumerableIterator<>(source, selector);
    }

    public static <TSource, TResult> IEnumerable<TResult> select(IEnumerable<TSource> source, Func2<TSource, Integer, TResult> selector) {
        if (source == null)
            throw Errors.argumentNull("source");

        if (selector == null)
            throw Errors.argumentNull("selector");

        return new SelectIterator<>(source, selector);
    }
}


final class SelectIterator<TSource, TResult> extends AbstractIterator<TResult> {
    private final IEnumerable<TSource> source;
    private final Func2<TSource, Integer, TResult> selector;
    private IEnumerator<TSource> enumerator;
    private int index;

    SelectIterator(IEnumerable<TSource> source, Func2<TSource, Integer, TResult> selector) {
        this.source = source;
        this.selector = selector;
    }

    @Override
    public AbstractIterator<TResult> clone() {
        return new SelectIterator<>(this.source, this.selector);
    }

    @Override
    public boolean moveNext() {
        switch (this.state) {
            case 1:
                this.index = -1;
                this.enumerator = this.source.enumerator();
                this.state = 2;
            case 2:
                if (this.enumerator.moveNext()) {
                    TSource item = this.enumerator.current();
                    this.index = Math.addExact(this.index, 1);
                    this.current = this.selector.apply(item, this.index);
                    return true;
                }
                this.close();
                return false;
            default:
                return false;
        }
    }

    @Override
    public void close() {
        if (this.enumerator != null) {
            this.enumerator.close();
            this.enumerator = null;
        }
        super.close();
    }
}


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

    @Override
    public Iterator<TResult> clone() {
        return new SelectEnumerableIterator<>(this.source, this.selector);
    }

    @Override
    public void close() {
        if (this.enumerator != null) {
            this.enumerator.close();
            this.enumerator = null;
        }
        super.close();
    }

    @Override
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

    @Override
    public <TResult2> IEnumerable<TResult2> _select(Func1<TResult, TResult2> selector) {
        return new SelectEnumerableIterator<>(this.source, Utilities.combineSelectors(this.selector, selector));
    }

    @Override
    public TResult[] _toArray(Class<TResult> clazz) {
        LargeArrayBuilder<TResult> builder = new LargeArrayBuilder<>();
        for (TSource item : this.source)
            builder.add(this.selector.apply(item));

        return builder.toArray(clazz);
    }

    @Override
    public Array<TResult> _toArray() {
        LargeArrayBuilder<TResult> builder = new LargeArrayBuilder<>();
        for (TSource item : this.source)
            builder.add(this.selector.apply(item));

        return builder.toArray();
    }

    @Override
    public List<TResult> _toList() {
        List<TResult> list = new ArrayList<>();
        for (TSource item : this.source)
            list.add(this.selector.apply(item));

        return list;
    }

    @Override
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

    @Override
    public Iterator<TResult> clone() {
        return new SelectArrayIterator<>(this.source, this.selector);
    }

    @Override
    public boolean moveNext() {
        if (this.state < 1 | this.state == this.source.length() + 1) {
            this.close();
            return false;
        }

        int index = this.state++ - 1;
        this.current = this.selector.apply(this.source.get(index));
        return true;
    }

    @Override
    public <TResult2> IEnumerable<TResult2> _select(Func1<TResult, TResult2> selector) {
        return new SelectArrayIterator<>(this.source, Utilities.combineSelectors(this.selector, selector));
    }

    @Override
    public TResult[] _toArray(Class<TResult> clazz) {
        // See assert : constructor.
        // Since source should never be empty, we don't check for 0/return Array.Empty.
        assert this.source.length() > 0;

        TResult[] results = ArrayUtils.newInstance(clazz, this.source.length());
        for (int i = 0; i < results.length; i++)
            results[i] = this.selector.apply(this.source.get(i));

        return results;
    }

    @Override
    public Array<TResult> _toArray() {
        // See assert : constructor.
        // Since source should never be empty, we don't check for 0/return Array.Empty.
        assert this.source.length() > 0;

        Array<TResult> results = Array.create(this.source.length());
        for (int i = 0; i < results.length(); i++)
            results.set(i, this.selector.apply(this.source.get(i)));

        return results;
    }

    @Override
    public List<TResult> _toList() {
        Array<TSource> source = this.source;
        List<TResult> results = new ArrayList<>(source.length());
        for (int i = 0; i < source.length(); i++)
            results.add(this.selector.apply(source.get(i)));

        return results;
    }

    @Override
    public int _getCount(boolean onlyIfCheap) {
        // In case someone uses Count() to force evaluation of
        // the selector, run it provided `onlyIfCheap` is false.
        if (!onlyIfCheap) {
            for (TSource item : this.source)
                this.selector.apply(item);
        }

        return this.source.length();
    }

    @Override
    public IPartition<TResult> _skip(int count) {
        assert count > 0;
        return count >= this.source.length()
                ? EmptyPartition.instance()
                : new SelectListPartitionIterator<>(this.source, this.selector, count, Integer.MAX_VALUE);
    }

    @Override
    public IPartition<TResult> _take(int count) {
        return count >= this.source.length()
                ? this
                : new SelectListPartitionIterator<>(this.source, this.selector, 0, count - 1);
    }

    @Override
    public TResult _tryGetElementAt(int index, out<Boolean> found) {
        if (index < this.source.length()) {
            found.setValue(true);
            return this.selector.apply(this.source.get(index));
        }

        found.setValue(false);
        return null;
    }

    @Override
    public TResult _tryGetFirst(out<Boolean> found) {
        assert this.source.length() > 0; // See assert : constructor

        found.setValue(true);
        return this.selector.apply(this.source.get(0));
    }

    @Override
    public TResult _tryGetLast(out<Boolean> found) {
        assert this.source.length() > 0; // See assert : constructor

        found.setValue(true);
        return this.selector.apply(this.source.get(this.source.length() - 1));
    }
}


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

    @Override
    public Iterator<TResult> clone() {
        return new SelectIListIterator<>(this.source, this.selector);
    }

    @Override
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

    @Override
    public void close() {
        if (this.enumerator != null) {
            this.enumerator.close();
            this.enumerator = null;
        }
        super.close();
    }

    @Override
    public <TResult2> IEnumerable<TResult2> _select(Func1<TResult, TResult2> selector) {
        return new SelectIListIterator<>(this.source, Utilities.combineSelectors(this.selector, selector));
    }

    @Override
    public TResult[] _toArray(Class<TResult> clazz) {
        int count = this.source._getCount();
        if (count == 0)
            return ArrayUtils.empty(clazz);

        TResult[] results = ArrayUtils.newInstance(clazz, count);
        for (int i = 0; i < results.length; i++)
            results[i] = this.selector.apply(this.source.get(i));

        return results;
    }

    @Override
    public Array<TResult> _toArray() {
        int count = this.source._getCount();
        if (count == 0)
            return Array.empty();

        Array<TResult> results = Array.create(count);
        for (int i = 0; i < results.length(); i++)
            results.set(i, this.selector.apply(this.source.get(i)));

        return results;
    }

    @Override
    public List<TResult> _toList() {
        int count = this.source._getCount();
        ArrayList<TResult> results = new ArrayList<>(count);
        for (int i = 0; i < count; i++)
            results.add(this.selector.apply(this.source.get(i)));

        return results;
    }

    @Override
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

    @Override
    public IPartition<TResult> _skip(int count) {
        assert count > 0;
        return new SelectListPartitionIterator<>(this.source, this.selector, count, Integer.MAX_VALUE);
    }

    @Override
    public IPartition<TResult> _take(int count) {
        return new SelectListPartitionIterator<>(this.source, this.selector, 0, count - 1);
    }

    @Override
    public TResult _tryGetElementAt(int index, out<Boolean> found) {
        if (index < this.source._getCount()) {
            found.setValue(true);
            return this.selector.apply(this.source.get(index));
        }

        found.setValue(false);
        return null;
    }

    @Override
    public TResult _tryGetFirst(out<Boolean> found) {
        if (this.source._getCount() != 0) {
            found.setValue(true);
            return this.selector.apply(this.source.get(0));
        }

        found.setValue(false);
        return null;
    }

    @Override
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

    @Override
    public Iterator<TResult> clone() {
        return new SelectIPartitionIterator<>(this.source, this.selector);
    }

    @Override
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

    @Override
    public void close() {
        if (this.enumerator != null) {
            this.enumerator.close();
            this.enumerator = null;
        }
        super.close();
    }

    @Override
    public <TResult2> IEnumerable<TResult2> _select(Func1<TResult, TResult2> selector) {
        return new SelectIPartitionIterator<>(this.source, Utilities.combineSelectors(this.selector, selector));
    }

    @Override
    public IPartition<TResult> _skip(int count) {
        assert count > 0;
        return new SelectIPartitionIterator<>(this.source._skip(count), this.selector);
    }

    @Override
    public IPartition<TResult> _take(int count) {
        return new SelectIPartitionIterator<>(this.source._take(count), this.selector);
    }

    @Override
    public TResult _tryGetElementAt(int index, out<Boolean> found) {
        TSource input = this.source._tryGetElementAt(index, found);
        return found.getValue() ? this.selector.apply(input) : null;
    }

    @Override
    public TResult _tryGetFirst(out<Boolean> found) {
        TSource input = this.source._tryGetFirst(found);
        return found.getValue() ? this.selector.apply(input) : null;
    }

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
    public Iterator<TResult> clone() {
        return new SelectListPartitionIterator<>(this.source, this.selector, this.minIndexInclusive, this.maxIndexInclusive);
    }

    @Override
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

    @Override
    public <TResult2> IEnumerable<TResult2> _select(Func1<TResult, TResult2> selector) {
        return new SelectListPartitionIterator<>(this.source, Utilities.combineSelectors(this.selector, selector), this.minIndexInclusive, this.maxIndexInclusive);
    }

    @Override
    public IPartition<TResult> _skip(int count) {
        assert count > 0;
        int minIndex = this.minIndexInclusive + count;
        return minIndex > this.maxIndexInclusive
                ? EmptyPartition.instance()
                : new SelectListPartitionIterator<>(this.source, this.selector, minIndex, this.maxIndexInclusive);
    }

    @Override
    public IPartition<TResult> _take(int count) {
        int maxIndex = this.minIndexInclusive + count - 1;
        return maxIndex >= this.maxIndexInclusive
                ? this
                : new SelectListPartitionIterator<>(this.source, this.selector, this.minIndexInclusive, maxIndex);
    }

    @Override
    public TResult _tryGetElementAt(int index, out<Boolean> found) {
        if (index <= (this.maxIndexInclusive - this.minIndexInclusive) && index < this.source._getCount() - this.minIndexInclusive) {
            found.setValue(true);
            return this.selector.apply(this.source.get(this.minIndexInclusive + index));
        }

        found.setValue(false);
        return null;
    }

    @Override
    public TResult _tryGetFirst(out<Boolean> found) {
        if (this.source._getCount() > this.minIndexInclusive) {
            found.setValue(true);
            return this.selector.apply(this.source.get(this.minIndexInclusive));
        }

        found.setValue(false);
        return null;
    }

    @Override
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

    @Override
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

    @Override
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

    @Override
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
