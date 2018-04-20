package com.bestvike.linq;

import com.bestvike.IDisposable;

import java.util.Iterator;

/**
 * Created by 许崇雷 on 2017/7/10.
 */
public interface IEnumerator<T> extends Iterator<T>, IDisposable {
    boolean moveNext();

    T current();

    boolean hasNext();

    T next();

    void reset();

    void close();
}
