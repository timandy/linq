package com.bestvike.collections.generic;

/**
 * Created by 许崇雷 on 2017-09-30.
 */
public interface ICollection<T> {
    int _getCount();

    boolean _contains(T item);

    void _copyTo(T[] array, int arrayIndex);

    void _copyTo(Array<T> array, int arrayIndex);
}
