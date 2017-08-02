package com.bestvike.linq.enumerator;

/**
 * Created by 许崇雷 on 2017/7/21.
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
