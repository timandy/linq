package com.bestvike.linq.enumerable;

import com.bestvike.function.Func0;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.exception.ExceptionArgument;
import com.bestvike.linq.exception.ThrowHelper;

/**
 * Created by 许崇雷 on 2019-07-26.
 */
public final class Infinite {
    private Infinite() {
    }

    public static <TSource> IEnumerable<TSource> infinite(TSource item) {
        return new InfiniteIterator<>(item);
    }

    public static <TSource> IEnumerable<TSource> infinite(Func0<TSource> func) {
        if (func == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.func);

        return new InfiniteIterator2<>(func);
    }
}


final class InfiniteIterator<TSource> extends AbstractIterator<TSource> {
    private final TSource item;

    InfiniteIterator(TSource item) {
        this.item = item;
    }

    @Override
    public AbstractIterator<TSource> clone() {
        return new InfiniteIterator<>(this.item);
    }

    @Override
    public boolean moveNext() {
        this.current = this.item;
        return true;
    }
}


final class InfiniteIterator2<TSource> extends AbstractIterator<TSource> {
    private final Func0<TSource> func;

    InfiniteIterator2(Func0<TSource> func) {
        this.func = func;
    }

    @Override
    public AbstractIterator<TSource> clone() {
        return new InfiniteIterator2<>(this.func);
    }

    @Override
    public boolean moveNext() {
        this.current = this.func.apply();
        return true;
    }
}
