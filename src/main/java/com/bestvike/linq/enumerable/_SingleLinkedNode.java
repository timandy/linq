package com.bestvike.linq.enumerable;

import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.adapter.enumerator.ArrayEnumerator;

/**
 * Created by 许崇雷 on 2018-05-09.
 */
final class SingleLinkedNode<TSource> {
    private final TSource item;
    private final SingleLinkedNode<TSource> linked;

    SingleLinkedNode(TSource item) {
        this.linked = null;
        this.item = item;
    }

    private SingleLinkedNode(SingleLinkedNode<TSource> linked, TSource item) {
        assert linked != null;
        this.linked = linked;
        this.item = item;
    }

    public TSource getItem() {
        return this.item;
    }

    public SingleLinkedNode<TSource> getLinked() {
        return this.linked;
    }

    public SingleLinkedNode<TSource> add(TSource item) {
        return new SingleLinkedNode<>(this, item);
    }

    public int getCount() {
        int count = 0;
        for (SingleLinkedNode<TSource> node = this; node != null; node = node.linked)
            count++;
        return count;
    }

    public IEnumerator<TSource> enumerator(int count) {
        return new ArrayEnumerator<>(this.toArray(count));
    }

    public SingleLinkedNode<TSource> getNode(int index) {
        assert index >= 0 && index < this.getCount();

        SingleLinkedNode<TSource> node = this;
        for (; index > 0; index--)
            node = node.linked;

        assert node != null;
        return node;
    }

    private Object[] toArray(int count) {
        assert count == this.getCount();

        Object[] array = new Object[count];
        int index = count;
        for (SingleLinkedNode<TSource> node = this; node != null; node = node.linked) {
            --index;
            array[index] = node.item;
        }

        assert index == 0;
        return array;
    }
}
