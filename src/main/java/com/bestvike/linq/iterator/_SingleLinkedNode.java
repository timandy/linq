package com.bestvike.linq.iterator;

import com.bestvike.collections.generic.Array;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.enumerator.ArrayEnumerator;

/**
 * Created by 许崇雷 on 2018-05-09.
 */
final class _SingleLinkedNode {
    private _SingleLinkedNode() {
    }
}


final class SingleLinkedNode<TSource> {
    private SingleLinkedNode<TSource> linked;
    private TSource item;

    public SingleLinkedNode(TSource item) {
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

    private Array<TSource> toArray(int count) {
        assert count == this.getCount();

        Array<TSource> array = Array.create(count);
        int index = count;
        for (SingleLinkedNode<TSource> node = this; node != null; node = node.linked) {
            --index;
            array.set(index, node.item);
        }

        assert index == 0;
        return array;
    }
}
