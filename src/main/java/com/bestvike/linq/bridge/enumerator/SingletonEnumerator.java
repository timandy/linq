package com.bestvike.linq.bridge.enumerator;

import com.bestvike.linq.enumerable.AbstractEnumerator;

/**
 * Created by 许崇雷 on 2019-04-10.
 */
public final class SingletonEnumerator<TSource> extends AbstractEnumerator<TSource> {
    private final TSource element;

    public SingletonEnumerator(TSource element) {
        this.element = element;
    }

    @Override
    public boolean moveNext() {
        switch (this.state) {
            case 0:
                this.current = this.element;
                this.state = 1;
                return true;
            case 1:
                this.close();
                return false;
            default:
                return false;
        }
    }
}
