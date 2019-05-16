package com.bestvike.linq.util;

import com.bestvike.collections.generic.EqualityComparer;
import com.bestvike.collections.generic.IEqualityComparer;

import java.util.AbstractSet;
import java.util.Iterator;

/**
 * Created by 许崇雷 on 2019-05-16.
 */
public final class HashSet<E> extends AbstractSet<E> {
    private static final Object VALUE = new Object();
    private final Dictionary<E, Object> map;

    public HashSet() {
        this(null);
    }

    public HashSet(IEqualityComparer<E> comparer) {
        if (comparer == null)
            comparer = EqualityComparer.Default();
        this.map = new Dictionary<>(comparer);
    }

    @Override
    public boolean add(E e) {
        return this.map.put(e, VALUE) != VALUE;
    }

    @Override
    public boolean remove(Object o) {
        return this.map.remove(o) == VALUE;
    }

    @Override
    public Iterator<E> iterator() {
        return this.map.keySet().iterator();
    }

    @Override
    public int size() {
        return this.map.size();
    }
}
