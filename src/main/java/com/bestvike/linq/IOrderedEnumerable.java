package com.bestvike.linq;

import com.bestvike.linq.function.Func1;
import com.bestvike.linq.iterator.Enumerable;

import java.util.Comparator;

/**
 * Created by 许崇雷 on 2017/7/11.
 */
public interface IOrderedEnumerable<TElement> extends IEnumerable<TElement> {
    <TKey> IOrderedEnumerable<TElement> createOrderedEnumerable(Func1<TElement, TKey> keySelector, Comparator<TKey> comparer, boolean descending);

    default <TKey> IOrderedEnumerable<TElement> thenBy(Func1<TElement, TKey> keySelector) {
        return Enumerable.thenBy(this, keySelector);
    }

    default <TKey> IOrderedEnumerable<TElement> thenBy(Func1<TElement, TKey> keySelector, Comparator<TKey> comparer) {
        return Enumerable.thenBy(this, keySelector, comparer);
    }

    default <TKey> IOrderedEnumerable<TElement> thenByDescending(Func1<TElement, TKey> keySelector) {
        return Enumerable.thenByDescending(this, keySelector);
    }

    default <TKey> IOrderedEnumerable<TElement> thenByDescending(Func1<TElement, TKey> keySelector, Comparator<TKey> comparer) {
        return Enumerable.thenByDescending(this, keySelector, comparer);
    }
}
