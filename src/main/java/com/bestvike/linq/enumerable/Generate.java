package com.bestvike.linq.enumerable;

import com.bestvike.function.Func0;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.exception.ExceptionArgument;
import com.bestvike.linq.exception.ThrowHelper;

/**
 * Created by 许崇雷 on 2019-07-26.
 */
public final class Generate {
    private Generate() {
    }

    public static <TSource> IEnumerable<TSource> generate(Func0<TSource> func) {
        if (func == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.func);

        return new GenerateIterator<>(func);
    }
}


final class GenerateIterator<TSource> extends Iterator<TSource> {
    private final Func0<TSource> func;

    GenerateIterator(Func0<TSource> func) {
        this.func = func;
    }

    @Override
    public AbstractIterator<TSource> clone() {
        return new GenerateIterator<>(this.func);
    }

    @Override
    public boolean moveNext() {
        this.current = this.func.apply();
        return true;
    }
}
