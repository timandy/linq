package com.bestvike.linq.enumerable;

import com.bestvike.collections.generic.IArray;
import com.bestvike.collections.generic.IArrayList;
import com.bestvike.function.Func1;
import com.bestvike.function.IndexPredicate2;
import com.bestvike.function.Predicate1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.exception.ExceptionArgument;
import com.bestvike.linq.exception.ThrowHelper;
import com.bestvike.linq.util.Utilities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 许崇雷 on 2018-05-08.
 */
public final class Where {
    private Where() {
    }

    public static <TSource> IEnumerable<TSource> where(IEnumerable<TSource> source, Predicate1<TSource> predicate) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (predicate == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.predicate);

        if (source instanceof Iterator) {
            Iterator<TSource> iterator = (Iterator<TSource>) source;
            return iterator._where(predicate);
        }

        if (source instanceof IArray) {
            IArray<TSource> array = (IArray<TSource>) source;
            return array._getCount() == 0
                    ? EmptyPartition.instance()
                    : new WhereArrayIterator<>(array, predicate);
        }

        if (source instanceof IArrayList) {
            IArrayList<TSource> list = (IArrayList<TSource>) source;
            return list._getCount() == 0
                    ? EmptyPartition.instance()
                    : new WhereListIterator<>(list, predicate);
        }

        return new WhereEnumerableIterator<>(source, predicate);
    }

    public static <TSource> IEnumerable<TSource> where(IEnumerable<TSource> source, IndexPredicate2<TSource> predicate) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (predicate == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.predicate);

        return new WhereIterator<>(source, predicate);
    }
}


final class WhereIterator<TSource> extends AbstractIterator<TSource> {
    private final IEnumerable<TSource> source;
    private final IndexPredicate2<TSource> predicate;
    private IEnumerator<TSource> enumerator;
    private int index;

    WhereIterator(IEnumerable<TSource> source, IndexPredicate2<TSource> predicate) {
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


final class WhereEnumerableIterator<TSource> extends Iterator<TSource> implements IIListProvider<TSource> {
    private final IEnumerable<TSource> source;
    private final Predicate1<TSource> predicate;
    private IEnumerator<TSource> enumerator;

    WhereEnumerableIterator(IEnumerable<TSource> source, Predicate1<TSource> predicate) {
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
    public void close() {
        if (this.enumerator != null) {
            this.enumerator.close();
            this.enumerator = null;
        }
        super.close();
    }

    @Override
    public <TResult> IEnumerable<TResult> _select(Func1<TSource, TResult> selector) {
        return new WhereSelectEnumerableIterator<>(this.source, this.predicate, selector);
    }

    @Override
    public IEnumerable<TSource> _where(Predicate1<TSource> predicate) {
        return new WhereEnumerableIterator<>(this.source, Utilities.combinePredicates(this.predicate, predicate));
    }

    @Override
    public TSource[] _toArray(Class<TSource> clazz) {
        LargeArrayBuilder<TSource> builder = new LargeArrayBuilder<>();
        try (IEnumerator<TSource> e = this.source.enumerator()) {
            while (e.moveNext()) {
                TSource item = e.current();
                if (this.predicate.apply(item))
                    builder.add(item);
            }
        }

        return builder.toArray(clazz);
    }

    @Override
    public Object[] _toArray() {
        LargeArrayBuilder<TSource> builder = new LargeArrayBuilder<>();
        try (IEnumerator<TSource> e = this.source.enumerator()) {
            while (e.moveNext()) {
                TSource item = e.current();
                if (this.predicate.apply(item))
                    builder.add(item);
            }
        }

        return builder.toArray();
    }

    @Override
    public List<TSource> _toList() {
        List<TSource> list = new ArrayList<>();
        try (IEnumerator<TSource> e = this.source.enumerator()) {
            while (e.moveNext()) {
                TSource item = e.current();
                if (this.predicate.apply(item))
                    list.add(item);
            }
        }

        return list;
    }

    @Override
    public int _getCount(boolean onlyIfCheap) {
        if (onlyIfCheap)
            return -1;

        int count = 0;
        try (IEnumerator<TSource> e = this.source.enumerator()) {
            while (e.moveNext()) {
                if (this.predicate.apply(e.current()))
                    count = Math.addExact(count, 1);
            }
        }

        return count;
    }
}


final class WhereArrayIterator<TSource> extends Iterator<TSource> implements IIListProvider<TSource> {
    private final IArray<TSource> source;
    private final Predicate1<TSource> predicate;

    WhereArrayIterator(IArray<TSource> source, Predicate1<TSource> predicate) {
        assert source != null && source._getCount() > 0;
        assert predicate != null;
        this.source = source;
        this.predicate = predicate;
    }

    @Override
    public AbstractIterator<TSource> clone() {
        return new WhereArrayIterator<>(this.source, this.predicate);
    }

    @Override
    public boolean moveNext() {
        if (this.state == -1)
            return false;

        int index = this.state - 1;
        while (Integer.compareUnsigned(index, this.source._getCount()) < 0) {
            TSource item = this.source.get(index);
            index = this.state++;
            if (this.predicate.apply(item)) {
                this.current = item;
                return true;
            }
        }
        this.close();
        return false;
    }

    @Override
    public <TResult> IEnumerable<TResult> _select(Func1<TSource, TResult> selector) {
        return new WhereSelectArrayIterator<>(this.source, this.predicate, selector);
    }

    @Override
    public IEnumerable<TSource> _where(Predicate1<TSource> predicate) {
        return new WhereArrayIterator<>(this.source, Utilities.combinePredicates(this.predicate, predicate));
    }

    @Override
    public TSource[] _toArray(Class<TSource> clazz) {
        int count = this.source._getCount();
        LargeArrayBuilder<TSource> builder = new LargeArrayBuilder<>(count);
        for (int i = 0; i < count; i++) {
            TSource item = this.source.get(i);
            if (this.predicate.apply(item))
                builder.add(item);
        }

        return builder.toArray(clazz);
    }

    @Override
    public Object[] _toArray() {
        int count = this.source._getCount();
        LargeArrayBuilder<TSource> builder = new LargeArrayBuilder<>(count);
        for (int i = 0; i < count; i++) {
            TSource item = this.source.get(i);
            if (this.predicate.apply(item))
                builder.add(item);
        }

        return builder.toArray();
    }

    @Override
    public List<TSource> _toList() {
        List<TSource> list = new ArrayList<>();
        for (int i = 0, count = this.source._getCount(); i < count; i++) {
            TSource item = this.source.get(i);
            if (this.predicate.apply(item))
                list.add(item);
        }

        return list;
    }

    @Override
    public int _getCount(boolean onlyIfCheap) {
        if (onlyIfCheap)
            return -1;

        int count = 0;
        for (int i = 0, length = this.source._getCount(); i < length; i++) {
            if (this.predicate.apply(this.source.get(i)))
                count = Math.addExact(count, 1);
        }

        return count;
    }
}


final class WhereListIterator<TSource> extends Iterator<TSource> implements IIListProvider<TSource> {
    private final IArrayList<TSource> source;
    private final Predicate1<TSource> predicate;
    private IEnumerator<TSource> enumerator;

    WhereListIterator(IArrayList<TSource> source, Predicate1<TSource> predicate) {
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
    public void close() {
        if (this.enumerator != null) {
            this.enumerator.close();
            this.enumerator = null;
        }
        super.close();
    }

    @Override
    public <TResult> IEnumerable<TResult> _select(Func1<TSource, TResult> selector) {
        return new WhereSelectListIterator<>(this.source, this.predicate, selector);
    }

    @Override
    public IEnumerable<TSource> _where(Predicate1<TSource> predicate) {
        return new WhereListIterator<>(this.source, Utilities.combinePredicates(this.predicate, predicate));
    }

    @Override
    public TSource[] _toArray(Class<TSource> clazz) {
        int count = this.source._getCount();
        LargeArrayBuilder<TSource> builder = new LargeArrayBuilder<>(count);
        for (int i = 0; i < count; i++) {
            TSource item = this.source.get(i);
            if (this.predicate.apply(item))
                builder.add(item);
        }

        return builder.toArray(clazz);
    }

    @Override
    public Object[] _toArray() {
        int count = this.source._getCount();
        LargeArrayBuilder<TSource> builder = new LargeArrayBuilder<>(count);
        for (int i = 0; i < count; i++) {
            TSource item = this.source.get(i);
            if (this.predicate.apply(item))
                builder.add(item);
        }

        return builder.toArray();
    }

    @Override
    public List<TSource> _toList() {
        List<TSource> list = new ArrayList<>();
        for (int i = 0, count = this.source._getCount(); i < count; i++) {
            TSource item = this.source.get(i);
            if (this.predicate.apply(item))
                list.add(item);
        }

        return list;
    }

    @Override
    public int _getCount(boolean onlyIfCheap) {
        if (onlyIfCheap)
            return -1;

        int count = 0;
        for (int i = 0, length = this.source._getCount(); i < length; i++) {
            if (this.predicate.apply(this.source.get(i)))
                count = Math.addExact(count, 1);
        }

        return count;
    }
}


final class WhereSelectArrayIterator<TSource, TResult> extends Iterator<TResult> implements IIListProvider<TResult> {
    private final IArray<TSource> source;
    private final Predicate1<TSource> predicate;
    private final Func1<TSource, TResult> selector;

    WhereSelectArrayIterator(IArray<TSource> source, Predicate1<TSource> predicate, Func1<TSource, TResult> selector) {
        assert source != null && source._getCount() > 0;
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
    public boolean moveNext() {
        if (this.state == -1)
            return false;

        int index = this.state - 1;
        while (Integer.compareUnsigned(index, this.source._getCount()) < 0) {
            TSource item = this.source.get(index);
            index = this.state++;
            if (this.predicate.apply(item)) {
                this.current = this.selector.apply(item);
                return true;
            }
        }
        this.close();
        return false;
    }

    @Override
    public <TResult2> IEnumerable<TResult2> _select(Func1<TResult, TResult2> selector) {
        return new WhereSelectArrayIterator<>(this.source, this.predicate, Utilities.combineSelectors(this.selector, selector));
    }

    @Override
    public TResult[] _toArray(Class<TResult> clazz) {
        int count = this.source._getCount();
        LargeArrayBuilder<TResult> builder = new LargeArrayBuilder<>(count);
        for (int i = 0; i < count; i++) {
            TSource item = this.source.get(i);
            if (this.predicate.apply(item))
                builder.add(this.selector.apply(item));
        }

        return builder.toArray(clazz);
    }

    @Override
    public Object[] _toArray() {
        int count = this.source._getCount();
        LargeArrayBuilder<TResult> builder = new LargeArrayBuilder<>(count);
        for (int i = 0; i < count; i++) {
            TSource item = this.source.get(i);
            if (this.predicate.apply(item))
                builder.add(this.selector.apply(item));
        }

        return builder.toArray();
    }

    @Override
    public List<TResult> _toList() {
        List<TResult> list = new ArrayList<>();
        for (int i = 0, count = this.source._getCount(); i < count; i++) {
            TSource item = this.source.get(i);
            if (this.predicate.apply(item))
                list.add(this.selector.apply(item));
        }

        return list;
    }

    @Override
    public int _getCount(boolean onlyIfCheap) {
        // In case someone uses Count() to force evaluation of
        // the selector, run it provided `onlyIfCheap` is false.
        if (onlyIfCheap)
            return -1;

        int count = 0;
        for (int i = 0, length = this.source._getCount(); i < length; i++) {
            TSource item = this.source.get(i);
            if (this.predicate.apply(item)) {
                this.selector.apply(item);
                count = Math.addExact(count, 1);
            }
        }

        return count;
    }
}


final class WhereSelectListIterator<TSource, TResult> extends Iterator<TResult> implements IIListProvider<TResult> {
    private final IArrayList<TSource> source;
    private final Predicate1<TSource> predicate;
    private final Func1<TSource, TResult> selector;
    private IEnumerator<TSource> enumerator;

    WhereSelectListIterator(IArrayList<TSource> source, Predicate1<TSource> predicate, Func1<TSource, TResult> selector) {
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
    public void close() {
        if (this.enumerator != null) {
            this.enumerator.close();
            this.enumerator = null;
        }
        super.close();
    }

    @Override
    public <TResult2> IEnumerable<TResult2> _select(Func1<TResult, TResult2> selector) {
        return new WhereSelectListIterator<>(this.source, this.predicate, Utilities.combineSelectors(this.selector, selector));
    }

    @Override
    public TResult[] _toArray(Class<TResult> clazz) {
        int count = this.source._getCount();
        LargeArrayBuilder<TResult> builder = new LargeArrayBuilder<>(count);
        for (int i = 0; i < count; i++) {
            TSource item = this.source.get(i);
            if (this.predicate.apply(item))
                builder.add(this.selector.apply(item));
        }

        return builder.toArray(clazz);
    }

    @Override
    public Object[] _toArray() {
        int count = this.source._getCount();
        LargeArrayBuilder<TResult> builder = new LargeArrayBuilder<>(count);
        for (int i = 0; i < count; i++) {
            TSource item = this.source.get(i);
            if (this.predicate.apply(item))
                builder.add(this.selector.apply(item));
        }

        return builder.toArray();
    }

    @Override
    public List<TResult> _toList() {
        List<TResult> list = new ArrayList<>();
        for (int i = 0, count = this.source._getCount(); i < count; i++) {
            TSource item = this.source.get(i);
            if (this.predicate.apply(item))
                list.add(this.selector.apply(item));
        }

        return list;
    }

    @Override
    public int _getCount(boolean onlyIfCheap) {
        // In case someone uses Count() to force evaluation of
        // the selector, run it provided `onlyIfCheap` is false.
        if (onlyIfCheap)
            return -1;

        int count = 0;
        for (int i = 0, length = this.source._getCount(); i < length; i++) {
            TSource item = this.source.get(i);
            if (this.predicate.apply(item)) {
                this.selector.apply(item);
                count = Math.addExact(count, 1);
            }
        }

        return count;
    }
}


final class WhereSelectEnumerableIterator<TSource, TResult> extends Iterator<TResult> implements IIListProvider<TResult> {
    private final IEnumerable<TSource> source;
    private final Predicate1<TSource> predicate;
    private final Func1<TSource, TResult> selector;
    private IEnumerator<TSource> enumerator;

    WhereSelectEnumerableIterator(IEnumerable<TSource> source, Predicate1<TSource> predicate, Func1<TSource, TResult> selector) {
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
    public void close() {
        if (this.enumerator != null) {
            this.enumerator.close();
            this.enumerator = null;
        }
        super.close();
    }

    @Override
    public <TResult2> IEnumerable<TResult2> _select(Func1<TResult, TResult2> selector) {
        return new WhereSelectEnumerableIterator<>(this.source, this.predicate, Utilities.combineSelectors(this.selector, selector));
    }

    @Override
    public TResult[] _toArray(Class<TResult> clazz) {
        LargeArrayBuilder<TResult> builder = new LargeArrayBuilder<>();
        try (IEnumerator<TSource> e = this.source.enumerator()) {
            while (e.moveNext()) {
                TSource item = e.current();
                if (this.predicate.apply(item))
                    builder.add(this.selector.apply(item));
            }
        }

        return builder.toArray(clazz);
    }

    @Override
    public Object[] _toArray() {
        LargeArrayBuilder<TResult> builder = new LargeArrayBuilder<>();
        try (IEnumerator<TSource> e = this.source.enumerator()) {
            while (e.moveNext()) {
                TSource item = e.current();
                if (this.predicate.apply(item))
                    builder.add(this.selector.apply(item));
            }
        }

        return builder.toArray();
    }

    @Override
    public List<TResult> _toList() {
        List<TResult> list = new ArrayList<>();
        try (IEnumerator<TSource> e = this.source.enumerator()) {
            while (e.moveNext()) {
                TSource item = e.current();
                if (this.predicate.apply(item))
                    list.add(this.selector.apply(item));
            }
        }

        return list;
    }

    @Override
    public int _getCount(boolean onlyIfCheap) {
        // In case someone uses Count() to force evaluation of
        // the selector, run it provided `onlyIfCheap` is false.
        if (onlyIfCheap)
            return -1;

        int count = 0;
        try (IEnumerator<TSource> e = this.source.enumerator()) {
            while (e.moveNext()) {
                TSource item = e.current();
                if (this.predicate.apply(item)) {
                    this.selector.apply(item);
                    count = Math.addExact(count, 1);
                }
            }
        }

        return count;
    }
}
