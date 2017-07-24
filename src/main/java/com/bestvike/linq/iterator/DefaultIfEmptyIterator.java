package com.bestvike.linq.iterator;

import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;

/**
 * @author 许崇雷
 * @date 2017/7/18
 */
final class DefaultIfEmptyIterator<TSource> extends AbstractIterator<TSource> {
    private final IEnumerable<TSource> source;
    private final TSource defaultValue;
    private IEnumerator<TSource> enumerator;

    public DefaultIfEmptyIterator(IEnumerable<TSource> source, TSource defaultValue) {
        this.source = source;
        this.defaultValue = defaultValue;
    }

    @Override
    public AbstractIterator<TSource> clone() {
        return new DefaultIfEmptyIterator<>(this.source, this.defaultValue);
    }

    @Override
    public boolean moveNext() {
        switch (this.state) {
            case 1:
                this.enumerator = this.source.enumerator();
                if (this.enumerator.moveNext()) {
                    this.current = this.enumerator.current();
                    this.state = 2;
                    return true;
                }
                this.current = this.defaultValue;
                this.state = 3;
                return true;
            case 2:
                if (this.enumerator.moveNext()) {
                    this.current = this.enumerator.current();
                    return true;
                }
                this.state = 3;
            case 3:
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
