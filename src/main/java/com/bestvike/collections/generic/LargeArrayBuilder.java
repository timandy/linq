package com.bestvike.collections.generic;

import com.bestvike.Tuple;
import com.bestvike.Tuple2;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;

/**
 * Created by 许崇雷 on 2017-09-11.
 */
final class CopyPosition {//struct
    private int row;
    private int column;

    CopyPosition(int row, int column) {
        assert row >= 0;
        assert column >= 0;
        this.row = row;
        this.column = column;
    }

    public int row() {
        return this.row;
    }

    public int column() {
        return this.column;
    }

    public static CopyPosition start() {
        return new CopyPosition(0, 0);
    }

    public CopyPosition normalize(int endColumn) {
        assert this.column <= endColumn;
        return this.column == endColumn ? new CopyPosition(this.row + 1, 0) : this;
    }

    @Override
    public String toString() {
        return String.format("[%s, %s]", this.row, this.column);
    }
}

/**
 * Created by 许崇雷 on 2017-09-11.
 */
final class LargeArrayBuilder<T> {//struct
    private static final int StartingCapacity = 4;
    private static final int ResizeLimit = 8;

    private final int maxCapacity;                                      // The maximum capacity this builder can have.
    private Array<T> first;                                             // The first buffer we store items in. Resized until ResizeLimit.
    private ArrayBuilder<Array<T>> buffers = new ArrayBuilder<>();      // After ResizeLimit * 2, we store previous buffers we've filled out here.
    private Array<T> current;                                           // Current buffer we're reading into. If count <= ResizeLimit, this is first.
    private int index;                                                  // Index into the current buffer.
    private int count;                                                  // size of all of the items in this builder.

    public LargeArrayBuilder() {
        this(Integer.MAX_VALUE);
    }

    public LargeArrayBuilder(int maxCapacity) {
        assert maxCapacity >= 0;
        this.first = this.current = Array.empty();
        this.maxCapacity = maxCapacity;
    }

    public int count() {
        return this.count;
    }

    public void add(T item) {
        assert this.maxCapacity > this.count;
        if (this.index == this.current.length())
            this.allocateBuffer();
        this.current.set(this.index++, item);
        this.count++;
    }

    public void addRange(IEnumerable<T> items) {
        assert items != null;
        try (IEnumerator<T> enumerator = items.enumerator()) {
            Array<T> destination = this.current;
            int index = this.index;

            // Continuously read in items from the enumerator, updating count
            // and index when we run out of space.
            while (enumerator.moveNext()) {
                if (index == destination.length()) {
                    // No more space in this buffer. Resize.
                    this.count += index - this.index;
                    this.index = index;
                    this.allocateBuffer();
                    destination = this.current;
                    index = this.index; // May have been reset to 0
                }
                destination.set(index++, enumerator.current());
            }

            // Final update to count and index.
            this.count += index - this.index;
            this.index = index;
        }
    }

    public void copyTo(Array<T> array, int arrayIndex, int count) {
        assert array != null;
        assert arrayIndex >= 0;
        assert count >= 0 && count <= this.count();
        assert array.length() - arrayIndex >= count;

        for (int i = 0; count > 0; i++) {
            // Find the buffer we're copying from.
            Array<T> buffer = this.getBuffer(i);

            // Copy until we satisfy count, or we reach the end of the buffer.
            int toCopy = Math.min(count, buffer.length());
            Array.copy(buffer, 0, array, arrayIndex, toCopy);

            // Increment variables to that position.
            count -= toCopy;
            arrayIndex += toCopy;
        }
    }

    public CopyPosition copyTo(CopyPosition position, T[] array, int arrayIndex, int count) {
        assert array != null;
        assert arrayIndex >= 0;
        assert count > 0 && count <= this.count();
        assert array.length - arrayIndex >= count;

        // Go through each buffer, which contains one 'row' of items.
        // The index in each buffer is referred to as the 'column'.
        /*
         * Visual representation:
         *
         *       C0   C1   C2 ..  C31 ..   C63
         * R0:  [0]  [1]  [2] .. [31]
         * R1: [32] [33] [34] .. [63]
         * R2: [64] [65] [66] .. [95] .. [127]
         */
        int row = position.row();
        int column = position.column();

        Array<T> buffer = this.getBuffer(row);
        int copied = this.copyToCore(buffer, column, array, arrayIndex, count);
        arrayIndex += copied;
        count -= copied;
        if (count == 0)
            return new CopyPosition(row, column + copied).normalize(buffer.length());

        do {
            buffer = this.getBuffer(++row);
            copied = this.copyToCore(buffer, 0, array, arrayIndex, count);
            arrayIndex += copied;
            count -= copied;
        } while (count > 0);

        return new CopyPosition(row, copied).normalize(buffer.length());
    }

    private int copyToCore(Array<T> sourceBuffer, int sourceIndex, T[] array, int arrayIndex, int count) {
        assert sourceBuffer.length() > sourceIndex;
        // Copy until we satisfy `count` or reach the end of the current buffer.
        int copyCount = Math.min(sourceBuffer.length() - sourceIndex, count);
        Array.copy(sourceBuffer, sourceIndex, array, arrayIndex, copyCount);
        return copyCount;
    }

    public Array<T> getBuffer(int index) {
        assert index >= 0 && index < this.buffers.count() + 2;

        return index == 0 ? this.first :
                index <= this.buffers.count() ? this.buffers.get(index - 1) :
                        this.current;
    }

    public void slowAdd(T item) {
        this.add(item);
    }

    public Array<T> toArray() {
        Tuple2<Boolean, Array<T>> move = this.tryMove();
        if (move.getItem1()) {
            // No resizing to do.
            return move.getItem2();
        }

        Array<T> array = Array.create(this.count);
        this.copyTo(array, 0, this.count);
        return array;
    }

    public Tuple2<Boolean, Array<T>> tryMove() {
        return Tuple.create(this.count == this.first.length(), this.first);
    }

    private void allocateBuffer() {
        // - On the first few adds, simply resize first.
        // - When we pass ResizeLimit, allocate ResizeLimit elements for current
        //   and start reading into current. Set index to 0.
        // - When current runs out of space, add it to buffers and repeat the
        //   above step, except with current.Length * 2.
        // - Make sure we never pass maxCapacity in all of the above steps.
        assert this.maxCapacity > this.count;
        assert this.index == this.current.length(); //$"{nameof(AllocateBuffer)} was called, but there's more space."
        // If count is int.MinValue, we want to go down the other path which will raise an exception.
        if (this.count < ResizeLimit) {
            // We haven't passed ResizeLimit. Resize first, copying over the previous items.
            assert this.current == this.first && this.count == this.first.length();
            int nextCapacity = Math.min(this.count == 0 ? StartingCapacity : this.count * 2, this.maxCapacity);
            this.current = Array.create(nextCapacity);
            Array.copy(this.first, 0, this.current, 0, this.count);
            this.first = this.current;
        } else {
            assert this.maxCapacity > ResizeLimit;
            assert this.count == ResizeLimit ^ this.current != this.first;
            int nextCapacity;
            if (this.count == ResizeLimit) {
                nextCapacity = ResizeLimit;
            } else {
                // Example scenario: Let's say count == 64.
                // Then our buffers look like this: | 8 | 8 | 16 | 32 |
                // As you can see, our count will be just double the last buffer.
                // Now, say maxCapacity is 100. We will find the right amount to allocate by
                // doing min(64, 100 - 64). The lhs represents double the last buffer,
                // the rhs the limit minus the amount we've already allocated.
                assert this.count >= ResizeLimit * 2;
                assert this.count == this.current.length() * 2;
                this.buffers.add(this.current);
                nextCapacity = Math.min(this.count, this.maxCapacity - this.count);
            }
            this.current = Array.create(nextCapacity);
            this.index = 0;
        }
    }
}
