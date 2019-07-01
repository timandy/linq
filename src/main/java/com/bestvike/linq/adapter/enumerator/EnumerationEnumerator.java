package com.bestvike.linq.adapter.enumerator;

import com.bestvike.linq.enumerable.AbstractEnumerator;

import java.util.Enumeration;

/**
 * Created by 许崇雷 on 2019-07-01.
 */
public final class EnumerationEnumerator<TSource> extends AbstractEnumerator<TSource> {
    private final Enumeration<TSource> source;

    public EnumerationEnumerator(Enumeration<TSource> source) {
        this.source = source;
    }

    @Override
    public boolean moveNext() {
        switch (this.state) {
            case 0:
                if (this.source.hasMoreElements()) {
                    this.current = this.source.nextElement();
                    return true;
                }
                this.close();
                return false;
            default:
                return false;
        }
    }
}
