package com.bestvike.linq.iterator;

import com.bestvike.collections.generic.Array;
import com.bestvike.collections.generic.IEqualityComparer;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.exception.Errors;
import com.bestvike.linq.util.Utilities;

import java.util.List;

/**
 * Created by 许崇雷 on 2018-05-08.
 */
public final class Union {
    private Union() {
    }

    public static <TSource> IEnumerable<TSource> union(IEnumerable<TSource> first, IEnumerable<TSource> second) {
        return union(first, second, null);
    }

    public static <TSource> IEnumerable<TSource> union(IEnumerable<TSource> first, IEnumerable<TSource> second, IEqualityComparer<TSource> comparer) {
        if (first == null)
            throw Errors.argumentNull("first");
        if (second == null)
            throw Errors.argumentNull("second");

        UnionIterator<TSource> union;
        return first instanceof UnionIterator && Utilities.areEqualityComparersEqual(comparer, (union = (UnionIterator<TSource>) first).comparer)
                ? union._union(second)
                : new UnionIterator2<>(first, second, comparer);
    }
}


abstract class UnionIterator<TSource> extends Iterator<TSource> implements IIListProvider<TSource> {
    final IEqualityComparer<TSource> comparer;
    private IEnumerator<TSource> enumerator;
    private Set<TSource> set;

    UnionIterator(IEqualityComparer<TSource> comparer) {
        this.comparer = comparer;
    }

    @Override
    public void close() {
        if (this.enumerator != null) {
            this.enumerator.close();
            this.enumerator = null;
        }
        this.set = null;
        super.close();
    }

    abstract IEnumerable<TSource> getEnumerable(int index);

    abstract UnionIterator<TSource> _union(IEnumerable<TSource> next);

    private void setEnumerator(IEnumerator<TSource> enumerator) {
        if (this.enumerator != null)
            this.enumerator.close();
        this.enumerator = enumerator;
    }

    private void storeFirst() {
        Set<TSource> set = new Set<>(this.comparer);
        TSource element = this.enumerator.current();
        set.add(element);
        this.current = element;
        this.set = set;
    }

    private boolean getNext() {
        Set<TSource> set = this.set;
        assert set != null;

        while (this.enumerator.moveNext()) {
            TSource element = this.enumerator.current();
            if (set.add(element)) {
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

    private Set<TSource> fillSet() {
        Set<TSource> set = new Set<>(this.comparer);
        for (int index = 0; ; ++index) {
            IEnumerable<TSource> enumerable = this.getEnumerable(index);
            if (enumerable == null)
                return set;
            set.unionWith(enumerable);
        }
    }

    @Override
    public TSource[] _toArray(Class<TSource> clazz) {
        return this.fillSet().toArray(clazz);
    }

    @Override
    public Array<TSource> _toArray() {
        return this.fillSet().toArray();
    }

    @Override
    public List<TSource> _toList() {
        return this.fillSet().toList();
    }

    @Override
    public int _getCount(boolean onlyIfCheap) {
        return onlyIfCheap ? -1 : this.fillSet().getCount();
    }
}


final class UnionIterator2<TSource> extends UnionIterator<TSource> {
    private final IEnumerable<TSource> first;
    private final IEnumerable<TSource> second;

    UnionIterator2(IEnumerable<TSource> first, IEnumerable<TSource> second, IEqualityComparer<TSource> comparer) {
        super(comparer);
        assert first != null;
        assert second != null;
        this.first = first;
        this.second = second;
    }

    @Override
    public Iterator<TSource> clone() {
        return new UnionIterator2<>(this.first, this.second, this.comparer);
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
    UnionIterator<TSource> _union(IEnumerable<TSource> next) {
        SingleLinkedNode<IEnumerable<TSource>> sources = new SingleLinkedNode<>(this.first).add(this.second).add(next);
        return new UnionIteratorN<>(sources, 2, this.comparer);
    }
}


final class UnionIteratorN<TSource> extends UnionIterator<TSource> {
    private final SingleLinkedNode<IEnumerable<TSource>> sources;
    private final int headIndex;

    UnionIteratorN(SingleLinkedNode<IEnumerable<TSource>> sources, int headIndex, IEqualityComparer<TSource> comparer) {
        super(comparer);
        assert headIndex >= 2;
        assert sources != null && sources.getCount() == headIndex + 1;
        this.sources = sources;
        this.headIndex = headIndex;
    }

    @Override
    public Iterator<TSource> clone() {
        return new UnionIteratorN<>(this.sources, this.headIndex, this.comparer);
    }

    @Override
    IEnumerable<TSource> getEnumerable(int index) {
        return index > this.headIndex ? null : this.sources.getNode(this.headIndex - index).getItem();
    }

    @Override
    UnionIterator<TSource> _union(IEnumerable<TSource> next) {
        if (this.headIndex == Integer.MAX_VALUE - 2) {
            // In the unlikely case of this many unions, if we produced a UnionIteratorN
            // with int.MaxValue then state would overflow before it matched it's index.
            // So we use the naïve approach of just having a left and right sequence.
            return new UnionIterator2<>(this, next, this.comparer);
        }

        return new UnionIteratorN<>(this.sources.add(next), this.headIndex + 1, this.comparer);
    }
}
