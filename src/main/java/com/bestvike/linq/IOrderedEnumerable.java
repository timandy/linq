package com.bestvike.linq;

import com.bestvike.function.Func1;
import com.bestvike.linq.iterator.OrderBy;

import java.util.Comparator;

/**
 * Created by 许崇雷 on 2017/7/11.
 */
public interface IOrderedEnumerable<TElement> extends IEnumerable<TElement> {
    <TKey> IOrderedEnumerable<TElement> createOrderedEnumerable(Func1<TElement, TKey> keySelector, Comparator<TKey> comparer, boolean descending);

    default <TKey> IOrderedEnumerable<TElement> thenBy(Func1<TElement, TKey> keySelector) {
        return OrderBy.thenBy(this, keySelector);
    }

    default <TKey> IOrderedEnumerable<TElement> thenBy(Func1<TElement, TKey> keySelector, Comparator<TKey> comparer) {
        return OrderBy.thenBy(this, keySelector, comparer);
    }

    default <TKey> IOrderedEnumerable<TElement> thenByDescending(Func1<TElement, TKey> keySelector) {
        return OrderBy.thenByDescending(this, keySelector);
    }

    default <TKey> IOrderedEnumerable<TElement> thenByDescending(Func1<TElement, TKey> keySelector, Comparator<TKey> comparer) {
        return OrderBy.thenByDescending(this, keySelector, comparer);
    }
}
