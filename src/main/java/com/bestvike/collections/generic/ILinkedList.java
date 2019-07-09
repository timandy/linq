package com.bestvike.collections.generic;

/**
 * Created by 许崇雷 on 2019-07-09.
 */
public interface ILinkedList<T> extends ICollection<T> {
    int _indexOf(T item);

    int _lastIndexOf(T item);
}
