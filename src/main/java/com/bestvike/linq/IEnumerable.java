package com.bestvike.linq;

import com.bestvike.linq.function.Func1;
import com.bestvike.linq.function.Func2;
import com.bestvike.linq.iterator.Enumerable;
import com.bestvike.linq.iterator.EnumerableEx;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author 许崇雷
 * @date 2017/7/10
 */
@SuppressWarnings("unchecked")
public interface IEnumerable<TSource> extends Iterable<TSource> {
    IEnumerator<TSource> enumerator();

    default Iterator<TSource> iterator() {
        return this.enumerator();
    }

    default IEnumerable<TSource> where(Func1<TSource, Boolean> predicate) {
        return Enumerable.where(this, predicate);
    }

    default IEnumerable<TSource> where(Func2<TSource, Integer, Boolean> predicate) {
        return Enumerable.where(this, predicate);
    }

    default <TResult> IEnumerable<TResult> select(Func1<TSource, TResult> selector) {
        return Enumerable.select(this, selector);
    }

    default <TResult> IEnumerable<TResult> select(Func2<TSource, Integer, TResult> selector) {
        return Enumerable.select(this, selector);
    }

    default <TResult> IEnumerable<TResult> selectMany(Func1<TSource, IEnumerable<TResult>> selector) {
        return Enumerable.selectMany(this, selector);
    }

    default <TResult> IEnumerable<TResult> selectMany(Func2<TSource, Integer, IEnumerable<TResult>> selector) {
        return Enumerable.selectMany(this, selector);
    }

    default <TCollection, TResult> IEnumerable<TResult> selectMany(Func1<TSource, IEnumerable<TCollection>> collectionSelector, Func2<TSource, TCollection, TResult> resultSelector) {
        return Enumerable.selectMany(this, collectionSelector, resultSelector);
    }

    default <TCollection, TResult> IEnumerable<TResult> selectMany(Func2<TSource, Integer, IEnumerable<TCollection>> collectionSelector, Func2<TSource, TCollection, TResult> resultSelector) {
        return Enumerable.selectMany(this, collectionSelector, resultSelector);
    }

    default IEnumerable<TSource> take(int count) {
        return Enumerable.take(this, count);
    }

    default IEnumerable<TSource> takeWhile(Func1<TSource, Boolean> predicate) {
        return Enumerable.takeWhile(this, predicate);
    }

    default IEnumerable<TSource> takeWhile(Func2<TSource, Integer, Boolean> predicate) {
        return Enumerable.takeWhile(this, predicate);
    }

    default IEnumerable<TSource> skip(int count) {
        return Enumerable.skip(this, count);
    }

    default IEnumerable<TSource> skipWhile(Func1<TSource, Boolean> predicate) {
        return Enumerable.skipWhile(this, predicate);
    }

    default IEnumerable<TSource> skipWhile(Func2<TSource, Integer, Boolean> predicate) {
        return Enumerable.skipWhile(this, predicate);
    }

    default <TInner, TKey, TResult> IEnumerable<TResult> join(IEnumerable<TInner> inner, Func1<TSource, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, Func2<TSource, TInner, TResult> resultSelector) {
        return Enumerable.join(this, inner, outerKeySelector, innerKeySelector, resultSelector);
    }

    default <TInner, TKey, TResult> IEnumerable<TResult> join(IEnumerable<TInner> inner, Func1<TSource, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, Func2<TSource, TInner, TResult> resultSelector, IEqualityComparer<TKey> comparer) {
        return Enumerable.join(this, inner, outerKeySelector, innerKeySelector, resultSelector, comparer);
    }

    default <TInner, TKey, TResult> IEnumerable<TResult> groupJoin(IEnumerable<TInner> inner, Func1<TSource, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, Func2<TSource, IEnumerable<TInner>, TResult> resultSelector) {
        return Enumerable.groupJoin(this, inner, outerKeySelector, innerKeySelector, resultSelector);
    }

    default <TInner, TKey, TResult> IEnumerable<TResult> groupJoin(IEnumerable<TInner> inner, Func1<TSource, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, Func2<TSource, IEnumerable<TInner>, TResult> resultSelector, IEqualityComparer<TKey> comparer) {
        return Enumerable.groupJoin(this, inner, outerKeySelector, innerKeySelector, resultSelector, comparer);
    }

    default <TKey> IOrderedEnumerable<TSource> orderBy(Func1<TSource, TKey> keySelector) {
        return Enumerable.orderBy(this, keySelector);
    }

    default <TKey> IOrderedEnumerable<TSource> orderBy(Func1<TSource, TKey> keySelector, Comparator<TKey> comparer) {
        return Enumerable.orderBy(this, keySelector, comparer);
    }

    default <TKey> IOrderedEnumerable<TSource> orderByDescending(Func1<TSource, TKey> keySelector) {
        return Enumerable.orderByDescending(this, keySelector);
    }

    default <TKey> IOrderedEnumerable<TSource> orderByDescending(Func1<TSource, TKey> keySelector, Comparator<TKey> comparer) {
        return Enumerable.orderByDescending(this, keySelector, comparer);
    }

    default <TKey> IEnumerable<IGrouping<TKey, TSource>> groupBy(Func1<TSource, TKey> keySelector) {
        return Enumerable.groupBy(this, keySelector);
    }

    default <TKey> IEnumerable<IGrouping<TKey, TSource>> groupBy(Func1<TSource, TKey> keySelector, IEqualityComparer<TKey> comparer) {
        return Enumerable.groupBy(this, keySelector, comparer);
    }

    default <TKey, TElement> IEnumerable<IGrouping<TKey, TElement>> groupBy(Func1<TSource, TKey> keySelector, Func1<TSource, TElement> elementSelector) {
        return Enumerable.groupBy(this, keySelector, elementSelector);
    }

    default <TKey, TElement> IEnumerable<IGrouping<TKey, TElement>> groupBy(Func1<TSource, TKey> keySelector, Func1<TSource, TElement> elementSelector, IEqualityComparer<TKey> comparer) {
        return Enumerable.groupBy(this, keySelector, elementSelector, comparer);
    }

    default <TKey, TResult> IEnumerable<TResult> groupBy(Func1<TSource, TKey> keySelector, Func2<TKey, IEnumerable<TSource>, TResult> resultSelector) {
        return Enumerable.groupBy(this, keySelector, resultSelector);
    }

    default <TKey, TElement, TResult> IEnumerable<TResult> groupBy(Func1<TSource, TKey> keySelector, Func1<TSource, TElement> elementSelector, Func2<TKey, IEnumerable<TElement>, TResult> resultSelector) {
        return Enumerable.groupBy(this, keySelector, elementSelector, resultSelector);
    }

    default <TKey, TResult> IEnumerable<TResult> groupBy(Func1<TSource, TKey> keySelector, Func2<TKey, IEnumerable<TSource>, TResult> resultSelector, IEqualityComparer<TKey> comparer) {
        return Enumerable.groupBy(this, keySelector, resultSelector, comparer);
    }

    default <TKey, TElement, TResult> IEnumerable<TResult> groupBy(Func1<TSource, TKey> keySelector, Func1<TSource, TElement> elementSelector, Func2<TKey, IEnumerable<TElement>, TResult> resultSelector, IEqualityComparer<TKey> comparer) {
        return Enumerable.groupBy(this, keySelector, elementSelector, resultSelector, comparer);
    }

    default IEnumerable<TSource> concat(IEnumerable<TSource> second) {
        return Enumerable.concat(this, second);
    }

    default <TSecond, TResult> IEnumerable<TResult> zip(IEnumerable<TSecond> second, Func2<TSource, TSecond, TResult> resultSelector) {
        return Enumerable.zip(this, second, resultSelector);
    }

    default IEnumerable<TSource> distinct() {
        return Enumerable.distinct(this);
    }

    default IEnumerable<TSource> distinct(IEqualityComparer<TSource> comparer) {
        return Enumerable.distinct(this, comparer);
    }

    default IEnumerable<TSource> union(IEnumerable<TSource> second) {
        return Enumerable.union(this, second);
    }

    default IEnumerable<TSource> union(IEnumerable<TSource> second, IEqualityComparer<TSource> comparer) {
        return Enumerable.union(this, second, comparer);
    }

    default IEnumerable<TSource> intersect(IEnumerable<TSource> second) {
        return Enumerable.intersect(this, second);
    }

    default IEnumerable<TSource> intersect(IEnumerable<TSource> second, IEqualityComparer<TSource> comparer) {
        return Enumerable.intersect(this, second, comparer);
    }

    default IEnumerable<TSource> except(IEnumerable<TSource> second) {
        return Enumerable.except(this, second);
    }

    default IEnumerable<TSource> except(IEnumerable<TSource> second, IEqualityComparer<TSource> comparer) {
        return Enumerable.except(this, second, comparer);
    }

    default IEnumerable<TSource> reverse() {
        return Enumerable.reverse(this);
    }

    default boolean sequenceEqual(IEnumerable<TSource> second) {
        return Enumerable.sequenceEqual(this, second);
    }

    default boolean sequenceEqual(IEnumerable<TSource> second, IEqualityComparer<TSource> comparer) {
        return Enumerable.sequenceEqual(this, second, comparer);
    }

    default TSource[] toArray(Class<TSource> clazz) {
        return Enumerable.toArray(this, clazz);
    }

    default List<TSource> toList() {
        return Enumerable.toList(this);
    }

    default <TKey> Map<TKey, TSource> toMap(Func1<TSource, TKey> keySelector) {
        return Enumerable.toMap(this, keySelector);
    }

    default <TKey, TElement> Map<TKey, TElement> toMap(Func1<TSource, TKey> keySelector, Func1<TSource, TElement> elementSelector) {
        return Enumerable.toMap(this, keySelector, elementSelector);
    }

    default <TKey> ILookup<TKey, TSource> toLookup(Func1<TSource, TKey> keySelector) {
        return Enumerable.toLookup(this, keySelector);
    }

    default <TKey> ILookup<TKey, TSource> toLookup(Func1<TSource, TKey> keySelector, IEqualityComparer<TKey> comparer) {
        return Enumerable.toLookup(this, keySelector, comparer);
    }

    default <TKey, TElement> ILookup<TKey, TElement> toLookup(Func1<TSource, TKey> keySelector, Func1<TSource, TElement> elementSelector) {
        return Enumerable.toLookup(this, keySelector, elementSelector);
    }

    default <TKey, TElement> ILookup<TKey, TElement> toLookup(Func1<TSource, TKey> keySelector, Func1<TSource, TElement> elementSelector, IEqualityComparer<TKey> comparer) {
        return Enumerable.toLookup(this, keySelector, elementSelector, comparer);
    }

    default IEnumerable<TSource> defaultIfEmpty() {
        return Enumerable.defaultIfEmpty(this);
    }

    default IEnumerable<TSource> defaultIfEmpty(TSource defaultValue) {
        return Enumerable.defaultIfEmpty(this, defaultValue);
    }

    default <TResult> IEnumerable<TResult> ofType(Class<TResult> clazz) {
        return Enumerable.ofType(this, clazz);
    }

    default <TResult> IEnumerable<TResult> cast(Class<TResult> clazz) {
        return Enumerable.cast(this, clazz);
    }

    default TSource first() {
        return Enumerable.first(this);
    }

    default TSource first(Func1<TSource, Boolean> predicate) {
        return Enumerable.first(this, predicate);
    }

    default TSource firstOrDefault() {
        return Enumerable.firstOrDefault(this);
    }

    default TSource firstOrDefault(Func1<TSource, Boolean> predicate) {
        return Enumerable.firstOrDefault(this, predicate);
    }

    default TSource last() {
        return Enumerable.last(this);
    }

    default TSource last(Func1<TSource, Boolean> predicate) {
        return Enumerable.last(this, predicate);
    }

    default TSource lastOrDefault() {
        return Enumerable.lastOrDefault(this);
    }

    default TSource lastOrDefault(Func1<TSource, Boolean> predicate) {
        return Enumerable.lastOrDefault(this, predicate);
    }

    default TSource single() {
        return Enumerable.single(this);
    }

    default TSource single(Func1<TSource, Boolean> predicate) {
        return Enumerable.single(this, predicate);
    }

    default TSource singleOrDefault() {
        return Enumerable.singleOrDefault(this);
    }

    default TSource singleOrDefault(Func1<TSource, Boolean> predicate) {
        return Enumerable.singleOrDefault(this, predicate);
    }

    default TSource elementAt(int index) {
        return Enumerable.elementAt(this, index);
    }

    default TSource elementAtOrDefault(int index) {
        return Enumerable.elementAtOrDefault(this, index);
    }

    default boolean any() {
        return Enumerable.any(this);
    }

    default boolean any(Func1<TSource, Boolean> predicate) {
        return Enumerable.any(this, predicate);
    }

    default boolean all(Func1<TSource, Boolean> predicate) {
        return Enumerable.all(this, predicate);
    }

    default int count() {
        return Enumerable.count(this);
    }

    default int count(Func1<TSource, Boolean> predicate) {
        return Enumerable.count(this, predicate);
    }

    default long longCount() {
        return Enumerable.longCount(this);
    }

    default long longCount(Func1<TSource, Boolean> predicate) {
        return Enumerable.longCount(this, predicate);
    }

    default boolean contains(TSource value) {
        return Enumerable.contains(this, value);
    }

    default boolean contains(TSource value, IEqualityComparer<TSource> comparer) {
        return Enumerable.contains(this, value, comparer);
    }

    default TSource aggregate(Func2<TSource, TSource, TSource> func) {
        return Enumerable.aggregate(this, func);
    }

    default <TAccumulate> TAccumulate aggregate(TAccumulate seed, Func2<TAccumulate, TSource, TAccumulate> func) {
        return Enumerable.aggregate(this, seed, func);
    }

    default <TAccumulate, TResult> TResult aggregate(TAccumulate seed, Func2<TAccumulate, TSource, TAccumulate> func, Func1<TAccumulate, TResult> resultSelector) {
        return Enumerable.aggregate(this, seed, func, resultSelector);
    }

    default int sumInt() {
        return Enumerable.sumInt((IEnumerable<Integer>) this);
    }

    default long sumLong() {
        return Enumerable.sumLong((IEnumerable<Long>) this);
    }

    default float sumFloat() {
        return Enumerable.sumFloat((IEnumerable<Float>) this);
    }

    default double sumDouble() {
        return Enumerable.sumDouble((IEnumerable<Double>) this);
    }

    default BigDecimal sumDecimal() {
        return Enumerable.sumDecimal((IEnumerable<BigDecimal>) this);
    }

    default int sumInt(Func1<TSource, Integer> selector) {
        return Enumerable.sumInt(this, selector);
    }

    default long sumLong(Func1<TSource, Long> selector) {
        return Enumerable.sumLong(this, selector);
    }

    default float sumFloat(Func1<TSource, Float> selector) {
        return Enumerable.sumFloat(this, selector);
    }

    default double sumDouble(Func1<TSource, Double> selector) {
        return Enumerable.sumDouble(this, selector);
    }

    default BigDecimal sumDecimal(Func1<TSource, BigDecimal> selector) {
        return Enumerable.sumDecimal(this, selector);
    }

    default int minInt() {
        return Enumerable.minInt((IEnumerable<Integer>) this);
    }

    default Integer minIntNull() {
        return Enumerable.minIntNull((IEnumerable<Integer>) this);
    }

    default long minLong() {
        return Enumerable.minLong((IEnumerable<Long>) this);
    }

    default Long minLongNull() {
        return Enumerable.minLongNull((IEnumerable<Long>) this);
    }

    default float minFloat() {
        return Enumerable.minFloat((IEnumerable<Float>) this);
    }

    default Float minFloatNull() {
        return Enumerable.minFloatNull((IEnumerable<Float>) this);
    }

    default double minDouble() {
        return Enumerable.minDouble((IEnumerable<Double>) this);
    }

    default Double minDoubleNull() {
        return Enumerable.minDoubleNull((IEnumerable<Double>) this);
    }

    default BigDecimal minDecimal() {
        return Enumerable.minDecimal((IEnumerable<BigDecimal>) this);
    }

    default BigDecimal minDecimalNull() {
        return Enumerable.minDecimalNull((IEnumerable<BigDecimal>) this);
    }

    default TSource minNull() {
        return Enumerable.minNull(this);
    }

    default int minInt(Func1<TSource, Integer> selector) {
        return Enumerable.minInt(this, selector);
    }

    default Integer minIntNull(Func1<TSource, Integer> selector) {
        return Enumerable.minIntNull(this, selector);
    }

    default long minLong(Func1<TSource, Long> selector) {
        return Enumerable.minLong(this, selector);
    }

    default Long minLongNull(Func1<TSource, Long> selector) {
        return Enumerable.minLongNull(this, selector);
    }

    default float minFloat(Func1<TSource, Float> selector) {
        return Enumerable.minFloat(this, selector);
    }

    default Float minFloatNull(Func1<TSource, Float> selector) {
        return Enumerable.minFloatNull(this, selector);
    }

    default double minDouble(Func1<TSource, Double> selector) {
        return Enumerable.minDouble(this, selector);
    }

    default Double minDoubleNull(Func1<TSource, Double> selector) {
        return Enumerable.minDoubleNull(this, selector);
    }

    default BigDecimal minDecimal(Func1<TSource, BigDecimal> selector) {
        return Enumerable.minDecimal(this, selector);
    }

    default BigDecimal minDecimalNull(Func1<TSource, BigDecimal> selector) {
        return Enumerable.minDecimalNull(this, selector);
    }

    default <TResult> TResult minNull(Func1<TSource, TResult> selector) {
        return Enumerable.minNull(this, selector);
    }

    default int maxInt() {
        return Enumerable.maxInt((IEnumerable<Integer>) this);
    }

    default Integer maxIntNull() {
        return Enumerable.maxIntNull((IEnumerable<Integer>) this);
    }

    default long maxLong() {
        return Enumerable.maxLong((IEnumerable<Long>) this);
    }

    default Long maxLongNull() {
        return Enumerable.maxLongNull((IEnumerable<Long>) this);
    }

    default float maxFloat() {
        return Enumerable.maxFloat((IEnumerable<Float>) this);
    }

    default Float maxFloatNull() {
        return Enumerable.maxFloatNull((IEnumerable<Float>) this);
    }

    default double maxDouble() {
        return Enumerable.maxDouble((IEnumerable<Double>) this);
    }

    default Double maxDoubleNull() {
        return Enumerable.maxDoubleNull((IEnumerable<Double>) this);
    }

    default BigDecimal maxDecimal() {
        return Enumerable.maxDecimal((IEnumerable<BigDecimal>) this);
    }

    default BigDecimal maxDecimalNull() {
        return Enumerable.maxDecimalNull((IEnumerable<BigDecimal>) this);
    }

    default TSource maxNull() {
        return Enumerable.maxNull(this);
    }

    default int maxInt(Func1<TSource, Integer> selector) {
        return Enumerable.maxInt(this, selector);
    }

    default Integer maxIntNull(Func1<TSource, Integer> selector) {
        return Enumerable.maxIntNull(this, selector);
    }

    default long maxLong(Func1<TSource, Long> selector) {
        return Enumerable.maxLong(this, selector);
    }

    default Long maxLongNull(Func1<TSource, Long> selector) {
        return Enumerable.maxLongNull(this, selector);
    }

    default float maxFloat(Func1<TSource, Float> selector) {
        return Enumerable.maxFloat(this, selector);
    }

    default Float maxFloatNull(Func1<TSource, Float> selector) {
        return Enumerable.maxFloatNull(this, selector);
    }

    default double maxDouble(Func1<TSource, Double> selector) {
        return Enumerable.maxDouble(this, selector);
    }

    default Double maxDoubleNull(Func1<TSource, Double> selector) {
        return Enumerable.maxDoubleNull(this, selector);
    }

    default BigDecimal maxDecimal(Func1<TSource, BigDecimal> selector) {
        return Enumerable.maxDecimal(this, selector);
    }

    default BigDecimal maxDecimalNull(Func1<TSource, BigDecimal> selector) {
        return Enumerable.maxDecimalNull(this, selector);
    }

    default <TResult> TResult maxNull(Func1<TSource, TResult> selector) {
        return Enumerable.maxNull(this, selector);
    }

    default double averageInt() {
        return Enumerable.averageInt((IEnumerable<Integer>) this);
    }

    default Double averageIntNull() {
        return Enumerable.averageIntNull((IEnumerable<Integer>) this);
    }

    default double averageLong() {
        return Enumerable.averageLong((IEnumerable<Long>) this);
    }

    default Double averageLongNull() {
        return Enumerable.averageLongNull((IEnumerable<Long>) this);
    }

    default float averageFloat() {
        return Enumerable.averageFloat((IEnumerable<Float>) this);
    }

    default Float averageFloatNull() {
        return Enumerable.averageFloatNull((IEnumerable<Float>) this);
    }

    default double averageDouble() {
        return Enumerable.averageDouble((IEnumerable<Double>) this);
    }

    default Double averageDoubleNull() {
        return Enumerable.averageDoubleNull((IEnumerable<Double>) this);
    }

    default BigDecimal averageDecimal() {
        return Enumerable.averageDecimal((IEnumerable<BigDecimal>) this);
    }

    default BigDecimal averageDecimalNull() {
        return Enumerable.averageDecimalNull((IEnumerable<BigDecimal>) this);
    }

    default double averageInt(Func1<TSource, Integer> selector) {
        return Enumerable.averageInt(this, selector);
    }

    default Double averageIntNull(Func1<TSource, Integer> selector) {
        return Enumerable.averageIntNull(this, selector);
    }

    default double averageLong(Func1<TSource, Long> selector) {
        return Enumerable.averageLong(this, selector);
    }

    default Double averageLongNull(Func1<TSource, Long> selector) {
        return Enumerable.averageLongNull(this, selector);
    }

    default float averageFloat(Func1<TSource, Float> selector) {
        return Enumerable.averageFloat(this, selector);
    }

    default Float averageFloatNull(Func1<TSource, Float> selector) {
        return Enumerable.averageFloatNull(this, selector);
    }

    default double averageDouble(Func1<TSource, Double> selector) {
        return Enumerable.averageDouble(this, selector);
    }

    default Double averageDoubleNull(Func1<TSource, Double> selector) {
        return Enumerable.averageDoubleNull(this, selector);
    }

    default BigDecimal averageDecimal(Func1<TSource, BigDecimal> selector) {
        return Enumerable.averageDecimal(this, selector);
    }

    default BigDecimal averageDecimalNull(Func1<TSource, BigDecimal> selector) {
        return Enumerable.averageDecimalNull(this, selector);
    }

    default <TInner, TKey, TResult> IEnumerable<TResult> leftJoin(IEnumerable<TInner> inner, Func1<TSource, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, Func2<TSource, TInner, TResult> resultSelector) {
        return EnumerableEx.leftJoin(this, inner, outerKeySelector, innerKeySelector, resultSelector);
    }

    default <TInner, TKey, TResult> IEnumerable<TResult> leftJoin(IEnumerable<TInner> inner, Func1<TSource, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, TInner defaultInner, Func2<TSource, TInner, TResult> resultSelector) {
        return EnumerableEx.leftJoin(this, inner, outerKeySelector, innerKeySelector, defaultInner, resultSelector);
    }

    default <TInner, TKey, TResult> IEnumerable<TResult> leftJoin(IEnumerable<TInner> inner, Func1<TSource, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, Func2<TSource, TInner, TResult> resultSelector, IEqualityComparer<TKey> comparer) {
        return EnumerableEx.leftJoin(this, inner, outerKeySelector, innerKeySelector, resultSelector, comparer);
    }

    default <TInner, TKey, TResult> IEnumerable<TResult> leftJoin(IEnumerable<TInner> inner, Func1<TSource, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, TInner defaultInner, Func2<TSource, TInner, TResult> resultSelector, IEqualityComparer<TKey> comparer) {
        return EnumerableEx.leftJoin(this, inner, outerKeySelector, innerKeySelector, defaultInner, resultSelector, comparer);
    }

    default <TInner, TKey, TResult> IEnumerable<TResult> rightJoin(IEnumerable<TInner> inner, Func1<TSource, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, Func2<TSource, TInner, TResult> resultSelector) {
        return EnumerableEx.rightJoin(this, inner, outerKeySelector, innerKeySelector, resultSelector);
    }

    default <TInner, TKey, TResult> IEnumerable<TResult> rightJoin(IEnumerable<TInner> inner, Func1<TSource, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, TSource defaultOuter, Func2<TSource, TInner, TResult> resultSelector) {
        return EnumerableEx.rightJoin(this, inner, outerKeySelector, innerKeySelector, defaultOuter, resultSelector);
    }

    default <TInner, TKey, TResult> IEnumerable<TResult> rightJoin(IEnumerable<TInner> inner, Func1<TSource, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, Func2<TSource, TInner, TResult> resultSelector, IEqualityComparer<TKey> comparer) {
        return EnumerableEx.rightJoin(this, inner, outerKeySelector, innerKeySelector, resultSelector, comparer);
    }

    default <TInner, TKey, TResult> IEnumerable<TResult> rightJoin(IEnumerable<TInner> inner, Func1<TSource, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, TSource defaultOuter, Func2<TSource, TInner, TResult> resultSelector, IEqualityComparer<TKey> comparer) {
        return EnumerableEx.rightJoin(this, inner, outerKeySelector, innerKeySelector, defaultOuter, resultSelector, comparer);
    }

    default <TInner, TKey, TResult> IEnumerable<TResult> fullJoin(IEnumerable<TInner> inner, Func1<TSource, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, Func2<TSource, TInner, TResult> resultSelector) {
        return EnumerableEx.fullJoin(this, inner, outerKeySelector, innerKeySelector, resultSelector);
    }

    default <TInner, TKey, TResult> IEnumerable<TResult> fullJoin(IEnumerable<TInner> inner, Func1<TSource, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, TSource defaultOuter, TInner defaultInner, Func2<TSource, TInner, TResult> resultSelector) {
        return EnumerableEx.fullJoin(this, inner, outerKeySelector, innerKeySelector, defaultOuter, defaultInner, resultSelector);
    }

    default <TInner, TKey, TResult> IEnumerable<TResult> fullJoin(IEnumerable<TInner> inner, Func1<TSource, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, Func2<TSource, TInner, TResult> resultSelector, IEqualityComparer<TKey> comparer) {
        return EnumerableEx.fullJoin(this, inner, outerKeySelector, innerKeySelector, resultSelector, comparer);
    }

    default <TInner, TKey, TResult> IEnumerable<TResult> fullJoin(IEnumerable<TInner> inner, Func1<TSource, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, TSource defaultOuter, TInner defaultInner, Func2<TSource, TInner, TResult> resultSelector, IEqualityComparer<TKey> comparer) {
        return EnumerableEx.fullJoin(this, inner, outerKeySelector, innerKeySelector, defaultOuter, defaultInner, resultSelector, comparer);
    }

    default IEnumerable<TSource> append(TSource item) {
        return EnumerableEx.append(this, item);
    }
}
