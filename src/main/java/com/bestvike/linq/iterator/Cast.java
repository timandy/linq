package com.bestvike.linq.iterator;

import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.exception.Errors;

/**
 * Created by 许崇雷 on 2018-04-23.
 */
public final class Cast {
    private Cast() {
    }

    public static <TResult> IEnumerable<TResult> ofType(IEnumerable source, Class<TResult> clazz) {
        if (source == null)
            throw Errors.argumentNull("source");
        if (clazz == null)
            throw Errors.argumentNull("clazz");

        return new OfTypeIterator<>(source, clazz);
    }

    public static <TResult> IEnumerable<TResult> cast(IEnumerable source, Class<TResult> clazz) {
        if (source == null)
            throw Errors.argumentNull("source");
        if (clazz == null)
            throw Errors.argumentNull("clazz");

        return new CastIterator<>(source, clazz);
    }
}


final class OfTypeIterator<TResult> extends AbstractIterator<TResult> {
    private final IEnumerable source;
    private final Class<TResult> clazz;
    private IEnumerator enumerator;

    OfTypeIterator(IEnumerable source, Class<TResult> clazz) {
        this.source = source;
        this.clazz = clazz;
    }

    @Override
    public AbstractIterator<TResult> clone() {
        return new OfTypeIterator<>(this.source, this.clazz);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean moveNext() {
        switch (this.state) {
            case 1:
                this.enumerator = this.source.enumerator();
                this.state = 2;
            case 2:
                while (this.enumerator.moveNext()) {
                    Object item = this.enumerator.current();
                    if (this.clazz.isInstance(item)) {
                        this.current = (TResult) item;
                        return true;
                    }
                }
                this.close();
                return false;
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


final class CastIterator<TResult> extends AbstractIterator<TResult> {
    private final IEnumerable source;
    private final Class<TResult> clazz;
    private IEnumerator enumerator;

    CastIterator(IEnumerable source, Class<TResult> clazz) {
        this.source = source;
        this.clazz = clazz;
    }

    @Override
    public AbstractIterator<TResult> clone() {
        return new CastIterator<>(this.source, this.clazz);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean moveNext() {
        switch (this.state) {
            case 1:
                this.enumerator = this.source.enumerator();
                this.state = 2;
            case 2:
                if (this.enumerator.moveNext()) {
                    this.current = this.clazz.cast(this.enumerator.current());
                    return true;
                }
                this.close();
                return false;
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
