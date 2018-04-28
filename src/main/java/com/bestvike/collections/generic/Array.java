package com.bestvike.collections.generic;

import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.enumerator.ArrayEnumerator;
import com.bestvike.linq.exception.Errors;
import com.bestvike.linq.util.ArrayUtils;

import java.util.Arrays;
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
        if (array == null) throw Errors.argumentNull("array");
        return new Array<>(array);
    }

    public static Array<Boolean> create(boolean[] array) {
        if (array == null) throw Errors.argumentNull("array");
        int length = array.length;
        Object[] objects = new Object[length];
        for (int i = 0; i < length; i++)
            objects[i] = array[i];
        return new Array<>(objects);
    }

    public static Array<Byte> create(byte[] array) {
        if (array == null) throw Errors.argumentNull("array");
        int length = array.length;
        Object[] objects = new Object[length];
        for (int i = 0; i < length; i++)
            objects[i] = array[i];
        return new Array<>(objects);
    }

    public static Array<Short> create(short[] array) {
        if (array == null) throw Errors.argumentNull("array");
        int length = array.length;
        Object[] objects = new Object[length];
        for (int i = 0; i < length; i++)
            objects[i] = array[i];
        return new Array<>(objects);
    }

    public static Array<Integer> create(int[] array) {
        if (array == null) throw Errors.argumentNull("array");
        int length = array.length;
        Object[] objects = new Object[length];
        for (int i = 0; i < length; i++)
            objects[i] = array[i];
        return new Array<>(objects);
    }

    public static Array<Long> create(long[] array) {
        if (array == null) throw Errors.argumentNull("array");
        int length = array.length;
        Object[] objects = new Object[length];
        for (int i = 0; i < length; i++)
            objects[i] = array[i];
        return new Array<>(objects);
    }

    public static Array<Float> create(float[] array) {
        if (array == null) throw Errors.argumentNull("array");
        int length = array.length;
        Object[] objects = new Object[length];
        for (int i = 0; i < length; i++)
            objects[i] = array[i];
        return new Array<>(objects);
    }

    public static Array<Double> create(double[] array) {
        if (array == null) throw Errors.argumentNull("array");
        int length = array.length;
        Object[] objects = new Object[length];
        for (int i = 0; i < length; i++)
            objects[i] = array[i];
        return new Array<>(objects);
    }

    public static Array<Character> create(char[] array) {
        if (array == null) throw Errors.argumentNull("array");
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

    public T[] _toArray(Class<T> clazz) {
        return ArrayUtils.toArray(this.elements, clazz);
    }

    @Override
    public Array<T> _toArray() {
        return this;
    }

    public List<T> _toList() {
        return ArrayUtils.toList(this.elements);
    }


    @Override
    public Collection<T> getCollection() {
        return (Collection<T>) Arrays.asList(this.elements);
    }

    @Override
    public int _getCount() {
        return this.length();
    }

    @Override
    public boolean _contains(T value) {
        return ArrayUtils.contains(this.elements, value);
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
