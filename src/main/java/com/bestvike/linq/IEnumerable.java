package com.bestvike.linq;

import com.bestvike.collections.generic.Array;
import com.bestvike.collections.generic.IEqualityComparer;
import com.bestvike.function.DecimalFunc1;
import com.bestvike.function.DoubleFunc1;
import com.bestvike.function.FloatFunc1;
import com.bestvike.function.Func1;
import com.bestvike.function.Func2;
import com.bestvike.function.IndexFunc2;
import com.bestvike.function.IndexPredicate2;
import com.bestvike.function.IntFunc1;
import com.bestvike.function.LongFunc1;
import com.bestvike.function.NullableDecimalFunc1;
import com.bestvike.function.NullableDoubleFunc1;
import com.bestvike.function.NullableFloatFunc1;
import com.bestvike.function.NullableIntFunc1;
import com.bestvike.function.NullableLongFunc1;
import com.bestvike.function.Predicate1;
import com.bestvike.linq.enumerable.Aggregate;
import com.bestvike.linq.enumerable.AnyAll;
import com.bestvike.linq.enumerable.AppendPrepend;
import com.bestvike.linq.enumerable.Average;
import com.bestvike.linq.enumerable.Cast;
import com.bestvike.linq.enumerable.Concat;
import com.bestvike.linq.enumerable.Contains;
import com.bestvike.linq.enumerable.Count;
import com.bestvike.linq.enumerable.DefaultIfEmpty;
import com.bestvike.linq.enumerable.Distinct;
import com.bestvike.linq.enumerable.DistinctBy;
import com.bestvike.linq.enumerable.ElementAt;
import com.bestvike.linq.enumerable.Except;
import com.bestvike.linq.enumerable.ExceptBy;
import com.bestvike.linq.enumerable.FindIndex;
import com.bestvike.linq.enumerable.First;
import com.bestvike.linq.enumerable.Format;
import com.bestvike.linq.enumerable.GroupBy;
import com.bestvike.linq.enumerable.GroupJoin;
import com.bestvike.linq.enumerable.IndexOf;
import com.bestvike.linq.enumerable.Intersect;
import com.bestvike.linq.enumerable.IntersectBy;
import com.bestvike.linq.enumerable.Join;
import com.bestvike.linq.enumerable.Last;
import com.bestvike.linq.enumerable.Max;
import com.bestvike.linq.enumerable.MaxBy;
import com.bestvike.linq.enumerable.Min;
import com.bestvike.linq.enumerable.MinBy;
import com.bestvike.linq.enumerable.OrderBy;
import com.bestvike.linq.enumerable.Reverse;
import com.bestvike.linq.enumerable.RunOnce;
import com.bestvike.linq.enumerable.Select;
import com.bestvike.linq.enumerable.SelectMany;
import com.bestvike.linq.enumerable.SequenceEqual;
import com.bestvike.linq.enumerable.Shuffle;
import com.bestvike.linq.enumerable.Single;
import com.bestvike.linq.enumerable.Skip;
import com.bestvike.linq.enumerable.Sum;
import com.bestvike.linq.enumerable.Take;
import com.bestvike.linq.enumerable.ToCollection;
import com.bestvike.linq.enumerable.ToEnumeration;
import com.bestvike.linq.enumerable.ToLookup;
import com.bestvike.linq.enumerable.ToSpliterator;
import com.bestvike.linq.enumerable.Union;
import com.bestvike.linq.enumerable.UnionBy;
import com.bestvike.linq.enumerable.Where;
import com.bestvike.linq.enumerable.Zip;
import com.bestvike.linq.exception.ExceptionArgument;
import com.bestvike.linq.exception.ThrowHelper;
import com.bestvike.linq.util.ArrayUtils;
import com.bestvike.linq.util.Formatter;
import com.bestvike.tuple.Tuple2;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Created by 许崇雷 on 2017-07-10.
 */
@SuppressWarnings("unchecked")
public interface IEnumerable<TSource> extends Iterable<TSource> {
    IEnumerator<TSource> enumerator();

    default Iterator<TSource> iterator() {
        return this.enumerator();
    }

    default void forEach(Consumer<? super TSource> action) {
        if (action == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.action);
        try (IEnumerator<TSource> enumerator = this.enumerator()) {
            while (enumerator.moveNext())
                action.accept(enumerator.current());
        }
    }

    default Spliterator<TSource> spliterator() {
        return ToSpliterator.spliterator(this);
    }

    default Stream<TSource> stream() {
        return StreamSupport.stream(this.spliterator(), false);
    }

    default Stream<TSource> stream(boolean parallel) {
        return StreamSupport.stream(this.spliterator(), parallel);
    }

    default Stream<TSource> parallelStream() {
        return StreamSupport.stream(this.spliterator(), true);
    }

    default TSource aggregate(Func2<? super TSource, ? super TSource, ? extends TSource> func) {
        return Aggregate.aggregate(this, (Func2<TSource, TSource, TSource>) func);
    }

    default <TAccumulate> TAccumulate aggregate(TAccumulate seed, Func2<? super TAccumulate, ? super TSource, ? extends TAccumulate> func) {
        return Aggregate.aggregate(this, seed, (Func2<TAccumulate, TSource, TAccumulate>) func);
    }

    default <TAccumulate, TResult> TResult aggregate(TAccumulate seed, Func2<? super TAccumulate, ? super TSource, ? extends TAccumulate> func, Func1<? super TAccumulate, ? extends TResult> resultSelector) {
        return Aggregate.aggregate(this, seed, (Func2<TAccumulate, TSource, TAccumulate>) func, (Func1<TAccumulate, TResult>) resultSelector);
    }

    default boolean all(Predicate1<? super TSource> predicate) {
        return AnyAll.all(this, (Predicate1<TSource>) predicate);
    }

    default boolean any() {
        return AnyAll.any(this);
    }

    default boolean any(Predicate1<? super TSource> predicate) {
        return AnyAll.any(this, (Predicate1<TSource>) predicate);
    }

    default IEnumerable<TSource> append(TSource element) {
        return AppendPrepend.append(this, element);
    }

    default IEnumerable<TSource> asEnumerable() {
        return this;
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

    default double averageInt(IntFunc1<? super TSource> selector) {
        return Average.averageInt(this, (IntFunc1<TSource>) selector);
    }

    default Double averageIntNull(NullableIntFunc1<? super TSource> selector) {
        return Average.averageIntNull(this, (NullableIntFunc1<TSource>) selector);
    }

    default double averageLong(LongFunc1<? super TSource> selector) {
        return Average.averageLong(this, (LongFunc1<TSource>) selector);
    }

    default Double averageLongNull(NullableLongFunc1<? super TSource> selector) {
        return Average.averageLongNull(this, (NullableLongFunc1<TSource>) selector);
    }

    default float averageFloat(FloatFunc1<? super TSource> selector) {
        return Average.averageFloat(this, (FloatFunc1<TSource>) selector);
    }

    default Float averageFloatNull(NullableFloatFunc1<? super TSource> selector) {
        return Average.averageFloatNull(this, (NullableFloatFunc1<TSource>) selector);
    }

    default double averageDouble(DoubleFunc1<? super TSource> selector) {
        return Average.averageDouble(this, (DoubleFunc1<TSource>) selector);
    }

    default Double averageDoubleNull(NullableDoubleFunc1<? super TSource> selector) {
        return Average.averageDoubleNull(this, (NullableDoubleFunc1<TSource>) selector);
    }

    default BigDecimal averageDecimal(DecimalFunc1<? super TSource> selector) {
        return Average.averageDecimal(this, (DecimalFunc1<TSource>) selector);
    }

    default BigDecimal averageDecimalNull(NullableDecimalFunc1<? super TSource> selector) {
        return Average.averageDecimalNull(this, (NullableDecimalFunc1<TSource>) selector);
    }

    default <TResult> IEnumerable<TResult> cast(Class<TResult> clazz) {
        return Cast.cast(this, clazz);
    }

    default IEnumerable<TSource> concat(IEnumerable<? extends TSource> second) {
        return Concat.concat(this, (IEnumerable<TSource>) second);
    }

    default boolean contains(TSource value) {
        return Contains.contains(this, value);
    }

    default boolean contains(TSource value, IEqualityComparer<? super TSource> comparer) {
        return Contains.contains(this, value, (IEqualityComparer<TSource>) comparer);
    }

    default int count() {
        return Count.count(this);
    }

    default int count(Predicate1<? super TSource> predicate) {
        return Count.count(this, (Predicate1<TSource>) predicate);
    }

    default <TInner, TResult> IEnumerable<TResult> crossJoin(IEnumerable<? extends TInner> inner, Func2<? super TSource, ? super TInner, ? extends TResult> resultSelector) {
        return Join.crossJoin(this, (IEnumerable<TInner>) inner, (Func2<TSource, TInner, TResult>) resultSelector);
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

    default IEnumerable<TSource> distinct(IEqualityComparer<? super TSource> comparer) {
        return Distinct.distinct(this, (IEqualityComparer<TSource>) comparer);
    }

    default <TKey> IEnumerable<TSource> distinctBy(Func1<? super TSource, ? extends TKey> keySelector) {
        return DistinctBy.distinctBy(this, (Func1<TSource, TKey>) keySelector);
    }

    default <TKey> IEnumerable<TSource> distinctBy(Func1<? super TSource, ? extends TKey> keySelector, IEqualityComparer<? super TKey> comparer) {
        return DistinctBy.distinctBy(this, (Func1<TSource, TKey>) keySelector, (IEqualityComparer<TKey>) comparer);
    }

    default TSource elementAt(int index) {
        return ElementAt.elementAt(this, index);
    }

    default TSource elementAtOrDefault(int index) {
        return ElementAt.elementAtOrDefault(this, index);
    }

    default IEnumerable<TSource> except(IEnumerable<? extends TSource> second) {
        return Except.except(this, (IEnumerable<TSource>) second);
    }

    default IEnumerable<TSource> except(IEnumerable<? extends TSource> second, IEqualityComparer<? super TSource> comparer) {
        return Except.except(this, (IEnumerable<TSource>) second, (IEqualityComparer<TSource>) comparer);
    }

    default <TKey> IEnumerable<TSource> exceptBy(IEnumerable<? extends TSource> second, Func1<? super TSource, ? extends TKey> keySelector) {
        return ExceptBy.exceptBy(this, (IEnumerable<TSource>) second, (Func1<TSource, TKey>) keySelector);
    }

    default <TKey> IEnumerable<TSource> exceptBy(IEnumerable<? extends TSource> second, Func1<? super TSource, ? extends TKey> keySelector, IEqualityComparer<? super TKey> comparer) {
        return ExceptBy.exceptBy(this, (IEnumerable<TSource>) second, (Func1<TSource, TKey>) keySelector, (IEqualityComparer<TKey>) comparer);
    }

    default int findIndex(Predicate1<? super TSource> predicate) {
        return FindIndex.findIndex(this, (Predicate1<TSource>) predicate);
    }

    default int findLastIndex(Predicate1<? super TSource> predicate) {
        return FindIndex.findLastIndex(this, (Predicate1<TSource>) predicate);
    }

    default TSource first() {
        return First.first(this);
    }

    default TSource first(Predicate1<? super TSource> predicate) {
        return First.first(this, (Predicate1<TSource>) predicate);
    }

    default TSource firstOrDefault() {
        return First.firstOrDefault(this);
    }

    default TSource firstOrDefault(Predicate1<? super TSource> predicate) {
        return First.firstOrDefault(this, (Predicate1<TSource>) predicate);
    }

    default String format() {
        return Format.format(this);
    }

    default String format(Formatter formatter) {
        return Format.format(this, formatter);
    }

    default <TInner, TKey, TResult> IEnumerable<TResult> fullJoin(IEnumerable<? extends TInner> inner, Func1<? super TSource, ? extends TKey> outerKeySelector, Func1<? super TInner, ? extends TKey> innerKeySelector, Func2<? super TSource, ? super TInner, ? extends TResult> resultSelector) {
        return Join.fullJoin(this, (IEnumerable<TInner>) inner, (Func1<TSource, TKey>) outerKeySelector, (Func1<TInner, TKey>) innerKeySelector, (Func2<TSource, TInner, TResult>) resultSelector);
    }

    default <TInner, TKey, TResult> IEnumerable<TResult> fullJoin(IEnumerable<? extends TInner> inner, Func1<? super TSource, ? extends TKey> outerKeySelector, Func1<? super TInner, ? extends TKey> innerKeySelector, TSource defaultOuter, TInner defaultInner, Func2<? super TSource, ? super TInner, ? extends TResult> resultSelector) {
        return Join.fullJoin(this, (IEnumerable<TInner>) inner, (Func1<TSource, TKey>) outerKeySelector, (Func1<TInner, TKey>) innerKeySelector, defaultOuter, defaultInner, (Func2<TSource, TInner, TResult>) resultSelector);
    }

    default <TInner, TKey, TResult> IEnumerable<TResult> fullJoin(IEnumerable<? extends TInner> inner, Func1<? super TSource, ? extends TKey> outerKeySelector, Func1<? super TInner, ? extends TKey> innerKeySelector, Func2<? super TSource, ? super TInner, ? extends TResult> resultSelector, IEqualityComparer<? super TKey> comparer) {
        return Join.fullJoin(this, (IEnumerable<TInner>) inner, (Func1<TSource, TKey>) outerKeySelector, (Func1<TInner, TKey>) innerKeySelector, (Func2<TSource, TInner, TResult>) resultSelector, (IEqualityComparer<TKey>) comparer);
    }

    default <TInner, TKey, TResult> IEnumerable<TResult> fullJoin(IEnumerable<? extends TInner> inner, Func1<? super TSource, ? extends TKey> outerKeySelector, Func1<? super TInner, ? extends TKey> innerKeySelector, TSource defaultOuter, TInner defaultInner, Func2<? super TSource, ? super TInner, ? extends TResult> resultSelector, IEqualityComparer<? super TKey> comparer) {
        return Join.fullJoin(this, (IEnumerable<TInner>) inner, (Func1<TSource, TKey>) outerKeySelector, (Func1<TInner, TKey>) innerKeySelector, defaultOuter, defaultInner, (Func2<TSource, TInner, TResult>) resultSelector, (IEqualityComparer<TKey>) comparer);
    }

    default <TKey> IEnumerable<IGrouping<TKey, TSource>> groupBy(Func1<? super TSource, ? extends TKey> keySelector) {
        return GroupBy.groupBy(this, (Func1<TSource, TKey>) keySelector);
    }

    default <TKey> IEnumerable<IGrouping<TKey, TSource>> groupBy(Func1<? super TSource, ? extends TKey> keySelector, IEqualityComparer<? super TKey> comparer) {
        return GroupBy.groupBy(this, (Func1<TSource, TKey>) keySelector, (IEqualityComparer<TKey>) comparer);
    }

    default <TKey, TElement> IEnumerable<IGrouping<TKey, TElement>> groupBy(Func1<? super TSource, ? extends TKey> keySelector, Func1<? super TSource, ? extends TElement> elementSelector) {
        return GroupBy.groupBy(this, (Func1<TSource, TKey>) keySelector, (Func1<TSource, TElement>) elementSelector);
    }

    default <TKey, TElement> IEnumerable<IGrouping<TKey, TElement>> groupBy(Func1<? super TSource, ? extends TKey> keySelector, Func1<? super TSource, ? extends TElement> elementSelector, IEqualityComparer<? super TKey> comparer) {
        return GroupBy.groupBy(this, (Func1<TSource, TKey>) keySelector, (Func1<TSource, TElement>) elementSelector, (IEqualityComparer<TKey>) comparer);
    }

    default <TKey, TResult> IEnumerable<TResult> groupBy(Func1<? super TSource, ? extends TKey> keySelector, Func2<? super TKey, ? super IEnumerable<TSource>, ? extends TResult> resultSelector) {
        return GroupBy.groupBy(this, (Func1<TSource, TKey>) keySelector, (Func2<TKey, IEnumerable<TSource>, TResult>) resultSelector);
    }

    default <TKey, TResult> IEnumerable<TResult> groupBy(Func1<? super TSource, ? extends TKey> keySelector, Func2<? super TKey, ? super IEnumerable<TSource>, ? extends TResult> resultSelector, IEqualityComparer<? super TKey> comparer) {
        return GroupBy.groupBy(this, (Func1<TSource, TKey>) keySelector, (Func2<TKey, IEnumerable<TSource>, TResult>) resultSelector, (IEqualityComparer<TKey>) comparer);
    }

    default <TKey, TElement, TResult> IEnumerable<TResult> groupBy(Func1<? super TSource, ? extends TKey> keySelector, Func1<? super TSource, ? extends TElement> elementSelector, Func2<? super TKey, ? super IEnumerable<TElement>, ? extends TResult> resultSelector) {
        return GroupBy.groupBy(this, (Func1<TSource, TKey>) keySelector, (Func1<TSource, TElement>) elementSelector, (Func2<TKey, IEnumerable<TElement>, TResult>) resultSelector);
    }

    default <TKey, TElement, TResult> IEnumerable<TResult> groupBy(Func1<? super TSource, ? extends TKey> keySelector, Func1<? super TSource, ? extends TElement> elementSelector, Func2<? super TKey, ? super IEnumerable<TElement>, ? extends TResult> resultSelector, IEqualityComparer<? super TKey> comparer) {
        return GroupBy.groupBy(this, (Func1<TSource, TKey>) keySelector, (Func1<TSource, TElement>) elementSelector, (Func2<TKey, IEnumerable<TElement>, TResult>) resultSelector, (IEqualityComparer<TKey>) comparer);
    }

    default <TInner, TKey, TResult> IEnumerable<TResult> groupJoin(IEnumerable<? extends TInner> inner, Func1<? super TSource, ? extends TKey> outerKeySelector, Func1<? super TInner, ? extends TKey> innerKeySelector, Func2<? super TSource, ? super IEnumerable<TInner>, ? extends TResult> resultSelector) {
        return GroupJoin.groupJoin(this, (IEnumerable<TInner>) inner, (Func1<TSource, TKey>) outerKeySelector, (Func1<TInner, TKey>) innerKeySelector, (Func2<TSource, IEnumerable<TInner>, TResult>) resultSelector);
    }

    default <TInner, TKey, TResult> IEnumerable<TResult> groupJoin(IEnumerable<? extends TInner> inner, Func1<? super TSource, ? extends TKey> outerKeySelector, Func1<? super TInner, ? extends TKey> innerKeySelector, Func2<? super TSource, ? super IEnumerable<TInner>, ? extends TResult> resultSelector, IEqualityComparer<? super TKey> comparer) {
        return GroupJoin.groupJoin(this, (IEnumerable<TInner>) inner, (Func1<TSource, TKey>) outerKeySelector, (Func1<TInner, TKey>) innerKeySelector, (Func2<TSource, IEnumerable<TInner>, TResult>) resultSelector, (IEqualityComparer<TKey>) comparer);
    }

    default int indexOf(TSource value) {
        return IndexOf.indexOf(this, value);
    }

    default int indexOf(TSource value, IEqualityComparer<? super TSource> comparer) {
        return IndexOf.indexOf(this, value, (IEqualityComparer<TSource>) comparer);
    }

    default IEnumerable<TSource> intersect(IEnumerable<? extends TSource> second) {
        return Intersect.intersect(this, (IEnumerable<TSource>) second);
    }

    default IEnumerable<TSource> intersect(IEnumerable<? extends TSource> second, IEqualityComparer<? super TSource> comparer) {
        return Intersect.intersect(this, (IEnumerable<TSource>) second, (IEqualityComparer<TSource>) comparer);
    }

    default <TKey> IEnumerable<TSource> intersectBy(IEnumerable<? extends TSource> second, Func1<? super TSource, ? extends TKey> keySelector) {
        return IntersectBy.intersectBy(this, (IEnumerable<TSource>) second, (Func1<TSource, TKey>) keySelector);
    }

    default <TKey> IEnumerable<TSource> intersectBy(IEnumerable<? extends TSource> second, Func1<? super TSource, ? extends TKey> keySelector, IEqualityComparer<? super TKey> comparer) {
        return IntersectBy.intersectBy(this, (IEnumerable<TSource>) second, (Func1<TSource, TKey>) keySelector, (IEqualityComparer<TKey>) comparer);
    }

    default <TInner, TKey, TResult> IEnumerable<TResult> join(IEnumerable<? extends TInner> inner, Func1<? super TSource, ? extends TKey> outerKeySelector, Func1<? super TInner, ? extends TKey> innerKeySelector, Func2<? super TSource, ? super TInner, ? extends TResult> resultSelector) {
        return Join.join(this, (IEnumerable<TInner>) inner, (Func1<TSource, TKey>) outerKeySelector, (Func1<TInner, TKey>) innerKeySelector, (Func2<TSource, TInner, TResult>) resultSelector);
    }

    default <TInner, TKey, TResult> IEnumerable<TResult> join(IEnumerable<? extends TInner> inner, Func1<? super TSource, ? extends TKey> outerKeySelector, Func1<? super TInner, ? extends TKey> innerKeySelector, Func2<? super TSource, ? super TInner, ? extends TResult> resultSelector, IEqualityComparer<? super TKey> comparer) {
        return Join.join(this, (IEnumerable<TInner>) inner, (Func1<TSource, TKey>) outerKeySelector, (Func1<TInner, TKey>) innerKeySelector, (Func2<TSource, TInner, TResult>) resultSelector, (IEqualityComparer<TKey>) comparer);
    }

    default TSource last() {
        return Last.last(this);
    }

    default TSource last(Predicate1<? super TSource> predicate) {
        return Last.last(this, (Predicate1<TSource>) predicate);
    }

    default int lastIndexOf(TSource value) {
        return IndexOf.lastIndexOf(this, value);
    }

    default int lastIndexOf(TSource value, IEqualityComparer<? super TSource> comparer) {
        return IndexOf.lastIndexOf(this, value, (IEqualityComparer<TSource>) comparer);
    }

    default TSource lastOrDefault() {
        return Last.lastOrDefault(this);
    }

    default TSource lastOrDefault(Predicate1<? super TSource> predicate) {
        return Last.lastOrDefault(this, (Predicate1<TSource>) predicate);
    }

    default <TInner, TKey, TResult> IEnumerable<TResult> leftJoin(IEnumerable<? extends TInner> inner, Func1<? super TSource, ? extends TKey> outerKeySelector, Func1<? super TInner, ? extends TKey> innerKeySelector, Func2<? super TSource, ? super TInner, ? extends TResult> resultSelector) {
        return Join.leftJoin(this, (IEnumerable<TInner>) inner, (Func1<TSource, TKey>) outerKeySelector, (Func1<TInner, TKey>) innerKeySelector, (Func2<TSource, TInner, TResult>) resultSelector);
    }

    default <TInner, TKey, TResult> IEnumerable<TResult> leftJoin(IEnumerable<? extends TInner> inner, Func1<? super TSource, ? extends TKey> outerKeySelector, Func1<? super TInner, ? extends TKey> innerKeySelector, TInner defaultInner, Func2<? super TSource, ? super TInner, ? extends TResult> resultSelector) {
        return Join.leftJoin(this, (IEnumerable<TInner>) inner, (Func1<TSource, TKey>) outerKeySelector, (Func1<TInner, TKey>) innerKeySelector, defaultInner, (Func2<TSource, TInner, TResult>) resultSelector);
    }

    default <TInner, TKey, TResult> IEnumerable<TResult> leftJoin(IEnumerable<? extends TInner> inner, Func1<? super TSource, ? extends TKey> outerKeySelector, Func1<? super TInner, ? extends TKey> innerKeySelector, Func2<? super TSource, ? super TInner, ? extends TResult> resultSelector, IEqualityComparer<? super TKey> comparer) {
        return Join.leftJoin(this, (IEnumerable<TInner>) inner, (Func1<TSource, TKey>) outerKeySelector, (Func1<TInner, TKey>) innerKeySelector, (Func2<TSource, TInner, TResult>) resultSelector, (IEqualityComparer<TKey>) comparer);
    }

    default <TInner, TKey, TResult> IEnumerable<TResult> leftJoin(IEnumerable<? extends TInner> inner, Func1<? super TSource, ? extends TKey> outerKeySelector, Func1<? super TInner, ? extends TKey> innerKeySelector, TInner defaultInner, Func2<? super TSource, ? super TInner, ? extends TResult> resultSelector, IEqualityComparer<? super TKey> comparer) {
        return Join.leftJoin(this, (IEnumerable<TInner>) inner, (Func1<TSource, TKey>) outerKeySelector, (Func1<TInner, TKey>) innerKeySelector, defaultInner, (Func2<TSource, TInner, TResult>) resultSelector, (IEqualityComparer<TKey>) comparer);
    }

    default long longCount() {
        return Count.longCount(this);
    }

    default long longCount(Predicate1<? super TSource> predicate) {
        return Count.longCount(this, (Predicate1<TSource>) predicate);
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

    default int maxInt(IntFunc1<? super TSource> selector) {
        return Max.maxInt(this, (IntFunc1<TSource>) selector);
    }

    default Integer maxIntNull(NullableIntFunc1<? super TSource> selector) {
        return Max.maxIntNull(this, (NullableIntFunc1<TSource>) selector);
    }

    default long maxLong(LongFunc1<? super TSource> selector) {
        return Max.maxLong(this, (LongFunc1<TSource>) selector);
    }

    default Long maxLongNull(NullableLongFunc1<? super TSource> selector) {
        return Max.maxLongNull(this, (NullableLongFunc1<TSource>) selector);
    }

    default float maxFloat(FloatFunc1<? super TSource> selector) {
        return Max.maxFloat(this, (FloatFunc1<TSource>) selector);
    }

    default Float maxFloatNull(NullableFloatFunc1<? super TSource> selector) {
        return Max.maxFloatNull(this, (NullableFloatFunc1<TSource>) selector);
    }

    default double maxDouble(DoubleFunc1<? super TSource> selector) {
        return Max.maxDouble(this, (DoubleFunc1<TSource>) selector);
    }

    default Double maxDoubleNull(NullableDoubleFunc1<? super TSource> selector) {
        return Max.maxDoubleNull(this, (NullableDoubleFunc1<TSource>) selector);
    }

    default BigDecimal maxDecimal(DecimalFunc1<? super TSource> selector) {
        return Max.maxDecimal(this, (DecimalFunc1<TSource>) selector);
    }

    default BigDecimal maxDecimalNull(NullableDecimalFunc1<? super TSource> selector) {
        return Max.maxDecimalNull(this, (NullableDecimalFunc1<TSource>) selector);
    }

    default <TResult> TResult max(Func1<? super TSource, ? extends TResult> selector) {
        return Max.max(this, (Func1<TSource, TResult>) selector);
    }

    default <TResult> TResult maxNull(Func1<? super TSource, ? extends TResult> selector) {
        return Max.maxNull(this, (Func1<TSource, TResult>) selector);
    }

    default TSource maxByInt(IntFunc1<? super TSource> keySelector) {
        return MaxBy.maxByInt(this, (IntFunc1<TSource>) keySelector);
    }

    default TSource maxByIntNull(NullableIntFunc1<? super TSource> keySelector) {
        return MaxBy.maxByIntNull(this, (NullableIntFunc1<TSource>) keySelector);
    }

    default TSource maxByLong(LongFunc1<? super TSource> keySelector) {
        return MaxBy.maxByLong(this, (LongFunc1<TSource>) keySelector);
    }

    default TSource maxByLongNull(NullableLongFunc1<? super TSource> keySelector) {
        return MaxBy.maxByLongNull(this, (NullableLongFunc1<TSource>) keySelector);
    }

    default TSource maxByFloat(FloatFunc1<? super TSource> keySelector) {
        return MaxBy.maxByFloat(this, (FloatFunc1<TSource>) keySelector);
    }

    default TSource maxByFloatNull(NullableFloatFunc1<? super TSource> keySelector) {
        return MaxBy.maxByFloatNull(this, (NullableFloatFunc1<TSource>) keySelector);
    }

    default TSource maxByDouble(DoubleFunc1<? super TSource> keySelector) {
        return MaxBy.maxByDouble(this, (DoubleFunc1<TSource>) keySelector);
    }

    default TSource maxByDoubleNull(NullableDoubleFunc1<? super TSource> keySelector) {
        return MaxBy.maxByDoubleNull(this, (NullableDoubleFunc1<TSource>) keySelector);
    }

    default TSource maxByDecimal(DecimalFunc1<? super TSource> keySelector) {
        return MaxBy.maxByDecimal(this, (DecimalFunc1<TSource>) keySelector);
    }

    default TSource maxByDecimalNull(NullableDecimalFunc1<? super TSource> keySelector) {
        return MaxBy.maxByDecimalNull(this, (NullableDecimalFunc1<TSource>) keySelector);
    }

    default <TKey> TSource maxBy(Func1<? super TSource, ? extends TKey> keySelector) {
        return MaxBy.maxBy(this, (Func1<TSource, TKey>) keySelector);
    }

    default <TKey> TSource maxByNull(Func1<? super TSource, ? extends TKey> keySelector) {
        return MaxBy.maxByNull(this, (Func1<TSource, TKey>) keySelector);
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

    default int minInt(IntFunc1<? super TSource> selector) {
        return Min.minInt(this, (IntFunc1<TSource>) selector);
    }

    default Integer minIntNull(NullableIntFunc1<? super TSource> selector) {
        return Min.minIntNull(this, (NullableIntFunc1<TSource>) selector);
    }

    default long minLong(LongFunc1<? super TSource> selector) {
        return Min.minLong(this, (LongFunc1<TSource>) selector);
    }

    default Long minLongNull(NullableLongFunc1<? super TSource> selector) {
        return Min.minLongNull(this, (NullableLongFunc1<TSource>) selector);
    }

    default float minFloat(FloatFunc1<? super TSource> selector) {
        return Min.minFloat(this, (FloatFunc1<TSource>) selector);
    }

    default Float minFloatNull(NullableFloatFunc1<? super TSource> selector) {
        return Min.minFloatNull(this, (NullableFloatFunc1<TSource>) selector);
    }

    default double minDouble(DoubleFunc1<? super TSource> selector) {
        return Min.minDouble(this, (DoubleFunc1<TSource>) selector);
    }

    default Double minDoubleNull(NullableDoubleFunc1<? super TSource> selector) {
        return Min.minDoubleNull(this, (NullableDoubleFunc1<TSource>) selector);
    }

    default BigDecimal minDecimal(DecimalFunc1<? super TSource> selector) {
        return Min.minDecimal(this, (DecimalFunc1<TSource>) selector);
    }

    default BigDecimal minDecimalNull(NullableDecimalFunc1<? super TSource> selector) {
        return Min.minDecimalNull(this, (NullableDecimalFunc1<TSource>) selector);
    }

    default <TResult> TResult min(Func1<? super TSource, ? extends TResult> selector) {
        return Min.min(this, (Func1<TSource, TResult>) selector);
    }

    default <TResult> TResult minNull(Func1<? super TSource, ? extends TResult> selector) {
        return Min.minNull(this, (Func1<TSource, TResult>) selector);
    }

    default TSource minByInt(IntFunc1<? super TSource> keySelector) {
        return MinBy.minByInt(this, (IntFunc1<TSource>) keySelector);
    }

    default TSource minByIntNull(NullableIntFunc1<? super TSource> keySelector) {
        return MinBy.minByIntNull(this, (NullableIntFunc1<TSource>) keySelector);
    }

    default TSource minByLong(LongFunc1<? super TSource> keySelector) {
        return MinBy.minByLong(this, (LongFunc1<TSource>) keySelector);
    }

    default TSource minByLongNull(NullableLongFunc1<? super TSource> keySelector) {
        return MinBy.minByLongNull(this, (NullableLongFunc1<TSource>) keySelector);
    }

    default TSource minByFloat(FloatFunc1<? super TSource> keySelector) {
        return MinBy.minByFloat(this, (FloatFunc1<TSource>) keySelector);
    }

    default TSource minByFloatNull(NullableFloatFunc1<? super TSource> keySelector) {
        return MinBy.minByFloatNull(this, (NullableFloatFunc1<TSource>) keySelector);
    }

    default TSource minByDouble(DoubleFunc1<? super TSource> keySelector) {
        return MinBy.minByDouble(this, (DoubleFunc1<TSource>) keySelector);
    }

    default TSource minByDoubleNull(NullableDoubleFunc1<? super TSource> keySelector) {
        return MinBy.minByDoubleNull(this, (NullableDoubleFunc1<TSource>) keySelector);
    }

    default TSource minByDecimal(DecimalFunc1<? super TSource> keySelector) {
        return MinBy.minByDecimal(this, (DecimalFunc1<TSource>) keySelector);
    }

    default TSource minByDecimalNull(NullableDecimalFunc1<? super TSource> keySelector) {
        return MinBy.minByDecimalNull(this, (NullableDecimalFunc1<TSource>) keySelector);
    }

    default <TKey> TSource minBy(Func1<? super TSource, ? extends TKey> keySelector) {
        return MinBy.minBy(this, (Func1<TSource, TKey>) keySelector);
    }

    default <TKey> TSource minByNull(Func1<? super TSource, ? extends TKey> keySelector) {
        return MinBy.minByNull(this, (Func1<TSource, TKey>) keySelector);
    }

    default <TResult> IEnumerable<TResult> ofType(Class<TResult> clazz) {
        return Cast.ofType(this, clazz);
    }

    default <TKey> IOrderedEnumerable<TSource> orderBy(Func1<? super TSource, ? extends TKey> keySelector) {
        return OrderBy.orderBy(this, (Func1<TSource, TKey>) keySelector);
    }

    default <TKey> IOrderedEnumerable<TSource> orderBy(Func1<? super TSource, ? extends TKey> keySelector, Comparator<? super TKey> comparer) {
        return OrderBy.orderBy(this, (Func1<TSource, TKey>) keySelector, (Comparator<TKey>) comparer);
    }

    default <TKey> IOrderedEnumerable<TSource> orderByDescending(Func1<? super TSource, ? extends TKey> keySelector) {
        return OrderBy.orderByDescending(this, (Func1<TSource, TKey>) keySelector);
    }

    default <TKey> IOrderedEnumerable<TSource> orderByDescending(Func1<? super TSource, ? extends TKey> keySelector, Comparator<? super TKey> comparer) {
        return OrderBy.orderByDescending(this, (Func1<TSource, TKey>) keySelector, (Comparator<TKey>) comparer);
    }

    default IEnumerable<TSource> prepend(TSource element) {
        return AppendPrepend.prepend(this, element);
    }

    default IEnumerable<TSource> reverse() {
        return Reverse.reverse(this);
    }

    default <TInner, TKey, TResult> IEnumerable<TResult> rightJoin(IEnumerable<? extends TInner> inner, Func1<? super TSource, ? extends TKey> outerKeySelector, Func1<? super TInner, ? extends TKey> innerKeySelector, Func2<? super TSource, ? super TInner, ? extends TResult> resultSelector) {
        return Join.rightJoin(this, (IEnumerable<TInner>) inner, (Func1<TSource, TKey>) outerKeySelector, (Func1<TInner, TKey>) innerKeySelector, (Func2<TSource, TInner, TResult>) resultSelector);
    }

    default <TInner, TKey, TResult> IEnumerable<TResult> rightJoin(IEnumerable<? extends TInner> inner, Func1<? super TSource, ? extends TKey> outerKeySelector, Func1<? super TInner, ? extends TKey> innerKeySelector, TSource defaultOuter, Func2<? super TSource, ? super TInner, ? extends TResult> resultSelector) {
        return Join.rightJoin(this, (IEnumerable<TInner>) inner, (Func1<TSource, TKey>) outerKeySelector, (Func1<TInner, TKey>) innerKeySelector, defaultOuter, (Func2<TSource, TInner, TResult>) resultSelector);
    }

    default <TInner, TKey, TResult> IEnumerable<TResult> rightJoin(IEnumerable<? extends TInner> inner, Func1<? super TSource, ? extends TKey> outerKeySelector, Func1<? super TInner, ? extends TKey> innerKeySelector, Func2<? super TSource, ? super TInner, ? extends TResult> resultSelector, IEqualityComparer<? super TKey> comparer) {
        return Join.rightJoin(this, (IEnumerable<TInner>) inner, (Func1<TSource, TKey>) outerKeySelector, (Func1<TInner, TKey>) innerKeySelector, (Func2<TSource, TInner, TResult>) resultSelector, (IEqualityComparer<TKey>) comparer);
    }

    default <TInner, TKey, TResult> IEnumerable<TResult> rightJoin(IEnumerable<? extends TInner> inner, Func1<? super TSource, ? extends TKey> outerKeySelector, Func1<? super TInner, ? extends TKey> innerKeySelector, TSource defaultOuter, Func2<? super TSource, ? super TInner, ? extends TResult> resultSelector, IEqualityComparer<? super TKey> comparer) {
        return Join.rightJoin(this, (IEnumerable<TInner>) inner, (Func1<TSource, TKey>) outerKeySelector, (Func1<TInner, TKey>) innerKeySelector, defaultOuter, (Func2<TSource, TInner, TResult>) resultSelector, (IEqualityComparer<TKey>) comparer);
    }

    default IEnumerable<TSource> runOnce() {
        return RunOnce.runOnce(this);
    }

    default <TResult> IEnumerable<TResult> select(Func1<? super TSource, ? extends TResult> selector) {
        return Select.select(this, (Func1<TSource, TResult>) selector);
    }

    default <TResult> IEnumerable<TResult> select(IndexFunc2<? super TSource, ? extends TResult> selector) {
        return Select.select(this, (IndexFunc2<TSource, TResult>) selector);
    }

    default <TResult> IEnumerable<TResult> selectMany(Func1<? super TSource, ? extends IEnumerable<? extends TResult>> selector) {
        return SelectMany.selectMany(this, (Func1<TSource, IEnumerable<TResult>>) selector);
    }

    default <TResult> IEnumerable<TResult> selectMany(IndexFunc2<? super TSource, ? extends IEnumerable<? extends TResult>> selector) {
        return SelectMany.selectMany(this, (IndexFunc2<TSource, IEnumerable<TResult>>) selector);
    }

    default <TCollection, TResult> IEnumerable<TResult> selectMany(Func1<? super TSource, ? extends IEnumerable<? extends TCollection>> collectionSelector, Func2<? super TSource, ? super TCollection, ? extends TResult> resultSelector) {
        return SelectMany.selectMany(this, (Func1<TSource, IEnumerable<TCollection>>) collectionSelector, (Func2<TSource, TCollection, TResult>) resultSelector);
    }

    default <TCollection, TResult> IEnumerable<TResult> selectMany(IndexFunc2<? super TSource, ? extends IEnumerable<? extends TCollection>> collectionSelector, Func2<? super TSource, ? super TCollection, ? extends TResult> resultSelector) {
        return SelectMany.selectMany(this, (IndexFunc2<TSource, IEnumerable<TCollection>>) collectionSelector, (Func2<TSource, TCollection, TResult>) resultSelector);
    }

    default boolean sequenceEqual(IEnumerable<? extends TSource> second) {
        return SequenceEqual.sequenceEqual(this, (IEnumerable<TSource>) second);
    }

    default boolean sequenceEqual(IEnumerable<? extends TSource> second, IEqualityComparer<? super TSource> comparer) {
        return SequenceEqual.sequenceEqual(this, (IEnumerable<TSource>) second, (IEqualityComparer<TSource>) comparer);
    }

    default IEnumerable<TSource> shuffle() {
        return Shuffle.shuffle(this);
    }

    default IEnumerable<TSource> shuffle(long seed) {
        return Shuffle.shuffle(this, seed);
    }

    default TSource single() {
        return Single.single(this);
    }

    default TSource single(Predicate1<? super TSource> predicate) {
        return Single.single(this, (Predicate1<TSource>) predicate);
    }

    default TSource singleOrDefault() {
        return Single.singleOrDefault(this);
    }

    default TSource singleOrDefault(Predicate1<? super TSource> predicate) {
        return Single.singleOrDefault(this, (Predicate1<TSource>) predicate);
    }

    default IEnumerable<TSource> skip(int count) {
        return Skip.skip(this, count);
    }

    default IEnumerable<TSource> skipLast(int count) {
        return Skip.skipLast(this, count);
    }

    default IEnumerable<TSource> skipWhile(Predicate1<? super TSource> predicate) {
        return Skip.skipWhile(this, (Predicate1<TSource>) predicate);
    }

    default IEnumerable<TSource> skipWhile(IndexPredicate2<? super TSource> predicate) {
        return Skip.skipWhile(this, (IndexPredicate2<TSource>) predicate);
    }

    default int sumInt() {
        return Sum.sumInt((IEnumerable<Integer>) this);
    }

    default int sumIntNull() {
        return Sum.sumIntNull((IEnumerable<Integer>) this);
    }

    default long sumLong() {
        return Sum.sumLong((IEnumerable<Long>) this);
    }

    default long sumLongNull() {
        return Sum.sumLongNull((IEnumerable<Long>) this);
    }

    default float sumFloat() {
        return Sum.sumFloat((IEnumerable<Float>) this);
    }

    default float sumFloatNull() {
        return Sum.sumFloatNull((IEnumerable<Float>) this);
    }

    default double sumDouble() {
        return Sum.sumDouble((IEnumerable<Double>) this);
    }

    default double sumDoubleNull() {
        return Sum.sumDoubleNull((IEnumerable<Double>) this);
    }

    default BigDecimal sumDecimal() {
        return Sum.sumDecimal((IEnumerable<BigDecimal>) this);
    }

    default BigDecimal sumDecimalNull() {
        return Sum.sumDecimalNull((IEnumerable<BigDecimal>) this);
    }

    default int sumInt(IntFunc1<? super TSource> selector) {
        return Sum.sumInt(this, (IntFunc1<TSource>) selector);
    }

    default int sumIntNull(NullableIntFunc1<? super TSource> selector) {
        return Sum.sumIntNull(this, (NullableIntFunc1<TSource>) selector);
    }

    default long sumLong(LongFunc1<? super TSource> selector) {
        return Sum.sumLong(this, (LongFunc1<TSource>) selector);
    }

    default long sumLongNull(NullableLongFunc1<? super TSource> selector) {
        return Sum.sumLongNull(this, (NullableLongFunc1<TSource>) selector);
    }

    default float sumFloat(FloatFunc1<? super TSource> selector) {
        return Sum.sumFloat(this, (FloatFunc1<TSource>) selector);
    }

    default float sumFloatNull(NullableFloatFunc1<? super TSource> selector) {
        return Sum.sumFloatNull(this, (NullableFloatFunc1<TSource>) selector);
    }

    default double sumDouble(DoubleFunc1<? super TSource> selector) {
        return Sum.sumDouble(this, (DoubleFunc1<TSource>) selector);
    }

    default double sumDoubleNull(NullableDoubleFunc1<? super TSource> selector) {
        return Sum.sumDoubleNull(this, (NullableDoubleFunc1<TSource>) selector);
    }

    default BigDecimal sumDecimal(DecimalFunc1<? super TSource> selector) {
        return Sum.sumDecimal(this, (DecimalFunc1<TSource>) selector);
    }

    default BigDecimal sumDecimalNull(NullableDecimalFunc1<? super TSource> selector) {
        return Sum.sumDecimalNull(this, (NullableDecimalFunc1<TSource>) selector);
    }

    default IEnumerable<TSource> take(int count) {
        return Take.take(this, count);
    }

    default IEnumerable<TSource> takeLast(int count) {
        return Take.takeLast(this, count);
    }

    default IEnumerable<TSource> takeWhile(Predicate1<? super TSource> predicate) {
        return Take.takeWhile(this, (Predicate1<TSource>) predicate);
    }

    default IEnumerable<TSource> takeWhile(IndexPredicate2<? super TSource> predicate) {
        return Take.takeWhile(this, (IndexPredicate2<TSource>) predicate);
    }

    default Array<TSource> toArray() {
        Object[] elements = ToCollection.toArray(this);
        return elements == ArrayUtils.empty() ? Array.empty() : new Array<>(elements);
    }

    default TSource[] toArray(Class<TSource> clazz) {
        return ToCollection.toArray(this, clazz);
    }

    default Enumeration<TSource> toEnumeration() {
        return ToEnumeration.toEnumeration(this);
    }

    default <TKey> Map<TKey, TSource> toLinkedMap(Func1<? super TSource, ? extends TKey> keySelector) {
        return ToCollection.toLinkedMap(this, (Func1<TSource, TKey>) keySelector);
    }

    default <TKey, TElement> Map<TKey, TElement> toLinkedMap(Func1<? super TSource, ? extends TKey> keySelector, Func1<? super TSource, ? extends TElement> elementSelector) {
        return ToCollection.toLinkedMap(this, (Func1<TSource, TKey>) keySelector, (Func1<TSource, TElement>) elementSelector);
    }

    default Set<TSource> toLinkedSet() {
        return ToCollection.toLinkedSet(this);
    }

    default List<TSource> toList() {
        return ToCollection.toList(this);
    }

    default <TKey> ILookup<TKey, TSource> toLookup(Func1<? super TSource, ? extends TKey> keySelector) {
        return ToLookup.toLookup(this, (Func1<TSource, TKey>) keySelector);
    }

    default <TKey> ILookup<TKey, TSource> toLookup(Func1<? super TSource, ? extends TKey> keySelector, IEqualityComparer<? super TKey> comparer) {
        return ToLookup.toLookup(this, (Func1<TSource, TKey>) keySelector, (IEqualityComparer<TKey>) comparer);
    }

    default <TKey, TElement> ILookup<TKey, TElement> toLookup(Func1<? super TSource, ? extends TKey> keySelector, Func1<? super TSource, ? extends TElement> elementSelector) {
        return ToLookup.toLookup(this, (Func1<TSource, TKey>) keySelector, (Func1<TSource, TElement>) elementSelector);
    }

    default <TKey, TElement> ILookup<TKey, TElement> toLookup(Func1<? super TSource, ? extends TKey> keySelector, Func1<? super TSource, ? extends TElement> elementSelector, IEqualityComparer<? super TKey> comparer) {
        return ToLookup.toLookup(this, (Func1<TSource, TKey>) keySelector, (Func1<TSource, TElement>) elementSelector, (IEqualityComparer<TKey>) comparer);
    }

    default <TKey> Map<TKey, TSource> toMap(Func1<? super TSource, ? extends TKey> keySelector) {
        return ToCollection.toMap(this, (Func1<TSource, TKey>) keySelector);
    }

    default <TKey, TElement> Map<TKey, TElement> toMap(Func1<? super TSource, ? extends TKey> keySelector, Func1<? super TSource, ? extends TElement> elementSelector) {
        return ToCollection.toMap(this, (Func1<TSource, TKey>) keySelector, (Func1<TSource, TElement>) elementSelector);
    }

    default Set<TSource> toSet() {
        return ToCollection.toSet(this);
    }

    default IEnumerable<TSource> union(IEnumerable<? extends TSource> second) {
        return Union.union(this, (IEnumerable<TSource>) second);
    }

    default IEnumerable<TSource> union(IEnumerable<? extends TSource> second, IEqualityComparer<? super TSource> comparer) {
        return Union.union(this, (IEnumerable<TSource>) second, (IEqualityComparer<TSource>) comparer);
    }

    default <TKey> IEnumerable<TSource> unionBy(IEnumerable<? extends TSource> second, Func1<? super TSource, ? extends TKey> keySelector) {
        return UnionBy.unionBy(this, (IEnumerable<TSource>) second, (Func1<TSource, TKey>) keySelector);
    }

    default <TKey> IEnumerable<TSource> unionBy(IEnumerable<? extends TSource> second, Func1<? super TSource, ? extends TKey> keySelector, IEqualityComparer<? super TKey> comparer) {
        return UnionBy.unionBy(this, (IEnumerable<TSource>) second, (Func1<TSource, TKey>) keySelector, (IEqualityComparer<TKey>) comparer);
    }

    default IEnumerable<TSource> where(Predicate1<? super TSource> predicate) {
        return Where.where(this, (Predicate1<TSource>) predicate);
    }

    default IEnumerable<TSource> where(IndexPredicate2<? super TSource> predicate) {
        return Where.where(this, (IndexPredicate2<TSource>) predicate);
    }

    default <TSecond> IEnumerable<Tuple2<TSource, TSecond>> zip(IEnumerable<? extends TSecond> second) {
        return Zip.zip(this, (IEnumerable<TSecond>) second);
    }

    default <TSecond, TResult> IEnumerable<TResult> zip(IEnumerable<? extends TSecond> second, Func2<? super TSource, ? super TSecond, ? extends TResult> resultSelector) {
        return Zip.zip(this, (IEnumerable<TSecond>) second, (Func2<TSource, TSecond, TResult>) resultSelector);
    }
}
