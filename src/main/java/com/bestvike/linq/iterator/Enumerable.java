package com.bestvike.linq.iterator;

import com.bestvike.collections.generic.Array;
import com.bestvike.linq.IEnumerable;

/**
 * Created by 许崇雷 on 2018-04-27.
 */
public final class Enumerable {
    private Enumerable() {
    }

    public static <TSource> IEnumerable<TSource> asEnumerable(IEnumerable<TSource> source) {
        return source;
    }

    public static <TResult> IEnumerable<TResult> empty() {
        return Array.empty();
    }
}
