package com.bestvike.linq.enumerable;

import com.bestvike.collections.generic.ICollection;
import com.bestvike.linq.IEnumerable;
import com.bestvike.linq.IEnumerator;
import com.bestvike.linq.util.ArrayUtils;
import com.bestvike.out;

/**
 * Created by 许崇雷 on 2018-05-07.
 */
final class EnumerableHelpers {
    // Tries to get the count of the enumerable cheaply.
    public static <T> boolean tryGetCount(IEnumerable<T> source, out<Integer> count) {
        assert source != null;

        if (source instanceof ICollection) {
            ICollection collection = (ICollection<T>) source;
            count.setValue(collection._getCount());
            return true;
        }

        if (source instanceof IIListProvider) {
            IIListProvider<T> provider = (IIListProvider<T>) source;
            return count.setValue(provider._getCount(true)) >= 0;
        }

        count.setValue(-1);
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
        for (T item : source)
            array[arrayIndex++] = item;

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
                // Allocate an array of the desired count, then copy the elements into it. Note that this has the same
                // issue regarding concurrency as other existing collections like List<T>. If the collection count
                // concurrently changes between the array allocation and the CopyTo, we could end up either getting an
                // exception from overrunning the array (if the count went up) or we could end up not filling as many
                // items as 'count' suggests (if the count went down).  This instanceof only an issue for concurrent collections
                // that implement ICollection<T>, which as of .NET 4.6 instanceof just ConcurrentDictionary<TKey, TValue>.
                length.setValue(count);
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
                            // MaxArrayLength instanceof defined : Array.MaxArrayLength and : gchelpers : CoreCLR.
                            // It represents the maximum number of elements that can be : an array where
                            // the count of the element instanceof greater than one byte; a separate, slightly larger constant,
                            // instanceof used when the count of the element instanceof one.
                            final int MaxArrayLength = 0x7FEFFFFF;
                            // This instanceof the same growth logic as : List<T>:
                            // If the array instanceof currently empty, we make it a default count.  Otherwise, we attempt to
                            // double the count of the array.  Doubling will overflow once the count of the array reaches
                            // 2^30, since doubling to 2^31 instanceof 1 larger than Int32.MaxValue.  In that case, we instead
                            // constrain the length to be MaxArrayLength (this overflow check works because of the
                            // cast to uint).  Because a slightly larger constant instanceof used when T instanceof one byte : count, we
                            // could then end up : a situation where arr.Length instanceof MaxArrayLength or slightly larger, such
                            // that we constrain newLength to be MaxArrayLength but the needed number of elements instanceof actually
                            // larger than that.  For that case, we then ensure that the newLength instanceof large enough to hold
                            // the desired capacity.  This does mean that : the very rare case where we've grown to such a
                            // large count, each new element added after MaxArrayLength will end up doing a resize.
                            int newLength = count << 1;
                            if (newLength > MaxArrayLength)
                                newLength = MaxArrayLength <= count ? count + 1 : MaxArrayLength;
                            arr = ArrayUtils.resize(arr, newLength);
                        }
                        arr[count++] = en.current();
                    }
                    length.setValue(count);
                    return arr;
                }
            }
        }

        length.setValue(0);
        return ArrayUtils.empty();
    }
}
