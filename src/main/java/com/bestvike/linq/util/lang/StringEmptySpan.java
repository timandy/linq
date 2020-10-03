package com.bestvike.linq.util.lang;

import com.bestvike.linq.util.Strings;

/**
 * Created by 许崇雷 on 2020-07-24.
 */
public final class StringEmptySpan implements IStringSpan {
    StringEmptySpan() {
    }

    @Override
    public CharSequence raw() {
        return Strings.Empty;
    }

    @Override
    public int offset() {
        return 0;
    }

    @Override
    public int length() {
        return 0;
    }

    @Override
    public char charAt(int index) {
        throw new StringIndexOutOfBoundsException(index);
    }

    @Override
    public IStringSpan trim() {
        return this;
    }

    @Override
    public IStringSpan subSequence(int start) {
        if (start != 0)
            throw new StringIndexOutOfBoundsException(start);
        return this;
    }

    @Override
    public IStringSpan subSequence(int start, int end) {
        if (start != 0)
            throw new StringIndexOutOfBoundsException(start);
        if (end != 0)
            throw new StringIndexOutOfBoundsException(end);
        return this;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that)
            return true;

        if (!(that instanceof IStringSpan))
            return false;

        IStringSpan thatSpan = (IStringSpan) that;
        return thatSpan.length() == 0;
    }

    @Override
    public int hashCode() {
        return 1;
    }

    @Override
    public String toString() {
        return Strings.Empty;
    }
}
