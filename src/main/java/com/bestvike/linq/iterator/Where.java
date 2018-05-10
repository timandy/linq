package com.bestvike.linq.iterator;

import com.bestvike.collections.generic.Array;
import com.bestvike.collections.generic.ICollection;
import com.bestvike.function.Func1;
import com.bestvike.function.Func2;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.exception.Errors;
import com.bestvike.linq.util.Utilities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 许崇雷 on 2018-05-08.
 */
public final class Where {
    private Where() {
    }

    public static <TSource> IEnumerable<TSource> where(IEnumerable<TSource> source, Func1<TSource, Boolean> predicate) {
        if (source == null)
            throw Errors.argumentNull("source");
        if (predicate == null)
            throw Errors.argumentNull("predicate");

        if (source instanceof Iterator) {
            Iterator<TSource> iterator = (Iterator<TSource>) source;
            return iterator._where(predicate);
        }

        if (source instanceof Array) {
            Array<TSource> array = (Array<TSource>) source;
            return array.length() == 0
                    ? EmptyPartition.instance()
                    : new WhereArrayIterator<>(array, predicate);
        }
        if (source instanceof ICollection) {
            ICollection<TSource> collection = (ICollection<TSource>) source;
            return collection._getCount() == 0
                    ? EmptyPartition.instance()
                    : new WhereListIterator<>(collection, predicate);
        }

        return new WhereEnumerableIterator<>(source, predicate);
    }

    public static <TSource> IEnumerable<TSource> where(IEnumerable<TSource> source, Func2<TSource, Integer, Boolean> predicate) {
        if (source == null)
            throw Errors.argumentNull("source");
        if (predicate == null)
            throw Errors.argumentNull("predicate");

        return new WhereIterator<>(source, predicate);
    }
}

final class WhereIterator<TSource> extends AbstractIterator<TSource> {
    private final IEnumerable<TSource> source;
    private final Func2<TSource, Integer, Boolean> predicate;
    private IEnumerator<TSource> enumerator;
    private int index;

    WhereIterator(IEnumerable<TSource> source, Func2<TSource, Integer, Boolean> predicate) {
        assert source != null;
        assert predicate != null;
        this.source = source;
        this.predicate = predicate;
    }

    @Override
    public AbstractIterator<TSource> clone() {
        return new WhereIterator<>(this.source, this.predicate);
    }

    @Override
    public boolean moveNext() {
        switch (this.state) {
            case 1:
                this.index = -1;
                this.enumerator = this.source.enumerator();
                this.state = 2;
            case 2:
                while (this.enumerator.moveNext()) {
                    TSource item = this.enumerator.current();
                    this.index = Math.addExact(this.index, 1);
                    if (this.predicate.apply(item, this.index)) {
                        this.current = item;
                        return true;
                    }
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


final class WhereArrayIterator<TSource> extends Iterator<TSource> implements IIListProvider<TSource> {
    private final Array<TSource> source;
    private final Func1<TSource, Boolean> predicate;
    private int index;

    WhereArrayIterator(Array<TSource> source, Func1<TSource, Boolean> predicate) {
        assert source != null && source.length() > 0;
        assert predicate != null;
        this.source = source;
        this.predicate = predicate;
    }

    @Override
    public AbstractIterator<TSource> clone() {
        return new WhereArrayIterator<>(this.source, this.predicate);
    }

    @Override
    public <TResult> IEnumerable<TResult> _select(Func1<TSource, TResult> selector) {
        return new WhereSelectArrayIterator<>(this.source, this.predicate, selector);
    }

    @Override
    public IEnumerable<TSource> _where(Func1<TSource, Boolean> predicate) {
        return new WhereArrayIterator<>(this.source, Utilities.combinePredicates(this.predicate, predicate));
    }

    @Override
    public boolean moveNext() {
        switch (this.state) {
            case 1:
                this.index = -1;
                this.state = 2;
            case 2:
                while ((this.index = Math.addExact(this.index, 1)) < this.source.length()) {
                    TSource item = this.source.get(this.index);
                    if (this.predicate.apply(item)) {
                        this.current = item;
                        return true;
                    }
                }
                this.close();
                return false;
            default:
                return false;
        }
    }

    @Override
    public int _getCount(boolean onlyIfCheap) {
        if (onlyIfCheap)
            return -1;

        int count = 0;
        for (TSource item : this.source) {
            if (this.predicate.apply(item))
                count = Math.addExact(count, 1);
        }

        return count;
    }

    @Override
    public TSource[] _toArray(Class<TSource> clazz) {
        LargeArrayBuilder<TSource> builder = new LargeArrayBuilder<>(this.source.length());
        for (TSource item : this.source) {
            if (this.predicate.apply(item))
                builder.add(item);
        }

        return builder.toArray(clazz);
    }

    @Override
    public Array<TSource> _toArray() {
        LargeArrayBuilder<TSource> builder = new LargeArrayBuilder<>(this.source.length());
        for (TSource item : this.source) {
            if (this.predicate.apply(item))
                builder.add(item);
        }

        return builder.toArray();
    }

    @Override
    public List<TSource> _toList() {
        List<TSource> list = new ArrayList<>();
        for (TSource item : this.source) {
            if (this.predicate.apply(item))
                list.add(item);
        }

        return list;
    }
}


final class WhereListIterator<TSource> extends Iterator<TSource> implements IIListProvider<TSource> {
    private final ICollection<TSource> source;
    private final Func1<TSource, Boolean> predicate;
    private IEnumerator<TSource> enumerator;

    WhereListIterator(ICollection<TSource> source, Func1<TSource, Boolean> predicate) {
        assert source != null;
        assert predicate != null;
        this.source = source;
        this.predicate = predicate;
    }

    @Override
    public Iterator<TSource> clone() {
        return new WhereListIterator<>(this.source, this.predicate);
    }

    @Override
    public <TResult> IEnumerable<TResult> _select(Func1<TSource, TResult> selector) {
        return new WhereSelectListIterator<>(this.source, this.predicate, selector);
    }

    @Override
    public IEnumerable<TSource> _where(Func1<TSource, Boolean> predicate) {
        return new WhereListIterator<>(this.source, Utilities.combinePredicates(this.predicate, predicate));
    }

    @Override
    public boolean moveNext() {
        switch (this.state) {
            case 1:
                this.enumerator = this.source.enumerator();
                this.state = 2;
            case 2:
                while (this.enumerator.moveNext()) {
                    TSource item = this.enumerator.current();
                    if (this.predicate.apply(item)) {
                        this.current = item;
                        return true;
                    }
                }
                this.close();
                return false;
            default:
                return false;
        }
    }

    @Override
    public int _getCount(boolean onlyIfCheap) {
        if (onlyIfCheap)
            return -1;

        int count = 0;
        for (TSource item : this.source) {
            if (this.predicate.apply(item))
                count = Math.addExact(count, 1);
        }

        return count;
    }

    @Override
    public TSource[] _toArray(Class<TSource> clazz) {
        LargeArrayBuilder<TSource> builder = new LargeArrayBuilder<>(this.source._getCount());
        for (TSource item : this.source) {
            if (this.predicate.apply(item)) {
                builder.add(item);
            }
        }

        return builder.toArray(clazz);
    }

    @Override
    public Array<TSource> _toArray() {
        LargeArrayBuilder<TSource> builder = new LargeArrayBuilder<>(this.source._getCount());
        for (TSource item : this.source) {
            if (this.predicate.apply(item)) {
                builder.add(item);
            }
        }

        return builder.toArray();
    }

    @Override
    public List<TSource> _toList() {
        List<TSource> list = new ArrayList<>();
        for (TSource item : this.source)
            if (this.predicate.apply(item)) {
                list.add(item);
            }

        return list;
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


final class WhereEnumerableIterator<TSource> extends Iterator<TSource> implements IIListProvider<TSource> {
    private final IEnumerable<TSource> source;
    private final Func1<TSource, Boolean> predicate;
    private IEnumerator<TSource> enumerator;

    WhereEnumerableIterator(IEnumerable<TSource> source, Func1<TSource, Boolean> predicate) {
        assert source != null;
        assert predicate != null;
        this.source = source;
        this.predicate = predicate;
    }

    @Override
    public AbstractIterator<TSource> clone() {
        return new WhereEnumerableIterator<>(this.source, this.predicate);
    }

    @Override
    public <TResult> IEnumerable<TResult> _select(Func1<TSource, TResult> selector) {
        return new WhereSelectEnumerableIterator<>(this.source, this.predicate, selector);
    }

    @Override
    public IEnumerable<TSource> _where(Func1<TSource, Boolean> predicate) {
        return new WhereEnumerableIterator<>(this.source, Utilities.combinePredicates(this.predicate, predicate));
    }

    @Override
    public boolean moveNext() {
        switch (this.state) {
            case 1:
                this.enumerator = this.source.enumerator();
                this.state = 2;
            case 2:
                while (this.enumerator.moveNext()) {
                    TSource item = this.enumerator.current();
                    if (this.predicate.apply(item)) {
                        this.current = item;
                        return true;
                    }
                }
                this.close();
                return false;
            default:
                return false;
        }
    }

    @Override
    public int _getCount(boolean onlyIfCheap) {
        if (onlyIfCheap)
            return -1;

        int count = 0;
        for (TSource item : this.source) {
            if (this.predicate.apply(item))
                count = Math.addExact(count, 1);
        }

        return count;
    }

    @Override
    public TSource[] _toArray(Class<TSource> clazz) {
        LargeArrayBuilder<TSource> builder = new LargeArrayBuilder<>();
        for (TSource item : this.source) {
            if (this.predicate.apply(item))
                builder.add(item);
        }

        return builder.toArray(clazz);
    }

    @Override
    public Array<TSource> _toArray() {
        LargeArrayBuilder<TSource> builder = new LargeArrayBuilder<>();
        for (TSource item : this.source) {
            if (this.predicate.apply(item))
                builder.add(item);
        }

        return builder.toArray();
    }

    @Override
    public List<TSource> _toList() {
        List<TSource> list = new ArrayList<>();
        for (TSource item : this.source) {
            if (this.predicate.apply(item))
                list.add(item);
        }

        return list;
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


final class WhereSelectArrayIterator<TSource, TResult> extends Iterator<TResult> implements IIListProvider<TResult> {
    private final Array<TSource> source;
    private final Func1<TSource, Boolean> predicate;
    private final Func1<TSource, TResult> selector;
    private int index;

    WhereSelectArrayIterator(Array<TSource> source, Func1<TSource, Boolean> predicate, Func1<TSource, TResult> selector) {
        assert source != null && source.length() > 0;
        assert predicate != null;
        assert selector != null;
        this.source = source;
        this.predicate = predicate;
        this.selector = selector;
    }

    @Override
    public Iterator<TResult> clone() {
        return new WhereSelectArrayIterator<>(this.source, this.predicate, this.selector);
    }

    @Override
    public int _getCount(boolean onlyIfCheap) {
        // In case someone uses Count() to force evaluation of
        // the selector, run it provided `onlyIfCheap` is false.
        if (onlyIfCheap)
            return -1;

        int count = 0;
        for (TSource item : this.source) {
            if (this.predicate.apply(item)) {
                this.selector.apply(item);
                count = Math.addExact(count, 1);
            }
        }

        return count;
    }

    @Override
    public boolean moveNext() {
        switch (this.state) {
            case 1:
                this.index = -1;
                this.state = 2;
            case 2:
                while ((this.index = Math.addExact(this.index, 1)) < this.source.length()) {
                    TSource item = this.source.get(this.index);
                    if (this.predicate.apply(item)) {
                        this.current = this.selector.apply(item);
                        return true;
                    }
                }
                this.close();
                return false;
            default:
                return false;
        }
    }

    @Override
    public <TResult2> IEnumerable<TResult2> _select(Func1<TResult, TResult2> selector) {
        return
                new WhereSelectArrayIterator<>(this.source, this.predicate, Utilities.combineSelectors(this.selector, selector));
    }

    @Override
    public TResult[] _toArray(Class<TResult> clazz) {
        LargeArrayBuilder<TResult> builder = new LargeArrayBuilder<>(this.source.length());
        for (TSource item : this.source) {
            if (this.predicate.apply(item))
                builder.add(this.selector.apply(item));
        }

        return builder.toArray(clazz);
    }

    @Override
    public Array<TResult> _toArray() {
        LargeArrayBuilder<TResult> builder = new LargeArrayBuilder<>(this.source.length());
        for (TSource item : this.source) {
            if (this.predicate.apply(item))
                builder.add(this.selector.apply(item));
        }

        return builder.toArray();
    }

    @Override
    public List<TResult> _toList() {
        List<TResult> list = new ArrayList<>();
        for (TSource item : this.source) {
            if (this.predicate.apply(item))
                list.add(this.selector.apply(item));
        }

        return list;
    }
}


final class WhereSelectListIterator<TSource, TResult> extends Iterator<TResult> implements IIListProvider<TResult> {
    private final ICollection<TSource> source;
    private final Func1<TSource, Boolean> predicate;
    private final Func1<TSource, TResult> selector;
    private IEnumerator<TSource> enumerator;

    WhereSelectListIterator(ICollection<TSource> source, Func1<TSource, Boolean> predicate, Func1<TSource, TResult> selector) {
        assert source != null;
        assert predicate != null;
        assert selector != null;
        this.source = source;
        this.predicate = predicate;
        this.selector = selector;
    }

    @Override
    public Iterator<TResult> clone() {
        return new WhereSelectListIterator<>(this.source, this.predicate, this.selector);
    }

    @Override
    public int _getCount(boolean onlyIfCheap) {
        // In case someone uses Count() to force evaluation of
        // the selector, run it provided `onlyIfCheap` is false.
        if (onlyIfCheap)
            return -1;

        int count = 0;
        for (TSource item : this.source) {
            if (this.predicate.apply(item)) {
                this.selector.apply(item);
                count = Math.addExact(count, 1);
            }
        }

        return count;
    }

    @Override
    public boolean moveNext() {
        switch (this.state) {
            case 1:
                this.enumerator = this.source.enumerator();
                this.state = 2;
            case 2:
                while (this.enumerator.moveNext()) {
                    TSource item = this.enumerator.current();
                    if (this.predicate.apply(item)) {
                        this.current = this.selector.apply(item);
                        return true;
                    }
                }
                this.close();
                return false;
            default:
                return false;
        }
    }

    @Override
    public <TResult2> IEnumerable<TResult2> _select(Func1<TResult, TResult2> selector) {
        return new WhereSelectListIterator<>(this.source, this.predicate, Utilities.combineSelectors(this.selector, selector));
    }

    @Override
    public TResult[] _toArray(Class<TResult> clazz) {
        LargeArrayBuilder<TResult> builder = new LargeArrayBuilder<>(this.source._getCount());
        for (TSource item : this.source) {
            if (this.predicate.apply(item))
                builder.add(this.selector.apply(item));
        }

        return builder.toArray(clazz);
    }

    @Override
    public Array<TResult> _toArray() {
        LargeArrayBuilder<TResult> builder = new LargeArrayBuilder<>(this.source._getCount());
        for (TSource item : this.source) {
            if (this.predicate.apply(item))
                builder.add(this.selector.apply(item));
        }

        return builder.toArray();
    }

    @Override
    public List<TResult> _toList() {
        List<TResult> list = new ArrayList<>();
        for (TSource item : this.source) {
            if (this.predicate.apply(item))
                list.add(this.selector.apply(item));
        }

        return list;
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


final class WhereSelectEnumerableIterator<TSource, TResult> extends Iterator<TResult> implements IIListProvider<TResult> {
    private final IEnumerable<TSource> source;
    private final Func1<TSource, Boolean> predicate;
    private final Func1<TSource, TResult> selector;
    private IEnumerator<TSource> enumerator;

    WhereSelectEnumerableIterator(IEnumerable<TSource> source, Func1<TSource, Boolean> predicate, Func1<TSource, TResult> selector) {
        assert source != null;
        assert predicate != null;
        assert selector != null;
        this.source = source;
        this.predicate = predicate;
        this.selector = selector;
    }

    @Override
    public Iterator<TResult> clone() {
        return new WhereSelectEnumerableIterator<>(this.source, this.predicate, this.selector);
    }

    @Override
    public int _getCount(boolean onlyIfCheap) {
        // In case someone uses Count() to force evaluation of
        // the selector, run it provided `onlyIfCheap` is false.
        if (onlyIfCheap)
            return -1;

        int count = 0;
        for (TSource item : this.source) {
            if (this.predicate.apply(item)) {
                this.selector.apply(item);
                count = Math.addExact(count, 1);
            }
        }

        return count;
    }

    @Override
    public boolean moveNext() {
        switch (this.state) {
            case 1:
                this.enumerator = this.source.enumerator();
                this.state = 2;
            case 2:
                while (this.enumerator.moveNext()) {
                    TSource item = this.enumerator.current();
                    if (this.predicate.apply(item)) {
                        this.current = this.selector.apply(item);
                        return true;
                    }
                }
                this.close();
                return false;
            default:
                return false;
        }
    }

    @Override
    public <TResult2> IEnumerable<TResult2> _select(Func1<TResult, TResult2> selector) {
        return new WhereSelectEnumerableIterator<>(this.source, this.predicate, Utilities.combineSelectors(this.selector, selector));
    }

    @Override
    public TResult[] _toArray(Class<TResult> clazz) {
        LargeArrayBuilder<TResult> builder = new LargeArrayBuilder<>();
        for (TSource item : this.source) {
            if (this.predicate.apply(item))
                builder.add(this.selector.apply(item));
        }

        return builder.toArray(clazz);
    }

    @Override
    public Array<TResult> _toArray() {
        LargeArrayBuilder<TResult> builder = new LargeArrayBuilder<>();
        for (TSource item : this.source) {
            if (this.predicate.apply(item))
                builder.add(this.selector.apply(item));
        }

        return builder.toArray();
    }

    @Override
    public List<TResult> _toList() {
        List<TResult> list = new ArrayList<>();
        for (TSource item : this.source) {
            if (this.predicate.apply(item))
                list.add(this.selector.apply(item));
        }

        return list;
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
