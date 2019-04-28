package com.bestvike.linq.adapter.enumerator;

import com.bestvike.linq.enumerable.AbstractEnumerator;

/**
 * Created by 许崇雷 on 2019-04-16.
 */
public final class LongArrayEnumerator extends AbstractEnumerator<Long> {
    private final long[] source;

    public LongArrayEnumerator(long[] source) {
        this.source = source;
    }

    @Override
    public boolean moveNext() {
        if (this.state == -1)
            return false;
        if (this.state < this.source.length) {
            this.current = this.source[this.state++];
            return true;
        }
        this.close();
        return false;
    }
}
