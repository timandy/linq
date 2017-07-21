package com.bestvike.linq.iterator;

/**
 * @author 许崇雷
 * @date 2017/7/21
 */
public class RangeIterator extends AbstractIterator<Integer> {
    private int start;
    private int count;
    private int index;

    public RangeIterator(int start, int count) {
        this.start = start;
        this.count = count;
    }

    @Override
    public AbstractIterator<Integer> clone() {
        return new RangeIterator(this.start, this.count);
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
                    this.current = this.start + this.index;
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
