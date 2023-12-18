package com.bestvike.linq.enumerable;

import com.bestvike.collections.generic.ICollection;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.util.ArrayUtils;
import com.bestvike.linq.util.ListUtils;
import com.bestvike.out;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 许崇雷 on 2018-05-07.
 */
final class EnumerableHelpers {
    private static final int MaxArrayLength = 0x7fffffc7;// All attempts to allocate a larger array will fail.

    private EnumerableHelpers() {
    }

    // Tries to get the count of the enumerable cheaply.
    public static <T> boolean tryGetCount(IEnumerable<T> source, out<Integer> count) {
        assert source != null;

        if (source instanceof ICollection) {
            ICollection<T> collection = (ICollection<T>) source;
            count.value = collection._getCount();
            return true;
        }

        if (source instanceof IIListProvider) {
            IIListProvider<T> provider = (IIListProvider<T>) source;
            return (count.value = provider._getCount(true)) >= 0;
        }

        count.value = -1;
        return false;
    }

    //Copies items from an enumerable to an array.
    public static <T> void copy(IEnumerable<T> source, Object[] array, int arrayIndex, int count) {
        assert source != null;
        assert arrayIndex >= 0;
        assert count >= 0;
        assert array != null && array.length - arrayIndex >= count;

        if (source instanceof ICollection) {
            ICollection<T> collection = (ICollection<T>) source;
            assert collection._getCount() == count;
            collection._copyTo(array, arrayIndex);
            return;
        }
        iterativeCopy(source, array, arrayIndex, count);
    }

    //Copies items from a non-collection enumerable to an array.
    public static <T> void iterativeCopy(IEnumerable<T> source, Object[] array, int arrayIndex, int count) {
        assert source != null && !(source instanceof ICollection);
        assert arrayIndex >= 0;
        assert count >= 0;
        assert array != null && array.length - arrayIndex >= count;

        int endIndex = arrayIndex + count;
        try (IEnumerator<T> e = source.enumerator()) {
            while (e.moveNext())
                array[arrayIndex++] = e.current();
        }

        assert arrayIndex == endIndex;
    }

    //Converts an enumerable to an array.
    public static <T> T[] toArray(IEnumerable<T> source, Class<T> clazz) {
        assert source != null;
        assert clazz != null;

        if (source instanceof ICollection) {
            ICollection<T> collection = (ICollection<T>) source;
            int count = collection._getCount();
            return count == 0 ? ArrayUtils.empty(clazz) : collection._toArray(clazz);
        }

        LargeArrayBuilder<T> builder = new LargeArrayBuilder<>();
        builder.addRange(source);
        return builder.toArray(clazz);
    }

    //Converts an enumerable to an array.
    public static <T> Object[] toArray(IEnumerable<T> source) {
        assert source != null;

        if (source instanceof ICollection) {
            ICollection<T> collection = (ICollection<T>) source;
            int count = collection._getCount();
            return count == 0 ? ArrayUtils.empty() : collection._toArray();
        }

        LargeArrayBuilder<T> builder = new LargeArrayBuilder<>();
        builder.addRange(source);
        return builder.toArray();
    }

    //Converts an enumerable to an array using the same logic as List{T}.
    public static <T> Object[] toArray(IEnumerable<T> source, out<Integer> length) {
        if (source instanceof ICollection) {
            ICollection<T> ic = (ICollection<T>) source;
            int count = ic._getCount();
            if (count != 0) {
                // Allocate an array of the desired size, then copy the elements into it. Note that this has the same
                // issue regarding concurrency as other existing collections like List<T>. If the collection size
                // concurrently changes between the array allocation and the CopyTo, we could end up either getting an
                // exception from overrunning the array (if the size went up) or we could end up not filling as many
                // items as 'count' suggests (if the size went down).  This is only an issue for concurrent collections
                // that implement ICollection<T>, which as of .NET 4.6 is just ConcurrentDictionary<TKey, TValue>.
                length.value = count;
                return ic._toArray();
            }
        } else {
            try (IEnumerator<T> en = source.enumerator()) {
                if (en.moveNext()) {
                    final int DefaultCapacity = 4;
                    Object[] arr = new Object[DefaultCapacity];
                    arr[0] = en.current();
                    int count = 1;

                    while (en.moveNext()) {
                        if (count == arr.length) {
                            // This is the same growth logic as in List<T>:
                            // If the array is currently empty, we make it a default size.  Otherwise, we attempt to
                            // double the size of the array.  Doubling will overflow once the size of the array reaches
                            // 2^30, since doubling to 2^31 is 1 larger than Int32.MaxValue.  In that case, we instead
                            // constrain the length to be MaxArrayLength (this overflow check works because of the
                            // cast to uint).
                            int newLength = count << 1;
                            if (Integer.compareUnsigned(newLength, MaxArrayLength) > 0)
                                newLength = MaxArrayLength <= count ? count + 1 : MaxArrayLength;
                            arr = ArrayUtils.resize(arr, newLength);
                        }
                        arr[count++] = en.current();
                    }
                    length.value = count;
                    return arr;
                }
            }
        }

        length.value = 0;
        return ArrayUtils.empty();
    }

    //Converts an enumerable to an list.
    public static <T> List<T> toList(IEnumerable<T> source) {
        assert source != null;

        if (source instanceof ICollection) {
            ICollection<T> collection = (ICollection<T>) source;
            int count = collection._getCount();
            return count == 0 ? ListUtils.empty() : collection._toList();
        }

        List<T> list = new ArrayList<>();
        try (IEnumerator<T> e = source.enumerator()) {
            while (e.moveNext())
                list.add(e.current());
        }
        return list;
    }
}
