package com.bestvike.linq.iterator;

import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;

/**
 * @author 许崇雷
 * @date 2017/7/18
 */
public class CastIterator<TResult> extends AbstractIterator<TResult> {
    private final IEnumerable source;
    private final Class<TResult> clazz;
    private IEnumerator enumerator;

    public CastIterator(IEnumerable source, Class<TResult> clazz) {
        this.source = source;
        this.clazz = clazz;
    }

    @Override
    public AbstractIterator<TResult> clone() {
        return new CastIterator<>(this.source, this.clazz);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean moveNext() {
        switch (this.state) {
            case 1:
                this.enumerator = this.source.enumerator();
                this.state = 2;
            case 2:
                if (this.enumerator.moveNext()) {
                    this.current = (TResult) this.enumerator.current();
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
