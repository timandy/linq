package com.bestvike.linq.util.lang;

import com.bestvike.linq.exception.ExceptionArgument;
import com.bestvike.linq.exception.ThrowHelper;

/**
 * Created by 许崇雷 on 2020-07-24.
 */
public final class StringSpan implements IStringSpan {
    public static final StringEmptySpan Empty = new StringEmptySpan();
    private final CharSequence value;

    StringSpan(CharSequence value) {
        this.value = value;
    }

    public static IStringSpan asSpan(CharSequence value) {
        if (value == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.value);

        return value.length() == 0 ? Empty : new StringSpan(value);
    }

    public static IStringSpan asSpan(CharSequence value, int startIndex) {
        if (value == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.value);

        return asSpan(value, startIndex, value.length() - startIndex);
    }

    public static IStringSpan asSpan(CharSequence value, int startIndex, int length) {
        if (value == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.value);
        if (startIndex < 0)
            ThrowHelper.throwArgumentOutOfRangeException(ExceptionArgument.startIndex);
        final int valueLength = value.length();
        if (startIndex > valueLength)
            ThrowHelper.throwArgumentOutOfRangeException(ExceptionArgument.startIndex);
        if (length < 0)
            ThrowHelper.throwArgumentOutOfRangeException(ExceptionArgument.length);
        if (startIndex > valueLength - length)
            ThrowHelper.throwArgumentOutOfRangeException(ExceptionArgument.length);

        if (length == 0)
            return Empty;
        if (startIndex == 0 && length == valueLength)
            return new StringSpan(value);
        return new StringSubSpan(value, startIndex, length);
    }

    @Override
    public CharSequence raw() {
        return this.value;
    }

    @Override
    public int offset() {
        return 0;
    }

    @Override
    public int length() {
        return this.value.length();
    }

    @Override
    public char charAt(int index) {
        return this.value.charAt(index);
    }

    @Override
    public IStringSpan trim() {
        final CharSequence value = this.value;
        final int length = value.length();

        int start;
        int end;

        //trim head
        for (start = 0; start < length; start++) {
            if (!Character.isWhitespace(value.charAt(start)))
                break;
        }

        //trim tail
        for (end = length - 1; end >= start; end--) {
            if (!Character.isWhitespace(value.charAt(end)))
                break;
        }

        //return
        int len = end - start + 1;
        return len == length ? this :
                len == 0 ? Empty : new StringSubSpan(this, start, len);
    }

    @Override
    public IStringSpan subSequence(int start) {
        return this.subSequence(start, this.value.length());
    }

    @Override
    public IStringSpan subSequence(int start, int end) {
        if (start < 0)
            throw new StringIndexOutOfBoundsException(start);

        int length = this.value.length();
        if (end > length)
            throw new StringIndexOutOfBoundsException(end);

        int subLen = end - start;
        if (subLen < 0)
            throw new StringIndexOutOfBoundsException(subLen);

        return start == 0 && end == length ? this : new StringSubSpan(this.value, start, subLen);
    }

    @Override
    public boolean equals(Object that) {
        if (this == that)
            return true;

        if (!(that instanceof IStringSpan))
            return false;

        IStringSpan thatSpan = (IStringSpan) that;
        int n = this.length();
        if (n != thatSpan.length())
            return false;

        int i = 0;
        while (n-- != 0) {
            if (this.charAt(i) != thatSpan.charAt(i))
                return false;
            i++;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        for (int i = 0, length = this.length(); i < length; i++)
            result = prime * result + this.charAt(i);
        return result;
    }

    @Override
    public String toString() {
        return this.value.toString();
    }
}
