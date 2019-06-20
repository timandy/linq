package com.bestvike.linq.enumerable;

import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.exception.ExceptionArgument;
import com.bestvike.linq.exception.ThrowHelper;
import com.bestvike.linq.util.Formatter;

/**
 * Created by 许崇雷 on 2019-06-20.
 */
public final class Format {
    private Format() {
    }

    public static <TSource> String format(IEnumerable<TSource> source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        return Formatter.DEFAULT.format(source);
    }

    public static <TSource> String format(IEnumerable<TSource> source, Formatter formatter) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (formatter == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.formatter);

        return formatter.format(source);
    }
}
