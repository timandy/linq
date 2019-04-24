package com.bestvike.linq.enumerable;

import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.bridge.enumerable.RunOnceEnumerable;
import com.bestvike.linq.exception.ExceptionArgument;
import com.bestvike.linq.exception.ThrowHelper;

/**
 * Created by 许崇雷 on 2018-05-11.
 */
public final class RunOnce {
    private RunOnce() {
    }

    public static <TSource> IEnumerable<TSource> runOnce(IEnumerable<TSource> source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        return new RunOnceEnumerable<>(source);
    }
}
