package com.bestvike.linq;

import com.bestvike.function.Func1;
import com.bestvike.linq.enumerable.OrderBy;

import java.util.Comparator;

/**
 * Created by 许崇雷 on 2017-07-11.
 */
@SuppressWarnings("unchecked")
public interface IOrderedEnumerable<TElement> extends IEnumerable<TElement> {
    <TKey> IOrderedEnumerable<TElement> createOrderedEnumerable(Func1<TElement, TKey> keySelector, Comparator<TKey> comparer, boolean descending);

    default <TKey> IOrderedEnumerable<TElement> thenBy(Func1<? super TElement, ? extends TKey> keySelector) {
        return OrderBy.thenBy(this, (Func1<TElement, TKey>) keySelector);
    }

    default <TKey> IOrderedEnumerable<TElement> thenBy(Func1<? super TElement, ? extends TKey> keySelector, Comparator<? super TKey> comparer) {
        return OrderBy.thenBy(this, (Func1<TElement, TKey>) keySelector, (Comparator<TKey>) comparer);
    }

    default <TKey> IOrderedEnumerable<TElement> thenByDescending(Func1<? super TElement, ? extends TKey> keySelector) {
        return OrderBy.thenByDescending(this, (Func1<TElement, TKey>) keySelector);
    }

    default <TKey> IOrderedEnumerable<TElement> thenByDescending(Func1<? super TElement, ? extends TKey> keySelector, Comparator<? super TKey> comparer) {
        return OrderBy.thenByDescending(this, (Func1<TElement, TKey>) keySelector, (Comparator<TKey>) comparer);
    }
}
