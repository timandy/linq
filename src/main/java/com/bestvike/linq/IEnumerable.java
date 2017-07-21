package com.bestvike.linq;

import java.util.Iterator;

/**
 * @author 许崇雷
 * @date 2017/7/10
 */
public interface IEnumerable<T> extends Iterable<T> {
    IEnumerator<T> enumerator();

    default Iterator<T> iterator() {
        return this.enumerator();
    }

    default T[] toArray() {
        return null;
    }
}
