package com.bestvike.linq.iterator;

import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;

/**
 * Created by 许崇雷 on 2017/7/21.
 */
final class AppendIterator<TSource> extends AbstractIterator<TSource> {
    private final IEnumerable<TSource> source;
    private final TSource element;
    private IEnumerator<TSource> enumerator;

    public AppendIterator(IEnumerable<TSource> source, TSource element) {
        this.source = source;
        this.element = element;
    }

    @Override
    public AbstractIterator<TSource> clone() {
        return new AppendIterator<>(this.source, this.element);
    }

    @Override
    public boolean moveNext() {
        switch (this.state) {
            case 1:
                this.enumerator = this.source.enumerator();
                this.state = 2;
            case 2:
                if (this.enumerator.moveNext()) {
                    this.current = this.enumerator.current();
                    return true;
                }
                this.enumerator.close();
                this.enumerator = null;
                this.state = 3;
            case 3:
                this.current = this.element;
                this.state = 4;
                return true;
            case 4:
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
