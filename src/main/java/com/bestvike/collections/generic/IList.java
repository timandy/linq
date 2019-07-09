package com.bestvike.collections.generic;

import java.util.RandomAccess;

/**
 * Created by 许崇雷 on 2018-04-27.
 */
public interface IList<T> extends ILinkedList<T>, RandomAccess {
    T get(int index);
}
