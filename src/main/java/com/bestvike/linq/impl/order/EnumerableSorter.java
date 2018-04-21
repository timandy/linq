package com.bestvike.linq.impl.order;

import com.bestvike.collections.generic.Array;
import com.bestvike.collections.generic.Comparer;
import com.bestvike.function.Func1;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by 许崇雷 on 2017/7/11.
 */
public final class EnumerableSorter<TElement, TKey> extends AbstractEnumerableSorter<TElement> {
    private final Func1<TElement, TKey> keySelector;
    private final Comparator<TKey> comparer;
    private final boolean descending;
    private final AbstractEnumerableSorter<TElement> next;
    private Array<TKey> keys;

    EnumerableSorter(Func1<TElement, TKey> keySelector, Comparator<TKey> comparer, boolean descending, AbstractEnumerableSorter<TElement> next) {
        this.keySelector = keySelector;
        this.comparer = comparer;
        this.descending = descending;
        this.next = next;
    }

    protected void computeKeys(Array<TElement> elements, int count) {
        this.keys = Array.create(count);
        for (int i = 0; i < count; i++)
            this.keys.set(i, this.keySelector.apply(elements.get(i)));
        if (this.next == null)
            return;
        this.next.computeKeys(elements, count);
    }

    protected int compareAnyKeys(int index1, int index2) {
        int c = this.comparer.compare(this.keys.get(index1), this.keys.get(index2));
        if (c == 0) {
            if (this.next == null)
                return index1 - index2; // ensure stability of sort
            return this.next.compareAnyKeys(index1, index2);
        }

        // -c will result in a negative value for int.MinValue (-int.MinValue == int.MinValue).
        // Flipping keys earlier is more likely to trigger something strange in a comparer,
        // particularly as it comes to the sort being stable.
        return (this.descending != (c > 0)) ? 1 : -1;
    }

    private int compareKeys(int index1, int index2) {
        return index1 == index2 ? 0 : this.compareAnyKeys(index1, index2);
    }

    protected void quickSort(Integer[] keys, int lo, int hi) {
        Arrays.sort(keys, lo, hi - lo + 1, Comparer.Create(this::compareAnyKeys)); // TODO #24115: Remove Create call when delegate-based overload is available
    }

    // Sorts the k elements between minIdx and maxIdx without sorting all elements
    // Time complexity: O(n + k log k) best and average case. O(n^2) worse case.
    protected void partialQuickSort(Integer[] map, int left, int right, int minIdx, int maxIdx) {
        do {
            int i = left;
            int j = right;
            int x = map[i + ((j - i) >> 1)];
            do {
                while (i < map.length && this.compareKeys(x, map[i]) > 0) {
                    i++;
                }

                while (j >= 0 && this.compareKeys(x, map[j]) < 0) {
                    j--;
                }

                if (i > j) {
                    break;
                }

                if (i < j) {
                    int temp = map[i];
                    map[i] = map[j];
                    map[j] = temp;
                }

                i++;
                j--;
            }
            while (i <= j);

            if (minIdx >= i) {
                left = i + 1;
            } else if (maxIdx <= j) {
                right = j - 1;
            }

            if (j - left <= right - i) {
                if (left < j) {
                    this.partialQuickSort(map, left, j, minIdx, maxIdx);
                }

                left = i;
            } else {
                if (i < right) {
                    this.partialQuickSort(map, i, right, minIdx, maxIdx);
                }

                right = j;
            }
        }
        while (left < right);
    }

    // Finds the element that would be at idx if the collection was sorted.
    // Time complexity: O(n) best and average case. O(n^2) worse case.
    protected int quickSelect(Integer[] map, int right, int idx) {
        int left = 0;
        do {
            int i = left;
            int j = right;
            int x = map[i + ((j - i) >> 1)];
            do {
                while (i < map.length && this.compareKeys(x, map[i]) > 0) {
                    i++;
                }

                while (j >= 0 && this.compareKeys(x, map[j]) < 0) {
                    j--;
                }

                if (i > j) {
                    break;
                }

                if (i < j) {
                    int temp = map[i];
                    map[i] = map[j];
                    map[j] = temp;
                }

                i++;
                j--;
            }
            while (i <= j);

            if (i <= idx) {
                left = i + 1;
            } else {
                right = j - 1;
            }

            if (j - left <= right - i) {
                if (left < j) {
                    right = j;
                }

                left = i;
            } else {
                if (i < right) {
                    left = i;
                }

                right = j;
            }
        }
        while (left < right);

        return map[idx];
    }
}
