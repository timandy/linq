package com.bestvike.linq.enumerable;

import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.util.ArrayUtils;
import com.bestvike.out;
import com.bestvike.ref;

import java.util.Objects;

/**
 * Created by 许崇雷 on 2018-05-09.
 */
final class CopyPosition {//struct
    private final int row;
    private final int column;

    CopyPosition(int row, int column) {
        assert row >= 0;
        assert column >= 0;
        this.row = row;
        this.column = column;
    }

    public static CopyPosition start() {
        return new CopyPosition(0, 0);
    }

    public int getRow() {
        return this.row;
    }

    public int getColumn() {
        return this.column;
    }

    public CopyPosition normalize(int endColumn) {
        assert this.column <= endColumn;
        return this.column == endColumn ? new CopyPosition(this.row + 1, 0) : this;
    }

    @Override
    public boolean equals(Object obj) {
        CopyPosition that;
        return obj instanceof CopyPosition
                && Objects.equals(this.row, (that = (CopyPosition) obj).row)
                && Objects.equals(this.column, that.column);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.row;
        result = prime * result + this.column;
        return result;
    }

    @Override
    public String toString() {
        return String.format("[%s, %s]", this.row, this.column);
    }
}


@SuppressWarnings("SameParameterValue")
final class LargeArrayBuilder<T> {//struct
    private static final int StartingCapacity = 4;
    private static final int ResizeLimit = 8;

    private final int maxCapacity;                                      // The maximum capacity this builder can have.
    private Object[] first;                                             // The first buffer we store items in. Resized until ResizeLimit.
    private ArrayBuilder<Object[]> buffers = new ArrayBuilder<>();      // After ResizeLimit * 2, we store previous buffers we've filled out here.
    private Object[] current;                                           // Current buffer we're reading into. If count <= ResizeLimit, this is first.
    private int index;                                                  // Index into the current buffer.
    private int count;                                                  // count of all of the items in this builder.

    LargeArrayBuilder() {
        this(Integer.MAX_VALUE);
    }

    LargeArrayBuilder(int maxCapacity) {
        assert maxCapacity >= 0;
        this.first = this.current = ArrayUtils.empty();
        this.maxCapacity = maxCapacity;
    }

    public int getCount() {
        return this.count;
    }

    public void add(T item) {
        assert this.maxCapacity > this.count;
        if (Integer.compareUnsigned(this.index, this.current.length) >= 0)
            this.addWithBufferAllocation(item);
        else
            this.current[this.index++] = item;
        this.count++;
    }

    private void addWithBufferAllocation(T item) {
        this.allocateBuffer();
        this.current[this.index++] = item;
    }

    public void addRange(IEnumerable<T> items) {
        assert items != null;
        try (IEnumerator<T> enumerator = items.enumerator()) {
            ref<Object[]> destinationRef = ref.init(this.current);
            ref<Integer> indexRef = ref.init(this.index);

            // Continuously read in items from the enumerator, updating count
            // and index when we run out of space.
            while (enumerator.moveNext()) {
                T item = enumerator.current();
                if (Integer.compareUnsigned(indexRef.value, destinationRef.value.length) >= 0)
                    this.addWithBufferAllocation(item, destinationRef, indexRef);
                else
                    destinationRef.value[indexRef.value] = item;
                indexRef.value++;
            }

            // Final update to count and index.
            this.count += indexRef.value - this.index;
            this.index = indexRef.value;
        }
    }

    private void addWithBufferAllocation(T item, ref<Object[]> destination, ref<Integer> index) {
        this.count += index.value - this.index;
        this.index = index.value;
        this.allocateBuffer();
        destination.value = this.current;
        index.value = this.index;
        this.current[index.value] = item;
    }

    private void copyTo(Object[] array, int arrayIndex, int count) {
        assert arrayIndex >= 0;
        assert count >= 0 && count <= this.getCount();
        assert array != null && array.length - arrayIndex >= count;

        for (int i = 0; count > 0; i++) {
            // Find the buffer we're copying from.
            Object[] buffer = this.getBuffer(i);

            // Copy until we satisfy count, or we reach the end of the buffer.
            int toCopy = Math.min(count, buffer.length);
            System.arraycopy(buffer, 0, array, arrayIndex, toCopy);

            // Increment variables to that position.
            count -= toCopy;
            arrayIndex += toCopy;
        }
    }

    public CopyPosition copyTo(CopyPosition position, Object[] array, int arrayIndex, int count) {
        assert array != null;
        assert arrayIndex >= 0;
        assert count > 0 && count <= this.getCount();
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
        int row = position.getRow();
        int column = position.getColumn();
        ref<Integer> arrayIndexRef = ref.init(arrayIndex);
        ref<Integer> countRef = ref.init(count);

        Object[] buffer = this.getBuffer(row);
        int copied = this.copyToCore(buffer, column, array, arrayIndexRef, countRef);

        if (countRef.value == 0)
            return new CopyPosition(row, column + copied).normalize(buffer.length);

        do {
            buffer = this.getBuffer(++row);
            copied = this.copyToCore(buffer, 0, array, arrayIndexRef, countRef);
        } while (countRef.value > 0);

        return new CopyPosition(row, copied).normalize(buffer.length);
    }

    private int copyToCore(Object[] sourceBuffer, int sourceIndex, Object[] array, ref<Integer> arrayIndex, ref<Integer> count) {
        assert sourceBuffer.length > sourceIndex;
        // Copy until we satisfy `count` or reach the end of the current buffer.
        int copyCount = Math.min(sourceBuffer.length - sourceIndex, count.value);
        System.arraycopy(sourceBuffer, sourceIndex, array, arrayIndex.value, copyCount);
        arrayIndex.value += copyCount;
        count.value -= copyCount;
        return copyCount;
    }

    public Object[] getBuffer(int index) {
        assert index >= 0 && index < this.buffers.getCount() + 2;

        return index == 0 ? this.first :
                index <= this.buffers.getCount() ? this.buffers.get(index - 1) :
                        this.current;
    }

    public void slowAdd(T item) {
        this.add(item);
    }

    public T[] toArray(Class<T> clazz) {
        out<Object[]> arrayRef = out.init();
        if (this.tryMove(arrayRef))
            return ArrayUtils.toArray(arrayRef.value, clazz);

        T[] array = ArrayUtils.newInstance(clazz, this.count);
        this.copyTo(array, 0, this.count);
        return array;
    }

    public Object[] toArray() {
        out<Object[]> arrayRef = out.init();
        if (this.tryMove(arrayRef))
            return arrayRef.value;

        Object[] array = new Object[this.count];
        this.copyTo(array, 0, this.count);
        return array;
    }

    public boolean tryMove(out<Object[]> array) {
        array.value = this.first;
        return this.count == this.first.length;
    }

    private void allocateBuffer() {
        // - On the first few adds, simply resize first.
        // - When we pass ResizeLimit, allocate ResizeLimit elements for current
        //   and start reading into current. Set index to 0.
        // - When current runs out of space, add it to buffers and repeat the
        //   above step, except with current.Length * 2.
        // - Make sure we never pass maxCapacity in all of the above steps.
        assert Integer.compareUnsigned(this.maxCapacity, this.count) > 0;
        assert this.index == this.current.length; //$"{nameof(AllocateBuffer)} was called, but there's more space."
        // If count is int.MinValue, we want to go down the other path which will raise an exception.
        if (Integer.compareUnsigned(this.count, ResizeLimit) < 0) {
            // We haven't passed ResizeLimit. Resize first, copying over the previous items.
            assert this.current == this.first && this.count == this.first.length;
            int nextCapacity = Math.min(this.count == 0 ? StartingCapacity : this.count * 2, this.maxCapacity);
            this.current = new Object[nextCapacity];
            System.arraycopy(this.first, 0, this.current, 0, this.count);
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
                assert this.count == this.current.length * 2;
                this.buffers.add(this.current);
                nextCapacity = Math.min(this.count, this.maxCapacity - this.count);
            }
            this.current = new Object[nextCapacity];
            this.index = 0;
        }
    }
}
