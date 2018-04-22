package com.bestvike.linq.impl.partition;

import com.bestvike.out;

/**
 * Created by 许崇雷 on 2017-09-30.
 */
public interface IPartition<TElement> extends IIListProvider<TElement> {
    IPartition<TElement> _skip(int count);

    IPartition<TElement> _take(int count);

    TElement _tryGetElementAt(int index, out<Boolean> found);

    TElement _tryGetFirst(out<Boolean> found);

    TElement _tryGetLast(out<Boolean> found);
}
