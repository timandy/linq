package com.bestvike.linq.iterator;

/**
 * @author 许崇雷
 * @date 2017/7/21
 */
public class RepeatIterator<TSource> extends AbstractIterator<TSource> {
    private TSource element;
    private int count;
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

    @Override
    public void close() {
        super.close();
    }
}