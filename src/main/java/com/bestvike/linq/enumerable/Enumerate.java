package com.bestvike.linq.enumerable;

import com.bestvike.function.Func0;
import com.bestvike.function.Predicate0;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.exception.ExceptionArgument;
import com.bestvike.linq.exception.ThrowHelper;

/**
 * Created by 许崇雷 on 2019-08-19.
 */
public final class Enumerate {
    private Enumerate() {
    }

    public static <TSource> IEnumerable<TSource> enumerate(Predicate0 moveNext, Func0<TSource> current) {
        if (moveNext == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.moveNext);
        if (current == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.current);

        return new EnumerateEnumerable<>(moveNext, current);
    }
}


final class EnumerateEnumerable<TSource> implements IEnumerable<TSource> {
    private final Predicate0 moveNext;
    private final Func0<TSource> current;
    private boolean called;

    EnumerateEnumerable(Predicate0 moveNext, Func0<TSource> current) {
        this.moveNext = moveNext;
        this.current = current;
    }

    @Override
    public IEnumerator<TSource> enumerator() {
        if (this.called)
            ThrowHelper.throwRepeatInvokeException();
        this.called = true;
        return new EnumerateEnumerator<>(this.moveNext, this.current);
    }
}


final class EnumerateEnumerator<TSource> extends AbstractEnumerator<TSource> {
    private final Predicate0 moveNext;
    private final Func0<TSource> current;

    EnumerateEnumerator(Predicate0 moveNext, Func0<TSource> current) {
        this.moveNext = moveNext;
        this.current = current;
    }

    @Override
    public boolean moveNext() {
        return this.moveNext.apply();
    }

    @Override
    public TSource current() {
        return this.current.apply();
    }
}
