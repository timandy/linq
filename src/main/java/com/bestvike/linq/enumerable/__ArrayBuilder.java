package com.bestvike.linq.enumerable;

import com.bestvike.linq.util.ArrayUtils;

/**
 * Created by 许崇雷 on 2018-05-09.
 */
final class ArrayBuilder<T> {//struct
    private static final int DefaultCapacity = 4;
    private static final int MaxArrayLength = 0x7fffffc7;// All attempts to allocate a larger array will fail.

    private Object[] array;     // Starts out null, initialized on first Add.
    private int count;          // Number of items into array we're using.

    ArrayBuilder() {
    }

    ArrayBuilder(int capacity) {
        assert capacity >= 0;
        if (capacity > 0)
            this.array = new Object[capacity];
    }

    public int getCapacity() {
        return this.array == null ? 0 : this.array.length;
    }

    public int getCount() {
        return this.count;
    }

    public T get(int index) {
        assert index >= 0 && index < this.count;
        //noinspection unchecked
        return (T) this.array[index];
    }

    public void set(int index, T value) {
        assert index >= 0 && index < this.count;
        this.array[index] = value;
    }

    public void add(T item) {
        if (this.count == this.getCapacity())
            this.ensureCapacity(this.count + 1);
        this.uncheckedAdd(item);
    }

    public T first() {
        assert this.count > 0;
        //noinspection unchecked
        return (T) this.array[0];
    }

    public T last() {
        assert this.count > 0;
        //noinspection unchecked
        return (T) this.array[this.count - 1];
    }

    public T[] toArray(Class<T> clazz) {
        if (this.count == 0)
            return ArrayUtils.empty(clazz);
        assert this.array != null; // Nonzero count should imply this
        return ArrayUtils.toArray(this.array, clazz, 0, this.count);
    }

    public Object[] toArray() {
        if (this.count == 0)
            return ArrayUtils.empty();
        assert this.array != null; // Nonzero count should imply this
        return ArrayUtils.resize(this.array, this.count);
    }

    public void uncheckedAdd(T item) {
        assert this.count < this.getCapacity();
        this.array[this.count++] = item;
    }

    private void ensureCapacity(int minimum) {
        assert minimum > this.getCapacity();
        int capacity = this.getCapacity();
        int nextCapacity = capacity == 0 ? DefaultCapacity : 2 * capacity;
        if (Integer.compareUnsigned(nextCapacity, MaxArrayLength) > 0)
            nextCapacity = Math.max(capacity + 1, MaxArrayLength);
        nextCapacity = Math.max(nextCapacity, minimum);
        Object[] next = new Object[nextCapacity];
        if (this.count > 0)
            System.arraycopy(this.array, 0, next, 0, this.count);
        this.array = next;
    }
}
