package com.bestvike.linq.iterator;

import com.bestvike.collections.generic.Array;
import com.bestvike.collections.generic.IList;
import com.bestvike.function.Func1;
import com.bestvike.function.Func2;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.enumerable.ListEnumerable;
import com.bestvike.linq.exception.Errors;
import com.bestvike.linq.impl.partition.EmptyPartition;
import com.bestvike.linq.impl.partition.IPartition;

/**
 * Created by 许崇雷 on 2018-05-03.
 */
public final class Select {
    private Select() {
    }

    public static <TSource, TResult> IEnumerable<TResult> select(IEnumerable<TSource> source, Func1<TSource, TResult> selector) {
        if (source == null)
            throw Errors.argumentNull("source");
        if (selector == null)
            throw Errors.argumentNull("selector");
        if (source instanceof Iterator)
            return ((Iterator<TSource>) source)._select(selector);

        if (source instanceof IList) {
            if (source instanceof Array) {
                Array<TSource> array = (Array<TSource>) source;
                return array.length() == 0
                        ? EmptyPartition.instance()
                        : new SelectArrayIterator<>(array, selector);
            }
            ListEnumerable<TSource> list = (ListEnumerable<TSource>) source;
            return list._getCount() == 0
                    ? EmptyPartition.instance()
                    : new SelectIListIterator<>(list, selector);
        }

        if (source instanceof IPartition) {
            IPartition<TSource> partition = (IPartition<TSource>) source;
            return new SelectIPartitionIterator<>(partition, selector);
        }

        return new SelectEnumerableIterator<>(source, selector);
    }

    public static <TSource, TResult> IEnumerable<TResult> select(IEnumerable<TSource> source, Func2<TSource, Integer, TResult> selector) {
        if (source == null)
            throw Errors.argumentNull("source");

        if (selector == null)
            throw Errors.argumentNull("selector");

        return new SelectIterator<>(source, selector);
    }
}
