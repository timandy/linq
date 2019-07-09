package com.bestvike.linq.adapter.enumerable;

import com.bestvike.collections.generic.IList;

import java.util.List;

/**
 * Created by 许崇雷 on 2019-07-09.
 */
public final class ArrayListEnumerable<TSource> extends LinkedListEnumerable<TSource> implements IList<TSource> {
    public ArrayListEnumerable(List<TSource> source) {
        super(source);
    }

    @Override
    public TSource get(int index) {
        return this.source.get(index);
    }
}
