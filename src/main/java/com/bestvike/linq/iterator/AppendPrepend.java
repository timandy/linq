package com.bestvike.linq.iterator;

import com.bestvike.collections.generic.Array;
import com.bestvike.collections.generic.ICollection;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.exception.Errors;
import com.bestvike.linq.util.ArrayUtils;
import com.bestvike.linq.util.ListUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by 许崇雷 on 2017-09-11.
 */
public final class AppendPrepend {
    private AppendPrepend() {
    }

    public static <TSource> IEnumerable<TSource> append(IEnumerable<TSource> source, TSource element) {
        if (source == null)
            throw Errors.argumentNull("source");

        if (source instanceof AppendPrependIterator) {
            AppendPrependIterator<TSource> appendable = (AppendPrependIterator<TSource>) source;
            return appendable._append(element);
        }
        return new AppendPrepend1Iterator<>(source, element, true);
    }

    public static <TSource> IEnumerable<TSource> prepend(IEnumerable<TSource> source, TSource element) {
        if (source == null)
            throw Errors.argumentNull("source");

        if (source instanceof AppendPrependIterator) {
            AppendPrependIterator<TSource> appendable = (AppendPrependIterator<TSource>) source;
            return appendable._prepend(element);
        }
        return new AppendPrepend1Iterator<>(source, element, false);
    }
}


abstract class AppendPrependIterator<TSource> extends Iterator<TSource> implements IIListProvider<TSource> {
    protected final IEnumerable<TSource> source;
    protected IEnumerator<TSource> enumerator;

    AppendPrependIterator(IEnumerable<TSource> source) {
        assert source != null;
        this.source = source;
    }

    protected void getSourceEnumerator() {
        assert this.enumerator == null;
        this.enumerator = this.source.enumerator();
    }

    public abstract AppendPrependIterator<TSource> _append(TSource item);

    public abstract AppendPrependIterator<TSource> _prepend(TSource item);

    protected boolean loadFromEnumerator() {
        if (this.enumerator.moveNext()) {
            this.current = this.enumerator.current();
            return true;
        }

        this.close();
        return false;
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
    public abstract TSource[] _toArray(Class<TSource> clazz);

    @Override
    public abstract Array<TSource> _toArray();

    @Override
    public abstract List<TSource> _toList();

    @Override
    public abstract int _getCount(boolean onlyIfCheap);
}


final class AppendPrepend1Iterator<TSource> extends AppendPrependIterator<TSource> {
    private final TSource item;
    private final boolean appending;

    AppendPrepend1Iterator(IEnumerable<TSource> source, TSource item, boolean appending) {
        super(source);
        this.item = item;
        this.appending = appending;
    }

    @Override
    public Iterator<TSource> clone() {
        return new AppendPrepend1Iterator<>(this.source, this.item, this.appending);
    }

    @Override
    public boolean moveNext() {
        switch (this.state) {
            case 1:
                this.state = 2;
                if (!this.appending) {
                    this.current = this.item;
                    return true;
                }
            case 2:
                this.getSourceEnumerator();
                this.state = 3;
            case 3:
                if (this.loadFromEnumerator())
                    return true;
                if (this.appending) {
                    this.current = this.item;
                    return true;
                }
                break;
        }

        this.close();
        return false;
    }

    @Override
    public AppendPrependIterator<TSource> _append(TSource item) {
        return this.appending
                ? new AppendPrependNIterator<>(this.source, null, new SingleLinkedNode<>(this.item).add(item), 0, 2)
                : new AppendPrependNIterator<>(this.source, new SingleLinkedNode<>(this.item), new SingleLinkedNode<>(item), 1, 1);
    }

    @Override
    public AppendPrependIterator<TSource> _prepend(TSource item) {
        return this.appending
                ? new AppendPrependNIterator<>(this.source, new SingleLinkedNode<>(item), new SingleLinkedNode<>(this.item), 1, 1)
                : new AppendPrependNIterator<>(this.source, new SingleLinkedNode<>(this.item).add(item), null, 2, 0);
    }

    private TSource[] lazyToArray(Class<TSource> clazz) {
        assert this._getCount(true) == -1;

        LargeArrayBuilder<TSource> builder = new LargeArrayBuilder<>();
        if (!this.appending)
            builder.slowAdd(this.item);
        builder.addRange(this.source);
        if (this.appending)
            builder.slowAdd(this.item);
        return builder.toArray(clazz);
    }

    private Array<TSource> lazyToArray() {
        assert this._getCount(true) == -1;

        LargeArrayBuilder<TSource> builder = new LargeArrayBuilder<>();
        if (!this.appending)
            builder.slowAdd(this.item);
        builder.addRange(this.source);
        if (this.appending)
            builder.slowAdd(this.item);
        return builder.toArray();
    }

    @Override
    public TSource[] _toArray(Class<TSource> clazz) {
        int count = this._getCount(true);
        if (count == -1)
            return this.lazyToArray(clazz);

        TSource[] array = ArrayUtils.newInstance(clazz, count);
        int index;
        if (this.appending) {
            index = 0;
        } else {
            array[0] = this.item;
            index = 1;
        }

        EnumerableHelpers.copy(this.source, array, index, count - 1);
        if (this.appending)
            array[array.length - 1] = this.item;

        return array;
    }

    @Override
    public Array<TSource> _toArray() {
        int count = this._getCount(true);
        if (count == -1)
            return this.lazyToArray();

        Array<TSource> array = Array.create(count);
        int index;
        if (this.appending) {
            index = 0;
        } else {
            array.set(0, this.item);
            index = 1;
        }

        EnumerableHelpers.copy(this.source, array, index, count - 1);
        if (this.appending)
            array.set(array.length() - 1, this.item);

        return array;
    }

    @Override
    public List<TSource> _toList() {
        int count = this._getCount(true);
        List<TSource> list = count == -1 ? new ArrayList<>() : new ArrayList<>(count);
        if (!this.appending)
            list.add(this.item);

        ListUtils.addRange(list, this.source);

        if (this.appending)
            list.add(this.item);
        return list;
    }

    @Override
    public int _getCount(boolean onlyIfCheap) {
        if (this.source instanceof IIListProvider) {
            IIListProvider<TSource> listProv = (IIListProvider<TSource>) this.source;
            int count = listProv._getCount(onlyIfCheap);
            return count == -1 ? -1 : count + 1;
        }

        return !onlyIfCheap || this.source instanceof ICollection ? this.source.count() + 1 : -1;
    }
}


final class AppendPrependNIterator<TSource> extends AppendPrependIterator<TSource> {
    private final SingleLinkedNode<TSource> prepended;
    private final SingleLinkedNode<TSource> appended;
    private final int prependCount;
    private final int appendCount;
    private SingleLinkedNode<TSource> node;

    AppendPrependNIterator(IEnumerable<TSource> source, SingleLinkedNode<TSource> prepended, SingleLinkedNode<TSource> appended, int prependCount, int appendCount) {
        super(source);

        assert prepended != null || appended != null;
        assert prependCount > 0 || appendCount > 0;
        assert prependCount + appendCount >= 2;
        assert (prepended == null ? 0 : prepended.getCount()) == prependCount;
        assert (appended == null ? 0 : appended.getCount()) == appendCount;

        this.prepended = prepended;
        this.appended = appended;
        this.prependCount = prependCount;
        this.appendCount = appendCount;
    }

    @Override
    public Iterator<TSource> clone() {
        return new AppendPrependNIterator<>(this.source, this.prepended, this.appended, this.prependCount, this.appendCount);
    }

    @Override
    public boolean moveNext() {
        switch (this.state) {
            case 1:
                this.node = this.prepended;
                this.state = 2;
            case 2:
                if (this.node != null) {
                    this.current = this.node.getItem();
                    this.node = this.node.getLinked();
                    return true;
                }
                this.getSourceEnumerator();
                this.state = 3;
            case 3:
                if (this.loadFromEnumerator())
                    return true;
                if (this.appended == null)
                    return false;
                this.enumerator = this.appended.enumerator(this.appendCount);
                this.state = 4;
            case 4:
                return this.loadFromEnumerator();
        }

        this.close();
        return false;
    }

    @Override
    public AppendPrependIterator<TSource> _append(TSource item) {
        SingleLinkedNode<TSource> appended = this.appended != null ? this.appended.add(item) : new SingleLinkedNode<>(item);
        return new AppendPrependNIterator<>(this.source, this.prepended, appended, this.prependCount, this.appendCount + 1);
    }

    @Override
    public AppendPrependIterator<TSource> _prepend(TSource item) {
        SingleLinkedNode<TSource> prepended = this.prepended != null ? this.prepended.add(item) : new SingleLinkedNode<>(item);
        return new AppendPrependNIterator<>(this.source, prepended, this.appended, this.prependCount + 1, this.appendCount);
    }

    private TSource[] lazyToArray(Class<TSource> clazz) {
        assert this._getCount(true) == -1;

        SparseArrayBuilder<TSource> builder = new SparseArrayBuilder<>();
        if (this.prepended != null)
            builder.reserve(this.prependCount);

        builder.addRange(this.source);
        if (this.appended != null)
            builder.reserve(this.appendCount);

        TSource[] array = builder.toArray(clazz);
        int index = 0;
        for (SingleLinkedNode<TSource> node = this.prepended; node != null; node = node.getLinked())
            array[index++] = node.getItem();

        index = array.length - 1;
        for (SingleLinkedNode<TSource> node = this.appended; node != null; node = node.getLinked())
            array[index--] = node.getItem();

        return array;
    }

    private Array<TSource> lazyToArray() {
        assert this._getCount(true) == -1;

        SparseArrayBuilder<TSource> builder = new SparseArrayBuilder<>();
        if (this.prepended != null)
            builder.reserve(this.prependCount);

        builder.addRange(this.source);
        if (this.appended != null)
            builder.reserve(this.appendCount);

        Array<TSource> array = builder.toArray();
        int index = 0;
        for (SingleLinkedNode<TSource> node = this.prepended; node != null; node = node.getLinked())
            array.set(index++, node.getItem());

        index = array.length() - 1;
        for (SingleLinkedNode<TSource> node = this.appended; node != null; node = node.getLinked())
            array.set(index--, node.getItem());

        return array;
    }

    @Override
    public TSource[] _toArray(Class<TSource> clazz) {
        int count = this._getCount(true);
        if (count == -1)
            return this.lazyToArray(clazz);

        TSource[] array = ArrayUtils.newInstance(clazz, count);
        int index = 0;
        for (SingleLinkedNode<TSource> node = this.prepended; node != null; node = node.getLinked()) {
            array[index] = node.getItem();
            ++index;
        }

        if (this.source instanceof ICollection) {
            ICollection<TSource> sourceCollection = (ICollection<TSource>) this.source;
            sourceCollection._copyTo(array, index);
        } else {
            try (IEnumerator<TSource> e = this.source.enumerator()) {
                while (e.moveNext()) {
                    array[index] = e.current();
                    ++index;
                }
            }
        }

        index = array.length;
        for (SingleLinkedNode<TSource> node = this.appended; node != null; node = node.getLinked()) {
            --index;
            array[index] = node.getItem();
        }

        return array;
    }

    @Override
    public Array<TSource> _toArray() {
        int count = this._getCount(true);
        if (count == -1)
            return this.lazyToArray();

        Array<TSource> array = Array.create(count);
        int index = 0;
        for (SingleLinkedNode<TSource> node = this.prepended; node != null; node = node.getLinked()) {
            array.set(index, node.getItem());
            ++index;
        }

        if (this.source instanceof ICollection) {
            ICollection<TSource> sourceCollection = (ICollection<TSource>) this.source;
            sourceCollection._copyTo(array, index);
        } else {
            try (IEnumerator<TSource> e = this.source.enumerator()) {
                while (e.moveNext()) {
                    array.set(index, e.current());
                    ++index;
                }
            }
        }

        index = array.length();
        for (SingleLinkedNode<TSource> node = this.appended; node != null; node = node.getLinked()) {
            --index;
            array.set(index, node.getItem());
        }

        return array;
    }

    @Override
    public List<TSource> _toList() {
        int count = this._getCount(true);
        List<TSource> list = count == -1 ? new ArrayList<>() : new ArrayList<>(count);
        for (SingleLinkedNode<TSource> node = this.prepended; node != null; node = node.getLinked())
            list.add(node.getItem());

        ListUtils.addRange(list, this.source);
        if (this.appended != null) {
            IEnumerator<TSource> e = this.appended.enumerator(this.appendCount);
            while (e.moveNext())
                list.add(e.current());
        }

        return list;
    }

    @Override
    public int _getCount(boolean onlyIfCheap) {
        if (this.source instanceof IIListProvider) {
            IIListProvider<TSource> listProv = (IIListProvider<TSource>) this.source;
            int count = listProv._getCount(onlyIfCheap);
            return count == -1 ? -1 : count + this.appendCount + this.prependCount;
        }

        return !onlyIfCheap || this.source instanceof ICollection ? this.source.count() + this.appendCount + this.prependCount : -1;
    }
}
