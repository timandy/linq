package com.bestvike.linq.util;

import com.bestvike.linq.exception.Errors;

import java.util.Collection;
import java.util.List;

/**
 * @author 许崇雷
 * @date 2017/7/19
 */
@SuppressWarnings("Duplicates")
public final class Array<T> implements Cloneable {
    private Object[] elements;

    private Array(Object[] elements) {
        this.elements = elements;
    }

    //region static

    public static <T> Array<T> create(int length) {
        Object[] objects = new Object[length];
        return new Array<>(objects);
    }

    public static <T> Array<T> create(T[] array) {
        if (array == null) throw Errors.argumentNull("elements");
        return new Array<>(array);
    }

    public static Array<Boolean> create(boolean[] array) {
        if (array == null) throw Errors.argumentNull("elements");
        int length = array.length;
        Object[] objects = new Object[length];
        for (int i = 0; i < length; i++)
            objects[i] = array[i];
        return new Array<>(objects);
    }

    public static Array<Byte> create(byte[] array) {
        if (array == null) throw Errors.argumentNull("elements");
        int length = array.length;
        Object[] objects = new Object[length];
        for (int i = 0; i < length; i++)
            objects[i] = array[i];
        return new Array<>(objects);
    }

    public static Array<Short> create(short[] array) {
        if (array == null) throw Errors.argumentNull("elements");
        int length = array.length;
        Object[] objects = new Object[length];
        for (int i = 0; i < length; i++)
            objects[i] = array[i];
        return new Array<>(objects);
    }

    public static Array<Integer> create(int[] array) {
        if (array == null) throw Errors.argumentNull("elements");
        int length = array.length;
        Object[] objects = new Object[length];
        for (int i = 0; i < length; i++)
            objects[i] = array[i];
        return new Array<>(objects);
    }

    public static Array<Long> create(long[] array) {
        if (array == null) throw Errors.argumentNull("elements");
        int length = array.length;
        Object[] objects = new Object[length];
        for (int i = 0; i < length; i++)
            objects[i] = array[i];
        return new Array<>(objects);
    }

    public static Array<Float> create(float[] array) {
        if (array == null) throw Errors.argumentNull("elements");
        int length = array.length;
        Object[] objects = new Object[length];
        for (int i = 0; i < length; i++)
            objects[i] = array[i];
        return new Array<>(objects);
    }

    public static Array<Double> create(double[] array) {
        if (array == null) throw Errors.argumentNull("elements");
        int length = array.length;
        Object[] objects = new Object[length];
        for (int i = 0; i < length; i++)
            objects[i] = array[i];
        return new Array<>(objects);
    }

    public static Array<Character> create(char[] array) {
        if (array == null) throw Errors.argumentNull("elements");
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
        if (dest.length() < src.size())
            throw Errors.argumentNotValid("dest");//dest is too small
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
        if (this.elements.length == newSize)
            return;
        Object[] newArray = new Object[newSize];
        System.arraycopy(this.elements, 0, newArray, 0, this.elements.length > newSize ? newSize : this.elements.length);
        this.elements = newArray;
    }

    public boolean contains(T value) {
        return ArrayUtils.contains(this.elements, value);
    }

    public boolean contains(T value, int startIndex, int count) {
        return ArrayUtils.contains(this.elements, value, startIndex, count);
    }

    public T[] toArray(Class<T> clazz) {
        return ArrayUtils.toArray(this.elements, clazz);
    }

    public List<T> toList() {
        return ArrayUtils.toList(this.elements);
    }

    @Override
    public Array<T> clone() {
        return new Array<>(this.elements.clone());
    }
}
