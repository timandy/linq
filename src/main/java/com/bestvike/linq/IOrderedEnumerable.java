package com.bestvike.linq;

import com.bestvike.linq.function.Func1;

import java.util.Comparator;

/**
 * @author 许崇雷
 * @date 2017/7/11
 */
public interface IOrderedEnumerable<TElement> extends IEnumerable<TElement> {
    <TKey> IOrderedEnumerable<TElement> createOrderedEnumerable(Func1<TElement, TKey> keySelector, Comparator<TKey> comparer, boolean descending);
}
