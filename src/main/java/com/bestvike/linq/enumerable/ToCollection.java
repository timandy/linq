package com.bestvike.linq.enumerable;

import com.bestvike.collections.generic.IArray;
import com.bestvike.collections.generic.ICollection;
import com.bestvike.function.Func1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.exception.ExceptionArgument;
import com.bestvike.linq.exception.ThrowHelper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by 许崇雷 on 2018-05-08.
 */
public final class ToCollection {
    private ToCollection() {
    }

    public static <TSource> TSource[] toArray(IEnumerable<TSource> source, Class<TSource> clazz) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (clazz == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.clazz);

        if (source instanceof IIListProvider) {
            IIListProvider<TSource> listProvider = (IIListProvider<TSource>) source;
            return listProvider._toArray(clazz);
        }

        return EnumerableHelpers.toArray(source, clazz);
    }

    public static <TSource> Object[] toArray(IEnumerable<TSource> source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        if (source instanceof IIListProvider) {
            IIListProvider<TSource> listProvider = (IIListProvider<TSource>) source;
            return listProvider._toArray();
        }

        return EnumerableHelpers.toArray(source);
    }

    public static <TSource> List<TSource> toList(IEnumerable<TSource> source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        if (source instanceof IIListProvider) {
            IIListProvider<TSource> listProvider = (IIListProvider<TSource>) source;
            return listProvider._toList();
        }

        return EnumerableHelpers.toList(source);
    }

    public static <TSource, TKey> Map<TKey, TSource> toMap(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (keySelector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.keySelector);

        int capacity = 0;
        if (source instanceof ICollection) {
            ICollection<TSource> collection = (ICollection<TSource>) source;
            capacity = collection._getCount();
            if (capacity == 0)
                return new HashMap<>();

            if (collection instanceof IArray) {
                IArray<TSource> array = (IArray<TSource>) collection;
                Map<TKey, TSource> map = new HashMap<>(capacity);
                for (int i = 0; i < capacity; i++) {
                    TSource element = array.get(i);
                    map.put(keySelector.apply(element), element);
                }
                return map;
            }
        }

        Map<TKey, TSource> map = new HashMap<>(capacity);
        for (TSource element : source)
            map.put(keySelector.apply(element), element);
        return map;
    }

    public static <TSource, TKey, TElement> Map<TKey, TElement> toMap(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector, Func1<TSource, TElement> elementSelector) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (keySelector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.keySelector);
        if (elementSelector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.elementSelector);

        int capacity = 0;
        if (source instanceof ICollection) {
            ICollection<TSource> collection = (ICollection<TSource>) source;
            capacity = collection._getCount();
            if (capacity == 0)
                return new HashMap<>();

            if (collection instanceof IArray) {
                IArray<TSource> array = (IArray<TSource>) collection;
                Map<TKey, TElement> map = new HashMap<>(capacity);
                for (int i = 0; i < capacity; i++) {
                    TSource element = array.get(i);
                    map.put(keySelector.apply(element), elementSelector.apply(element));
                }
                return map;
            }
        }

        Map<TKey, TElement> map = new HashMap<>(capacity);
        for (TSource element : source)
            map.put(keySelector.apply(element), elementSelector.apply(element));
        return map;
    }

    public static <TSource> Set<TSource> toSet(IEnumerable<TSource> source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        // Don't pre-allocate based on knowledge of size, as potentially many elements will be dropped.
        int capacity = 0;
        if (source instanceof ICollection) {
            ICollection<TSource> collection = (ICollection<TSource>) source;
            capacity = collection._getCount();
            if (capacity == 0)
                return new HashSet<>();

            if (collection instanceof IArray) {
                IArray<TSource> array = (IArray<TSource>) collection;
                Set<TSource> set = new HashSet<>(capacity);
                for (int i = 0; i < capacity; i++)
                    set.add(array.get(i));
                return set;
            }
        }
        Set<TSource> set = new HashSet<>(capacity);
        for (TSource element : source)
            set.add(element);
        return set;
    }
}
