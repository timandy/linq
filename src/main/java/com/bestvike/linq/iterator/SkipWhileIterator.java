package com.bestvike.linq.iterator;

import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.function.Func1;

/**
 * @author 许崇雷
 * @date 2017/7/17
 */
public class SkipWhileIterator<TSource> extends AbstractIterator<TSource> {
    private final IEnumerable<TSource> source;
    private final Func1<TSource, Boolean> predicate;
    private IEnumerator<TSource> enumerator;

    public SkipWhileIterator(IEnumerable<TSource> source, Func1<TSource, Boolean> predicate) {
        this.source = source;
        this.predicate = predicate;
    }

    @Override
    public AbstractIterator<TSource> clone() {
        return new SkipWhileIterator<>(this.source, this.predicate);
    }

    @Override
    public boolean moveNext() {
        switch (this.state) {
            case 1:
                this.enumerator = this.source.enumerator();
                while (this.enumerator.moveNext()) {
                    TSource item = this.enumerator.current();
                    if (!this.predicate.apply(item)) {
                        this.current = item;
                        this.state = 2;
                        return true;
                    }
                }
                this.close();
                return false;
            case 2:
                if (this.enumerator.moveNext()) {
                    this.current = this.enumerator.current();
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
        if (this.enumerator != null) {
            this.enumerator.close();
            this.enumerator = null;
        }
        super.close();
    }
}
