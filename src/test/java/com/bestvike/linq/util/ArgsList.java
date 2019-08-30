package com.bestvike.linq.util;

import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.adapter.enumerator.IterableEnumerator;
import com.bestvike.linq.exception.ArgumentNullException;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by 许崇雷 on 2019-08-29.
 */
public final class ArgsList implements IEnumerable<Object[]> {
    private final List<Object[]> source;

    public ArgsList() {
        this.source = new LinkedList<>();
    }

    @Override
    public IEnumerator<Object[]> enumerator() {
        return new IterableEnumerator<>(this.source);
    }

    public void add(Object... args) {
        if (args == null)
            throw new ArgumentNullException("args");
        this.source.add(args);
    }
}
