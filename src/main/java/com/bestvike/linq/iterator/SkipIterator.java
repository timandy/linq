package com.bestvike.linq.iterator;

import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;

/**
 * @author 许崇雷
 * @date 2017/7/17
 */
public class SkipIterator<TSource> extends AbstractIterator<TSource> {
    private IEnumerable<TSource> source;
    private int count;
    private IEnumerator<TSource> enumerator;

    public SkipIterator(IEnumerable<TSource> source, int count) {
        this.source = source;
        this.count = count;
    }

    @Override
    public AbstractIterator<TSource> clone() {
        return new SkipIterator<>(this.source, this.count);
    }

    @Override
    public boolean moveNext() {
        switch (this.state) {
            case 1:
                this.enumerator = this.source.enumerator();
                while (this.count > 0 && this.enumerator.moveNext())
                    this.count--;
                if (this.count > 0) {
                    this.close();
                    return false;
                }
                this.state = 2;
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
