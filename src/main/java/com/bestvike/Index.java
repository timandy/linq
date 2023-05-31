package com.bestvike;

import com.bestvike.linq.exception.ExceptionArgument;
import com.bestvike.linq.exception.ThrowHelper;

/**
 * Represent a type can be used to index a collection either from the start or the end.
 * <p>
 * <pre>
 * {@code
 * List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
 * Integer lastElement = Linq.of(list).elementAt(Index.fromEnd(1)); // lastElement = 5
 * }
 * </pre>
 * <p>
 * Created by 许崇雷 on 2023-05-31.
 */
public final class Index {
    public static final Index Start = new Index(0);
    public static final Index End = new Index(~0);
    private final int value;

    // The following private constructors mainly created for perf reason to avoid the checks
    private Index(int value) {
        this.value = value;
    }

    public Index(int value, boolean fromEnd) {
        if (value < 0)
            ThrowHelper.throwArgumentOutOfRangeException(ExceptionArgument.value);
        if (fromEnd) {
            this.value = ~value;
        } else {
            this.value = value;
        }
    }

    public static Index fromStart(int value) {
        if (value < 0)
            ThrowHelper.throwArgumentOutOfRangeException(ExceptionArgument.value);
        return new Index(value);
    }

    public static Index fromEnd(int value) {
        if (value < 0)
            ThrowHelper.throwArgumentOutOfRangeException(ExceptionArgument.value);
        return new Index(~value);
    }

    public int getValue() {
        if (this.value < 0)
            return ~this.value;
        else
            return this.value;
    }

    public boolean isFromEnd() {
        return this.value < 0;
    }

    public int getOffset(int length) {
        int offset = this.value;
        if (this.isFromEnd()) {
            offset += length + 1;
        }
        return offset;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Index) {
            return this.value == ((Index) obj).value;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.value;
    }

    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    protected Index clone() {
        return new Index(this.value);
    }

    @Override
    public String toString() {
        if (this.isFromEnd()) {
            return this.toStringFromEnd();
        }
        return String.valueOf(this.value);
    }

    private String toStringFromEnd() {
        return '^' + String.valueOf(this.getValue());
    }
}
