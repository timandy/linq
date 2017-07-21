package com.bestvike.linq.impl;

import com.bestvike.linq.ICollectionEnumerable;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.util.Array;

/**
 * @author 许崇雷
 * @date 2017/7/17
 */
public class Buffer<TElement> {
    private Array<TElement> items;
    private int count;

    public Buffer(IEnumerable<TElement> source) {
        Array<TElement> items = null;
        int count = 0;
        if (source instanceof ICollectionEnumerable) {
            ICollectionEnumerable<TElement> collection = (ICollectionEnumerable<TElement>) source;
            count = collection.internalSize();
            if (count > 0)
                items = collection.internalToArray();
        } else {
            for (TElement item : source) {
                if (items == null)
                    items = Array.create(4);
                else if (items.length() == count)
                    items.resize(Math.multiplyExact(count, 2));
                items.set(count, item);
                count++;
            }
        }
        this.items = items;
        this.count = count;
    }

    public int count() {
        return this.count;
    }

    public Array<TElement> items() {
        return this.items;
    }

    public TElement[] toArray(Class<TElement> clazz) {
        return this.items.toArray(clazz);
    }
}
