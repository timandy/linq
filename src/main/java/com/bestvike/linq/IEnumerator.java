package com.bestvike.linq;

import com.bestvike.IDisposable;

import java.util.Iterator;
import java.util.function.Consumer;

/**
 * Created by 许崇雷 on 2017-07-10.
 */
public interface IEnumerator<T> extends Iterator<T>, IDisposable {
    boolean moveNext();

    T current();

    boolean hasNext();

    T next();

    void forEachRemaining(Consumer<? super T> action);

    void remove();

    void reset();

    void close();
}
