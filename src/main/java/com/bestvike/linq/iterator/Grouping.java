package com.bestvike.linq.iterator;

import com.bestvike.collections.generic.Array;
import com.bestvike.collections.generic.IEqualityComparer;
import com.bestvike.collections.generic.IList;
import com.bestvike.function.Func1;
import com.bestvike.function.Func2;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.IGrouping;
import com.bestvike.linq.enumerator.ArrayEnumerator;
import com.bestvike.linq.exception.Errors;

import java.util.Collection;
import java.util.List;

/**
 * Created by 许崇雷 on 2018-04-28.
 */
public final class Grouping<TKey, TElement> implements IGrouping<TKey, TElement>, IList<TElement> {
    TKey key;
    int hashCode;
    Array<TElement> elements;
    int count;
    Grouping<TKey, TElement> hashNext;
    Grouping<TKey, TElement> next;
    boolean fetched;

    Grouping() {
    }

    public static <TSource, TKey> IEnumerable<IGrouping<TKey, TSource>> groupBy(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector) {
        return new GroupedEnumerable<>(source, keySelector, null);
    }

    public static <TSource, TKey> IEnumerable<IGrouping<TKey, TSource>> groupBy(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector, IEqualityComparer<TKey> comparer) {
        return new GroupedEnumerable<>(source, keySelector, comparer);
    }

    public static <TSource, TKey, TElement> IEnumerable<IGrouping<TKey, TElement>> groupBy(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector, Func1<TSource, TElement> elementSelector) {
        return new GroupedEnumerable2<>(source, keySelector, elementSelector, null);
    }

    public static <TSource, TKey, TElement> IEnumerable<IGrouping<TKey, TElement>> groupBy(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector, Func1<TSource, TElement> elementSelector, IEqualityComparer<TKey> comparer) {
        return new GroupedEnumerable2<>(source, keySelector, elementSelector, comparer);
    }

    public static <TSource, TKey, TResult> IEnumerable<TResult> groupBy(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector, Func2<TKey, IEnumerable<TSource>, TResult> resultSelector) {
        return new GroupedResultEnumerable<>(source, keySelector, resultSelector, null);
    }

    public static <TSource, TKey, TResult> IEnumerable<TResult> groupBy(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector, Func2<TKey, IEnumerable<TSource>, TResult> resultSelector, IEqualityComparer<TKey> comparer) {
        return new GroupedResultEnumerable<>(source, keySelector, resultSelector, comparer);
    }

    public static <TSource, TKey, TElement, TResult> IEnumerable<TResult> groupBy(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector, Func1<TSource, TElement> elementSelector, Func2<TKey, IEnumerable<TElement>, TResult> resultSelector) {
        return new GroupedResultEnumerable2<>(source, keySelector, elementSelector, resultSelector, null);
    }

    public static <TSource, TKey, TElement, TResult> IEnumerable<TResult> groupBy(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector, Func1<TSource, TElement> elementSelector, Func2<TKey, IEnumerable<TElement>, TResult> resultSelector, IEqualityComparer<TKey> comparer) {
        return new GroupedResultEnumerable2<>(source, keySelector, elementSelector, resultSelector, comparer);
    }

    void add(TElement element) {
        if (this.elements.length() == this.count)
            this.elements = Array.resize(this.elements, Math.multiplyExact(this.count, 2));
        this.elements.set(this.count, element);
        this.count++;
    }

    public void trim() {
        if (this.elements.length() != this.count)
            this.elements = Array.resize(this.elements, this.count);
    }

    @Override
    public IEnumerator<TElement> enumerator() {
        return new ArrayEnumerator<>(this.elements, 0, this.count);
    }

    @Override
    public TKey getKey() {
        return this.key;
    }

    @Override
    public TElement get(int index) {
        if (index < 0 || index >= this.count)
            throw Errors.argumentOutOfRange("index");
        return this.elements.get(index);
    }

    @Override
    public Collection<TElement> getCollection() {
        return this.elements.getCollection(0, this.count);
    }

    @Override
    public int _getCount() {
        return this.count;
    }

    @Override
    public boolean _contains(TElement item) {
        return this.elements._contains(item, 0, this.count);
    }

    @Override
    public void _copyTo(TElement[] array, int arrayIndex) {
        Array.copy(this.elements, 0, array, arrayIndex, this.count);
    }

    @Override
    public void _copyTo(Array<TElement> array, int arrayIndex) {
        Array.copy(this.elements, 0, array, arrayIndex, this.count);
    }

    public int indexOf(TElement item) {
        return Array.indexOf(this.elements, item, 0, this.count);
    }

    @Override
    public TElement[] _toArray(Class<TElement> clazz) {
        return this.elements._toArray(clazz, 0, this.count);
    }

    @Override
    public Array<TElement> _toArray() {
        return this.elements._toArray(0, this.count);
    }

    @Override
    public List<TElement> _toList() {
        return this.elements._toList(0, this.count);
    }
}


final class GroupedEnumerable<TSource, TKey> implements IIListProvider<IGrouping<TKey, TSource>> {
    private final IEnumerable<TSource> source;
    private final Func1<TSource, TKey> keySelector;
    private final IEqualityComparer<TKey> comparer;

    public GroupedEnumerable(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector, IEqualityComparer<TKey> comparer) {
        if (source == null)
            throw Errors.argumentNull("source");
        if (keySelector == null)
            throw Errors.argumentNull("keySelector");

        this.source = source;
        this.keySelector = keySelector;
        this.comparer = comparer;
    }

    @Override
    public IEnumerator<IGrouping<TKey, TSource>> enumerator() {

        return Lookup.create(this.source, this.keySelector, this.comparer).enumerator();
    }

    @Override
    public IGrouping<TKey, TSource>[] _toArray(Class<IGrouping<TKey, TSource>> clazz) {
        IIListProvider<IGrouping<TKey, TSource>> lookup = Lookup.create(this.source, this.keySelector, this.comparer);
        return lookup._toArray(clazz);
    }

    @Override
    public Array<IGrouping<TKey, TSource>> _toArray() {
        IIListProvider<IGrouping<TKey, TSource>> lookup = Lookup.create(this.source, this.keySelector, this.comparer);
        return lookup._toArray();
    }

    @Override
    public List<IGrouping<TKey, TSource>> _toList() {
        IIListProvider<IGrouping<TKey, TSource>> lookup = Lookup.create(this.source, this.keySelector, this.comparer);
        return lookup._toList();
    }

    @Override
    public int _getCount(boolean onlyIfCheap) {
        return onlyIfCheap ? -1 : Lookup.create(this.source, this.keySelector, this.comparer).getCount();
    }
}


final class GroupedEnumerable2<TSource, TKey, TElement> implements IIListProvider<IGrouping<TKey, TElement>> {
    private final IEnumerable<TSource> source;
    private final Func1<TSource, TKey> keySelector;
    private final Func1<TSource, TElement> elementSelector;
    private final IEqualityComparer<TKey> comparer;

    public GroupedEnumerable2(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector, Func1<TSource, TElement> elementSelector, IEqualityComparer<TKey> comparer) {
        if (source == null)
            throw Errors.argumentNull("source");
        if (keySelector == null)
            throw Errors.argumentNull("keySelector");
        if (elementSelector == null)
            throw Errors.argumentNull("elementSelector");

        this.source = source;
        this.keySelector = keySelector;
        this.elementSelector = elementSelector;
        this.comparer = comparer;
    }

    @Override
    public IEnumerator<IGrouping<TKey, TElement>> enumerator() {
        return Lookup.create(this.source, this.keySelector, this.elementSelector, this.comparer).enumerator();
    }

    @Override
    public IGrouping<TKey, TElement>[] _toArray(Class<IGrouping<TKey, TElement>> clazz) {
        IIListProvider<IGrouping<TKey, TElement>> lookup = Lookup.create(this.source, this.keySelector, this.elementSelector, this.comparer);
        return lookup._toArray(clazz);
    }

    @Override
    public Array<IGrouping<TKey, TElement>> _toArray() {
        IIListProvider<IGrouping<TKey, TElement>> lookup = Lookup.create(this.source, this.keySelector, this.elementSelector, this.comparer);
        return lookup._toArray();
    }

    @Override
    public List<IGrouping<TKey, TElement>> _toList() {
        IIListProvider<IGrouping<TKey, TElement>> lookup = Lookup.create(this.source, this.keySelector, this.elementSelector, this.comparer);
        return lookup._toList();
    }

    @Override
    public int _getCount(boolean onlyIfCheap) {
        return onlyIfCheap ? -1 : Lookup.create(this.source, this.keySelector, this.elementSelector, this.comparer).getCount();
    }
}


final class GroupedResultEnumerable<TSource, TKey, TResult> implements IIListProvider<TResult> {
    private final IEnumerable<TSource> source;
    private final Func1<TSource, TKey> keySelector;
    private final IEqualityComparer<TKey> comparer;
    private final Func2<TKey, IEnumerable<TSource>, TResult> resultSelector;

    public GroupedResultEnumerable(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector, Func2<TKey, IEnumerable<TSource>, TResult> resultSelector, IEqualityComparer<TKey> comparer) {
        if (source == null)
            throw Errors.argumentNull("source");
        if (keySelector == null)
            throw Errors.argumentNull("keySelector");
        if (resultSelector == null)
            throw Errors.argumentNull("resultSelector");

        this.source = source;
        this.keySelector = keySelector;
        this.resultSelector = resultSelector;
        this.comparer = comparer;
    }

    @Override
    public IEnumerator<TResult> enumerator() {
        Lookup<TKey, TSource> lookup = Lookup.create(this.source, this.keySelector, this.comparer);
        return lookup.applyResultSelector(this.resultSelector).enumerator();
    }

    @Override
    public TResult[] _toArray(Class<TResult> clazz) {
        return Lookup.create(this.source, this.keySelector, this.comparer)._toArray(clazz, this.resultSelector);
    }

    @Override
    public Array<TResult> _toArray() {
        return Lookup.create(this.source, this.keySelector, this.comparer)._toArray(this.resultSelector);
    }

    @Override
    public List<TResult> _toList() {
        return Lookup.create(this.source, this.keySelector, this.comparer)._toList(this.resultSelector);
    }

    @Override
    public int _getCount(boolean onlyIfCheap) {
        return onlyIfCheap ? -1 : Lookup.create(this.source, this.keySelector, this.comparer).getCount();
    }
}


final class GroupedResultEnumerable2<TSource, TKey, TElement, TResult> implements IIListProvider<TResult> {
    private final IEnumerable<TSource> source;
    private final Func1<TSource, TKey> keySelector;
    private final Func1<TSource, TElement> elementSelector;
    private final IEqualityComparer<TKey> comparer;
    private final Func2<TKey, IEnumerable<TElement>, TResult> resultSelector;

    public GroupedResultEnumerable2(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector, Func1<TSource, TElement> elementSelector, Func2<TKey, IEnumerable<TElement>, TResult> resultSelector, IEqualityComparer<TKey> comparer) {
        if (source == null)
            throw Errors.argumentNull("source");
        if (keySelector == null)
            throw Errors.argumentNull("keySelector");
        if (elementSelector == null)
            throw Errors.argumentNull("elementSelector");
        if (resultSelector == null)
            throw Errors.argumentNull("resultSelector");

        this.source = source;
        this.keySelector = keySelector;
        this.elementSelector = elementSelector;
        this.comparer = comparer;
        this.resultSelector = resultSelector;
    }

    @Override
    public IEnumerator<TResult> enumerator() {
        Lookup<TKey, TElement> lookup = Lookup.create(this.source, this.keySelector, this.elementSelector, this.comparer);
        return lookup.applyResultSelector(this.resultSelector).enumerator();
    }

    @Override
    public TResult[] _toArray(Class<TResult> clazz) {
        return Lookup.create(this.source, this.keySelector, this.elementSelector, this.comparer)._toArray(clazz, this.resultSelector);
    }

    @Override
    public Array<TResult> _toArray() {
        return Lookup.create(this.source, this.keySelector, this.elementSelector, this.comparer)._toArray(this.resultSelector);
    }

    @Override
    public List<TResult> _toList() {
        return Lookup.create(this.source, this.keySelector, this.elementSelector, this.comparer)._toList(this.resultSelector);
    }

    @Override
    public int _getCount(boolean onlyIfCheap) {
        return onlyIfCheap ? -1 : Lookup.create(this.source, this.keySelector, this.elementSelector, this.comparer).getCount();
    }
}