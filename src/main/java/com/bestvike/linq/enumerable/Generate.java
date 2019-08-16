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

    public static <TSource> IEnumerable<TSource> generate(TSource item) {
        return new GenerateIterator<>(item);
    }

    public static <TSource> IEnumerable<TSource> generate(Func0<TSource> func) {
        if (func == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.func);

        return new GenerateIterator2<>(func);
    }
}


final class GenerateIterator<TSource> extends AbstractIterator<TSource> {
    private final TSource item;

    GenerateIterator(TSource item) {
        this.item = item;
    }

    @Override
    public AbstractIterator<TSource> clone() {
        return new GenerateIterator<>(this.item);
    }

    @Override
    public boolean moveNext() {
        this.current = this.item;
        return true;
    }
}


final class GenerateIterator2<TSource> extends AbstractIterator<TSource> {
    private final Func0<TSource> func;

    GenerateIterator2(Func0<TSource> func) {
        this.func = func;
    }

    @Override
    public AbstractIterator<TSource> clone() {
        return new GenerateIterator2<>(this.func);
    }

    @Override
    public boolean moveNext() {
        this.current = this.func.apply();
        return true;
    }
}
