package com.bestvike.linq.adapter.enumerable;

import com.bestvike.collections.generic.IList;
import com.bestvike.function.Predicate1;
import com.bestvike.linq.exception.ExceptionArgument;
import com.bestvike.linq.exception.ThrowHelper;

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

    @Override
    public int _findIndex(Predicate1<TSource> match) {
        if (match == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.match);
        for (int i = 0, length = this.source.size(); i < length; i++) {
            if (match.apply(this.source.get(i)))
                return i;
        }
        return -1;
    }

    @Override
    public int _findLastIndex(Predicate1<TSource> match) {
        if (match == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.match);
        for (int i = this.source.size() - 1; i >= 0; i--) {
            if (match.apply(this.source.get(i)))
                return i;
        }
        return -1;
    }
}
