package com.bestvike.linq.util;

import com.bestvike.collections.generic.EqualityComparer;
import com.bestvike.linq.exception.Errors;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by 许崇雷 on 2017-07-19.
 */
public final class ArrayUtils {
    private static final Object[] EMPTY = new Object[0];

    private ArrayUtils() {
    }

    public static Object[] empty() {
        return EMPTY;
    }

    public static <T> T[] empty(Class<T> clazz) {
        return newInstance(clazz, 0);
    }

    public static Object[] singleton(Object item) {
        Object[] array = new Object[1];
        array[0] = item;
        return array;
    }

    public static <T> T[] singleton(Class<T> clazz, T item) {
        T[] array = newInstance(clazz, 1);
        array[0] = item;
        return array;
    }

    public static <T> T[] newInstance(Class<T> clazz, int length) {
        //noinspection unchecked
        return (T[]) Array.newInstance(clazz, length);
    }

    public static <T> boolean contains(T[] array, T item) {
        return indexOf(array, item) != -1;
    }

    public static <T> boolean contains(T[] array, T item, int startIndex, int count) {
        return indexOf(array, item, startIndex, count) != -1;
    }

    public static <T> int indexOf(T[] array, T item) {
        return indexOf(array, item, 0, array.length);
    }

    public static <T> int indexOf(T[] array, T item, int startIndex, int count) {
        if (array == null)
            throw Errors.argumentNull("array");
        if (startIndex > array.length)
            throw Errors.argumentOutOfRange("startIndex");
        if (count < 0 || startIndex > array.length - count)
            throw Errors.argumentOutOfRange("count");

        return EqualityComparer.Default().indexOf(array, item, startIndex, count);
    }

    public static Object[] clone(Object[] array) {
        if (array == null)
            throw Errors.argumentNull("array");

        int length = array.length;
        Object[] newArray = new Object[length];
        System.arraycopy(array, 0, newArray, 0, length);
        return newArray;
    }

    public static Object[] resize(Object[] array, int newSize) {
        if (array == null)
            throw Errors.argumentNull("array");
        if (newSize < 0)
            throw Errors.argumentOutOfRange("newSize");

        int oldSize = array.length;
        if (oldSize == newSize)
            return array;
        Object[] newArray = new Object[newSize];
        System.arraycopy(array, 0, newArray, 0, oldSize > newSize ? newSize : oldSize);
        return newArray;
    }

    public static <T> void fill(T[] array, T value) {
        if (array == null)
            throw Errors.argumentNull("array");

        for (int i = 0, length = array.length; i < length; i++)
            array[i] = value;
    }

    public static <T> void reverse(T[] array) {
        if (array == null)
            throw Errors.argumentNull("array");

        int length = array.length;
        for (int i = 0, mid = length >> 1, j = length - 1; i < mid; i++, j--) {
            T tmp = array[i];
            array[i] = array[j];
            array[j] = tmp;
        }
    }

    public static <T> T[] toArray(Object[] source, Class<T> clazz) {
        return toArray(source, clazz, 0, source.length);
    }

    public static <T> T[] toArray(Object[] source, Class<T> clazz, int startIndex, int count) {
        T[] array = newInstance(clazz, count);
        //noinspection SuspiciousSystemArraycopy
        System.arraycopy(source, startIndex, array, 0, count);
        return array;
    }

    public static <T> List<T> toList(Object[] source) {
        return new ArrayList<>(toCollection(source));
    }

    public static <T> List<T> toList(Object[] source, int startIndex, int count) {
        return new ArrayList<>(toCollection(source, startIndex, count));
    }

    public static <T> Collection<T> toCollection(Object[] source) {
        return new AnonymousCollection<T>() {
            @Override
            public int size() {
                return source.length;
            }

            @Override
            public Object[] toArray() {
                return source;
            }

            @Override
            public Iterator<T> iterator() {
                return new ArrayIterator<>(source);
            }
        };
    }

    public static <T> Collection<T> toCollection(Object[] source, int startIndex, int count) {
        return new AnonymousCollection<T>() {
            @Override
            public int size() {
                return count;
            }

            @Override
            public Object[] toArray() {
                if (startIndex == 0 && count == source.length)
                    return source;
                Object[] array = new Object[count];
                System.arraycopy(source, startIndex, array, 0, count);
                return array;
            }

            @Override
            public Iterator<T> iterator() {
                return new ArrayIterator<>(source, startIndex, count);
            }
        };
    }

    private static abstract class AnonymousCollection<T> implements Collection<T> {
        @Override
        public boolean isEmpty() {
            return this.size() == 0;
        }

        @Override
        public boolean contains(Object o) {
            throw Errors.notSupported();
        }

        @Override
        public <E> E[] toArray(E[] a) {
            throw Errors.notSupported();
        }

        @Override
        public boolean add(T t) {
            throw Errors.notSupported();
        }

        @Override
        public boolean remove(Object o) {
            throw Errors.notSupported();
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            throw Errors.notSupported();
        }

        @Override
        public boolean addAll(Collection<? extends T> c) {
            throw Errors.notSupported();
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            throw Errors.notSupported();
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            throw Errors.notSupported();
        }

        @Override
        public void clear() {
            throw Errors.notSupported();
        }
    }

    private static final class ArrayIterator<E> implements Iterator<E> {
        private final Object[] source;
        private final int endIndex;
        private int index;

        ArrayIterator(final Object[] source) {
            this(source, 0, source.length);
        }

        ArrayIterator(final Object[] source, final int index, final int count) {
            this.source = source;
            this.index = index;
            this.endIndex = Math.addExact(index, count);
        }

        @Override
        public boolean hasNext() {
            return this.index < this.endIndex;
        }

        @Override
        public E next() {
            //noinspection unchecked
            return (E) this.source[this.index++];
        }

        @Override
        public void remove() {
            throw Errors.notSupported();
        }
    }
}
