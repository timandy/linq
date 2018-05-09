package com.bestvike.linq.iterator;

import com.bestvike.function.Func2;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.exception.Errors;

/**
 * Created by 许崇雷 on 2018-05-09.
 */
public final class Zip {
    private Zip() {
    }

    public static <TFirst, TSecond, TResult> IEnumerable<TResult> zip(IEnumerable<TFirst> first, IEnumerable<TSecond> second, Func2<TFirst, TSecond, TResult> resultSelector) {
        if (first == null)
            throw Errors.argumentNull("first");
        if (second == null)
            throw Errors.argumentNull("second");
        if (resultSelector == null)
            throw Errors.argumentNull("resultSelector");

        return new ZipIterator<>(first, second, resultSelector);
    }
}


final class ZipIterator<TFirst, TSecond, TResult> extends AbstractIterator<TResult> {
    private final IEnumerable<TFirst> first;
    private final IEnumerable<TSecond> second;
    private final Func2<TFirst, TSecond, TResult> resultSelector;
    private IEnumerator<TFirst> firstEnumerator;
    private IEnumerator<TSecond> secondEnumerator;

    ZipIterator(IEnumerable<TFirst> first, IEnumerable<TSecond> second, Func2<TFirst, TSecond, TResult> resultSelector) {
        this.first = first;
        this.second = second;
        this.resultSelector = resultSelector;
    }

    @Override
    public AbstractIterator<TResult> clone() {
        return new ZipIterator<>(this.first, this.second, this.resultSelector);
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
