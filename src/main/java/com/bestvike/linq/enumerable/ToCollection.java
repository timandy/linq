package com.bestvike.linq.enumerable;

import com.bestvike.collections.generic.IArrayList;
import com.bestvike.collections.generic.ICollection;
import com.bestvike.function.Action2;
import com.bestvike.function.Func1;
import com.bestvike.function.Func2;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.exception.ExceptionArgument;
import com.bestvike.linq.exception.ThrowHelper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by 许崇雷 on 2018-05-08.
 */
public final class ToCollection {
    private static final int MAXIMUM_CAPACITY = 1 << 30;

    private ToCollection() {
    }

    private static int initialCapacity(int capacity) {
        if (capacity < 3)
            return capacity + 1;
        if (capacity < MAXIMUM_CAPACITY)
            return (int) ((float) capacity / 0.75f + 1.0f);
        return Integer.MAX_VALUE;
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

    public static <TSource> List<TSource> toLinkedList(IEnumerable<TSource> source) {
        if (source instanceof ICollection) {
            ICollection<TSource> collection = (ICollection<TSource>) source;
            int capacity = collection._getCount();
            if (capacity == 0)
                return new LinkedList<>();

            if (collection instanceof IArrayList) {
                IArrayList<TSource> ilist = (IArrayList<TSource>) collection;
                List<TSource> list = new LinkedList<>();
                for (int i = 0; i < capacity; i++)
                    list.add(ilist.get(i));
                return list;
            }
        }

        List<TSource> list = new LinkedList<>();
        try (IEnumerator<TSource> e = source.enumerator()) {
            while (e.moveNext())
                list.add(e.current());
        }
        return list;
    }

    public static <TSource, TKey> Map<TKey, TSource> toMap(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (keySelector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.keySelector);

        if (source instanceof ICollection) {
            ICollection<TSource> collection = (ICollection<TSource>) source;
            int capacity = collection._getCount();
            if (capacity == 0)
                return new HashMap<>();

            if (collection instanceof IArrayList) {
                IArrayList<TSource> list = (IArrayList<TSource>) collection;
                Map<TKey, TSource> map = new HashMap<>(initialCapacity(capacity));
                for (int i = 0; i < capacity; i++) {
                    TSource element = list.get(i);
                    map.putIfAbsent(keySelector.apply(element), element);
                }
                return map;
            }

            Map<TKey, TSource> map = new HashMap<>(initialCapacity(capacity));
            try (IEnumerator<TSource> e = source.enumerator()) {
                while (e.moveNext()) {
                    TSource element = e.current();
                    map.putIfAbsent(keySelector.apply(element), element);
                }
            }
            return map;
        }

        Map<TKey, TSource> map = new HashMap<>();
        try (IEnumerator<TSource> e = source.enumerator()) {
            while (e.moveNext()) {
                TSource element = e.current();
                map.putIfAbsent(keySelector.apply(element), element);
            }
        }
        return map;
    }

    public static <TSource, TKey, TElement> Map<TKey, TElement> toMap(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector, Func1<TSource, TElement> elementSelector) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (keySelector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.keySelector);
        if (elementSelector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.elementSelector);

        if (source instanceof ICollection) {
            ICollection<TSource> collection = (ICollection<TSource>) source;
            int capacity = collection._getCount();
            if (capacity == 0)
                return new HashMap<>();

            if (collection instanceof IArrayList) {
                IArrayList<TSource> list = (IArrayList<TSource>) collection;
                Map<TKey, TElement> map = new HashMap<>(initialCapacity(capacity));
                for (int i = 0; i < capacity; i++) {
                    TSource element = list.get(i);
                    map.putIfAbsent(keySelector.apply(element), elementSelector.apply(element));
                }
                return map;
            }

            Map<TKey, TElement> map = new HashMap<>(initialCapacity(capacity));
            try (IEnumerator<TSource> e = source.enumerator()) {
                while (e.moveNext()) {
                    TSource element = e.current();
                    map.putIfAbsent(keySelector.apply(element), elementSelector.apply(element));
                }
            }
            return map;
        }

        Map<TKey, TElement> map = new HashMap<>();
        try (IEnumerator<TSource> e = source.enumerator()) {
            while (e.moveNext()) {
                TSource element = e.current();
                map.putIfAbsent(keySelector.apply(element), elementSelector.apply(element));
            }
        }
        return map;
    }

    public static <TSource, TKey, TElement> Map<TKey, TElement> toMap(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector, Func1<TSource, TElement> elementSelector, Func2<TElement, TElement, TElement> resultElementSelector) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (keySelector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.keySelector);
        if (elementSelector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.elementSelector);
        if (resultElementSelector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.resultElementSelector);

        if (source instanceof ICollection) {
            ICollection<TSource> collection = (ICollection<TSource>) source;
            int capacity = collection._getCount();
            if (capacity == 0)
                return new HashMap<>();

            if (collection instanceof IArrayList) {
                IArrayList<TSource> list = (IArrayList<TSource>) collection;
                Map<TKey, TElement> map = new HashMap<>(initialCapacity(capacity));
                for (int i = 0; i < capacity; i++) {
                    TSource element = list.get(i);
                    map.merge(keySelector.apply(element), elementSelector.apply(element), resultElementSelector::apply);
                }
                return map;
            }

            Map<TKey, TElement> map = new HashMap<>(initialCapacity(capacity));
            try (IEnumerator<TSource> e = source.enumerator()) {
                while (e.moveNext()) {
                    TSource element = e.current();
                    map.merge(keySelector.apply(element), elementSelector.apply(element), resultElementSelector::apply);
                }
            }
            return map;
        }

        Map<TKey, TElement> map = new HashMap<>();
        try (IEnumerator<TSource> e = source.enumerator()) {
            while (e.moveNext()) {
                TSource element = e.current();
                map.merge(keySelector.apply(element), elementSelector.apply(element), resultElementSelector::apply);
            }
        }
        return map;
    }

    public static <TSource, TKey> Map<TKey, TSource> toLinkedMap(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (keySelector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.keySelector);

        if (source instanceof ICollection) {
            ICollection<TSource> collection = (ICollection<TSource>) source;
            int capacity = collection._getCount();
            if (capacity == 0)
                return new LinkedHashMap<>();

            if (collection instanceof IArrayList) {
                IArrayList<TSource> list = (IArrayList<TSource>) collection;
                Map<TKey, TSource> map = new LinkedHashMap<>(initialCapacity(capacity));
                for (int i = 0; i < capacity; i++) {
                    TSource element = list.get(i);
                    map.putIfAbsent(keySelector.apply(element), element);
                }
                return map;
            }

            Map<TKey, TSource> map = new LinkedHashMap<>(initialCapacity(capacity));
            try (IEnumerator<TSource> e = source.enumerator()) {
                while (e.moveNext()) {
                    TSource element = e.current();
                    map.putIfAbsent(keySelector.apply(element), element);
                }
            }
            return map;
        }

        Map<TKey, TSource> map = new LinkedHashMap<>();
        try (IEnumerator<TSource> e = source.enumerator()) {
            while (e.moveNext()) {
                TSource element = e.current();
                map.putIfAbsent(keySelector.apply(element), element);
            }
        }
        return map;
    }

    public static <TSource, TKey, TElement> Map<TKey, TElement> toLinkedMap(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector, Func1<TSource, TElement> elementSelector) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (keySelector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.keySelector);
        if (elementSelector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.elementSelector);

        if (source instanceof ICollection) {
            ICollection<TSource> collection = (ICollection<TSource>) source;
            int capacity = collection._getCount();
            if (capacity == 0)
                return new LinkedHashMap<>();

            if (collection instanceof IArrayList) {
                IArrayList<TSource> list = (IArrayList<TSource>) collection;
                Map<TKey, TElement> map = new LinkedHashMap<>(initialCapacity(capacity));
                for (int i = 0; i < capacity; i++) {
                    TSource element = list.get(i);
                    map.putIfAbsent(keySelector.apply(element), elementSelector.apply(element));
                }
                return map;
            }

            Map<TKey, TElement> map = new LinkedHashMap<>(initialCapacity(capacity));
            try (IEnumerator<TSource> e = source.enumerator()) {
                while (e.moveNext()) {
                    TSource element = e.current();
                    map.putIfAbsent(keySelector.apply(element), elementSelector.apply(element));
                }
            }
            return map;
        }

        Map<TKey, TElement> map = new LinkedHashMap<>();
        try (IEnumerator<TSource> e = source.enumerator()) {
            while (e.moveNext()) {
                TSource element = e.current();
                map.putIfAbsent(keySelector.apply(element), elementSelector.apply(element));
            }
        }
        return map;
    }

    public static <TSource, TKey, TElement> Map<TKey, TElement> toLinkedMap(IEnumerable<TSource> source, Func1<TSource, TKey> keySelector, Func1<TSource, TElement> elementSelector, Func2<TElement, TElement, TElement> resultElementSelector) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (keySelector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.keySelector);
        if (elementSelector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.elementSelector);

        if (source instanceof ICollection) {
            ICollection<TSource> collection = (ICollection<TSource>) source;
            int capacity = collection._getCount();
            if (capacity == 0)
                return new LinkedHashMap<>();

            if (collection instanceof IArrayList) {
                IArrayList<TSource> list = (IArrayList<TSource>) collection;
                Map<TKey, TElement> map = new LinkedHashMap<>(initialCapacity(capacity));
                for (int i = 0; i < capacity; i++) {
                    TSource element = list.get(i);
                    map.merge(keySelector.apply(element), elementSelector.apply(element), resultElementSelector::apply);
                }
                return map;
            }

            Map<TKey, TElement> map = new LinkedHashMap<>(initialCapacity(capacity));
            try (IEnumerator<TSource> e = source.enumerator()) {
                while (e.moveNext()) {
                    TSource element = e.current();
                    map.merge(keySelector.apply(element), elementSelector.apply(element), resultElementSelector::apply);
                }
            }
            return map;
        }

        Map<TKey, TElement> map = new LinkedHashMap<>();
        try (IEnumerator<TSource> e = source.enumerator()) {
            while (e.moveNext()) {
                TSource element = e.current();
                map.merge(keySelector.apply(element), elementSelector.apply(element), resultElementSelector::apply);
            }
        }
        return map;
    }

    public static <TSource> Set<TSource> toSet(IEnumerable<TSource> source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        // Don't pre-allocate based on knowledge of size, as potentially many elements will be dropped.
        if (source instanceof ICollection) {
            ICollection<TSource> collection = (ICollection<TSource>) source;
            int capacity = collection._getCount();
            if (capacity == 0)
                return new HashSet<>();

            if (collection instanceof IArrayList) {
                IArrayList<TSource> list = (IArrayList<TSource>) collection;
                Set<TSource> set = new HashSet<>(initialCapacity(capacity));
                for (int i = 0; i < capacity; i++)
                    set.add(list.get(i));
                return set;
            }

            Set<TSource> set = new HashSet<>(initialCapacity(capacity));
            try (IEnumerator<TSource> e = source.enumerator()) {
                while (e.moveNext())
                    set.add(e.current());
            }
            return set;
        }

        Set<TSource> set = new HashSet<>();
        try (IEnumerator<TSource> e = source.enumerator()) {
            while (e.moveNext())
                set.add(e.current());
        }
        return set;
    }

    public static <TSource> Set<TSource> toLinkedSet(IEnumerable<TSource> source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        // Don't pre-allocate based on knowledge of size, as potentially many elements will be dropped.
        if (source instanceof ICollection) {
            ICollection<TSource> collection = (ICollection<TSource>) source;
            int capacity = collection._getCount();
            if (capacity == 0)
                return new LinkedHashSet<>();

            if (collection instanceof IArrayList) {
                IArrayList<TSource> list = (IArrayList<TSource>) collection;
                Set<TSource> set = new LinkedHashSet<>(initialCapacity(capacity));
                for (int i = 0; i < capacity; i++)
                    set.add(list.get(i));
                return set;
            }

            Set<TSource> set = new LinkedHashSet<>(initialCapacity(capacity));
            try (IEnumerator<TSource> e = source.enumerator()) {
                while (e.moveNext())
                    set.add(e.current());
            }
            return set;
        }

        Set<TSource> set = new LinkedHashSet<>();
        try (IEnumerator<TSource> e = source.enumerator()) {
            while (e.moveNext())
                set.add(e.current());
        }
        return set;
    }

    public static <TSource, TCollection> TCollection toCollection(IEnumerable<TSource> source, TCollection collection, Action2<TCollection, TSource> action) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (collection == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.collection);
        if (action == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.action);

        if (source instanceof IArrayList) {
            IArrayList<TSource> list = (IArrayList<TSource>) source;
            for (int i = 0, count = list._getCount(); i < count; i++)
                action.apply(collection, list.get(i));
            return collection;
        }

        try (IEnumerator<TSource> e = source.enumerator()) {
            while (e.moveNext())
                action.apply(collection, e.current());
        }
        return collection;
    }

    public static <TSource, TCollection, TResult> TResult toCollection(IEnumerable<TSource> source, TCollection collection, Action2<TCollection, TSource> action, Func1<TCollection, TResult> resultSelector) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (collection == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.collection);
        if (action == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.action);
        if (resultSelector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.resultSelector);

        if (source instanceof IArrayList) {
            IArrayList<TSource> list = (IArrayList<TSource>) source;
            for (int i = 0, count = list._getCount(); i < count; i++)
                action.apply(collection, list.get(i));
            return resultSelector.apply(collection);
        }

        try (IEnumerator<TSource> e = source.enumerator()) {
            while (e.moveNext())
                action.apply(collection, e.current());
        }
        return resultSelector.apply(collection);
    }
}
