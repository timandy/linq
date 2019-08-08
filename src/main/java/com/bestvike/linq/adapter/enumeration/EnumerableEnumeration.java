package com.bestvike.linq.adapter.enumeration;

import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.enumerable.AbstractEnumerator;

import java.util.Enumeration;
import java.util.Iterator;

/**
 * Created by 许崇雷 on 2019-07-01.
 */
public final class EnumerableEnumeration<TSource> extends AbstractEnumerator<TSource> implements Enumeration<TSource> {
    private final IEnumerable<TSource> source;
    private IEnumerator<TSource> enumerator;

    public EnumerableEnumeration(IEnumerable<TSource> source) {
        this.source = source;
    }

    @Override
    public boolean moveNext() {
        switch (this.state) {
            case 0:
                this.enumerator = this.source.enumerator();
                this.state = 1;
            case 1:
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
    public boolean hasMoreElements() {
        return this.hasNext();
    }

    @Override
    public TSource nextElement() {
        return this.next();
    }

    public Iterator<TSource> asIterator() {
        return this;
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
