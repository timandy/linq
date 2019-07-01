package com.bestvike.linq.enumerable;

import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.adapter.enumeration.EnumeratorEnumeration;
import com.bestvike.linq.exception.ExceptionArgument;
import com.bestvike.linq.exception.ThrowHelper;

import java.util.Enumeration;

/**
 * Created by 许崇雷 on 2019-07-01.
 */
public final class ToEnumeration {
    private ToEnumeration() {
    }

    public static <TSource> Enumeration<TSource> toEnumeration(IEnumerable<TSource> source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        return new EnumeratorEnumeration<>(source.enumerator());
    }
}
