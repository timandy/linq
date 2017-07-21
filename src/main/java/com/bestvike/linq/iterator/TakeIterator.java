package com.bestvike.linq.iterator;

import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;

/**
 * @author 许崇雷
 * @date 2017/7/16
 */
public class TakeIterator<TSource> extends AbstractIterator<TSource> {
    private IEnumerable<TSource> source;
    private int count;
    private IEnumerator<TSource> enumerator;

    public TakeIterator(IEnumerable<TSource> source, int count) {
        this.source = source;
        this.count = count;
    }

    @Override
    public AbstractIterator<TSource> clone() {
        return new TakeIterator<>(this.source, this.count);
    }

    @Override
    public boolean moveNext() {
        switch (this.state) {
            case 1:
                if (this.count <= 0) {
                    this.close();
                    return false;
                }
                this.enumerator = this.source.enumerator();
                this.state = 2;
            case 2:
                this.count--;
                if (this.count >= 0 && this.enumerator.moveNext()) {
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
