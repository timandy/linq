package com.bestvike.linq.iterator;

import com.bestvike.collections.generic.Array;
import com.bestvike.linq.util.ArrayUtils;

/**
 * Created by 许崇雷 on 2018-05-09.
 */
final class _ArrayBuilder {
    private _ArrayBuilder() {
    }
}


final class ArrayBuilder<T> {//struct
    private static final int DefaultCapacity = 4;
    private static final int MaxCoreClrArrayLength = 0x7fefffff; // For byte arrays the limit is slightly larger

    private Array<T> array;     // Starts out null, initialized on first Add.
    private int count;          // Number of items into array we're using.

    public ArrayBuilder() {
    }

    public ArrayBuilder(int capacity) {
        assert capacity >= 0;
        if (capacity > 0)
            this.array = Array.create(capacity);
    }

    public int getCapacity() {
        return this.array == null ? 0 : this.array.length();
    }

    public int getCount() {
        return this.count;
    }

    public T get(int index) {
        assert index >= 0 && index < this.count;
        return this.array.get(index);
    }

    public void set(int index, T value) {
        assert index >= 0 && index < this.count;
        this.array.set(index, value);
    }

    public void add(T item) {
        if (this.count == this.getCapacity())
            this.ensureCapacity(this.count + 1);
        this.uncheckedAdd(item);
    }

    public T first() {
        assert this.count > 0;
        return this.array.get(0);
    }

    public T last() {
        assert this.count > 0;
        return this.array.get(this.count - 1);
    }

    public Array<T> toArray() {
        if (this.count == 0)
            return Array.empty();
        assert this.array != null; // Nonzero count should imply this
        Array<T> result = this.array;
        if (this.count < this.array.length()) {
            // Avoid a bit of overhead (method call, some branches, extra codegen)
            // which would be incurred by using Array.Resize
            result = Array.create(this.count);
            Array.copy(this.array, 0, result, 0, this.count);
        }
//#if DEBUG
//        // Try to prevent callers from using the ArrayBuilder after toArray, if count != 0.
//        count = -1;
//        array = null;
//#endif
        return result;
    }

    public T[] toArray(Class<T> clazz) {
        if (this.count == 0)
            return ArrayUtils.empty(clazz);
        assert this.array != null; // Nonzero count should imply this
        T[] result;
        if (this.count < this.array.length()) {
            // Avoid a bit of overhead (method call, some branches, extra codegen)
            // which would be incurred by using Array.Resize
            result = ArrayUtils.newInstance(clazz, this.count);
            Array.copy(this.array, 0, result, 0, this.count);
        } else {
            result = this.array._toArray(clazz);
        }
//#if DEBUG
//        // Try to prevent callers from using the ArrayBuilder after toArray, if count != 0.
//        count = -1;
//        array = null;
//#endif
        return result;
    }

    public void uncheckedAdd(T item) {
        assert this.count < this.getCapacity();
        this.array.set(this.count++, item);
    }

    private void ensureCapacity(int minimum) {
        assert minimum > this.getCapacity();
        int capacity = this.getCapacity();
        int nextCapacity = capacity == 0 ? DefaultCapacity : 2 * capacity;
        if (nextCapacity > MaxCoreClrArrayLength)
            nextCapacity = Math.max(capacity + 1, MaxCoreClrArrayLength);
        nextCapacity = Math.max(nextCapacity, minimum);
        Array<T> next = Array.create(nextCapacity);
        if (this.count > 0)
            Array.copy(this.array, 0, next, 0, this.count);
        this.array = next;
    }
}
