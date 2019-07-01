package com.bestvike.linq.adapter.enumeration;

import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.enumerable.AbstractEnumerator;

import java.util.Enumeration;

/**
 * Created by 许崇雷 on 2019-07-01.
 */
public final class EnumeratorEnumeration<TSource> extends AbstractEnumerator<TSource> implements Enumeration<TSource> {
    private IEnumerator<TSource> enumerator;

    public EnumeratorEnumeration(IEnumerator<TSource> enumerator) {
        this.enumerator = enumerator;
    }

    @Override
    public boolean hasMoreElements() {
        return this.hasNext();
    }

    @Override
    public TSource nextElement() {
        return this.next();
    }

    @Override
    public boolean moveNext() {
        switch (this.state) {
            case 0:
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
