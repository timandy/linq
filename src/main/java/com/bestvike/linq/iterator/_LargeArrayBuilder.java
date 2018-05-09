package com.bestvike.linq.iterator;

import com.bestvike.collections.generic.Array;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.util.ArrayUtils;
import com.bestvike.out;
import com.bestvike.ref;

import java.util.Objects;

/**
 * Created by 许崇雷 on 2018-05-09.
 */
final class _LargeArrayBuilder {
    private _LargeArrayBuilder() {
    }
}


final class CopyPosition {//struct
    private int row;
    private int column;

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
        CopyPosition that = (CopyPosition) obj;
        return Objects.equals(this.row, that.row)
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


final class LargeArrayBuilder<T> {//struct
    private static final int StartingCapacity = 4;
    private static final int ResizeLimit = 8;

    private final int maxCapacity;                                      // The maximum capacity this builder can have.
    private Array<T> first;                                             // The first buffer we store items in. Resized until ResizeLimit.
    private ArrayBuilder<Array<T>> buffers = new ArrayBuilder<>();      // After ResizeLimit * 2, we store previous buffers we've filled out here.
    private Array<T> current;                                           // Current buffer we're reading into. If count <= ResizeLimit, this is first.
    private int index;                                                  // Index into the current buffer.
    private int count;                                                  // count of all of the items in this builder.

    public LargeArrayBuilder() {
        this(Integer.MAX_VALUE);
    }

    public LargeArrayBuilder(int maxCapacity) {
        assert maxCapacity >= 0;
        this.first = this.current = Array.empty();
        this.maxCapacity = maxCapacity;
    }

    public int getCount() {
        return this.count;
    }

    public void add(T item) {
        assert this.maxCapacity > this.count;
        if (this.index >= this.current.length())
            this.addWithBufferAllocation(item);
        else
            this.current.set(this.index++, item);
        this.count++;
    }

    private void addWithBufferAllocation(T item) {
        this.allocateBuffer();
        this.current.set(this.index++, item);
    }

    public void addRange(IEnumerable<T> items) {
        assert items != null;
        try (IEnumerator<T> enumerator = items.enumerator()) {
            ref<Array<T>> destinationRef = ref.init(this.current);
            ref<Integer> indexRef = ref.init(this.index);

            // Continuously read in items from the enumerator, updating count
            // and index when we run out of space.
            while (enumerator.moveNext()) {
                T item = enumerator.current();
                if (indexRef.getValue() >= destinationRef.getValue().length())
                    this.addWithBufferAllocation(item, destinationRef, indexRef);
                else
                    destinationRef.getValue().set(indexRef.getValue(), item);
                indexRef.setValue(indexRef.getValue() + 1);
            }

            // Final update to count and index.
            this.count += indexRef.getValue() - this.index;
            this.index = indexRef.getValue();
        }
    }

    private void addWithBufferAllocation(T item, ref<Array<T>> destination, ref<Integer> index) {
        this.count += index.getValue() - this.index;
        this.index = index.getValue();
        this.allocateBuffer();
        destination.setValue(this.current);
        index.setValue(this.index);
        this.current.set(index.getValue(), item);
    }

    private void copyTo(T[] array, int arrayIndex, int count) {
        assert array != null;
        assert arrayIndex >= 0;
        assert count >= 0 && count <= this.getCount();
        assert array.length - arrayIndex >= count;

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

    private void copyTo(Array<T> array, int arrayIndex, int count) {
        assert array != null;
        assert arrayIndex >= 0;
        assert count >= 0 && count <= this.getCount();
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
        Array<T> buffer = this.getBuffer(row);

        int copied = this.copyToCore(buffer, column, array, arrayIndexRef, countRef);
        if (countRef.getValue() == 0)
            return new CopyPosition(row, column + copied).normalize(buffer.length());

        do {
            buffer = this.getBuffer(++row);
            copied = this.copyToCore(buffer, 0, array, arrayIndexRef, countRef);
        } while (countRef.getValue() > 0);

        return new CopyPosition(row, copied).normalize(buffer.length());
    }

    public CopyPosition copyTo(CopyPosition position, Array<T> array, int arrayIndex, int count) {
        assert array != null;
        assert arrayIndex >= 0;
        assert count > 0 && count <= this.getCount();
        assert array.length() - arrayIndex >= count;

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
        Array<T> buffer = this.getBuffer(row);

        int copied = this.copyToCore(buffer, column, array, arrayIndexRef, countRef);
        if (countRef.getValue() == 0)
            return new CopyPosition(row, column + copied).normalize(buffer.length());

        do {
            buffer = this.getBuffer(++row);
            copied = this.copyToCore(buffer, 0, array, arrayIndexRef, countRef);
        } while (countRef.getValue() > 0);

        return new CopyPosition(row, copied).normalize(buffer.length());
    }

    private int copyToCore(Array<T> sourceBuffer, int sourceIndex, T[] array, ref<Integer> arrayIndex, ref<Integer> count) {
        assert sourceBuffer.length() > sourceIndex;
        // Copy until we satisfy `count` or reach the end of the current buffer.
        int copyCount = Math.min(sourceBuffer.length() - sourceIndex, count.getValue());
        Array.copy(sourceBuffer, sourceIndex, array, arrayIndex.getValue(), copyCount);
        arrayIndex.setValue(arrayIndex.getValue() + copyCount);
        count.setValue(count.getValue() - copyCount);
        return copyCount;
    }

    private int copyToCore(Array<T> sourceBuffer, int sourceIndex, Array<T> array, ref<Integer> arrayIndex, ref<Integer> count) {
        assert sourceBuffer.length() > sourceIndex;
        // Copy until we satisfy `count` or reach the end of the current buffer.
        int copyCount = Math.min(sourceBuffer.length() - sourceIndex, count.getValue());
        Array.copy(sourceBuffer, sourceIndex, array, arrayIndex.getValue(), copyCount);
        arrayIndex.setValue(arrayIndex.getValue() + copyCount);
        count.setValue(count.getValue() - copyCount);
        return copyCount;
    }

    private Array<T> getBuffer(int index) {
        assert index >= 0 && index < this.buffers.getCount() + 2;

        return index == 0 ? this.first :
                index <= this.buffers.getCount() ? this.buffers.get(index - 1) :
                        this.current;
    }

    public void slowAdd(T item) {
        this.add(item);
    }

    public T[] toArray(Class<T> clazz) {
        out<T[]> arrayRef = out.init();
        if (this.tryMove(clazz, arrayRef))
            return arrayRef.getValue();

        T[] array = ArrayUtils.newInstance(clazz, this.count);
        this.copyTo(array, 0, this.count);
        return array;
    }

    public Array<T> toArray() {
        out<Array<T>> arrayRef = out.init();
        if (this.tryMove(arrayRef))
            return arrayRef.getValue();

        Array<T> array = Array.create(this.count);
        this.copyTo(array, 0, this.count);
        return array;
    }

    public boolean tryMove(Class<T> clazz, out<T[]> array) {
        array.setValue(this.first._toArray(clazz));
        return this.count == this.first.length();
    }

    public boolean tryMove(out<Array<T>> array) {
        array.setValue(this.first);
        return this.count == this.first.length();
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
