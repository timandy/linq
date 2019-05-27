package com.bestvike.linq.enumerable;

import com.bestvike.collections.generic.Array;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.exception.ExceptionArgument;
import com.bestvike.linq.exception.ThrowHelper;

import java.util.Random;

/**
 * Created by 许崇雷 on 2019-05-27.
 */
public final class Shuffle {
    private Shuffle() {
    }

    public static <TSource> IEnumerable<TSource> shuffle(IEnumerable<TSource> source) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        return new ShuffledEnumerable<>(source);
    }

    public static <TSource> IEnumerable<TSource> shuffle(IEnumerable<TSource> source, long seed) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);

        return new ShuffledEnumerable<>(source, seed);
    }
}


final class ShuffledEnumerable<TSource> implements IEnumerable<TSource> {
    private final IEnumerable<TSource> source;
    private final Long seed;

    ShuffledEnumerable(IEnumerable<TSource> source) {
        this.source = source;
        this.seed = null;
    }

    ShuffledEnumerable(IEnumerable<TSource> source, long seed) {
        this.source = source;
        this.seed = seed;
    }

    @Override
    public IEnumerator<TSource> enumerator() {
        Random rnd = this.seed == null ? new Random() : new Random(this.seed);
        Array<TSource> array = this.source.toArray();
        int count = array._getCount();
        for (int i = count - 1; i > 0; --i) {
            int j = rnd.nextInt(i + 1);
            if (i != j) {
                TSource swapped = array.get(i);
                array.set(i, array.get(j));
                array.set(j, swapped);
            }
        }
        return array.enumerator();
    }
}
