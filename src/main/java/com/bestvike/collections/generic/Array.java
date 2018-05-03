package com.bestvike.collections.generic;

import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.enumerator.ArrayEnumerator;
import com.bestvike.linq.exception.Errors;
import com.bestvike.linq.util.ArrayUtils;

import java.util.Collection;
import java.util.List;

/**
 * Created by 许崇雷 on 2017/7/19.
 */
@SuppressWarnings("Duplicates")
public final class Array<T> implements IList<T>, Cloneable {
    private Object[] elements;

    private Array(Object[] elements) {
        this.elements = elements;
    }

    //region static

    public static <T> Array<T> empty() {
        Object[] objects = new Object[0];
        return new Array<>(objects);
    }

    public static <T> Array<T> singleton(T element) {
        Object[] objects = new Object[]{element};
        return new Array<>(objects);
    }

    public static <T> Array<T> create(int length) {
        Object[] objects = new Object[length];
        return new Array<>(objects);
    }

    public static <T> Array<T> create(Object[] array) {
        if (array == null)
            throw Errors.argumentNull("array");

        return new Array<>(array);
    }

    public static Array<Boolean> create(boolean[] array) {
        if (array == null)
            throw Errors.argumentNull("array");

        int length = array.length;
        Object[] objects = new Object[length];
        for (int i = 0; i < length; i++)
            objects[i] = array[i];
        return new Array<>(objects);
    }

    public static Array<Byte> create(byte[] array) {
        if (array == null)
            throw Errors.argumentNull("array");

        int length = array.length;
        Object[] objects = new Object[length];
        for (int i = 0; i < length; i++)
            objects[i] = array[i];
        return new Array<>(objects);
    }

    public static Array<Short> create(short[] array) {
        if (array == null)
            throw Errors.argumentNull("array");

        int length = array.length;
        Object[] objects = new Object[length];
        for (int i = 0; i < length; i++)
            objects[i] = array[i];
        return new Array<>(objects);
    }

    public static Array<Integer> create(int[] array) {
        if (array == null)
            throw Errors.argumentNull("array");

        int length = array.length;
        Object[] objects = new Object[length];
        for (int i = 0; i < length; i++)
            objects[i] = array[i];
        return new Array<>(objects);
    }

    public static Array<Long> create(long[] array) {
        if (array == null)
            throw Errors.argumentNull("array");

        int length = array.length;
        Object[] objects = new Object[length];
        for (int i = 0; i < length; i++)
            objects[i] = array[i];
        return new Array<>(objects);
    }

    public static Array<Float> create(float[] array) {
        if (array == null)
            throw Errors.argumentNull("array");

        int length = array.length;
        Object[] objects = new Object[length];
        for (int i = 0; i < length; i++)
            objects[i] = array[i];
        return new Array<>(objects);
    }

    public static Array<Double> create(double[] array) {
        if (array == null)
            throw Errors.argumentNull("array");

        int length = array.length;
        Object[] objects = new Object[length];
        for (int i = 0; i < length; i++)
            objects[i] = array[i];
        return new Array<>(objects);
    }

    public static Array<Character> create(char[] array) {
        if (array == null)
            throw Errors.argumentNull("array");

        int length = array.length;
        Object[] objects = new Object[length];
        for (int i = 0; i < length; i++)
            objects[i] = array[i];
        return new Array<>(objects);
    }

    public static <T> void copy(Array<T> src, int srcPos, Array<T> dest, int destPos, int length) {
        System.arraycopy(src.elements, srcPos, dest.elements, destPos, length);
    }

    public static <T> void copy(Object[] src, int srcPos, Array<T> dest, int destPos, int length) {
        System.arraycopy(src, srcPos, dest.elements, destPos, length);
    }

    public static <T> void copy(Array<T> src, int srcPos, Object[] dest, int destPos, int length) {
        System.arraycopy(src.elements, srcPos, dest, destPos, length);
    }

    public static <T> void copy(Collection<T> src, Array<T> dest) {
        int index = 0;
        for (T item : src)
            dest.set(index++, item);
    }

    //endregion

    public int length() {
        return this.elements.length;
    }

    @SuppressWarnings("unchecked")
    public T get(int index) {
        return (T) this.elements[index];
    }

    public void set(int index, T item) {
        this.elements[index] = item;
    }

    public void resize(int newSize) {
        if (newSize < 0)
            throw Errors.argumentOutOfRange("newSize");
        if (this.length() == newSize)
            return;
        Object[] newArray = new Object[newSize];
        System.arraycopy(this.elements, 0, newArray, 0, this.length() > newSize ? newSize : this.length());
        this.elements = newArray;
    }

    public void fill(T value) {
        for (int i = 0, length = this.length(); i < length; i++)
            this.elements[i] = value;
    }

    public void _reverse() {
        ArrayUtils.reverse(this.elements);
    }

    public T[] _toArray(Class<T> clazz) {
        return ArrayUtils.toArray(this.elements, clazz);
    }

    public T[] _toArray(Class<T> clazz, int startIndex, int count) {
        return ArrayUtils.toArray(this.elements, clazz, startIndex, count);
    }

    @Override
    public Array<T> _toArray() {
        return this;
    }

    public Array<T> _toArray(int startIndex, int count) {
        if (startIndex == 0 && count == this.elements.length)
            return this;
        Array<T> array = Array.create(count);
        copy(this.elements, startIndex, array, 0, count);
        return array;
    }

    public List<T> _toList() {
        return ArrayUtils.toList(this.elements);
    }

    public List<T> _toList(int startIndex, int count) {
        return ArrayUtils.toList(this.elements, startIndex, count);
    }

    @Override
    public Collection<T> getCollection() {
        return ArrayUtils.toCollection(this.elements);
    }

    public Collection<T> getCollection(int startIndex, int count) {
        return ArrayUtils.toCollection(this.elements, startIndex, count);
    }

    @Override
    public int _getCount() {
        return this.length();
    }

    @Override
    public boolean _contains(T value) {
        return ArrayUtils.contains(this.elements, value);
    }

    public boolean _contains(T value, int startIndex) {
        return ArrayUtils.contains(this.elements, value, startIndex);
    }

    public boolean _contains(T value, int startIndex, int count) {
        return ArrayUtils.contains(this.elements, value, startIndex, count);
    }

    public int indexOf(T value) {
        return ArrayUtils.indexOf(this.elements, value);
    }

    public int indexOf(T value, int startIndex) {
        return ArrayUtils.indexOf(this.elements, value, startIndex);
    }

    public int indexOf(T value, int startIndex, int count) {
        return ArrayUtils.indexOf(this.elements, value, startIndex, count);
    }

    @Override
    public void _copyTo(T[] array, int arrayIndex) {
        copy(this, 0, array, arrayIndex, this.length());
    }

    @Override
    public void _copyTo(Array<T> array, int arrayIndex) {
        copy(this, 0, array, arrayIndex, this.length());
    }

    @Override
    public Array<T> clone() {
        return new Array<>(this.elements.clone());
    }

    @Override
    public IEnumerator<T> enumerator() {
        return new ArrayEnumerator<>(this);
    }
}
