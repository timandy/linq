package com.bestvike.collections.generic;

import com.bestvike.linq.IEnumerable;

import java.util.Collection;
import java.util.List;

/**
 * Created by 许崇雷 on 2017-09-30.
 */
public interface ICollection<T> extends IEnumerable<T> {
    Collection<T> getCollection();

    int _getCount();

    boolean _contains(T item);

    void _copyTo(Object[] array, int arrayIndex);

    T[] _toArray(Class<T> clazz);

    Object[] _toArray();

    List<T> _toList();
}
