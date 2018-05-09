package com.bestvike.linq.iterator;

import com.bestvike.collections.generic.Array;
import com.bestvike.collections.generic.EqualityComparer;
import com.bestvike.collections.generic.IEqualityComparer;
import com.bestvike.function.Func1;
import com.bestvike.function.Func2;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.IGrouping;
import com.bestvike.linq.ILookup;
import com.bestvike.linq.enumerator.AbstractEnumerator;
import com.bestvike.linq.exception.Errors;
import com.bestvike.linq.util.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 许崇雷 on 2018-05-02.
 */
public final class Lookup<TKey, TElement> implements ILookup<TKey, TElement>, IIListProvider<IGrouping<TKey, TElement>> {
    private final IEqualityComparer<TKey> comparer;
    private Array<Grouping<TKey, TElement>> groupings;
    private Grouping<TKey, TElement> lastGrouping;
    private Grouping<TKey, TElement> nullKeyGrouping;
    private int count;

    private Lookup(IEqualityComparer<TKey> comparer) {
        this.comparer = comparer == null ? EqualityComparer.Default() : comparer;
        this.groupings = Array.create(7);
    }

    static <TKey, TElement> Lookup<TKey, TElement> create(IEnumerable<TElement> source, Func1<TElement, TKey> keySelector, IEqualityComparer<TKey> comparer) {
        assert source != null;
        assert keySelector != null;

        Lookup<TKey, TElement> lookup = new Lookup<>(comparer);
        for (TElement item : source)
            lookup.getGrouping(keySelector.apply(item), true).add(item);
        return lookup;
    }

    static <TSource, TKey, TElement> Lookup<TKey, TElement> create(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector, Func1<TSource, TElement> elementSelector, IEqualityComparer<TKey> comparer) {
        assert source != null;
        assert keySelector != null;
        assert elementSelector != null;

        Lookup<TKey, TElement> lookup = new Lookup<>(comparer);
        for (TSource item : source)
            lookup.getGrouping(keySelector.apply(item), true).add(elementSelector.apply(item));
        return lookup;
    }

    static <TKey, TElement> Lookup<TKey, TElement> createForJoin(IEnumerable<TElement> source, Func1<TElement, TKey> keySelector, IEqualityComparer<TKey> comparer) {
        Lookup<TKey, TElement> lookup = new Lookup<>(comparer);
        for (TElement item : source) {
            TKey key = keySelector.apply(item);
            if (key != null)
                lookup.getGrouping(key, true).add(item);
        }
        return lookup;
    }

    static <TKey, TElement> Lookup<TKey, TElement> createForFullJoin(IEnumerable<TElement> source, Func1<TElement, TKey> keySelector, IEqualityComparer<TKey> comparer) {
        Lookup<TKey, TElement> lookup = new Lookup<>(comparer);
        for (TElement item : source) {
            TKey key = keySelector.apply(item);
            if (key == null)
                lookup.getNullKeyGrouping().add(item);
            else
                lookup.getGrouping(key, true).add(item);
        }
        return lookup;
    }

    public static <TSource, TKey> ILookup<TKey, TSource> toLookup(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector) {
        return toLookup(source, keySelector, (IEqualityComparer<TKey>) null);
    }

    public static <TSource, TKey> ILookup<TKey, TSource> toLookup(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector, IEqualityComparer<TKey> comparer) {
        if (source == null)
            throw Errors.argumentNull("source");
        if (keySelector == null)
            throw Errors.argumentNull("keySelector");

        return Lookup.create(source, keySelector, comparer);
    }

    public static <TSource, TKey, TElement> ILookup<TKey, TElement> toLookup(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector, Func1<TSource, TElement> elementSelector) {
        return toLookup(source, keySelector, elementSelector, null);
    }

    public static <TSource, TKey, TElement> ILookup<TKey, TElement> toLookup(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector, Func1<TSource, TElement> elementSelector, IEqualityComparer<TKey> comparer) {
        if (source == null)
            throw Errors.argumentNull("source");
        if (keySelector == null)
            throw Errors.argumentNull("keySelector");
        if (elementSelector == null)
            throw Errors.argumentNull("elementSelector");

        return Lookup.create(source, keySelector, elementSelector, comparer);
    }

    @Override
    public int getCount() {
        return this.count;
    }

    @Override
    public IEnumerable<TElement> get(TKey key) {
        Grouping<TKey, TElement> grouping = this.getGrouping(key, false);
        return grouping == null ? Array.empty() : grouping;
    }

    @Override
    public boolean containsKey(TKey key) {
        return this.getGrouping(key, false) != null;
    }

    @Override
    public IEnumerator<IGrouping<TKey, TElement>> enumerator() {
        return new LookupEnumerator();
    }

    @Override
    public IGrouping<TKey, TElement>[] _toArray(Class<IGrouping<TKey, TElement>> clazz) {
        IGrouping<TKey, TElement>[] array = ArrayUtils.newInstance(clazz, this.count);
        int index = 0;
        Grouping<TKey, TElement> g = this.lastGrouping;
        if (g != null) {
            do {
                g = g.next;
                array[index] = g;
                ++index;
            } while (g != this.lastGrouping);
        }

        return array;
    }

    public <TResult> TResult[] _toArray(Class<TResult> clazz, Func2<TKey, IEnumerable<TElement>, TResult> resultSelector) {
        TResult[] array = ArrayUtils.newInstance(clazz, this.count);
        int index = 0;
        Grouping<TKey, TElement> g = this.lastGrouping;
        if (g != null) {
            do {
                g = g.next;
                g.trim();
                array[index] = resultSelector.apply(g.key, g.elements);
                ++index;
            } while (g != this.lastGrouping);
        }

        return array;
    }

    @Override
    public Array<IGrouping<TKey, TElement>> _toArray() {
        Array<IGrouping<TKey, TElement>> array = Array.create(this.count);
        int index = 0;
        Grouping<TKey, TElement> g = this.lastGrouping;
        if (g != null) {
            do {
                g = g.next;
                array.set(index, g);
                ++index;
            } while (g != this.lastGrouping);
        }

        return array;
    }

    public <TResult> Array<TResult> _toArray(Func2<TKey, IEnumerable<TElement>, TResult> resultSelector) {
        Array<TResult> array = Array.create(this.count);
        int index = 0;
        Grouping<TKey, TElement> g = this.lastGrouping;
        if (g != null) {
            do {
                g = g.next;
                g.trim();
                array.set(index, resultSelector.apply(g.key, g.elements));
                ++index;
            } while (g != this.lastGrouping);
        }

        return array;
    }

    @Override
    public List<IGrouping<TKey, TElement>> _toList() {
        List<IGrouping<TKey, TElement>> list = new ArrayList<>(this.count);
        Grouping<TKey, TElement> g = this.lastGrouping;
        if (g != null) {
            do {
                g = g.next;
                list.add(g);
            }
            while (g != this.lastGrouping);
        }

        return list;
    }

    public <TResult> List<TResult> _toList(Func2<TKey, IEnumerable<TElement>, TResult> resultSelector) {
        List<TResult> list = new ArrayList<>(this.count);
        Grouping<TKey, TElement> g = this.lastGrouping;
        if (g != null) {
            do {
                g = g.next;
                g.trim();
                list.add(resultSelector.apply(g.key, g.elements));
            }
            while (g != this.lastGrouping);
        }

        return list;
    }

    @Override
    public int _getCount(boolean onlyIfCheap) {
        return this.count;
    }

    public <TResult> IEnumerable<TResult> applyResultSelector(Func2<TKey, IEnumerable<TElement>, TResult> resultSelector) {
        return new ApplyResultSelector<>(resultSelector);
    }

    private int hashCode(TKey key) {
        // Handle comparer implementations that throw when passed null
        return (key == null) ? 0 : this.comparer.hashCode(key) & 0x7FFFFFFF;
    }

    private Grouping<TKey, TElement> getGrouping(TKey key, boolean create) {
        int hashCode = this.hashCode(key);
        for (Grouping<TKey, TElement> g = this.groupings.get(hashCode % this.groupings.length()); g != null; g = g.hashNext)
            if (g.hashCode == hashCode && this.comparer.equals(g.key, key) && g != this.nullKeyGrouping)
                return g;
        return create ? this.createGrouping(key, hashCode) : null;
    }

    private Grouping<TKey, TElement> createGrouping(TKey key, int hashCode) {
        if (this.count == this.groupings.length())
            this.resize();
        int index = hashCode % this.groupings.length();
        Grouping<TKey, TElement> g = new Grouping<>();
        g.key = key;
        g.hashCode = hashCode;
        g.elements = Array.create(1);
        g.hashNext = this.groupings.get(index);
        this.groupings.set(index, g);
        if (this.lastGrouping == null) {
            g.next = g;
        } else {
            g.next = this.lastGrouping.next;
            this.lastGrouping.next = g;
        }
        this.lastGrouping = g;
        this.count++;
        return g;
    }

    private void resize() {
        int newSize = Math.addExact(Math.multiplyExact(this.count, 2), 1);
        Array<Grouping<TKey, TElement>> newGroupings = Array.create(newSize);
        Grouping<TKey, TElement> g = this.lastGrouping;
        do {
            g = g.next;
            int index = g.hashCode % newSize;
            g.hashNext = newGroupings.get(index);
            newGroupings.set(index, g);
        } while (g != this.lastGrouping);
        this.groupings = newGroupings;
    }

    private Grouping<TKey, TElement> getNullKeyGrouping() {
        if (this.nullKeyGrouping == null)
            this.nullKeyGrouping = this.createGrouping(null, this.hashCode(null));
        return this.nullKeyGrouping;
    }

    public Grouping<TKey, TElement> fetchGrouping(TKey key) {
        if (key == null)
            return null;
        Grouping<TKey, TElement> g = this.getGrouping(key, false);
        if (g != null)
            g.fetched = true;
        return g;
    }

    public IEnumerable<TElement> fetch(TKey key) {
        Grouping<TKey, TElement> grouping = this.fetchGrouping(key);
        return grouping == null ? Array.empty() : grouping;
    }

    public IEnumerator<Grouping<TKey, TElement>> unfetchedEnumerator() {
        return new UnfetchedLookupEnumerator();
    }

    private final class LookupEnumerator extends AbstractEnumerator<IGrouping<TKey, TElement>> {
        private Grouping<TKey, TElement> g;

        @Override
        public boolean moveNext() {
            do {
                switch (this.state) {
                    case 0:
                        this.g = Lookup.this.lastGrouping;
                        if (this.g == null) {
                            this.close();
                            return false;
                        }
                        this.state = 2;
                        break;
                    case 1:
                        if (this.g == Lookup.this.lastGrouping) {
                            this.close();
                            return false;
                        }
                        this.state = 2;
                    case 2:
                        this.g = this.g.next;
                        this.current = this.g;
                        this.state = 1;
                        return true;
                    default:
                        return false;
                }
            } while (true);
        }

        @Override
        public void close() {
            this.g = null;
            super.close();
        }
    }

    private final class ApplyResultSelector<TResult> extends AbstractIterator<TResult> {
        private final Func2<TKey, IEnumerable<TElement>, TResult> resultSelector;
        private Grouping<TKey, TElement> g;

        private ApplyResultSelector(Func2<TKey, IEnumerable<TElement>, TResult> resultSelector) {
            this.resultSelector = resultSelector;
        }

        @Override
        public AbstractIterator<TResult> clone() {
            return new ApplyResultSelector<>(this.resultSelector);
        }

        @Override
        public boolean moveNext() {
            do {
                switch (this.state) {
                    case 0:
                        this.g = Lookup.this.lastGrouping;
                        if (this.g == null) {
                            this.close();
                            return false;
                        }
                        this.state = 2;
                        break;
                    case 1:
                        if (this.g == Lookup.this.lastGrouping) {
                            this.close();
                            return false;
                        }
                        this.state = 2;
                    case 2:
                        this.g = this.g.next;
                        this.g.trim();
                        this.current = this.resultSelector.apply(this.g.key, this.g.elements);
                        this.state = 1;
                        return true;
                    default:
                        return false;
                }
            } while (true);
        }

        @Override
        public void close() {
            this.g = null;
            super.close();
        }
    }

    private final class UnfetchedLookupEnumerator extends AbstractEnumerator<Grouping<TKey, TElement>> {
        private Grouping<TKey, TElement> g;

        @Override
        public boolean moveNext() {
            do {
                switch (this.state) {
                    case 0:
                        this.g = Lookup.this.lastGrouping;
                        if (this.g == null) {
                            this.close();
                            return false;
                        }
                        this.state = 2;
                        break;
                    case 1:
                        if (this.g == Lookup.this.lastGrouping) {
                            this.close();
                            return false;
                        }
                        this.state = 2;
                    case 2:
                        this.state = 1;
                        this.g = this.g.next;
                        if (this.g.fetched)
                            break;
                        this.current = this.g;
                        return true;
                    default:
                        return false;
                }
            } while (true);
        }

        @Override
        public void close() {
            this.g = null;
            super.close();
        }
    }
}
