package com.bestvike.linq;

import java.util.Iterator;

/**
 * @author 许崇雷
 * @date 2017/7/10
 */
public interface IEnumerator<T> extends Iterator<T>, AutoCloseable {
    boolean moveNext();

    T current();

    boolean hasNext();

    T next();

    void reset();

    void close();
}
