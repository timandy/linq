package com.bestvike.collections.generic;

import java.util.RandomAccess;

/**
 * Created by 许崇雷 on 2018-04-27.
 */
public interface IList<T> extends ICollection<T>, RandomAccess {
    T get(int index);

    int _indexOf(T item);

    int _lastIndexOf(T item);
}
