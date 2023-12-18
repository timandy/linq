package com.bestvike;

import com.bestvike.linq.exception.ExceptionArgument;
import com.bestvike.linq.exception.ThrowHelper;

/**
 * Represent a range has start and end indexes.
 * <pre>
 * {@code
 * List<Integer> someArray = Arrays.asList(1, 2, 3, 4, 5);
 * Integer[] subArray1 = Linq.of(someArray).take(Range.endAt(Index.fromStart(2))).toArray(Integer.class); // { 1, 2 }
 * Integer[] subArray2 = Linq.of(someArray).take(Range.startAt(Index.fromStart(1))).toArray(Integer.class); // { 2, 3, 4, 5 }
 * }
 * </pre>
 * Created by 许崇雷 on 2023-05-31.
 */
public final class Range {
    public static final Range All = new Range(Index.Start, Index.End);
    private final Index start;
    private final Index end;

    public Range(Index start, Index end) {
        if (start == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.start);
        if (end == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.end);
        this.start = start;
        this.end = end;
    }

    public static Range startAt(Index start) {
        return new Range(start, Index.End);
    }

    public static Range endAt(Index end) {
        return new Range(Index.Start, end);
    }

    public Index getStart() {
        return this.start;
    }

    public Index getEnd() {
        return this.end;
    }

    public OffsetLength getOffsetAndLength(int length) {
        int start;
        Index startIndex = this.getStart();
        if (startIndex.isFromEnd())
            start = length - startIndex.getValue();
        else
            start = startIndex.getValue();

        int end;
        Index endIndex = this.getEnd();
        if (endIndex.isFromEnd())
            end = length - endIndex.getValue();
        else
            end = endIndex.getValue();

        if (Integer.compareUnsigned(end, length) > 0 || Integer.compareUnsigned(start, end) > 0)
            ThrowHelper.throwArgumentOutOfRangeException(ExceptionArgument.length);

        return new OffsetLength(start, end - start);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Range) {
            Range r = (Range) obj;
            return this.start.equals(r.start) && this.end.equals(r.end);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int h1 = this.start.hashCode();
        int h2 = this.end.hashCode();
        return (h1 << 5) + h1 ^ h2;
    }

    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    protected Range clone() {
        return new Range(this.start, this.end);
    }

    @Override
    public String toString() {
        return this.start.toString() + ".." + this.end.toString();
    }

    public static final class OffsetLength {
        public final int Offset;
        public final int Length;

        public OffsetLength(int offset, int length) {
            this.Offset = offset;
            this.Length = length;
        }
    }
}
