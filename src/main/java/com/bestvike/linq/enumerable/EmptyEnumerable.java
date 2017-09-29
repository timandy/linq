package com.bestvike.linq.enumerable;

import com.bestvike.collections.generic.Array;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.IListEnumerable;
import com.bestvike.linq.enumerator.EmptyEnumerator;
import com.bestvike.linq.exception.Errors;
import com.bestvike.linq.util.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 许崇雷 on 2017/7/11.
 */
public final class EmptyEnumerable<TElement> implements IListEnumerable<TElement> {
    private EmptyEnumerable() {
    }

    public static <TElement> IEnumerable<TElement> Instance() {
        return new EmptyEnumerable<>();
    }

    @Override
    public TElement internalGet(int index) {
        throw Errors.argumentOutOfRange("index");
    }

    @Override
    public int internalSize() {
        return 0;
    }

    @Override
    public boolean internalContains(TElement value) {
        return false;
    }

    @Override
    public Array<TElement> internalToArray() {
        return Array.empty();
    }

    @Override
    public TElement[] internalToArray(Class<TElement> clazz) {
        return ArrayUtils.newInstance(clazz, 0);
    }

    @Override
    public List<TElement> internalToList() {
        return new ArrayList<>();
    }

    @Override
    public IEnumerator<TElement> enumerator() {
        return EmptyEnumerator.Instance();
    }
}
