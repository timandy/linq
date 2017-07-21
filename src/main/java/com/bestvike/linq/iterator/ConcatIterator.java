package com.bestvike.linq.iterator;

import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;

/**
 * @author 许崇雷
 * @date 2017/7/18
 */
public class ConcatIterator<TSource> extends AbstractIterator<TSource> {
    private final IEnumerable<TSource> first;
    private final IEnumerable<TSource> second;
    private IEnumerator<TSource> enumerator;

    public ConcatIterator(IEnumerable<TSource> first, IEnumerable<TSource> second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public AbstractIterator<TSource> clone() {
        return new ConcatIterator<>(this.first, this.second);
    }

    @Override
    public boolean moveNext() {
        switch (this.state) {
            case 1:
                this.enumerator = this.first.enumerator();
                this.state = 2;
            case 2:
                if (this.enumerator.moveNext()) {
                    this.current = this.enumerator.current();
                    return true;
                }
                this.enumerator.close();
                this.enumerator = this.second.enumerator();
                this.state = 3;
            case 3:
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
