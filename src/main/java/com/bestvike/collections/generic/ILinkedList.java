package com.bestvike.collections.generic;

import com.bestvike.function.Predicate1;

/**
 * Created by 许崇雷 on 2019-07-09.
 */
public interface ILinkedList<T> extends ICollection<T> {
    int _indexOf(T item);

    int _lastIndexOf(T item);

    int _findIndex(Predicate1<T> match);

    int _findLastIndex(Predicate1<T> match);
}
