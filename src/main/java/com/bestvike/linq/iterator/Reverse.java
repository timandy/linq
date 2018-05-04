package com.bestvike.linq.iterator;

import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.exception.Errors;

/**
 * Created by 许崇雷 on 2018-05-03.
 */
public final class Reverse {
    private Reverse() {
    }

    public static <TSource> IEnumerable<TSource> reverse(IEnumerable<TSource> source) {
        if (source == null) throw Errors.argumentNull("source");
        return new ReverseIterator<>(source);
    }
}
