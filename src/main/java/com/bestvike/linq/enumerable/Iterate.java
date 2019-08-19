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
public final class Iterate {
    private Iterate() {
    }

    public static <TSource> IEnumerable<TSource> iterate(Predicate0 hasNext, Func0<TSource> next) {
        if (hasNext == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.hasNext);
        if (next == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.next);

        return new IterateEnumerable<>(hasNext, next);
    }
}


final class IterateEnumerable<TSource> implements IEnumerable<TSource> {
    private final Predicate0 hasNext;
    private final Func0<TSource> next;
    private boolean called;

    IterateEnumerable(Predicate0 hasNext, Func0<TSource> next) {
        this.hasNext = hasNext;
        this.next = next;
    }

    @Override
    public IEnumerator<TSource> enumerator() {
        if (this.called)
            ThrowHelper.throwRepeatInvokeException();
        this.called = true;
        return new IterateEnumerator<>(this.hasNext, this.next);
    }
}


final class IterateEnumerator<TSource> extends AbstractEnumerator<TSource> {
    private final Predicate0 hasNext;
    private final Func0<TSource> next;

    IterateEnumerator(Predicate0 hasNext, Func0<TSource> next) {
        this.hasNext = hasNext;
        this.next = next;
    }

    @Override
    public boolean moveNext() {
        switch (this.state) {
            case 0:
                if (this.hasNext.apply()) {
                    this.current = this.next.apply();
                    return true;
                }
                this.close();
                return false;
            default:
                return false;
        }
    }
}
