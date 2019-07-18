package com.bestvike.linq.enumerable;

import com.bestvike.collections.generic.IEqualityComparer;
import com.bestvike.function.Func1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.exception.ExceptionArgument;
import com.bestvike.linq.exception.ThrowHelper;
import com.bestvike.linq.util.Utilities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 许崇雷 on 2018-05-09.
 */
public final class UnionBy {
    private UnionBy() {
    }

    public static <TSource, TKey> IEnumerable<TSource> unionBy(IEnumerable<TSource> first, IEnumerable<TSource> second, Func1<TSource, TKey> keySelector) {
        return unionBy(first, second, keySelector, null);
    }

    public static <TSource, TKey> IEnumerable<TSource> unionBy(IEnumerable<TSource> first, IEnumerable<TSource> second, Func1<TSource, TKey> keySelector, IEqualityComparer<TKey> comparer) {
        if (first == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.first);
        if (second == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.second);
        if (keySelector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.keySelector);

        UnionByIterator<TSource, TKey> unionBy;
        return first instanceof UnionByIterator && keySelector == (unionBy = (UnionByIterator<TSource, TKey>) first).keySelector && Utilities.areEqualityComparersEqual(comparer, unionBy.comparer)
                ? unionBy._unionBy(second)
                : new UnionByIterator2<>(first, second, keySelector, comparer);
    }
}


abstract class UnionByIterator<TSource, TKey> extends Iterator<TSource> implements IIListProvider<TSource> {
    final Func1<TSource, TKey> keySelector;
    final IEqualityComparer<TKey> comparer;
    private IEnumerator<TSource> enumerator;
    private Set<TKey> set;

    UnionByIterator(Func1<TSource, TKey> keySelector, IEqualityComparer<TKey> comparer) {
        this.keySelector = keySelector;
        this.comparer = comparer;
    }

    @Override
    public void close() {
        if (this.enumerator != null) {
            this.enumerator.close();
            this.enumerator = null;
            this.set = null;
        }
        super.close();
    }

    abstract IEnumerable<TSource> getEnumerable(int index);

    abstract UnionByIterator<TSource, TKey> _unionBy(IEnumerable<TSource> next);

    private void setEnumerator(IEnumerator<TSource> enumerator) {
        if (this.enumerator != null)
            this.enumerator.close();
        this.enumerator = enumerator;
    }

    private void storeFirst() {
        Set<TKey> set = new Set<>(this.comparer);
        TSource element = this.enumerator.current();
        set.add(this.keySelector.apply(element));
        this.current = element;
        this.set = set;
    }

    private boolean getNext() {
        Set<TKey> set = this.set;
        assert set != null;

        while (this.enumerator.moveNext()) {
            TSource element = this.enumerator.current();
            if (set.add(this.keySelector.apply(element))) {
                this.current = element;
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean moveNext() {
        if (this.state == 1) {
            for (IEnumerable<TSource> enumerable = this.getEnumerable(0); enumerable != null; enumerable = this.getEnumerable(this.state - 1)) {
                IEnumerator<TSource> enumerator = enumerable.enumerator();
                ++this.state;
                if (enumerator.moveNext()) {
                    this.setEnumerator(enumerator);
                    this.storeFirst();
                    return true;
                }
            }
        } else if (this.state > 0) {
            while (true) {
                if (this.getNext())
                    return true;

                IEnumerable<TSource> enumerable = this.getEnumerable(this.state - 1);
                if (enumerable == null)
                    break;

                this.setEnumerator(enumerable.enumerator());
                ++this.state;
            }
        }

        this.close();
        return false;
    }

    private Set<TKey> fillSet() {
        Set<TKey> set = new Set<>(this.comparer);
        for (int index = 0; ; ++index) {
            IEnumerable<TSource> enumerable = this.getEnumerable(index);
            if (enumerable == null)
                return set;
            set.unionWith(enumerable, this.keySelector);
        }
    }

    @Override
    public TSource[] _toArray(Class<TSource> clazz) {
        LargeArrayBuilder<TSource> builder = new LargeArrayBuilder<>();
        Set<TKey> set = new Set<>(this.comparer);
        for (int index = 0; ; ++index) {
            IEnumerable<TSource> enumerable = this.getEnumerable(index);
            if (enumerable == null)
                return builder.toArray(clazz);
            try (IEnumerator<TSource> e = enumerable.enumerator()) {
                while (e.moveNext()) {
                    TSource element = e.current();
                    if (set.add(this.keySelector.apply(element)))
                        builder.add(element);
                }
            }
        }
    }

    @Override
    public Object[] _toArray() {
        LargeArrayBuilder<TSource> builder = new LargeArrayBuilder<>();
        Set<TKey> set = new Set<>(this.comparer);
        for (int index = 0; ; ++index) {
            IEnumerable<TSource> enumerable = this.getEnumerable(index);
            if (enumerable == null)
                return builder.toArray();
            try (IEnumerator<TSource> e = enumerable.enumerator()) {
                while (e.moveNext()) {
                    TSource element = e.current();
                    if (set.add(this.keySelector.apply(element)))
                        builder.add(element);
                }
            }
        }
    }

    @Override
    public List<TSource> _toList() {
        List<TSource> list = new ArrayList<>();
        Set<TKey> set = new Set<>(this.comparer);
        for (int index = 0; ; ++index) {
            IEnumerable<TSource> enumerable = this.getEnumerable(index);
            if (enumerable == null)
                return list;
            try (IEnumerator<TSource> e = enumerable.enumerator()) {
                while (e.moveNext()) {
                    TSource element = e.current();
                    if (set.add(this.keySelector.apply(element)))
                        list.add(element);
                }
            }
        }
    }

    @Override
    public int _getCount(boolean onlyIfCheap) {
        return onlyIfCheap ? -1 : this.fillSet().getCount();
    }
}


final class UnionByIterator2<TSource, TKey> extends UnionByIterator<TSource, TKey> {
    private final IEnumerable<TSource> first;
    private final IEnumerable<TSource> second;

    UnionByIterator2(IEnumerable<TSource> first, IEnumerable<TSource> second, Func1<TSource, TKey> keySelector, IEqualityComparer<TKey> comparer) {
        super(keySelector, comparer);
        assert first != null;
        assert second != null;
        this.first = first;
        this.second = second;
    }

    @Override
    public Iterator<TSource> clone() {
        return new UnionByIterator2<>(this.first, this.second, this.keySelector, this.comparer);
    }

    @Override
    IEnumerable<TSource> getEnumerable(int index) {
        assert index >= 0 && index <= 2;
        switch (index) {
            case 0:
                return this.first;
            case 1:
                return this.second;
            default:
                return null;
        }
    }

    @Override
    UnionByIterator<TSource, TKey> _unionBy(IEnumerable<TSource> next) {
        SingleLinkedNode<IEnumerable<TSource>> sources = new SingleLinkedNode<>(this.first).add(this.second).add(next);
        return new UnionByIteratorN<>(sources, 2, this.keySelector, this.comparer);
    }
}


final class UnionByIteratorN<TSource, TKey> extends UnionByIterator<TSource, TKey> {
    private final SingleLinkedNode<IEnumerable<TSource>> sources;
    private final int headIndex;

    UnionByIteratorN(SingleLinkedNode<IEnumerable<TSource>> sources, int headIndex, Func1<TSource, TKey> keySelector, IEqualityComparer<TKey> comparer) {
        super(keySelector, comparer);
        assert headIndex >= 2;
        assert sources != null && sources.getCount() == headIndex + 1;
        this.sources = sources;
        this.headIndex = headIndex;
    }

    @Override
    public Iterator<TSource> clone() {
        return new UnionByIteratorN<>(this.sources, this.headIndex, this.keySelector, this.comparer);
    }

    @Override
    IEnumerable<TSource> getEnumerable(int index) {
        return index > this.headIndex ? null : this.sources.getNode(this.headIndex - index).getItem();
    }

    @Override
    UnionByIterator<TSource, TKey> _unionBy(IEnumerable<TSource> next) {
        if (this.headIndex == Integer.MAX_VALUE - 2) {
            // In the unlikely case of this many unions, if we produced a UnionIteratorN
            // with int.MaxValue then state would overflow before it matched it's index.
            // So we use the naïve approach of just having a left and right sequence.
            return new UnionByIterator2<>(this, next, this.keySelector, this.comparer);
        }

        return new UnionByIteratorN<>(this.sources.add(next), this.headIndex + 1, this.keySelector, this.comparer);
    }
}
