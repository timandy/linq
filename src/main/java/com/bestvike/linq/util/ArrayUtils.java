package com.bestvike.linq.util;

import com.bestvike.collections.generic.EqualityComparer;
import com.bestvike.linq.exception.ExceptionArgument;
import com.bestvike.linq.exception.ThrowHelper;

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
            ThrowHelper.throwArgumentNullException(ExceptionArgument.array);
        if (startIndex < 0 || startIndex > array.length)
            ThrowHelper.throwArgumentOutOfRangeException(ExceptionArgument.startIndex);
        if (count < 0 || count > array.length - startIndex)
            ThrowHelper.throwArgumentOutOfRangeException(ExceptionArgument.count);

        return EqualityComparer.Default().indexOf(array, item, startIndex, count);
    }

    public static <T> int lastIndexOf(T[] array, T item) {
        return lastIndexOf(array, item, array.length - 1, array.length);
    }

    public static <T> int lastIndexOf(T[] array, T item, int startIndex, int count) {
        if (array == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.array);
        if (array.length == 0) {
            //
            // Special case for 0 length List
            // accept -1 and 0 as valid startIndex for compablility reason.
            //
            if (startIndex != -1 && startIndex != 0)
                ThrowHelper.throwArgumentOutOfRangeException(ExceptionArgument.startIndex);
            // only 0 is a valid value for count if array is empty
            if (count != 0)
                ThrowHelper.throwArgumentOutOfRangeException(ExceptionArgument.count);
            return -1;
        }
        if (startIndex < 0 || startIndex >= array.length)
            ThrowHelper.throwArgumentOutOfRangeException(ExceptionArgument.startIndex);
        if (count < 0 || startIndex - count + 1 < 0)
            ThrowHelper.throwArgumentOutOfRangeException(ExceptionArgument.count);

        return EqualityComparer.Default().lastIndexOf(array, item, startIndex, count);
    }

    public static Object[] clone(Object[] array) {
        if (array == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.array);

        return array.clone();
    }

    public static Object[] resize(Object[] array, int newSize) {
        if (array == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.array);
        if (newSize < 0)
            ThrowHelper.throwArgumentOutOfRangeException(ExceptionArgument.newSize);

        if (array.length == newSize)
            return array;
        Object[] newArray = new Object[newSize];
        System.arraycopy(array, 0, newArray, 0, array.length > newSize ? newSize : array.length);
        return newArray;
    }

    public static <T> void fill(T[] array, T value) {
        if (array == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.array);

        for (int i = 0; i < array.length; i++)
            array[i] = value;
    }

    public static <T> void reverse(T[] array) {
        if (array == null)
            ThrowHelper.throwArgumentNullException(ExceptionArgument.array);

        int i = 0;
        int j = array.length - 1;
        T temp;
        while (i < j) {
            temp = array[i];
            array[i] = array[j];
            array[j] = temp;
            i++;
            j--;
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
        return new ArrayCollection<>(source);
    }

    public static <T> Collection<T> toCollection(Object[] source, int startIndex, int count) {
        return new ArrayRangeCollection<>(source, startIndex, count);
    }

    private static abstract class AbstractCollection<T> implements Collection<T> {
        @Override
        public boolean isEmpty() {
            return this.size() == 0;
        }

        @Override
        public boolean contains(Object o) {
            ThrowHelper.throwNotSupportedException();
            return false;
        }

        @Override
        public <E> E[] toArray(E[] a) {
            ThrowHelper.throwNotSupportedException();
            return null;
        }

        @Override
        public boolean add(T t) {
            ThrowHelper.throwNotSupportedException();
            return false;
        }

        @Override
        public boolean remove(Object o) {
            ThrowHelper.throwNotSupportedException();
            return false;
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            ThrowHelper.throwNotSupportedException();
            return false;
        }

        @Override
        public boolean addAll(Collection<? extends T> c) {
            ThrowHelper.throwNotSupportedException();
            return false;
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            ThrowHelper.throwNotSupportedException();
            return false;
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            ThrowHelper.throwNotSupportedException();
            return false;
        }

        @Override
        public void clear() {
            ThrowHelper.throwNotSupportedException();
        }
    }

    private static final class ArrayCollection<T> extends AbstractCollection<T> {
        private final Object[] source;

        ArrayCollection(Object[] source) {
            this.source = source;
        }

        @Override
        public int size() {
            return this.source.length;
        }

        @Override
        public Object[] toArray() {
            return this.source;
        }

        @Override
        public Iterator<T> iterator() {
            return new ArrayIterator<>(this.source);
        }
    }

    private static final class ArrayRangeCollection<T> extends AbstractCollection<T> {
        private final Object[] source;
        private final int startIndex;
        private final int count;

        ArrayRangeCollection(Object[] source, int startIndex, int count) {
            this.source = source;
            this.startIndex = startIndex;
            this.count = count;
        }

        @Override
        public int size() {
            return this.count;
        }

        @Override
        public Object[] toArray() {
            if (this.startIndex == 0 && this.count == this.source.length)
                return this.source;
            Object[] array = new Object[this.count];
            System.arraycopy(this.source, this.startIndex, array, 0, this.count);
            return array;
        }

        @Override
        public Iterator<T> iterator() {
            return new ArrayRangeIterator<>(this.source, this.startIndex, this.count);
        }
    }

    private static final class ArrayIterator<E> implements Iterator<E> {
        private final Object[] source;
        private int index;

        ArrayIterator(Object[] source) {
            this.source = source;
            this.index = 0;
        }

        @Override
        public boolean hasNext() {
            return this.index < this.source.length;
        }

        @Override
        public E next() {
            //noinspection unchecked
            return (E) this.source[this.index++];
        }

        @Override
        public void remove() {
            ThrowHelper.throwNotSupportedException();
        }
    }

    private static final class ArrayRangeIterator<E> implements Iterator<E> {
        private final Object[] source;
        private final int endIndex;
        private int index;

        ArrayRangeIterator(Object[] source, int startIndex, int count) {
            this.source = source;
            this.index = startIndex;
            this.endIndex = Math.addExact(startIndex, count);
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
            ThrowHelper.throwNotSupportedException();
        }
    }
}
