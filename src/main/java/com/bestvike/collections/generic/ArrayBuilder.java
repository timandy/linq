package com.bestvike.collections.generic;

import com.bestvike.linq.util.ArrayUtils;

/**
 * Created by 许崇雷 on 2017-09-11.
 */
public final class ArrayBuilder<T> {
    private static final int DefaultCapacity = 4;
    private static final int MaxCoreClrArrayLength = 0x7fefffff; // For byte arrays the limit is slightly larger

    private Array<T> _array;    // Starts out null, initialized on first Add.
    private int _count;         // Number of items into _array we're using.

    public ArrayBuilder() {
    }

    public ArrayBuilder(int capacity) {
        assert capacity >= 0;
        if (capacity > 0)
            this._array = Array.create(capacity);
    }

    public int capacity() {
        return this._array == null ? 0 : this._array.length();
    }

    public int count() {
        return this._count;
    }

    public T get(int index) {
        assert index >= 0 && index < this._count;
        return this._array.get(index);
    }

    public void set(int index, T value) {
        assert index >= 0 && index < this._count;
        this._array.set(index, value);
    }

    public void add(T item) {
        if (this._count == this.capacity())
            this.ensureCapacity(this._count + 1);
        this.uncheckedAdd(item);
    }

    public T first() {
        assert this._count > 0;
        return this._array.get(0);
    }

    public T last() {
        assert this._count > 0;
        return this._array.get(this._count - 1);
    }

    public Array<T> toArray() {
        if (this._count == 0)
            return Array.empty();

        assert this._array != null; // Nonzero _count should imply this
        Array<T> result = this._array;
        if (this._count < this._array.length()) {
            // Avoid a bit of overhead (method call, some branches, extra codegen)
            // which would be incurred by using Array.Resize
            result = Array.create(this._count);
            Array.copy(this._array, 0, result, 0, this._count);
        }
//#if DEBUG
//        // Try to prevent callers from using the ArrayBuilder after toArray, if _count != 0.
//        _count = -1;
//        _array = null;
//#endif
        return result;
    }

    public T[] toArray(Class<T> clazz) {
        if (this._count == 0)
            return ArrayUtils.empty(clazz);

        assert this._array != null; // Nonzero _count should imply this
        T[] result;
        if (this._count < this._array.length()) {
            // Avoid a bit of overhead (method call, some branches, extra codegen)
            // which would be incurred by using Array.Resize
            result = ArrayUtils.newInstance(clazz, this._count);
            Array.copy(this._array, 0, result, 0, this._count);
        } else {
            result = this._array.toArray(clazz);
        }
//#if DEBUG
//        // Try to prevent callers from using the ArrayBuilder after toArray, if _count != 0.
//        _count = -1;
//        _array = null;
//#endif
        return result;
    }

    public void uncheckedAdd(T item) {
        assert this._count < this.capacity();
        this._array.set(this._count++, item);
    }

    private void ensureCapacity(int minimum) {
        assert minimum > this.capacity();
        int capacity = this.capacity();
        int nextCapacity = capacity == 0 ? DefaultCapacity : 2 * capacity;
        if (nextCapacity > MaxCoreClrArrayLength)
            nextCapacity = Math.max(capacity + 1, MaxCoreClrArrayLength);
        nextCapacity = Math.max(nextCapacity, minimum);
        Array<T> next = Array.create(nextCapacity);
        if (this._count > 0)
            Array.copy(this._array, 0, next, 0, this._count);
        this._array = next;
    }
}
