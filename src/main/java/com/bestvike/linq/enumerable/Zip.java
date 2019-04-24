package com.bestvike.linq.enumerable;

import com.bestvike.function.Func2;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.exception.ExceptionArgument;
import com.bestvike.linq.exception.ThrowHelper;
import com.bestvike.tuple.Tuple2;

/**
 * Created by 许崇雷 on 2018-05-09.
 */
public final class Zip {
    private Zip() {
    }

    public static <TFirst, TSecond> IEnumerable<Tuple2<TFirst, TSecond>> zip(IEnumerable<TFirst> first, IEnumerable<TSecond> second) {
        if (first == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.first);
        if (second == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.second);

        return new ZipIterator<>(first, second);
    }

    public static <TFirst, TSecond, TResult> IEnumerable<TResult> zip(IEnumerable<TFirst> first, IEnumerable<TSecond> second, Func2<TFirst, TSecond, TResult> resultSelector) {
        if (first == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.first);
        if (second == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.second);
        if (resultSelector == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.resultSelector);

        return new ZipIterator2<>(first, second, resultSelector);
    }
}


final class ZipIterator<TFirst, TSecond> extends AbstractIterator<Tuple2<TFirst, TSecond>> {
    private final IEnumerable<TFirst> first;
    private final IEnumerable<TSecond> second;
    private IEnumerator<TFirst> firstEnumerator;
    private IEnumerator<TSecond> secondEnumerator;

    ZipIterator(IEnumerable<TFirst> first, IEnumerable<TSecond> second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public AbstractIterator<Tuple2<TFirst, TSecond>> clone() {
        return new ZipIterator<>(this.first, this.second);
    }

    @Override
    public boolean moveNext() {
        switch (this.state) {
            case 1:
                this.firstEnumerator = this.first.enumerator();
                this.secondEnumerator = this.second.enumerator();
                this.state = 2;
            case 2:
                if (this.firstEnumerator.moveNext() && this.secondEnumerator.moveNext()) {
                    this.current = new Tuple2<>(this.firstEnumerator.current(), this.secondEnumerator.current());
                    return true;
                }
                this.close();
                return false;
            default:
                return false;
        }
    }

    @Override
    public void close() {
        if (this.firstEnumerator != null) {
            this.firstEnumerator.close();
            this.firstEnumerator = null;
        }
        if (this.secondEnumerator != null) {
            this.secondEnumerator.close();
            this.secondEnumerator = null;
        }
        super.close();
    }
}


final class ZipIterator2<TFirst, TSecond, TResult> extends AbstractIterator<TResult> {
    private final IEnumerable<TFirst> first;
    private final IEnumerable<TSecond> second;
    private final Func2<TFirst, TSecond, TResult> resultSelector;
    private IEnumerator<TFirst> firstEnumerator;
    private IEnumerator<TSecond> secondEnumerator;

    ZipIterator2(IEnumerable<TFirst> first, IEnumerable<TSecond> second, Func2<TFirst, TSecond, TResult> resultSelector) {
        this.first = first;
        this.second = second;
        this.resultSelector = resultSelector;
    }

    @Override
    public AbstractIterator<TResult> clone() {
        return new ZipIterator2<>(this.first, this.second, this.resultSelector);
    }

    @Override
    public boolean moveNext() {
        switch (this.state) {
            case 1:
                this.firstEnumerator = this.first.enumerator();
                this.secondEnumerator = this.second.enumerator();
                this.state = 2;
            case 2:
                if (this.firstEnumerator.moveNext() && this.secondEnumerator.moveNext()) {
                    this.current = this.resultSelector.apply(this.firstEnumerator.current(), this.secondEnumerator.current());
                    return true;
                }
                this.close();
                return false;
            default:
                return false;
        }
    }

    @Override
    public void close() {
        if (this.firstEnumerator != null) {
            this.firstEnumerator.close();
            this.firstEnumerator = null;
        }
        if (this.secondEnumerator != null) {
            this.secondEnumerator.close();
            this.secondEnumerator = null;
        }
        super.close();
    }
}
