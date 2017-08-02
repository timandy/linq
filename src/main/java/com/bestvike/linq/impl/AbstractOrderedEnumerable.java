package com.bestvike.linq.impl;

import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.IOrderedEnumerable;
import com.bestvike.linq.enumerator.AbstractEnumerator;
import com.bestvike.linq.function.Func1;

import java.util.Comparator;

/**
 * Created by 许崇雷 on 2017/7/17.
 */
public abstract class AbstractOrderedEnumerable<TElement> implements IOrderedEnumerable<TElement> {
    protected IEnumerable<TElement> source;

    protected abstract AbstractEnumerableSorter<TElement> getEnumerableSorter(AbstractEnumerableSorter<TElement> next);

    @Override
    public IEnumerator<TElement> enumerator() {
        return new OrderedEnumerableEnumerator();
    }

    @Override
    public <TKey> IOrderedEnumerable<TElement> createOrderedEnumerable(Func1<TElement, TKey> keySelector, Comparator<TKey> comparer, boolean descending) {
        OrderedEnumerable<TElement, TKey> result = new OrderedEnumerable<>(this.source, keySelector, comparer, descending);
        result.parent = this;
        return result;
    }

    private class OrderedEnumerableEnumerator extends AbstractEnumerator<TElement> {
        private Buffer<TElement> buffer;
        private int[] map;
        private int index;

        @Override
        public boolean moveNext() {
            switch (this.state) {
                case 0:
                    this.buffer = new Buffer<>(AbstractOrderedEnumerable.this.source);
                    if (this.buffer.count() <= 0) {
                        this.close();
                        return false;
                    }
                    AbstractEnumerableSorter<TElement> sorter = AbstractOrderedEnumerable.this.getEnumerableSorter(null);
                    this.map = sorter.sort(this.buffer.items(), this.buffer.count());
                    this.index = -1;
                    this.state = 1;
                case 1:
                    this.index++;
                    if (this.index < this.buffer.count()) {
                        this.current = this.buffer.items().get(this.map[this.index]);
                        return true;
                    }
                    this.close();
                    return false;
                default:
                    return false;
            }
        }
    }
}
