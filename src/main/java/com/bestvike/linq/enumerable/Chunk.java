package com.bestvike.linq.enumerable;

import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.exception.ExceptionArgument;
import com.bestvike.linq.exception.ThrowHelper;
import com.bestvike.linq.util.ArrayUtils;

import java.util.Arrays;

/**
 * Created by 许崇雷 on 2023-05-31.
 */
public final class Chunk {
    private Chunk() {
    }

    public static <TSource> IEnumerable<TSource[]> chunk(IEnumerable<TSource> source, int size, Class<TSource> clazz) {
        if (source == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.source);
        if (size < 1)
            ThrowHelper.throwArgumentOutOfRangeException(ExceptionArgument.size);
        if (clazz == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.clazz);

        return new ChunkIterator<>(source, size, clazz);
    }
}


final class ChunkIterator<TSource> extends AbstractIterator<TSource[]> {
    private final IEnumerable<TSource> source;
    private final int size;
    private final Class<TSource> clazz;
    private IEnumerator<TSource> enumerator;

    ChunkIterator(IEnumerable<TSource> source, int size, Class<TSource> clazz) {
        this.source = source;
        this.size = size;
        this.clazz = clazz;
    }

    @Override
    public AbstractIterator<TSource[]> clone() {
        return new ChunkIterator<>(this.source, this.size, this.clazz);
    }

    @Override
    public boolean moveNext() {
        switch (this.state) {
            case 1:
                this.enumerator = this.source.enumerator();
                this.state = 2;
            case 2:
                if (!this.enumerator.moveNext()) {
                    this.close();
                    return false;
                }
                TSource[] chunk = ArrayUtils.newInstance(this.clazz, this.size);
                chunk[0] = this.enumerator.current();
                int chunkSize = 1;
                while (chunkSize < chunk.length && this.enumerator.moveNext()) {
                    chunk[chunkSize] = this.enumerator.current();
                    chunkSize++;
                }
                if (chunkSize == chunk.length) {
                    this.current = chunk;
                    return true;
                }
                this.current = Arrays.copyOf(chunk, chunkSize);
                return true;
            default:
                return false;
        }
    }

    @Override
    public void close() {
        if (this.enumerator != null) {
            this.enumerator.close();
            this.enumerator = null;
        }
        super.close();
    }
}
