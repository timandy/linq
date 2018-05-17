package com.bestvike.linq.enumerable;

import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.util.ArrayUtils;
import com.bestvike.out;

/**
 * Created by 许崇雷 on 2018-05-07.
 */
final class Buffer<TElement> {//struct
    final Object[] items;
    final int count;

    Buffer(IEnumerable<TElement> source) {
        if (source instanceof IIListProvider) {
            IIListProvider<TElement> iterator = (IIListProvider<TElement>) source;
            this.items = iterator._toArray();
            this.count = this.items.length;
        } else {
            out<Integer> countRef = out.init();
            this.items = EnumerableHelpers.toArray(source, countRef);
            this.count = countRef.getValue();
        }
    }

    public TElement[] toArray(Class<TElement> clazz) {
        TElement[] array = ArrayUtils.newInstance(clazz, this.count);
        if (this.count > 0)
            //noinspection SuspiciousSystemArraycopy
            System.arraycopy(this.items, 0, array, 0, this.count);
        return array;
    }
}
