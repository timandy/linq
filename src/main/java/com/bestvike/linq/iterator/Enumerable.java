package com.bestvike.linq.iterator;

import com.bestvike.linq.ICollectionEnumerable;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.IEqualityComparer;
import com.bestvike.linq.IGrouping;
import com.bestvike.linq.IListEnumerable;
import com.bestvike.linq.ILookup;
import com.bestvike.linq.IOrderedEnumerable;
import com.bestvike.linq.enumerable.ArrayEnumerable;
import com.bestvike.linq.enumerable.EmptyEnumerable;
import com.bestvike.linq.exception.Errors;
import com.bestvike.linq.function.Func1;
import com.bestvike.linq.function.Func2;
import com.bestvike.linq.impl.Buffer;
import com.bestvike.linq.impl.GroupedEnumerable;
import com.bestvike.linq.impl.GroupedEnumerable2;
import com.bestvike.linq.impl.IdentityFunction;
import com.bestvike.linq.impl.Lookup;
import com.bestvike.linq.impl.OrderedEnumerable;
import com.bestvike.linq.util.Comparer;
import com.bestvike.linq.util.EqualityComparer;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 许崇雷
 * @date 2017/7/10
 */
public final class Enumerable {
    private Enumerable() {
    }

    public static <TSource> IEnumerable<TSource> where(IEnumerable<TSource> source, Func1<TSource, Boolean> predicate) {
        if (source == null) throw Errors.argumentNull("source");
        if (predicate == null) throw Errors.argumentNull("predicate");
        if (source instanceof Iterator) return ((Iterator<TSource>) source).internalWhere(predicate);
        if (source instanceof ArrayEnumerable) return new WhereArrayIterator<>(((ArrayEnumerable<TSource>) source).internalSource(), predicate);
        return new WhereEnumerableIterator<>(source, predicate);
    }

    public static <TSource> IEnumerable<TSource> where(IEnumerable<TSource> source, Func2<TSource, Integer, Boolean> predicate) {
        if (source == null) throw Errors.argumentNull("source");
        if (predicate == null) throw Errors.argumentNull("predicate");
        return new WhereIterator<>(source, predicate);
    }

    public static <TSource, TResult> IEnumerable<TResult> select(IEnumerable<TSource> source, Func1<TSource, TResult> selector) {
        if (source == null) throw Errors.argumentNull("source");
        if (selector == null) throw Errors.argumentNull("selector");
        if (source instanceof Iterator) return ((Iterator<TSource>) source).internalSelect(selector);
        if (source instanceof ArrayEnumerable) return new WhereSelectArrayIterator<>(((ArrayEnumerable<TSource>) source).internalSource(), null, selector);
        return new WhereSelectEnumerableIterator<>(source, null, selector);
    }

    public static <TSource, TResult> IEnumerable<TResult> select(IEnumerable<TSource> source, Func2<TSource, Integer, TResult> selector) {
        if (source == null) throw Errors.argumentNull("source");
        if (selector == null) throw Errors.argumentNull("selector");
        return new SelectIterator<>(source, selector);
    }

    public static <TSource, TResult> IEnumerable<TResult> selectMany(IEnumerable<TSource> source, Func1<TSource, IEnumerable<TResult>> selector) {
        if (source == null) throw Errors.argumentNull("source");
        if (selector == null) throw Errors.argumentNull("selector");
        return new SelectManyIterator<>(source, selector);
    }

    public static <TSource, TResult> IEnumerable<TResult> selectMany(IEnumerable<TSource> source, Func2<TSource, Integer, IEnumerable<TResult>> selector) {
        if (source == null) throw Errors.argumentNull("source");
        if (selector == null) throw Errors.argumentNull("selector");
        return new SelectManyIterator2<>(source, selector);
    }

    public static <TSource, TCollection, TResult> IEnumerable<TResult> selectMany(IEnumerable<TSource> source, Func1<TSource, IEnumerable<TCollection>> collectionSelector, Func2<TSource, TCollection, TResult> resultSelector) {
        if (source == null) throw Errors.argumentNull("source");
        if (collectionSelector == null) throw Errors.argumentNull("collectionSelector");
        if (resultSelector == null) throw Errors.argumentNull("resultSelector");
        return new SelectManyIterator3<>(source, collectionSelector, resultSelector);
    }

    public static <TSource, TCollection, TResult> IEnumerable<TResult> selectMany(IEnumerable<TSource> source, Func2<TSource, Integer, IEnumerable<TCollection>> collectionSelector, Func2<TSource, TCollection, TResult> resultSelector) {
        if (source == null) throw Errors.argumentNull("source");
        if (collectionSelector == null) throw Errors.argumentNull("collectionSelector");
        if (resultSelector == null) throw Errors.argumentNull("resultSelector");
        return new SelectManyIterator4<>(source, collectionSelector, resultSelector);
    }

    public static <TSource> IEnumerable<TSource> take(IEnumerable<TSource> source, int count) {
        if (source == null) throw Errors.argumentNull("source");
        return new TakeIterator<>(source, count);
    }

    public static <TSource> IEnumerable<TSource> takeWhile(IEnumerable<TSource> source, Func1<TSource, Boolean> predicate) {
        if (source == null) throw Errors.argumentNull("source");
        if (predicate == null) throw Errors.argumentNull("predicate");
        return new TakeWhileIterator<>(source, predicate);
    }

    public static <TSource> IEnumerable<TSource> takeWhile(IEnumerable<TSource> source, Func2<TSource, Integer, Boolean> predicate) {
        if (source == null) throw Errors.argumentNull("source");
        if (predicate == null) throw Errors.argumentNull("predicate");
        return new TakeWhileIterator2<>(source, predicate);
    }

    public static <TSource> IEnumerable<TSource> skip(IEnumerable<TSource> source, int count) {
        if (source == null) throw Errors.argumentNull("source");
        return new SkipIterator<>(source, count);
    }

    public static <TSource> IEnumerable<TSource> skipWhile(IEnumerable<TSource> source, Func1<TSource, Boolean> predicate) {
        if (source == null) throw Errors.argumentNull("source");
        if (predicate == null) throw Errors.argumentNull("predicate");
        return new SkipWhileIterator<>(source, predicate);
    }

    public static <TSource> IEnumerable<TSource> skipWhile(IEnumerable<TSource> source, Func2<TSource, Integer, Boolean> predicate) {
        if (source == null) throw Errors.argumentNull("source");
        if (predicate == null) throw Errors.argumentNull("predicate");
        return new SkipWhileIterator2<>(source, predicate);
    }

    public static <TOuter, TInner, TKey, TResult> IEnumerable<TResult> join(IEnumerable<TOuter> outer, IEnumerable<TInner> inner, Func1<TOuter, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, Func2<TOuter, TInner, TResult> resultSelector) {
        if (outer == null) throw Errors.argumentNull("outer");
        if (inner == null) throw Errors.argumentNull("inner");
        if (outerKeySelector == null) throw Errors.argumentNull("outerKeySelector");
        if (innerKeySelector == null) throw Errors.argumentNull("innerKeySelector");
        if (resultSelector == null) throw Errors.argumentNull("resultSelector");
        return new JoinIterator<>(outer, inner, outerKeySelector, innerKeySelector, resultSelector, null);
    }

    public static <TOuter, TInner, TKey, TResult> IEnumerable<TResult> join(IEnumerable<TOuter> outer, IEnumerable<TInner> inner, Func1<TOuter, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, Func2<TOuter, TInner, TResult> resultSelector, IEqualityComparer<TKey> comparer) {
        if (outer == null) throw Errors.argumentNull("outer");
        if (inner == null) throw Errors.argumentNull("inner");
        if (outerKeySelector == null) throw Errors.argumentNull("outerKeySelector");
        if (innerKeySelector == null) throw Errors.argumentNull("innerKeySelector");
        if (resultSelector == null) throw Errors.argumentNull("resultSelector");
        return new JoinIterator<>(outer, inner, outerKeySelector, innerKeySelector, resultSelector, comparer);
    }

    public static <TOuter, TInner, TKey, TResult> IEnumerable<TResult> groupJoin(IEnumerable<TOuter> outer, IEnumerable<TInner> inner, Func1<TOuter, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, Func2<TOuter, IEnumerable<TInner>, TResult> resultSelector) {
        if (outer == null) throw Errors.argumentNull("outer");
        if (inner == null) throw Errors.argumentNull("inner");
        if (outerKeySelector == null) throw Errors.argumentNull("outerKeySelector");
        if (innerKeySelector == null) throw Errors.argumentNull("innerKeySelector");
        if (resultSelector == null) throw Errors.argumentNull("resultSelector");
        return new GroupJoinIterator<>(outer, inner, outerKeySelector, innerKeySelector, resultSelector, null);
    }

    public static <TOuter, TInner, TKey, TResult> IEnumerable<TResult> groupJoin(IEnumerable<TOuter> outer, IEnumerable<TInner> inner, Func1<TOuter, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, Func2<TOuter, IEnumerable<TInner>, TResult> resultSelector, IEqualityComparer<TKey> comparer) {
        if (outer == null) throw Errors.argumentNull("outer");
        if (inner == null) throw Errors.argumentNull("inner");
        if (outerKeySelector == null) throw Errors.argumentNull("outerKeySelector");
        if (innerKeySelector == null) throw Errors.argumentNull("innerKeySelector");
        if (resultSelector == null) throw Errors.argumentNull("resultSelector");
        return new GroupJoinIterator<>(outer, inner, outerKeySelector, innerKeySelector, resultSelector, comparer);
    }

    public static <TSource, TKey> IOrderedEnumerable<TSource> orderBy(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector) {
        return new OrderedEnumerable<>(source, keySelector, null, false);
    }

    public static <TSource, TKey> IOrderedEnumerable<TSource> orderBy(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector, Comparator<TKey> comparer) {
        return new OrderedEnumerable<>(source, keySelector, comparer, false);
    }

    public static <TSource, TKey> IOrderedEnumerable<TSource> orderByDescending(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector) {
        return new OrderedEnumerable<>(source, keySelector, null, true);
    }

    public static <TSource, TKey> IOrderedEnumerable<TSource> orderByDescending(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector, Comparator<TKey> comparer) {
        return new OrderedEnumerable<>(source, keySelector, comparer, true);
    }

    public static <TSource, TKey> IOrderedEnumerable<TSource> thenBy(IOrderedEnumerable<TSource> source, Func1<TSource, TKey> keySelector) {
        if (source == null) throw Errors.argumentNull("source");
        return source.createOrderedEnumerable(keySelector, null, false);
    }

    public static <TSource, TKey> IOrderedEnumerable<TSource> thenBy(IOrderedEnumerable<TSource> source, Func1<TSource, TKey> keySelector, Comparator<TKey> comparer) {
        if (source == null) throw Errors.argumentNull("source");
        return source.createOrderedEnumerable(keySelector, comparer, false);
    }

    public static <TSource, TKey> IOrderedEnumerable<TSource> thenByDescending(IOrderedEnumerable<TSource> source, Func1<TSource, TKey> keySelector) {
        if (source == null) throw Errors.argumentNull("source");
        return source.createOrderedEnumerable(keySelector, null, true);
    }

    public static <TSource, TKey> IOrderedEnumerable<TSource> thenByDescending(IOrderedEnumerable<TSource> source, Func1<TSource, TKey> keySelector, Comparator<TKey> comparer) {
        if (source == null) throw Errors.argumentNull("source");
        return source.createOrderedEnumerable(keySelector, comparer, true);
    }

    public static <TSource, TKey> IEnumerable<IGrouping<TKey, TSource>> groupBy(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector) {
        return new GroupedEnumerable<>(source, keySelector, IdentityFunction.Instance(), null);
    }

    public static <TSource, TKey> IEnumerable<IGrouping<TKey, TSource>> groupBy(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector, IEqualityComparer<TKey> comparer) {
        return new GroupedEnumerable<>(source, keySelector, IdentityFunction.Instance(), comparer);
    }

    public static <TSource, TKey, TElement> IEnumerable<IGrouping<TKey, TElement>> groupBy(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector, Func1<TSource, TElement> elementSelector) {
        return new GroupedEnumerable<>(source, keySelector, elementSelector, null);
    }

    public static <TSource, TKey, TElement> IEnumerable<IGrouping<TKey, TElement>> groupBy(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector, Func1<TSource, TElement> elementSelector, IEqualityComparer<TKey> comparer) {
        return new GroupedEnumerable<>(source, keySelector, elementSelector, comparer);
    }

    public static <TSource, TKey, TResult> IEnumerable<TResult> groupBy(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector, Func2<TKey, IEnumerable<TSource>, TResult> resultSelector) {
        return new GroupedEnumerable2<>(source, keySelector, IdentityFunction.Instance(), resultSelector, null);
    }

    public static <TSource, TKey, TResult> IEnumerable<TResult> groupBy(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector, Func2<TKey, IEnumerable<TSource>, TResult> resultSelector, IEqualityComparer<TKey> comparer) {
        return new GroupedEnumerable2<>(source, keySelector, IdentityFunction.Instance(), resultSelector, comparer);
    }

    public static <TSource, TKey, TElement, TResult> IEnumerable<TResult> groupBy(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector, Func1<TSource, TElement> elementSelector, Func2<TKey, IEnumerable<TElement>, TResult> resultSelector) {
        return new GroupedEnumerable2<>(source, keySelector, elementSelector, resultSelector, null);
    }

    public static <TSource, TKey, TElement, TResult> IEnumerable<TResult> groupBy(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector, Func1<TSource, TElement> elementSelector, Func2<TKey, IEnumerable<TElement>, TResult> resultSelector, IEqualityComparer<TKey> comparer) {
        return new GroupedEnumerable2<>(source, keySelector, elementSelector, resultSelector, comparer);
    }

    public static <TSource> IEnumerable<TSource> concat(IEnumerable<TSource> first, IEnumerable<TSource> second) {
        if (first == null) throw Errors.argumentNull("first");
        if (second == null) throw Errors.argumentNull("second");
        return new ConcatIterator<>(first, second);
    }

    public static <TFirst, TSecond, TResult> IEnumerable<TResult> zip(IEnumerable<TFirst> first, IEnumerable<TSecond> second, Func2<TFirst, TSecond, TResult> resultSelector) {
        if (first == null) throw Errors.argumentNull("first");
        if (second == null) throw Errors.argumentNull("second");
        if (resultSelector == null) throw Errors.argumentNull("resultSelector");
        return new ZipIterator<>(first, second, resultSelector);
    }

    public static <TSource> IEnumerable<TSource> distinct(IEnumerable<TSource> source) {
        if (source == null) throw Errors.argumentNull("source");
        return new DistinctIterator<>(source, null);
    }

    public static <TSource> IEnumerable<TSource> distinct(IEnumerable<TSource> source, IEqualityComparer<TSource> comparer) {
        if (source == null) throw Errors.argumentNull("source");
        return new DistinctIterator<>(source, comparer);
    }

    public static <TSource> IEnumerable<TSource> union(IEnumerable<TSource> first, IEnumerable<TSource> second) {
        if (first == null) throw Errors.argumentNull("first");
        if (second == null) throw Errors.argumentNull("second");
        return new UnionIterator<>(first, second, null);
    }

    public static <TSource> IEnumerable<TSource> union(IEnumerable<TSource> first, IEnumerable<TSource> second, IEqualityComparer<TSource> comparer) {
        if (first == null) throw Errors.argumentNull("first");
        if (second == null) throw Errors.argumentNull("second");
        return new UnionIterator<>(first, second, comparer);
    }

    public static <TSource> IEnumerable<TSource> intersect(IEnumerable<TSource> first, IEnumerable<TSource> second) {
        if (first == null) throw Errors.argumentNull("first");
        if (second == null) throw Errors.argumentNull("second");
        return new IntersectIterator<>(first, second, null);
    }

    public static <TSource> IEnumerable<TSource> intersect(IEnumerable<TSource> first, IEnumerable<TSource> second, IEqualityComparer<TSource> comparer) {
        if (first == null) throw Errors.argumentNull("first");
        if (second == null) throw Errors.argumentNull("second");
        return new IntersectIterator<>(first, second, comparer);
    }

    public static <TSource> IEnumerable<TSource> except(IEnumerable<TSource> first, IEnumerable<TSource> second) {
        if (first == null) throw Errors.argumentNull("first");
        if (second == null) throw Errors.argumentNull("second");
        return new ExceptIterator<>(first, second, null);
    }

    public static <TSource> IEnumerable<TSource> except(IEnumerable<TSource> first, IEnumerable<TSource> second, IEqualityComparer<TSource> comparer) {
        if (first == null) throw Errors.argumentNull("first");
        if (second == null) throw Errors.argumentNull("second");
        return new ExceptIterator<>(first, second, comparer);
    }

    public static <TSource> IEnumerable<TSource> reverse(IEnumerable<TSource> source) {
        if (source == null) throw Errors.argumentNull("source");
        return new ReverseIterator<>(source);
    }

    public static <TSource> boolean sequenceEqual(IEnumerable<TSource> first, IEnumerable<TSource> second) {
        return sequenceEqual(first, second, null);
    }

    public static <TSource> boolean sequenceEqual(IEnumerable<TSource> first, IEnumerable<TSource> second, IEqualityComparer<TSource> comparer) {
        if (comparer == null) comparer = EqualityComparer.Default();
        if (first == null) throw Errors.argumentNull("first");
        if (second == null) throw Errors.argumentNull("second");
        try (IEnumerator<TSource> e1 = first.enumerator();
             IEnumerator<TSource> e2 = second.enumerator()) {
            while (e1.moveNext()) {
                if (!(e2.moveNext() && comparer.equals(e1.current(), e2.current())))
                    return false;
            }
            if (e2.moveNext())
                return false;
        }
        return true;
    }

    public static <TSource> TSource[] toArray(IEnumerable<TSource> source, Class<TSource> clazz) {
        if (source == null) throw Errors.argumentNull("source");
        if (clazz == null) throw Errors.argumentNull("clazz");
        if (source instanceof ICollectionEnumerable) return ((ICollectionEnumerable<TSource>) source).internalToArray(clazz);
        return new Buffer<>(source).toArray(clazz);
    }

    public static <TSource> List<TSource> toList(IEnumerable<TSource> source) {
        if (source == null) throw Errors.argumentNull("source");
        if (source instanceof ICollectionEnumerable) return ((ICollectionEnumerable<TSource>) source).internalToList();
        List<TSource> list = new ArrayList<>();
        for (TSource item : source)
            list.add(item);
        return list;
    }

    public static <TSource, TKey> Map<TKey, TSource> toMap(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector) {
        return toMap(source, keySelector, IdentityFunction.Instance());
    }

    public static <TSource, TKey, TElement> Map<TKey, TElement> toMap(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector, Func1<TSource, TElement> elementSelector) {
        if (source == null) throw Errors.argumentNull("source");
        if (keySelector == null) throw Errors.argumentNull("keySelector");
        if (elementSelector == null) throw Errors.argumentNull("elementSelector");
        Map<TKey, TElement> map = new HashMap<>();
        for (TSource element : source)
            map.put(keySelector.apply(element), elementSelector.apply(element));
        return map;
    }

    public static <TSource, TKey> ILookup<TKey, TSource> toLookup(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector) {
        return Lookup.create(source, keySelector, null);
    }

    public static <TSource, TKey> ILookup<TKey, TSource> toLookup(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector, IEqualityComparer<TKey> comparer) {
        return Lookup.create(source, keySelector, comparer);
    }

    public static <TSource, TKey, TElement> ILookup<TKey, TElement> toLookup(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector, Func1<TSource, TElement> elementSelector) {
        return Lookup.create(source, keySelector, elementSelector, null);
    }

    public static <TSource, TKey, TElement> ILookup<TKey, TElement> toLookup(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector, Func1<TSource, TElement> elementSelector, IEqualityComparer<TKey> comparer) {
        return Lookup.create(source, keySelector, elementSelector, comparer);
    }

    public static <TSource> IEnumerable<TSource> defaultIfEmpty(IEnumerable<TSource> source) {
        return defaultIfEmpty(source, null);
    }

    public static <TSource> IEnumerable<TSource> defaultIfEmpty(IEnumerable<TSource> source, TSource defaultValue) {
        if (source == null) throw Errors.argumentNull("source");
        return new DefaultIfEmptyIterator<>(source, defaultValue);
    }

    public static <TResult> IEnumerable<TResult> ofType(IEnumerable source, Class<TResult> clazz) {
        if (source == null) throw Errors.argumentNull("source");
        if (clazz == null) throw Errors.argumentNull("clazz");
        return new OfTypeIterator<>(source, clazz);
    }

    public static <TResult> IEnumerable<TResult> cast(IEnumerable source, Class<TResult> clazz) {
        if (source == null) throw Errors.argumentNull("source");
        if (clazz == null) throw Errors.argumentNull("clazz");
        return new CastIterator<>(source, clazz);
    }

    public static <TSource> TSource first(IEnumerable<TSource> source) {
        if (source == null) throw Errors.argumentNull("source");
        if (source instanceof IListEnumerable) {
            IListEnumerable<TSource> list = (IListEnumerable<TSource>) source;
            if (list.internalSize() > 0) return list.internalGet(0);
        } else {
            try (IEnumerator<TSource> e = source.enumerator()) {
                if (e.moveNext()) return e.current();
            }
        }
        throw Errors.noElements();
    }

    public static <TSource> TSource first(IEnumerable<TSource> source, Func1<TSource, Boolean> predicate) {
        if (source == null) throw Errors.argumentNull("source");
        if (predicate == null) throw Errors.argumentNull("predicate");
        for (TSource element : source) {
            if (predicate.apply(element)) return element;
        }
        throw Errors.noMatch();
    }

    public static <TSource> TSource firstOrDefault(IEnumerable<TSource> source) {
        if (source == null) throw Errors.argumentNull("source");
        if (source instanceof IListEnumerable) {
            IListEnumerable<TSource> list = (IListEnumerable<TSource>) source;
            if (list.internalSize() > 0) return list.internalGet(0);
        } else {
            try (IEnumerator<TSource> e = source.enumerator()) {
                if (e.moveNext()) return e.current();
            }
        }
        return null;
    }

    public static <TSource> TSource firstOrDefault(IEnumerable<TSource> source, Func1<TSource, Boolean> predicate) {
        if (source == null) throw Errors.argumentNull("source");
        if (predicate == null) throw Errors.argumentNull("predicate");
        for (TSource element : source) {
            if (predicate.apply(element)) return element;
        }
        return null;
    }

    public static <TSource> TSource last(IEnumerable<TSource> source) {
        if (source == null) throw Errors.argumentNull("source");
        if (source instanceof IListEnumerable) {
            IListEnumerable<TSource> list = (IListEnumerable<TSource>) source;
            int count = list.internalSize();
            if (count > 0) return list.internalGet(count - 1);
        } else {
            try (IEnumerator<TSource> e = source.enumerator()) {
                if (e.moveNext()) {
                    TSource result;
                    do {
                        result = e.current();
                    } while (e.moveNext());
                    return result;
                }
            }
        }
        throw Errors.noElements();
    }

    public static <TSource> TSource last(IEnumerable<TSource> source, Func1<TSource, Boolean> predicate) {
        if (source == null) throw Errors.argumentNull("source");
        if (predicate == null) throw Errors.argumentNull("predicate");
        TSource result = null;
        boolean found = false;
        if (source instanceof IListEnumerable) {
            IListEnumerable<TSource> list = (IListEnumerable<TSource>) source;
            int count = list.internalSize();
            for (int i = count - 1; i >= 0; i--) {
                TSource element = list.internalGet(i);
                if (predicate.apply(element)) {
                    result = element;
                    found = true;
                    break;
                }
            }
        } else {
            for (TSource element : source) {
                if (predicate.apply(element)) {
                    result = element;
                    found = true;
                }
            }
        }
        if (found) return result;
        throw Errors.noMatch();
    }

    public static <TSource> TSource lastOrDefault(IEnumerable<TSource> source) {
        if (source == null) throw Errors.argumentNull("source");
        if (source instanceof IListEnumerable) {
            IListEnumerable<TSource> list = (IListEnumerable<TSource>) source;
            int count = list.internalSize();
            if (count > 0) return list.internalGet(count - 1);
        } else {
            try (IEnumerator<TSource> e = source.enumerator()) {
                if (e.moveNext()) {
                    TSource result;
                    do {
                        result = e.current();
                    } while (e.moveNext());
                    return result;
                }
            }
        }
        return null;
    }

    public static <TSource> TSource lastOrDefault(IEnumerable<TSource> source, Func1<TSource, Boolean> predicate) {
        if (source == null) throw Errors.argumentNull("source");
        if (predicate == null) throw Errors.argumentNull("predicate");
        TSource result = null;
        if (source instanceof IListEnumerable) {
            IListEnumerable<TSource> list = (IListEnumerable<TSource>) source;
            int count = list.internalSize();
            for (int i = count - 1; i >= 0; i--) {
                TSource element = list.internalGet(i);
                if (predicate.apply(element)) {
                    result = element;
                    break;
                }
            }
        } else {
            for (TSource element : source) {
                if (predicate.apply(element)) {
                    result = element;
                }
            }
        }
        return result;
    }

    public static <TSource> TSource single(IEnumerable<TSource> source) {
        if (source == null) throw Errors.argumentNull("source");
        if (source instanceof IListEnumerable) {
            IListEnumerable<TSource> list = (IListEnumerable<TSource>) source;
            switch (list.internalSize()) {
                case 0:
                    throw Errors.noElements();
                case 1:
                    return list.internalGet(0);
            }
        } else {
            try (IEnumerator<TSource> e = source.enumerator()) {
                if (!e.moveNext()) throw Errors.noElements();
                TSource result = e.current();
                if (!e.moveNext()) return result;
            }
        }
        throw Errors.moreThanOneElement();
    }

    public static <TSource> TSource single(IEnumerable<TSource> source, Func1<TSource, Boolean> predicate) {
        if (source == null) throw Errors.argumentNull("source");
        if (predicate == null) throw Errors.argumentNull("predicate");
        TSource result = null;
        int count = 0;
        for (TSource element : source) {
            if (predicate.apply(element)) {
                result = element;
                count = Math.addExact(count, 1);
                if (count > 1)
                    break;
            }
        }
        switch (count) {
            case 0:
                throw Errors.noMatch();
            case 1:
                return result;
        }
        throw Errors.moreThanOneMatch();
    }

    public static <TSource> TSource singleOrDefault(IEnumerable<TSource> source) {
        if (source == null) throw Errors.argumentNull("source");
        if (source instanceof IListEnumerable) {
            IListEnumerable<TSource> list = (IListEnumerable<TSource>) source;
            switch (list.internalSize()) {
                case 0:
                    return null;
                case 1:
                    return list.internalGet(0);
            }
        } else {
            try (IEnumerator<TSource> e = source.enumerator()) {
                if (!e.moveNext()) return null;
                TSource result = e.current();
                if (!e.moveNext()) return result;
            }
        }
        throw Errors.moreThanOneElement();
    }

    public static <TSource> TSource singleOrDefault(IEnumerable<TSource> source, Func1<TSource, Boolean> predicate) {
        if (source == null) throw Errors.argumentNull("source");
        if (predicate == null) throw Errors.argumentNull("predicate");
        TSource result = null;
        int count = 0;
        for (TSource element : source) {
            if (predicate.apply(element)) {
                result = element;
                count = Math.addExact(count, 1);
                if (count > 1)
                    break;
            }
        }
        switch (count) {
            case 0:
                return null;
            case 1:
                return result;
        }
        throw Errors.moreThanOneMatch();
    }

    public static <TSource> TSource elementAt(IEnumerable<TSource> source, int index) {
        if (source == null) throw Errors.argumentNull("source");
        if (source instanceof IListEnumerable) {
            IListEnumerable<TSource> list = (IListEnumerable<TSource>) source;
            return list.internalGet(index);
        }
        if (index < 0) throw Errors.argumentOutOfRange("index");
        try (IEnumerator<TSource> e = source.enumerator()) {
            while (true) {
                if (!e.moveNext()) throw Errors.argumentOutOfRange("index");
                if (index == 0) return e.current();
                index--;
            }
        }
    }

    public static <TSource> TSource elementAtOrDefault(IEnumerable<TSource> source, int index) {
        if (source == null) throw Errors.argumentNull("source");
        if (index >= 0) {
            if (source instanceof IListEnumerable) {
                IListEnumerable<TSource> list = (IListEnumerable<TSource>) source;
                if (index < list.internalSize()) return list.internalGet(index);
            } else {
                try (IEnumerator<TSource> e = source.enumerator()) {
                    while (true) {
                        if (!e.moveNext()) break;
                        if (index == 0) return e.current();
                        index--;
                    }
                }
            }
        }
        return null;
    }

    public static IEnumerable<Integer> range(int start, int count) {
        long max = ((long) start) + count - 1;
        if (count < 0 || max > Integer.MAX_VALUE) throw Errors.argumentOutOfRange("count");
        return new RangeIterator(start, count);
    }

    public static <TResult> IEnumerable<TResult> repeat(TResult element, int count) {
        if (count < 0) throw Errors.argumentOutOfRange("count");
        return new RepeatIterator<>(element, count);
    }

    public static <TResult> IEnumerable<TResult> empty() {
        return EmptyEnumerable.Instance();
    }

    public static <TSource> boolean any(IEnumerable<TSource> source) {
        if (source == null) throw Errors.argumentNull("source");
        try (IEnumerator<TSource> e = source.enumerator()) {
            if (e.moveNext()) return true;
        }
        return false;
    }

    public static <TSource> boolean any(IEnumerable<TSource> source, Func1<TSource, Boolean> predicate) {
        if (source == null) throw Errors.argumentNull("source");
        if (predicate == null) throw Errors.argumentNull("predicate");
        for (TSource element : source) {
            if (predicate.apply(element)) return true;
        }
        return false;
    }

    public static <TSource> boolean all(IEnumerable<TSource> source, Func1<TSource, Boolean> predicate) {
        if (source == null) throw Errors.argumentNull("source");
        if (predicate == null) throw Errors.argumentNull("predicate");
        for (TSource element : source) {
            if (!predicate.apply(element)) return false;
        }
        return true;
    }

    public static <TSource> int count(IEnumerable<TSource> source) {
        if (source == null) throw Errors.argumentNull("source");
        if (source instanceof ICollectionEnumerable) return ((ICollectionEnumerable) source).internalSize();
        int count = 0;
        try (IEnumerator<TSource> e = source.enumerator()) {
            while (e.moveNext()) count = Math.addExact(count, 1);
        }
        return count;
    }

    public static <TSource> int count(IEnumerable<TSource> source, Func1<TSource, Boolean> predicate) {
        if (source == null) throw Errors.argumentNull("source");
        if (predicate == null) throw Errors.argumentNull("predicate");
        int count = 0;
        for (TSource element : source) {
            if (predicate.apply(element)) count = Math.addExact(count, 1);
        }
        return count;
    }

    public static <TSource> long longCount(IEnumerable<TSource> source) {
        if (source == null) throw Errors.argumentNull("source");
        long count = 0;
        try (IEnumerator<TSource> e = source.enumerator()) {
            while (e.moveNext()) count = Math.addExact(count, 1);
        }
        return count;
    }

    public static <TSource> long longCount(IEnumerable<TSource> source, Func1<TSource, Boolean> predicate) {
        if (source == null) throw Errors.argumentNull("source");
        if (predicate == null) throw Errors.argumentNull("predicate");
        long count = 0;
        for (TSource element : source) {
            if (predicate.apply(element)) count = Math.addExact(count, 1);
        }
        return count;
    }

    public static <TSource> boolean contains(IEnumerable<TSource> source, TSource value) {
        if (source instanceof ICollectionEnumerable) return ((ICollectionEnumerable<TSource>) source).internalContains(value);
        return contains(source, value, null);
    }

    public static <TSource> boolean contains(IEnumerable<TSource> source, TSource value, IEqualityComparer<TSource> comparer) {
        if (comparer == null) comparer = EqualityComparer.Default();
        if (source == null) throw Errors.argumentNull("source");
        for (TSource element : source)
            if (comparer.equals(element, value)) return true;
        return false;
    }

    public static <TSource> TSource aggregate(IEnumerable<TSource> source, Func2<TSource, TSource, TSource> func) {
        if (source == null) throw Errors.argumentNull("source");
        if (func == null) throw Errors.argumentNull("func");
        try (IEnumerator<TSource> e = source.enumerator()) {
            if (!e.moveNext()) throw Errors.noElements();
            TSource result = e.current();
            while (e.moveNext()) result = func.apply(result, e.current());
            return result;
        }
    }

    public static <TSource, TAccumulate> TAccumulate aggregate(IEnumerable<TSource> source, TAccumulate seed, Func2<TAccumulate, TSource, TAccumulate> func) {
        if (source == null) throw Errors.argumentNull("source");
        if (func == null) throw Errors.argumentNull("func");
        TAccumulate result = seed;
        for (TSource element : source) result = func.apply(result, element);
        return result;
    }

    public static <TSource, TAccumulate, TResult> TResult aggregate(IEnumerable<TSource> source, TAccumulate seed, Func2<TAccumulate, TSource, TAccumulate> func, Func1<TAccumulate, TResult> resultSelector) {
        if (source == null) throw Errors.argumentNull("source");
        if (func == null) throw Errors.argumentNull("func");
        if (resultSelector == null) throw Errors.argumentNull("resultSelector");
        TAccumulate result = seed;
        for (TSource element : source) result = func.apply(result, element);
        return resultSelector.apply(result);
    }

    public static int sumInt(IEnumerable<Integer> source) {
        if (source == null) throw Errors.argumentNull("source");
        int sum = 0;
        for (Integer v : source)
            if (v != null) sum = Math.addExact(sum, v);
        return sum;
    }

    public static long sumLong(IEnumerable<Long> source) {
        if (source == null) throw Errors.argumentNull("source");
        long sum = 0;
        for (Long v : source)
            if (v != null) sum = Math.addExact(sum, v);
        return sum;
    }

    public static float sumFloat(IEnumerable<Float> source) {
        if (source == null) throw Errors.argumentNull("source");
        double sum = 0;
        for (Float v : source)
            if (v != null) sum += v;
        return (float) sum;
    }

    public static double sumDouble(IEnumerable<Double> source) {
        if (source == null) throw Errors.argumentNull("source");
        double sum = 0;
        for (Double v : source)
            if (v != null) sum += v;
        return sum;
    }

    public static BigDecimal sumDecimal(IEnumerable<BigDecimal> source) {
        if (source == null) throw Errors.argumentNull("source");
        BigDecimal sum = BigDecimal.ZERO;
        for (BigDecimal v : source)
            if (v != null) sum = sum.add(v);
        return sum;
    }

    public static <TSource> int sumInt(IEnumerable<TSource> source, Func1<TSource, Integer> selector) {
        return Enumerable.sumInt(Enumerable.select(source, selector));
    }

    public static <TSource> long sumLong(IEnumerable<TSource> source, Func1<TSource, Long> selector) {
        return Enumerable.sumLong(Enumerable.select(source, selector));
    }

    public static <TSource> float sumFloat(IEnumerable<TSource> source, Func1<TSource, Float> selector) {
        return Enumerable.sumFloat(Enumerable.select(source, selector));
    }

    public static <TSource> double sumDouble(IEnumerable<TSource> source, Func1<TSource, Double> selector) {
        return Enumerable.sumDouble(Enumerable.select(source, selector));
    }

    public static <TSource> BigDecimal sumDecimal(IEnumerable<TSource> source, Func1<TSource, BigDecimal> selector) {
        return Enumerable.sumDecimal(Enumerable.select(source, selector));
    }

    public static int minInt(IEnumerable<Integer> source) {
        if (source == null) throw Errors.argumentNull("source");
        int value = 0;
        boolean hasValue = false;
        for (Integer x : source) {
            if (x == null) continue;
            if (hasValue) {
                if (x < value) value = x;
            } else {
                value = x;
                hasValue = true;
            }
        }
        if (hasValue) return value;
        throw Errors.noElements();
    }

    public static Integer minIntNull(IEnumerable<Integer> source) {
        if (source == null) throw Errors.argumentNull("source");
        Integer value = null;
        for (Integer x : source) {
            if (x == null) continue;
            if (value == null || x < value)
                value = x;
        }
        return value;
    }

    public static long minLong(IEnumerable<Long> source) {
        if (source == null) throw Errors.argumentNull("source");
        long value = 0;
        boolean hasValue = false;
        for (Long x : source) {
            if (x == null) continue;
            if (hasValue) {
                if (x < value) value = x;
            } else {
                value = x;
                hasValue = true;
            }
        }
        if (hasValue) return value;
        throw Errors.noElements();
    }

    public static Long minLongNull(IEnumerable<Long> source) {
        if (source == null) throw Errors.argumentNull("source");
        Long value = null;
        for (Long x : source) {
            if (x == null) continue;
            if (value == null || x < value) value = x;
        }
        return value;
    }

    public static float minFloat(IEnumerable<Float> source) {
        if (source == null) throw Errors.argumentNull("source");
        float value = 0;
        boolean hasValue = false;
        for (Float x : source) {
            if (x == null) continue;
            if (hasValue) {
                // Normally NaN < anything is false, as is anything < NaN
                // However,  leads to some irksome outcomes : Min and Max.
                // If we use those semantics then Min(NaN, 5.0) is NaN, but
                // Min(5.0, NaN) is 5.0!  To fix , we impose a total
                // ordering where NaN is smaller than every value, including
                // negative infinity.
                if (x < value || Float.isNaN(x)) value = x;
            } else {
                value = x;
                hasValue = true;
            }
        }
        if (hasValue) return value;
        throw Errors.noElements();
    }

    public static Float minFloatNull(IEnumerable<Float> source) {
        if (source == null) throw Errors.argumentNull("source");
        Float value = null;
        for (Float x : source) {
            if (x == null) continue;
            if (value == null || x < value || Float.isNaN(x)) value = x;
        }
        return value;
    }

    public static double minDouble(IEnumerable<Double> source) {
        if (source == null) throw Errors.argumentNull("source");
        double value = 0;
        boolean hasValue = false;
        for (Double x : source) {
            if (x == null) continue;
            if (hasValue) {
                if (x < value || Double.isNaN(x)) value = x;
            } else {
                value = x;
                hasValue = true;
            }
        }
        if (hasValue) return value;
        throw Errors.noElements();
    }

    public static Double minDoubleNull(IEnumerable<Double> source) {
        if (source == null) throw Errors.argumentNull("source");
        Double value = null;
        for (Double x : source) {
            if (x == null) continue;
            if (value == null || x < value || Double.isNaN(x)) value = x;
        }
        return value;
    }

    public static BigDecimal minDecimal(IEnumerable<BigDecimal> source) {
        if (source == null) throw Errors.argumentNull("source");
        BigDecimal value = BigDecimal.ZERO;
        boolean hasValue = false;
        for (BigDecimal x : source) {
            if (x == null) continue;
            if (hasValue) {
                if (x.compareTo(value) < 0) value = x;
            } else {
                value = x;
                hasValue = true;
            }
        }
        if (hasValue) return value;
        throw Errors.noElements();
    }

    public static BigDecimal minDecimalNull(IEnumerable<BigDecimal> source) {
        if (source == null) throw Errors.argumentNull("source");
        BigDecimal value = null;
        for (BigDecimal x : source) {
            if (x == null) continue;
            if (value == null || x.compareTo(value) < 0) value = x;
        }
        return value;
    }

    public static <TSource> TSource min(IEnumerable<TSource> source) {
        if (source == null) throw Errors.argumentNull("source");
        Comparator<TSource> comparer = Comparer.Default();
        TSource value = null;
        boolean hasValue = false;
        for (TSource x : source) {
            if (x == null) continue;
            if (hasValue) {
                if (comparer.compare(x, value) < 0)
                    value = x;
            } else {
                value = x;
                hasValue = true;
            }
        }
        if (hasValue) return value;
        throw Errors.noElements();
    }

    public static <TSource> TSource minNull(IEnumerable<TSource> source) {
        if (source == null) throw Errors.argumentNull("source");
        Comparator<TSource> comparer = Comparer.Default();
        TSource value = null;
        for (TSource x : source) {
            if (x != null && (value == null || comparer.compare(x, value) < 0))
                value = x;
        }
        return value;
    }

    public static <TSource> int minInt(IEnumerable<TSource> source, Func1<TSource, Integer> selector) {
        return Enumerable.minInt(Enumerable.select(source, selector));
    }

    public static <TSource> Integer minIntNull(IEnumerable<TSource> source, Func1<TSource, Integer> selector) {
        return Enumerable.minIntNull(Enumerable.select(source, selector));
    }

    public static <TSource> long minLong(IEnumerable<TSource> source, Func1<TSource, Long> selector) {
        return Enumerable.minLong(Enumerable.select(source, selector));
    }

    public static <TSource> Long minLongNull(IEnumerable<TSource> source, Func1<TSource, Long> selector) {
        return Enumerable.minLongNull(Enumerable.select(source, selector));
    }

    public static <TSource> float minFloat(IEnumerable<TSource> source, Func1<TSource, Float> selector) {
        return Enumerable.minFloat(Enumerable.select(source, selector));
    }

    public static <TSource> Float minFloatNull(IEnumerable<TSource> source, Func1<TSource, Float> selector) {
        return Enumerable.minFloatNull(Enumerable.select(source, selector));
    }

    public static <TSource> double minDouble(IEnumerable<TSource> source, Func1<TSource, Double> selector) {
        return Enumerable.minDouble(Enumerable.select(source, selector));
    }

    public static <TSource> Double minDoubleNull(IEnumerable<TSource> source, Func1<TSource, Double> selector) {
        return Enumerable.minDoubleNull(Enumerable.select(source, selector));
    }

    public static <TSource> BigDecimal minDecimal(IEnumerable<TSource> source, Func1<TSource, BigDecimal> selector) {
        return Enumerable.minDecimal(Enumerable.select(source, selector));
    }

    public static <TSource> BigDecimal minDecimalNull(IEnumerable<TSource> source, Func1<TSource, BigDecimal> selector) {
        return Enumerable.minDecimalNull(Enumerable.select(source, selector));
    }

    public static <TSource, TResult> TResult min(IEnumerable<TSource> source, Func1<TSource, TResult> selector) {
        return Enumerable.min(Enumerable.select(source, selector));
    }

    public static <TSource, TResult> TResult minNull(IEnumerable<TSource> source, Func1<TSource, TResult> selector) {
        return Enumerable.minNull(Enumerable.select(source, selector));
    }

    public static int maxInt(IEnumerable<Integer> source) {
        if (source == null) throw Errors.argumentNull("source");
        int value = 0;
        boolean hasValue = false;
        for (Integer x : source) {
            if (x == null) continue;
            if (hasValue) {
                if (x > value) value = x;
            } else {
                value = x;
                hasValue = true;
            }
        }
        if (hasValue) return value;
        throw Errors.noElements();
    }

    public static Integer maxIntNull(IEnumerable<Integer> source) {
        if (source == null) throw Errors.argumentNull("source");
        Integer value = null;
        for (Integer x : source) {
            if (x == null) continue;
            if (value == null || x > value) value = x;
        }
        return value;
    }

    public static long maxLong(IEnumerable<Long> source) {
        if (source == null) throw Errors.argumentNull("source");
        long value = 0;
        boolean hasValue = false;
        for (Long x : source) {
            if (x == null) continue;
            if (hasValue) {
                if (x > value) value = x;
            } else {
                value = x;
                hasValue = true;
            }
        }
        if (hasValue) return value;
        throw Errors.noElements();
    }

    public static Long maxLongNull(IEnumerable<Long> source) {
        if (source == null) throw Errors.argumentNull("source");
        Long value = null;
        for (Long x : source) {
            if (x == null) continue;
            if (value == null || x > value) value = x;
        }
        return value;
    }

    public static float maxFloat(IEnumerable<Float> source) {
        if (source == null) throw Errors.argumentNull("source");
        float value = 0;
        boolean hasValue = false;
        for (Float x : source) {
            if (x == null) continue;
            if (hasValue) {
                if (x > value || Float.isNaN(value)) value = x;
            } else {
                value = x;
                hasValue = true;
            }
        }
        if (hasValue) return value;
        throw Errors.noElements();
    }

    public static Float maxFloatNull(IEnumerable<Float> source) {
        if (source == null) throw Errors.argumentNull("source");
        Float value = null;
        for (Float x : source) {
            if (x == null) continue;
            if (value == null || x > value || Float.isNaN(value)) value = x;
        }
        return value;
    }

    public static double maxDouble(IEnumerable<Double> source) {
        if (source == null) throw Errors.argumentNull("source");
        double value = 0;
        boolean hasValue = false;
        for (Double x : source) {
            if (x == null) continue;
            if (hasValue) {
                if (x > value || Double.isNaN(value)) value = x;
            } else {
                value = x;
                hasValue = true;
            }
        }
        if (hasValue) return value;
        throw Errors.noElements();
    }

    public static Double maxDoubleNull(IEnumerable<Double> source) {
        if (source == null) throw Errors.argumentNull("source");
        Double value = null;
        for (Double x : source) {
            if (x == null) continue;
            if (value == null || x > value || Double.isNaN(value)) value = x;
        }
        return value;
    }

    public static BigDecimal maxDecimal(IEnumerable<BigDecimal> source) {
        if (source == null) throw Errors.argumentNull("source");
        BigDecimal value = BigDecimal.ZERO;
        boolean hasValue = false;
        for (BigDecimal x : source) {
            if (x == null) continue;
            if (hasValue) {
                if (x.compareTo(value) > 0) value = x;
            } else {
                value = x;
                hasValue = true;
            }
        }
        if (hasValue) return value;
        throw Errors.noElements();
    }

    public static BigDecimal maxDecimalNull(IEnumerable<BigDecimal> source) {
        if (source == null) throw Errors.argumentNull("source");
        BigDecimal value = null;
        for (BigDecimal x : source) {
            if (x == null) continue;
            if (value == null || x.compareTo(value) > 0) value = x;
        }
        return value;
    }

    public static <TSource> TSource max(IEnumerable<TSource> source) {
        if (source == null) throw Errors.argumentNull("source");
        Comparator<TSource> comparer = Comparer.Default();
        TSource value = null;
        boolean hasValue = false;
        for (TSource x : source) {
            if (x == null) continue;
            if (hasValue) {
                if (comparer.compare(x, value) > 0)
                    value = x;
            } else {
                value = x;
                hasValue = true;
            }
        }
        if (hasValue) return value;
        throw Errors.noElements();
    }

    public static <TSource> TSource maxNull(IEnumerable<TSource> source) {
        if (source == null) throw Errors.argumentNull("source");
        Comparator<TSource> comparer = Comparer.Default();
        TSource value = null;
        for (TSource x : source) {
            if (x != null && (value == null || comparer.compare(x, value) > 0))
                value = x;
        }
        return value;
    }

    public static <TSource> int maxInt(IEnumerable<TSource> source, Func1<TSource, Integer> selector) {
        return Enumerable.maxInt(Enumerable.select(source, selector));
    }

    public static <TSource> Integer maxIntNull(IEnumerable<TSource> source, Func1<TSource, Integer> selector) {
        return Enumerable.maxIntNull(Enumerable.select(source, selector));
    }

    public static <TSource> long maxLong(IEnumerable<TSource> source, Func1<TSource, Long> selector) {
        return Enumerable.maxLong(Enumerable.select(source, selector));
    }

    public static <TSource> Long maxLongNull(IEnumerable<TSource> source, Func1<TSource, Long> selector) {
        return Enumerable.maxLongNull(Enumerable.select(source, selector));
    }

    public static <TSource> float maxFloat(IEnumerable<TSource> source, Func1<TSource, Float> selector) {
        return Enumerable.maxFloat(Enumerable.select(source, selector));
    }

    public static <TSource> Float maxFloatNull(IEnumerable<TSource> source, Func1<TSource, Float> selector) {
        return Enumerable.maxFloatNull(Enumerable.select(source, selector));
    }

    public static <TSource> double maxDouble(IEnumerable<TSource> source, Func1<TSource, Double> selector) {
        return Enumerable.maxDouble(Enumerable.select(source, selector));
    }

    public static <TSource> Double maxDoubleNull(IEnumerable<TSource> source, Func1<TSource, Double> selector) {
        return Enumerable.maxDoubleNull(Enumerable.select(source, selector));
    }

    public static <TSource> BigDecimal maxDecimal(IEnumerable<TSource> source, Func1<TSource, BigDecimal> selector) {
        return Enumerable.maxDecimal(Enumerable.select(source, selector));
    }

    public static <TSource> BigDecimal maxDecimalNull(IEnumerable<TSource> source, Func1<TSource, BigDecimal> selector) {
        return Enumerable.maxDecimalNull(Enumerable.select(source, selector));
    }

    public static <TSource, TResult> TResult max(IEnumerable<TSource> source, Func1<TSource, TResult> selector) {
        return Enumerable.max(Enumerable.select(source, selector));
    }

    public static <TSource, TResult> TResult maxNull(IEnumerable<TSource> source, Func1<TSource, TResult> selector) {
        return Enumerable.maxNull(Enumerable.select(source, selector));
    }

    public static double averageInt(IEnumerable<Integer> source) {
        if (source == null) throw Errors.argumentNull("source");
        long sum = 0;
        long count = 0;
        for (Integer v : source) {
            if (v != null) {
                sum = Math.addExact(sum, v);
                count = Math.addExact(count, 1);
            }
        }
        if (count > 0) return (double) sum / count;
        throw Errors.noElements();
    }

    public static Double averageIntNull(IEnumerable<Integer> source) {
        if (source == null) throw Errors.argumentNull("source");
        long sum = 0;
        long count = 0;
        for (Integer v : source) {
            if (v != null) {
                sum = Math.addExact(sum, v);
                count = Math.addExact(count, 1);
            }
        }
        if (count > 0) return (double) sum / count;
        return null;
    }

    public static double averageLong(IEnumerable<Long> source) {
        if (source == null) throw Errors.argumentNull("source");
        long sum = 0;
        long count = 0;
        for (Long v : source) {
            if (v != null) {
                sum = Math.addExact(sum, v);
                count = Math.addExact(count, 1);
            }
        }
        if (count > 0) return (double) sum / count;
        throw Errors.noElements();
    }

    public static Double averageLongNull(IEnumerable<Long> source) {
        if (source == null) throw Errors.argumentNull("source");
        long sum = 0;
        long count = 0;
        for (Long v : source) {
            if (v != null) {
                sum = Math.addExact(sum, v);
                count = Math.addExact(count, 1);
            }
        }
        if (count > 0) return (double) sum / count;
        return null;
    }

    public static float averageFloat(IEnumerable<Float> source) {
        if (source == null) throw Errors.argumentNull("source");
        double sum = 0;
        long count = 0;
        for (Float v : source) {
            if (v != null) {
                sum += v;
                count = Math.addExact(count, 1);
            }
        }
        if (count > 0) return (float) (sum / count);
        throw Errors.noElements();
    }

    public static Float averageFloatNull(IEnumerable<Float> source) {
        if (source == null) throw Errors.argumentNull("source");
        double sum = 0;
        long count = 0;
        for (Float v : source) {
            if (v != null) {
                sum += v;
                count = Math.addExact(count, 1);
            }
        }
        if (count > 0) return (float) (sum / count);
        return null;
    }

    public static double averageDouble(IEnumerable<Double> source) {
        if (source == null) throw Errors.argumentNull("source");
        double sum = 0;
        long count = 0;
        for (Double v : source) {
            if (v != null) {
                sum += v;
                count = Math.addExact(count, 1);
            }
        }
        if (count > 0) return sum / count;
        throw Errors.noElements();
    }

    public static Double averageDoubleNull(IEnumerable<Double> source) {
        if (source == null) throw Errors.argumentNull("source");
        double sum = 0;
        long count = 0;
        for (Double v : source) {
            if (v != null) {
                sum += v;
                count = Math.addExact(count, 1);
            }
        }
        if (count > 0) return sum / count;
        return null;
    }

    public static BigDecimal averageDecimal(IEnumerable<BigDecimal> source) {
        if (source == null) throw Errors.argumentNull("source");
        BigDecimal sum = BigDecimal.ZERO;
        long count = 0;
        for (BigDecimal v : source) {
            if (v != null) {
                sum = sum.add(v);
                count = Math.addExact(count, 1);
            }
        }
        if (count > 0) return sum.divide(BigDecimal.valueOf(count), MathContext.DECIMAL128);
        throw Errors.noElements();
    }

    public static BigDecimal averageDecimalNull(IEnumerable<BigDecimal> source) {
        if (source == null) throw Errors.argumentNull("source");
        BigDecimal sum = BigDecimal.ZERO;
        long count = 0;
        for (BigDecimal v : source) {
            if (v != null) {
                sum = sum.add(v);
                count = Math.addExact(count, 1);
            }
        }
        if (count > 0) return sum.divide(BigDecimal.valueOf(count), MathContext.DECIMAL128);
        return null;
    }

    public static <TSource> double averageInt(IEnumerable<TSource> source, Func1<TSource, Integer> selector) {
        return Enumerable.averageInt(Enumerable.select(source, selector));
    }

    public static <TSource> Double averageIntNull(IEnumerable<TSource> source, Func1<TSource, Integer> selector) {
        return Enumerable.averageIntNull(Enumerable.select(source, selector));
    }

    public static <TSource> double averageLong(IEnumerable<TSource> source, Func1<TSource, Long> selector) {
        return Enumerable.averageLong(Enumerable.select(source, selector));
    }

    public static <TSource> Double averageLongNull(IEnumerable<TSource> source, Func1<TSource, Long> selector) {
        return Enumerable.averageLongNull(Enumerable.select(source, selector));
    }

    public static <TSource> float averageFloat(IEnumerable<TSource> source, Func1<TSource, Float> selector) {
        return Enumerable.averageFloat(Enumerable.select(source, selector));
    }

    public static <TSource> Float averageFloatNull(IEnumerable<TSource> source, Func1<TSource, Float> selector) {
        return Enumerable.averageFloatNull(Enumerable.select(source, selector));
    }

    public static <TSource> double averageDouble(IEnumerable<TSource> source, Func1<TSource, Double> selector) {
        return Enumerable.averageDouble(Enumerable.select(source, selector));
    }

    public static <TSource> Double averageDoubleNull(IEnumerable<TSource> source, Func1<TSource, Double> selector) {
        return Enumerable.averageDoubleNull(Enumerable.select(source, selector));
    }

    public static <TSource> BigDecimal averageDecimal(IEnumerable<TSource> source, Func1<TSource, BigDecimal> selector) {
        return Enumerable.averageDecimal(Enumerable.select(source, selector));
    }

    public static <TSource> BigDecimal averageDecimalNull(IEnumerable<TSource> source, Func1<TSource, BigDecimal> selector) {
        return Enumerable.averageDecimalNull(Enumerable.select(source, selector));
    }
}
