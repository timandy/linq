package com.bestvike.linq.impl.partition;

import com.bestvike.collections.generic.Array;
import com.bestvike.linq.IEnumerable;

import java.util.List;

/**
 * Created by 许崇雷 on 2017-09-30.
 */
public interface IIListProvider<TElement> extends IEnumerable<TElement> {
    int _getCount(boolean onlyIfCheap);

    TElement[] _toArray(Class<TElement> clazz);

    Array<TElement> _toArray();

    List<TElement> _toList();
}
