package com.bestvike.linq.enumerator;

/**
 * @author 许崇雷
 * @date 2017/7/21
 */
public final class SingletonEnumerator<TSource> extends AbstractEnumerator<TSource> {
    private TSource element;

    public SingletonEnumerator(TSource element) {
        this.element = element;
    }

    @Override
    public boolean moveNext() {
        switch (this.state) {
            case 0:
                this.current = this.element;
                this.close();
                return true;
            default:
                return false;
        }
    }
}
