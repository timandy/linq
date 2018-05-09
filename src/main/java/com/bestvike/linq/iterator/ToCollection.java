package com.bestvike.linq.iterator;

import com.bestvike.collections.generic.Array;
import com.bestvike.collections.generic.ICollection;
import com.bestvike.function.Func1;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.exception.Errors;

import java.util.ArrayList;
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
            throw Errors.argumentNull("source");
        if (clazz == null)
            throw Errors.argumentNull("clazz");

        if (source instanceof IIListProvider) {
            IIListProvider<TSource> listProvider = (IIListProvider<TSource>) source;
            return listProvider._toArray(clazz);
        }

        return EnumerableHelpers.toArray(source, clazz);
    }

    public static <TSource> Array<TSource> toArray(IEnumerable<TSource> source) {
        if (source == null)
            throw Errors.argumentNull("source");

        if (source instanceof IIListProvider) {
            IIListProvider<TSource> listProvider = (IIListProvider<TSource>) source;
            return listProvider._toArray();
        }

        return EnumerableHelpers.toArray(source);
    }

    public static <TSource> List<TSource> toList(IEnumerable<TSource> source) {
        if (source == null)
            throw Errors.argumentNull("source");

        if (source instanceof IIListProvider) {
            IIListProvider<TSource> listProvider = (IIListProvider<TSource>) source;
            return listProvider._toList();
        }

        if (source instanceof ICollection) {
            ICollection<TSource> collection = (ICollection<TSource>) source;
            return collection._toList();
        }

        List<TSource> list = new ArrayList<>();
        for (TSource item : source)
            list.add(item);
        return list;
    }

    public static <TSource, TKey> Map<TKey, TSource> toMap(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector) {
        if (source == null)
            throw Errors.argumentNull("source");
        if (keySelector == null)
            throw Errors.argumentNull("keySelector");

        int capacity = 0;
        if (source instanceof ICollection) {
            ICollection<TSource> collection = (ICollection<TSource>) source;
            capacity = collection._getCount();
            if (capacity == 0)
                return new HashMap<>();

            if (collection instanceof Array) {
                Array<TSource> array = (Array<TSource>) collection;
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
            throw Errors.argumentNull("source");
        if (keySelector == null)
            throw Errors.argumentNull("keySelector");
        if (elementSelector == null)
            throw Errors.argumentNull("elementSelector");

        int capacity = 0;
        if (source instanceof ICollection) {
            ICollection<TSource> collection = (ICollection<TSource>) source;
            capacity = collection._getCount();
            if (capacity == 0)
                return new HashMap<>();

            if (collection instanceof Array) {
                Array<TSource> array = (Array<TSource>) collection;
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
            throw Errors.argumentNull("source");

        // Don't pre-allocate based on knowledge of size, as potentially many elements will be dropped.
        int capacity = 0;
        if (source instanceof ICollection) {
            ICollection<TSource> collection = (ICollection<TSource>) source;
            capacity = collection._getCount();
            if (capacity == 0)
                return new HashSet<>();

            if (collection instanceof Array) {
                Array<TSource> array = (Array<TSource>) collection;
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
