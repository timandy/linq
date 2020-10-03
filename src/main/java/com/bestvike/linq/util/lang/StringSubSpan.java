package com.bestvike.linq.util.lang;

/**
 * Created by 许崇雷 on 2020-07-24.
 */
public final class StringSubSpan implements IStringSpan {
    private final CharSequence value;
    private final int offset;
    private final int length;

    StringSubSpan(CharSequence value, int offset, int length) {
        this.value = value;
        this.offset = offset;
        this.length = length;
    }

    @Override
    public CharSequence raw() {
        return this.value;
    }

    @Override
    public int offset() {
        return this.offset;
    }

    @Override
    public int length() {
        return this.length;
    }

    @Override
    public char charAt(int index) {
        return this.value.charAt(this.offset + index);
    }

    @Override
    public IStringSpan trim() {
        final CharSequence value = this.value;
        final int length = this.length;

        int start;
        int end = this.offset + length - 1;

        //trim head
        for (start = this.offset; start <= end; start++) {
            if (!Character.isWhitespace(value.charAt(start)))
                break;
        }

        //trim tail
        for (; end >= start; end--) {
            if (!Character.isWhitespace(value.charAt(end)))
                break;
        }

        //return
        int len = end - start + 1;
        return len == length ? this :
                len == 0 ? StringSpan.Empty : new StringSubSpan(this.value, start, len);
    }

    @Override
    public IStringSpan subSequence(int start) {
        return this.subSequence(start, this.length);
    }

    @Override
    public IStringSpan subSequence(int start, int end) {
        if (start < 0)
            throw new StringIndexOutOfBoundsException(start);

        int length = this.length;
        if (end > length)
            throw new StringIndexOutOfBoundsException(end);

        int subLen = end - start;
        if (subLen < 0)
            throw new StringIndexOutOfBoundsException(subLen);

        return start == 0 && end == length ? this : new StringSubSpan(this.value, this.offset + start, subLen);
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
        return this.value.subSequence(this.offset, this.offset + this.length).toString();
    }
}
