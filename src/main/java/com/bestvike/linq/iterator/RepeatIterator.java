package com.bestvike.linq.iterator;

/**
 * Created by 许崇雷 on 2017/7/21.
 */
final class RepeatIterator<TSource> extends AbstractIterator<TSource> {
    private final TSource element;
    private final int count;
    private int index;

    public RepeatIterator(TSource element, int count) {
        this.element = element;
        this.count = count;
    }

    @Override
    public AbstractIterator<TSource> clone() {
        return new RepeatIterator<>(this.element, this.count);
    }

    @Override
    public boolean moveNext() {
        switch (this.state) {
            case 1:
                this.index = -1;
                this.state = 2;
            case 2:
                this.index++;
                if (this.index < this.count) {
                    this.current = this.element;
                    return true;
                }
                this.close();
                return false;
            default:
                return false;
        }
    }
}
