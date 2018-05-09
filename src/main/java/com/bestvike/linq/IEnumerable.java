package com.bestvike.linq;

import com.bestvike.collections.generic.Array;
import com.bestvike.collections.generic.IEqualityComparer;
import com.bestvike.function.Func1;
import com.bestvike.function.Func2;
import com.bestvike.linq.iterator.Aggregate;
import com.bestvike.linq.iterator.AnyAll;
import com.bestvike.linq.iterator.AppendPrepend;
import com.bestvike.linq.iterator.Average;
import com.bestvike.linq.iterator.Cast;
import com.bestvike.linq.iterator.Concat;
import com.bestvike.linq.iterator.Contains;
import com.bestvike.linq.iterator.Count;
import com.bestvike.linq.iterator.DefaultIfEmpty;
import com.bestvike.linq.iterator.Distinct;
import com.bestvike.linq.iterator.DistinctBy;
import com.bestvike.linq.iterator.ElementAt;
import com.bestvike.linq.iterator.Except;
import com.bestvike.linq.iterator.ExceptBy;
import com.bestvike.linq.iterator.First;
import com.bestvike.linq.iterator.GroupJoin;
import com.bestvike.linq.iterator.Grouping;
import com.bestvike.linq.iterator.Intersect;
import com.bestvike.linq.iterator.IntersectBy;
import com.bestvike.linq.iterator.Join;
import com.bestvike.linq.iterator.Last;
import com.bestvike.linq.iterator.Lookup;
import com.bestvike.linq.iterator.Max;
import com.bestvike.linq.iterator.MaxBy;
import com.bestvike.linq.iterator.Min;
import com.bestvike.linq.iterator.MinBy;
import com.bestvike.linq.iterator.OrderBy;
import com.bestvike.linq.iterator.Reverse;
import com.bestvike.linq.iterator.Select;
import com.bestvike.linq.iterator.SelectMany;
import com.bestvike.linq.iterator.SequenceEqual;
import com.bestvike.linq.iterator.Single;
import com.bestvike.linq.iterator.Skip;
import com.bestvike.linq.iterator.Sum;
import com.bestvike.linq.iterator.Take;
import com.bestvike.linq.iterator.ToCollection;
import com.bestvike.linq.iterator.Union;
import com.bestvike.linq.iterator.UnionBy;
import com.bestvike.linq.iterator.Where;
import com.bestvike.linq.iterator.Zip;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by 许崇雷 on 2017/7/10.
 */
@SuppressWarnings("unchecked")
public interface IEnumerable<TSource> extends Iterable<TSource> {
    IEnumerator<TSource> enumerator();

    default Iterator<TSource> iterator() {
        return this.enumerator();
    }

    default TSource aggregate(Func2<TSource, TSource, TSource> func) {
        return Aggregate.aggregate(this, func);
    }

    default <TAccumulate> TAccumulate aggregate(TAccumulate seed, Func2<TAccumulate, TSource, TAccumulate> func) {
        return Aggregate.aggregate(this, seed, func);
    }

    default <TAccumulate, TResult> TResult aggregate(TAccumulate seed, Func2<TAccumulate, TSource, TAccumulate> func, Func1<TAccumulate, TResult> resultSelector) {
        return Aggregate.aggregate(this, seed, func, resultSelector);
    }

    default boolean any() {
        return AnyAll.any(this);
    }

    default boolean any(Func1<TSource, Boolean> predicate) {
        return AnyAll.any(this, predicate);
    }

    default boolean all(Func1<TSource, Boolean> predicate) {
        return AnyAll.all(this, predicate);
    }

    default IEnumerable<TSource> append(TSource element) {
        return AppendPrepend.append(this, element);
    }

    default IEnumerable<TSource> prepend(TSource element) {
        return AppendPrepend.prepend(this, element);
    }

    default double averageInt() {
        return Average.averageInt((IEnumerable<Integer>) this);
    }

    default Double averageIntNull() {
        return Average.averageIntNull((IEnumerable<Integer>) this);
    }

    default double averageLong() {
        return Average.averageLong((IEnumerable<Long>) this);
    }

    default Double averageLongNull() {
        return Average.averageLongNull((IEnumerable<Long>) this);
    }

    default float averageFloat() {
        return Average.averageFloat((IEnumerable<Float>) this);
    }

    default Float averageFloatNull() {
        return Average.averageFloatNull((IEnumerable<Float>) this);
    }

    default double averageDouble() {
        return Average.averageDouble((IEnumerable<Double>) this);
    }

    default Double averageDoubleNull() {
        return Average.averageDoubleNull((IEnumerable<Double>) this);
    }

    default BigDecimal averageDecimal() {
        return Average.averageDecimal((IEnumerable<BigDecimal>) this);
    }

    default BigDecimal averageDecimalNull() {
        return Average.averageDecimalNull((IEnumerable<BigDecimal>) this);
    }

    default double averageInt(Func1<TSource, Integer> selector) {
        return Average.averageInt(this, selector);
    }

    default Double averageIntNull(Func1<TSource, Integer> selector) {
        return Average.averageIntNull(this, selector);
    }

    default double averageLong(Func1<TSource, Long> selector) {
        return Average.averageLong(this, selector);
    }

    default Double averageLongNull(Func1<TSource, Long> selector) {
        return Average.averageLongNull(this, selector);
    }

    default float averageFloat(Func1<TSource, Float> selector) {
        return Average.averageFloat(this, selector);
    }

    default Float averageFloatNull(Func1<TSource, Float> selector) {
        return Average.averageFloatNull(this, selector);
    }

    default double averageDouble(Func1<TSource, Double> selector) {
        return Average.averageDouble(this, selector);
    }

    default Double averageDoubleNull(Func1<TSource, Double> selector) {
        return Average.averageDoubleNull(this, selector);
    }

    default BigDecimal averageDecimal(Func1<TSource, BigDecimal> selector) {
        return Average.averageDecimal(this, selector);
    }

    default BigDecimal averageDecimalNull(Func1<TSource, BigDecimal> selector) {
        return Average.averageDecimalNull(this, selector);
    }

    default <TResult> IEnumerable<TResult> ofType(Class<TResult> clazz) {
        return Cast.ofType(this, clazz);
    }

    default <TResult> IEnumerable<TResult> cast(Class<TResult> clazz) {
        return Cast.cast(this, clazz);
    }

    default IEnumerable<TSource> concat(IEnumerable<TSource> second) {
        return Concat.concat(this, second);
    }

    default boolean contains(TSource value) {
        return Contains.contains(this, value);
    }

    default boolean contains(TSource value, IEqualityComparer<TSource> comparer) {
        return Contains.contains(this, value, comparer);
    }

    default int count() {
        return Count.count(this);
    }

    default int count(Func1<TSource, Boolean> predicate) {
        return Count.count(this, predicate);
    }

    default long longCount() {
        return Count.longCount(this);
    }

    default long longCount(Func1<TSource, Boolean> predicate) {
        return Count.longCount(this, predicate);
    }

    default IEnumerable<TSource> defaultIfEmpty() {
        return DefaultIfEmpty.defaultIfEmpty(this);
    }

    default IEnumerable<TSource> defaultIfEmpty(TSource defaultValue) {
        return DefaultIfEmpty.defaultIfEmpty(this, defaultValue);
    }

    default IEnumerable<TSource> distinct() {
        return Distinct.distinct(this);
    }

    default IEnumerable<TSource> distinct(IEqualityComparer<TSource> comparer) {
        return Distinct.distinct(this, comparer);
    }

    default <TKey> IEnumerable<TSource> distinctBy(Func1<TSource, TKey> keySelector) {
        return DistinctBy.distinctBy(this, keySelector);
    }

    default <TKey> IEnumerable<TSource> distinctBy(Func1<TSource, TKey> keySelector, IEqualityComparer<TKey> comparer) {
        return DistinctBy.distinctBy(this, keySelector, comparer);
    }

    default TSource elementAt(int index) {
        return ElementAt.elementAt(this, index);
    }

    default TSource elementAtOrDefault(int index) {
        return ElementAt.elementAtOrDefault(this, index);
    }

    default IEnumerable<TSource> except(IEnumerable<TSource> second) {
        return Except.except(this, second);
    }

    default IEnumerable<TSource> except(IEnumerable<TSource> second, IEqualityComparer<TSource> comparer) {
        return Except.except(this, second, comparer);
    }

    default <TKey> IEnumerable<TSource> exceptBy(IEnumerable<TSource> second, Func1<TSource, TKey> keySelector) {
        return ExceptBy.exceptBy(this, second, keySelector);
    }

    default <TKey> IEnumerable<TSource> exceptBy(IEnumerable<TSource> second, Func1<TSource, TKey> keySelector, IEqualityComparer<TKey> comparer) {
        return ExceptBy.exceptBy(this, second, keySelector, comparer);
    }

    default TSource first() {
        return First.first(this);
    }

    default TSource first(Func1<TSource, Boolean> predicate) {
        return First.first(this, predicate);
    }

    default TSource firstOrDefault() {
        return First.firstOrDefault(this);
    }

    default TSource firstOrDefault(Func1<TSource, Boolean> predicate) {
        return First.firstOrDefault(this, predicate);
    }

    default <TKey> IEnumerable<IGrouping<TKey, TSource>> groupBy(Func1<TSource, TKey> keySelector) {
        return Grouping.groupBy(this, keySelector);
    }

    default <TKey> IEnumerable<IGrouping<TKey, TSource>> groupBy(Func1<TSource, TKey> keySelector, IEqualityComparer<TKey> comparer) {
        return Grouping.groupBy(this, keySelector, comparer);
    }

    default <TKey, TElement> IEnumerable<IGrouping<TKey, TElement>> groupBy(Func1<TSource, TKey> keySelector, Func1<TSource, TElement> elementSelector) {
        return Grouping.groupBy(this, keySelector, elementSelector);
    }

    default <TKey, TElement> IEnumerable<IGrouping<TKey, TElement>> groupBy(Func1<TSource, TKey> keySelector, Func1<TSource, TElement> elementSelector, IEqualityComparer<TKey> comparer) {
        return Grouping.groupBy(this, keySelector, elementSelector, comparer);
    }

    default <TKey, TResult> IEnumerable<TResult> groupBy(Func1<TSource, TKey> keySelector, Func2<TKey, IEnumerable<TSource>, TResult> resultSelector) {
        return Grouping.groupBy(this, keySelector, resultSelector);
    }

    default <TKey, TResult> IEnumerable<TResult> groupBy(Func1<TSource, TKey> keySelector, Func2<TKey, IEnumerable<TSource>, TResult> resultSelector, IEqualityComparer<TKey> comparer) {
        return Grouping.groupBy(this, keySelector, resultSelector, comparer);
    }

    default <TKey, TElement, TResult> IEnumerable<TResult> groupBy(Func1<TSource, TKey> keySelector, Func1<TSource, TElement> elementSelector, Func2<TKey, IEnumerable<TElement>, TResult> resultSelector) {
        return Grouping.groupBy(this, keySelector, elementSelector, resultSelector);
    }

    default <TKey, TElement, TResult> IEnumerable<TResult> groupBy(Func1<TSource, TKey> keySelector, Func1<TSource, TElement> elementSelector, Func2<TKey, IEnumerable<TElement>, TResult> resultSelector, IEqualityComparer<TKey> comparer) {
        return Grouping.groupBy(this, keySelector, elementSelector, resultSelector, comparer);
    }

    default <TInner, TKey, TResult> IEnumerable<TResult> groupJoin(IEnumerable<TInner> inner, Func1<TSource, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, Func2<TSource, IEnumerable<TInner>, TResult> resultSelector) {
        return GroupJoin.groupJoin(this, inner, outerKeySelector, innerKeySelector, resultSelector);
    }

    default <TInner, TKey, TResult> IEnumerable<TResult> groupJoin(IEnumerable<TInner> inner, Func1<TSource, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, Func2<TSource, IEnumerable<TInner>, TResult> resultSelector, IEqualityComparer<TKey> comparer) {
        return GroupJoin.groupJoin(this, inner, outerKeySelector, innerKeySelector, resultSelector, comparer);
    }

    default IEnumerable<TSource> intersect(IEnumerable<TSource> second) {
        return Intersect.intersect(this, second);
    }

    default IEnumerable<TSource> intersect(IEnumerable<TSource> second, IEqualityComparer<TSource> comparer) {
        return Intersect.intersect(this, second, comparer);
    }

    default <TKey> IEnumerable<TSource> intersectBy(IEnumerable<TSource> second, Func1<TSource, TKey> keySelector) {
        return IntersectBy.intersectBy(this, second, keySelector);
    }

    default <TKey> IEnumerable<TSource> intersectBy(IEnumerable<TSource> second, Func1<TSource, TKey> keySelector, IEqualityComparer<TKey> comparer) {
        return IntersectBy.intersectBy(this, second, keySelector, comparer);
    }

    default <TInner, TKey, TResult> IEnumerable<TResult> join(IEnumerable<TInner> inner, Func1<TSource, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, Func2<TSource, TInner, TResult> resultSelector) {
        return Join.join(this, inner, outerKeySelector, innerKeySelector, resultSelector);
    }

    default <TInner, TKey, TResult> IEnumerable<TResult> join(IEnumerable<TInner> inner, Func1<TSource, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, Func2<TSource, TInner, TResult> resultSelector, IEqualityComparer<TKey> comparer) {
        return Join.join(this, inner, outerKeySelector, innerKeySelector, resultSelector, comparer);
    }

    default <TInner, TKey, TResult> IEnumerable<TResult> leftJoin(IEnumerable<TInner> inner, Func1<TSource, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, Func2<TSource, TInner, TResult> resultSelector) {
        return Join.leftJoin(this, inner, outerKeySelector, innerKeySelector, resultSelector);
    }

    default <TInner, TKey, TResult> IEnumerable<TResult> leftJoin(IEnumerable<TInner> inner, Func1<TSource, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, TInner defaultInner, Func2<TSource, TInner, TResult> resultSelector) {
        return Join.leftJoin(this, inner, outerKeySelector, innerKeySelector, defaultInner, resultSelector);
    }

    default <TInner, TKey, TResult> IEnumerable<TResult> leftJoin(IEnumerable<TInner> inner, Func1<TSource, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, Func2<TSource, TInner, TResult> resultSelector, IEqualityComparer<TKey> comparer) {
        return Join.leftJoin(this, inner, outerKeySelector, innerKeySelector, resultSelector, comparer);
    }

    default <TInner, TKey, TResult> IEnumerable<TResult> leftJoin(IEnumerable<TInner> inner, Func1<TSource, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, TInner defaultInner, Func2<TSource, TInner, TResult> resultSelector, IEqualityComparer<TKey> comparer) {
        return Join.leftJoin(this, inner, outerKeySelector, innerKeySelector, defaultInner, resultSelector, comparer);
    }

    default <TInner, TKey, TResult> IEnumerable<TResult> rightJoin(IEnumerable<TInner> inner, Func1<TSource, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, Func2<TSource, TInner, TResult> resultSelector) {
        return Join.rightJoin(this, inner, outerKeySelector, innerKeySelector, resultSelector);
    }

    default <TInner, TKey, TResult> IEnumerable<TResult> rightJoin(IEnumerable<TInner> inner, Func1<TSource, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, TSource defaultOuter, Func2<TSource, TInner, TResult> resultSelector) {
        return Join.rightJoin(this, inner, outerKeySelector, innerKeySelector, defaultOuter, resultSelector);
    }

    default <TInner, TKey, TResult> IEnumerable<TResult> rightJoin(IEnumerable<TInner> inner, Func1<TSource, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, Func2<TSource, TInner, TResult> resultSelector, IEqualityComparer<TKey> comparer) {
        return Join.rightJoin(this, inner, outerKeySelector, innerKeySelector, resultSelector, comparer);
    }

    default <TInner, TKey, TResult> IEnumerable<TResult> rightJoin(IEnumerable<TInner> inner, Func1<TSource, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, TSource defaultOuter, Func2<TSource, TInner, TResult> resultSelector, IEqualityComparer<TKey> comparer) {
        return Join.rightJoin(this, inner, outerKeySelector, innerKeySelector, defaultOuter, resultSelector, comparer);
    }

    default <TInner, TKey, TResult> IEnumerable<TResult> fullJoin(IEnumerable<TInner> inner, Func1<TSource, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, Func2<TSource, TInner, TResult> resultSelector) {
        return Join.fullJoin(this, inner, outerKeySelector, innerKeySelector, resultSelector);
    }

    default <TInner, TKey, TResult> IEnumerable<TResult> fullJoin(IEnumerable<TInner> inner, Func1<TSource, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, TSource defaultOuter, TInner defaultInner, Func2<TSource, TInner, TResult> resultSelector) {
        return Join.fullJoin(this, inner, outerKeySelector, innerKeySelector, defaultOuter, defaultInner, resultSelector);
    }

    default <TInner, TKey, TResult> IEnumerable<TResult> fullJoin(IEnumerable<TInner> inner, Func1<TSource, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, Func2<TSource, TInner, TResult> resultSelector, IEqualityComparer<TKey> comparer) {
        return Join.fullJoin(this, inner, outerKeySelector, innerKeySelector, resultSelector, comparer);
    }

    default <TInner, TKey, TResult> IEnumerable<TResult> fullJoin(IEnumerable<TInner> inner, Func1<TSource, TKey> outerKeySelector, Func1<TInner, TKey> innerKeySelector, TSource defaultOuter, TInner defaultInner, Func2<TSource, TInner, TResult> resultSelector, IEqualityComparer<TKey> comparer) {
        return Join.fullJoin(this, inner, outerKeySelector, innerKeySelector, defaultOuter, defaultInner, resultSelector, comparer);
    }

    default <TInner, TResult> IEnumerable<TResult> crossJoin(IEnumerable<TInner> inner, Func2<TSource, TInner, TResult> resultSelector) {
        return Join.crossJoin(this, inner, resultSelector);
    }

    default TSource last() {
        return Last.last(this);
    }

    default TSource last(Func1<TSource, Boolean> predicate) {
        return Last.last(this, predicate);
    }

    default TSource lastOrDefault() {
        return Last.lastOrDefault(this);
    }

    default TSource lastOrDefault(Func1<TSource, Boolean> predicate) {
        return Last.lastOrDefault(this, predicate);
    }

    default <TKey> ILookup<TKey, TSource> toLookup(Func1<TSource, TKey> keySelector) {
        return Lookup.toLookup(this, keySelector);
    }

    default <TKey> ILookup<TKey, TSource> toLookup(Func1<TSource, TKey> keySelector, IEqualityComparer<TKey> comparer) {
        return Lookup.toLookup(this, keySelector, comparer);
    }

    default <TKey, TElement> ILookup<TKey, TElement> toLookup(Func1<TSource, TKey> keySelector, Func1<TSource, TElement> elementSelector) {
        return Lookup.toLookup(this, keySelector, elementSelector);
    }

    default <TKey, TElement> ILookup<TKey, TElement> toLookup(Func1<TSource, TKey> keySelector, Func1<TSource, TElement> elementSelector, IEqualityComparer<TKey> comparer) {
        return Lookup.toLookup(this, keySelector, elementSelector, comparer);
    }

    default int maxInt() {
        return Max.maxInt((IEnumerable<Integer>) this);
    }

    default Integer maxIntNull() {
        return Max.maxIntNull((IEnumerable<Integer>) this);
    }

    default long maxLong() {
        return Max.maxLong((IEnumerable<Long>) this);
    }

    default Long maxLongNull() {
        return Max.maxLongNull((IEnumerable<Long>) this);
    }

    default float maxFloat() {
        return Max.maxFloat((IEnumerable<Float>) this);
    }

    default Float maxFloatNull() {
        return Max.maxFloatNull((IEnumerable<Float>) this);
    }

    default double maxDouble() {
        return Max.maxDouble((IEnumerable<Double>) this);
    }

    default Double maxDoubleNull() {
        return Max.maxDoubleNull((IEnumerable<Double>) this);
    }

    default BigDecimal maxDecimal() {
        return Max.maxDecimal((IEnumerable<BigDecimal>) this);
    }

    default BigDecimal maxDecimalNull() {
        return Max.maxDecimalNull((IEnumerable<BigDecimal>) this);
    }

    default TSource max() {
        return Max.max(this);
    }

    default TSource maxNull() {
        return Max.maxNull(this);
    }

    default int maxInt(Func1<TSource, Integer> selector) {
        return Max.maxInt(this, selector);
    }

    default Integer maxIntNull(Func1<TSource, Integer> selector) {
        return Max.maxIntNull(this, selector);
    }

    default long maxLong(Func1<TSource, Long> selector) {
        return Max.maxLong(this, selector);
    }

    default Long maxLongNull(Func1<TSource, Long> selector) {
        return Max.maxLongNull(this, selector);
    }

    default float maxFloat(Func1<TSource, Float> selector) {
        return Max.maxFloat(this, selector);
    }

    default Float maxFloatNull(Func1<TSource, Float> selector) {
        return Max.maxFloatNull(this, selector);
    }

    default double maxDouble(Func1<TSource, Double> selector) {
        return Max.maxDouble(this, selector);
    }

    default Double maxDoubleNull(Func1<TSource, Double> selector) {
        return Max.maxDoubleNull(this, selector);
    }

    default BigDecimal maxDecimal(Func1<TSource, BigDecimal> selector) {
        return Max.maxDecimal(this, selector);
    }

    default BigDecimal maxDecimalNull(Func1<TSource, BigDecimal> selector) {
        return Max.maxDecimalNull(this, selector);
    }

    default <TResult> TResult max(Func1<TSource, TResult> selector) {
        return Max.max(this, selector);
    }

    default <TResult> TResult maxNull(Func1<TSource, TResult> selector) {
        return Max.maxNull(this, selector);
    }

    default TSource maxByInt(Func1<TSource, Integer> keySelector) {
        return MaxBy.maxByInt(this, keySelector);
    }

    default TSource maxByIntNull(Func1<TSource, Integer> keySelector) {
        return MaxBy.maxByIntNull(this, keySelector);
    }

    default TSource maxByLong(Func1<TSource, Long> keySelector) {
        return MaxBy.maxByLong(this, keySelector);
    }

    default TSource maxByLongNull(Func1<TSource, Long> keySelector) {
        return MaxBy.maxByLongNull(this, keySelector);
    }

    default TSource maxByFloat(Func1<TSource, Float> keySelector) {
        return MaxBy.maxByFloat(this, keySelector);
    }

    default TSource maxByFloatNull(Func1<TSource, Float> keySelector) {
        return MaxBy.maxByFloatNull(this, keySelector);
    }

    default TSource maxByDouble(Func1<TSource, Double> keySelector) {
        return MaxBy.maxByDouble(this, keySelector);
    }

    default TSource maxByDoubleNull(Func1<TSource, Double> keySelector) {
        return MaxBy.maxByDoubleNull(this, keySelector);
    }

    default TSource maxByDecimal(Func1<TSource, BigDecimal> keySelector) {
        return MaxBy.maxByDecimal(this, keySelector);
    }

    default TSource maxByDecimalNull(Func1<TSource, BigDecimal> keySelector) {
        return MaxBy.maxByDecimalNull(this, keySelector);
    }

    default <TKey> TSource maxBy(Func1<TSource, TKey> keySelector) {
        return MaxBy.maxBy(this, keySelector);
    }

    default <TKey> TSource maxByNull(Func1<TSource, TKey> keySelector) {
        return MaxBy.maxByNull(this, keySelector);
    }

    default int minInt() {
        return Min.minInt((IEnumerable<Integer>) this);
    }

    default Integer minIntNull() {
        return Min.minIntNull((IEnumerable<Integer>) this);
    }

    default long minLong() {
        return Min.minLong((IEnumerable<Long>) this);
    }

    default Long minLongNull() {
        return Min.minLongNull((IEnumerable<Long>) this);
    }

    default float minFloat() {
        return Min.minFloat((IEnumerable<Float>) this);
    }

    default Float minFloatNull() {
        return Min.minFloatNull((IEnumerable<Float>) this);
    }

    default double minDouble() {
        return Min.minDouble((IEnumerable<Double>) this);
    }

    default Double minDoubleNull() {
        return Min.minDoubleNull((IEnumerable<Double>) this);
    }

    default BigDecimal minDecimal() {
        return Min.minDecimal((IEnumerable<BigDecimal>) this);
    }

    default BigDecimal minDecimalNull() {
        return Min.minDecimalNull((IEnumerable<BigDecimal>) this);
    }

    default TSource min() {
        return Min.min(this);
    }

    default TSource minNull() {
        return Min.minNull(this);
    }

    default int minInt(Func1<TSource, Integer> selector) {
        return Min.minInt(this, selector);
    }

    default Integer minIntNull(Func1<TSource, Integer> selector) {
        return Min.minIntNull(this, selector);
    }

    default long minLong(Func1<TSource, Long> selector) {
        return Min.minLong(this, selector);
    }

    default Long minLongNull(Func1<TSource, Long> selector) {
        return Min.minLongNull(this, selector);
    }

    default float minFloat(Func1<TSource, Float> selector) {
        return Min.minFloat(this, selector);
    }

    default Float minFloatNull(Func1<TSource, Float> selector) {
        return Min.minFloatNull(this, selector);
    }

    default double minDouble(Func1<TSource, Double> selector) {
        return Min.minDouble(this, selector);
    }

    default Double minDoubleNull(Func1<TSource, Double> selector) {
        return Min.minDoubleNull(this, selector);
    }

    default BigDecimal minDecimal(Func1<TSource, BigDecimal> selector) {
        return Min.minDecimal(this, selector);
    }

    default BigDecimal minDecimalNull(Func1<TSource, BigDecimal> selector) {
        return Min.minDecimalNull(this, selector);
    }

    default <TResult> TResult min(Func1<TSource, TResult> selector) {
        return Min.min(this, selector);
    }

    default <TResult> TResult minNull(Func1<TSource, TResult> selector) {
        return Min.minNull(this, selector);
    }

    default TSource minByInt(Func1<TSource, Integer> keySelector) {
        return MinBy.minByInt(this, keySelector);
    }

    default TSource minByIntNull(Func1<TSource, Integer> keySelector) {
        return MinBy.minByIntNull(this, keySelector);
    }

    default TSource minByLong(Func1<TSource, Long> keySelector) {
        return MinBy.minByLong(this, keySelector);
    }

    default TSource minByLongNull(Func1<TSource, Long> keySelector) {
        return MinBy.minByLongNull(this, keySelector);
    }

    default TSource minByFloat(Func1<TSource, Float> keySelector) {
        return MinBy.minByFloat(this, keySelector);
    }

    default TSource minByFloatNull(Func1<TSource, Float> keySelector) {
        return MinBy.minByFloatNull(this, keySelector);
    }

    default TSource minByDouble(Func1<TSource, Double> keySelector) {
        return MinBy.minByDouble(this, keySelector);
    }

    default TSource minByDoubleNull(Func1<TSource, Double> keySelector) {
        return MinBy.minByDoubleNull(this, keySelector);
    }

    default TSource minByDecimal(Func1<TSource, BigDecimal> keySelector) {
        return MinBy.minByDecimal(this, keySelector);
    }

    default TSource minByDecimalNull(Func1<TSource, BigDecimal> keySelector) {
        return MinBy.minByDecimalNull(this, keySelector);
    }

    default <TKey> TSource minBy(Func1<TSource, TKey> keySelector) {
        return MinBy.minBy(this, keySelector);
    }

    default <TKey> TSource minByNull(Func1<TSource, TKey> keySelector) {
        return MinBy.minByNull(this, keySelector);
    }

    default <TKey> IOrderedEnumerable<TSource> orderBy(Func1<TSource, TKey> keySelector) {
        return OrderBy.orderBy(this, keySelector);
    }

    default <TKey> IOrderedEnumerable<TSource> orderBy(Func1<TSource, TKey> keySelector, Comparator<TKey> comparer) {
        return OrderBy.orderBy(this, keySelector, comparer);
    }

    default <TKey> IOrderedEnumerable<TSource> orderByDescending(Func1<TSource, TKey> keySelector) {
        return OrderBy.orderByDescending(this, keySelector);
    }

    default <TKey> IOrderedEnumerable<TSource> orderByDescending(Func1<TSource, TKey> keySelector, Comparator<TKey> comparer) {
        return OrderBy.orderByDescending(this, keySelector, comparer);
    }

    default IEnumerable<TSource> reverse() {
        return Reverse.reverse(this);
    }

    default <TResult> IEnumerable<TResult> select(Func1<TSource, TResult> selector) {
        return Select.select(this, selector);
    }

    default <TResult> IEnumerable<TResult> select(Func2<TSource, Integer, TResult> selector) {
        return Select.select(this, selector);
    }

    default <TResult> IEnumerable<TResult> selectMany(Func1<TSource, IEnumerable<TResult>> selector) {
        return SelectMany.selectMany(this, selector);
    }

    default <TResult> IEnumerable<TResult> selectMany(Func2<TSource, Integer, IEnumerable<TResult>> selector) {
        return SelectMany.selectMany(this, selector);
    }

    default <TCollection, TResult> IEnumerable<TResult> selectMany(Func1<TSource, IEnumerable<TCollection>> collectionSelector, Func2<TSource, TCollection, TResult> resultSelector) {
        return SelectMany.selectMany(this, collectionSelector, resultSelector);
    }

    default <TCollection, TResult> IEnumerable<TResult> selectMany(Func2<TSource, Integer, IEnumerable<TCollection>> collectionSelector, Func2<TSource, TCollection, TResult> resultSelector) {
        return SelectMany.selectMany(this, collectionSelector, resultSelector);
    }

    default boolean sequenceEqual(IEnumerable<TSource> second) {
        return SequenceEqual.sequenceEqual(this, second);
    }

    default boolean sequenceEqual(IEnumerable<TSource> second, IEqualityComparer<TSource> comparer) {
        return SequenceEqual.sequenceEqual(this, second, comparer);
    }

    default TSource single() {
        return Single.single(this);
    }

    default TSource single(Func1<TSource, Boolean> predicate) {
        return Single.single(this, predicate);
    }

    default TSource singleOrDefault() {
        return Single.singleOrDefault(this);
    }

    default TSource singleOrDefault(Func1<TSource, Boolean> predicate) {
        return Single.singleOrDefault(this, predicate);
    }

    default IEnumerable<TSource> skip(int count) {
        return Skip.skip(this, count);
    }

    default IEnumerable<TSource> skipWhile(Func1<TSource, Boolean> predicate) {
        return Skip.skipWhile(this, predicate);
    }

    default IEnumerable<TSource> skipWhile(Func2<TSource, Integer, Boolean> predicate) {
        return Skip.skipWhile(this, predicate);
    }

    default IEnumerable<TSource> skipLast(int count) {
        return Skip.skipLast(this, count);
    }

    default int sumInt() {
        return Sum.sumInt((IEnumerable<Integer>) this);
    }

    default long sumLong() {
        return Sum.sumLong((IEnumerable<Long>) this);
    }

    default float sumFloat() {
        return Sum.sumFloat((IEnumerable<Float>) this);
    }

    default double sumDouble() {
        return Sum.sumDouble((IEnumerable<Double>) this);
    }

    default BigDecimal sumDecimal() {
        return Sum.sumDecimal((IEnumerable<BigDecimal>) this);
    }

    default int sumInt(Func1<TSource, Integer> selector) {
        return Sum.sumInt(this, selector);
    }

    default long sumLong(Func1<TSource, Long> selector) {
        return Sum.sumLong(this, selector);
    }

    default float sumFloat(Func1<TSource, Float> selector) {
        return Sum.sumFloat(this, selector);
    }

    default double sumDouble(Func1<TSource, Double> selector) {
        return Sum.sumDouble(this, selector);
    }

    default BigDecimal sumDecimal(Func1<TSource, BigDecimal> selector) {
        return Sum.sumDecimal(this, selector);
    }

    default IEnumerable<TSource> take(int count) {
        return Take.take(this, count);
    }

    default IEnumerable<TSource> takeWhile(Func1<TSource, Boolean> predicate) {
        return Take.takeWhile(this, predicate);
    }

    default IEnumerable<TSource> takeWhile(Func2<TSource, Integer, Boolean> predicate) {
        return Take.takeWhile(this, predicate);
    }

    default IEnumerable<TSource> takeLast(int count) {
        return Take.takeLast(this, count);
    }

    default TSource[] toArray(Class<TSource> clazz) {
        return ToCollection.toArray(this, clazz);
    }

    default Array<TSource> toArray() {
        return ToCollection.toArray(this);
    }

    default List<TSource> toList() {
        return ToCollection.toList(this);
    }

    default <TKey> Map<TKey, TSource> toMap(Func1<TSource, TKey> keySelector) {
        return ToCollection.toMap(this, keySelector);
    }

    default <TKey, TElement> Map<TKey, TElement> toMap(Func1<TSource, TKey> keySelector, Func1<TSource, TElement> elementSelector) {
        return ToCollection.toMap(this, keySelector, elementSelector);
    }

    default Set<TSource> toSet() {
        return ToCollection.toSet(this);
    }

    default IEnumerable<TSource> union(IEnumerable<TSource> second) {
        return Union.union(this, second);
    }

    default IEnumerable<TSource> union(IEnumerable<TSource> second, IEqualityComparer<TSource> comparer) {
        return Union.union(this, second, comparer);
    }

    default <TKey> IEnumerable<TSource> unionBy(IEnumerable<TSource> second, Func1<TSource, TKey> keySelector) {
        return UnionBy.unionBy(this, second, keySelector);
    }

    default <TKey> IEnumerable<TSource> unionBy(IEnumerable<TSource> second, Func1<TSource, TKey> keySelector, IEqualityComparer<TKey> comparer) {
        return UnionBy.unionBy(this, second, keySelector, comparer);
    }

    default IEnumerable<TSource> where(Func1<TSource, Boolean> predicate) {
        return Where.where(this, predicate);
    }

    default IEnumerable<TSource> where(Func2<TSource, Integer, Boolean> predicate) {
        return Where.where(this, predicate);
    }

    default <TSecond, TResult> IEnumerable<TResult> zip(IEnumerable<TSecond> second, Func2<TSource, TSecond, TResult> resultSelector) {
        return Zip.zip(this, second, resultSelector);
    }
}
