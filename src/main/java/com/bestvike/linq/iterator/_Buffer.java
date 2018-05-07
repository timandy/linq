package com.bestvike.linq.iterator;

import com.bestvike.collections.generic.Array;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.util.ArrayUtils;
import com.bestvike.out;

/**
 * Created by 许崇雷 on 2018-05-07.
 */
final class _Buffer {
    private _Buffer() {
    }
}

final class Buffer<TElement> {//struct
    private final Array<TElement> items;
    private final int count;

    Buffer(IEnumerable<TElement> source) {
        if (source instanceof IIListProvider) {
            IIListProvider<TElement> iterator = (IIListProvider<TElement>) source;
            this.items = iterator._toArray();
            this.count = this.items.length();
        } else {
            out<Integer> countRef = out.init();
            this.items = EnumerableHelpers.toArray(source, countRef);
            this.count = countRef.getValue();
        }
    }

    public Array<TElement> getItems() {
        return this.items;
    }

    public int getCount() {
        return this.count;
    }

    public TElement[] toArray(Class<TElement> clazz) {
        TElement[] array = ArrayUtils.newInstance(clazz, this.count);
        if (this.count > 0)
            Array.copy(this.items, 0, array, 0, this.count);
        return array;
    }
}
